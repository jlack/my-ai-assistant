package org.dromara.witdock.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.AsyncUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.dromara.common.core.constant.CacheConstants;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.redis.utils.CacheUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.websocket.service.MsgService;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.dromara.langchain4j.MyCLLModel;
import org.dromara.witdock.domain.DatasetDocParagraphs;
import org.dromara.witdock.mapper.AppInfoMapper;
import org.dromara.witdock.mapper.DatasetDocParagraphsMapper;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.MessageInfoBo;
import org.dromara.witdock.domain.vo.MessageInfoVo;
import org.dromara.witdock.domain.MessageInfo;
import org.dromara.witdock.mapper.MessageInfoMapper;
import org.dromara.witdock.service.IMessageInfoService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

import static java.util.stream.Collectors.joining;

/**
 * 对话消息Service业务层处理
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@RequiredArgsConstructor
@Service
public class MessageInfoServiceImpl implements IMessageInfoService, MsgService {

    private final MessageInfoMapper baseMapper;
    private final AppInfoMapper appInfoMapper;
    private final DatasetDocParagraphsMapper datasetDocParagraphsMapper;

    /**
     * 查询对话消息
     */
    @Override
    public MessageInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询对话消息列表
     */
    @Override
    public TableDataInfo<MessageInfoVo> queryPageList(MessageInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MessageInfo> lqw = buildQueryWrapper(bo);
        Page<MessageInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询对话消息列表
     */
    @Override
    public List<MessageInfoVo> queryList(MessageInfoBo bo) {
        LambdaQueryWrapper<MessageInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MessageInfo> buildQueryWrapper(MessageInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MessageInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getConversationId() != null, MessageInfo::getConversationId, bo.getConversationId());
        lqw.like(StringUtils.isNotBlank(bo.getQuery()), MessageInfo::getQuery, bo.getQuery());
        lqw.eq(StringUtils.isNotBlank(bo.getAnswer()), MessageInfo::getAnswer, bo.getAnswer());
        lqw.eq(bo.getReDatetime() != null, MessageInfo::getReDatetime, bo.getReDatetime());
        lqw.eq(bo.getMsgToken() != null, MessageInfo::getMsgToken, bo.getMsgToken());
        return lqw;
    }

    /**
     * 新增对话消息
     */
    @Override
    public Boolean insertByBo(MessageInfoBo bo) {
        MessageInfo add = MapstructUtils.convert(bo, MessageInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改对话消息
     */
    @Override
    public Boolean updateByBo(MessageInfoBo bo) {
        MessageInfo update = MapstructUtils.convert(bo, MessageInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MessageInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除对话消息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 调用LLM回答问题
     *
     * @return
     */
    public String answer(MessageInfoBo messageInfoBo) {
        //根据消息查所属的会话，再根据会话查所属的APP，再根据APP查包含的数据集文档段落，再请求LLM
        List<DatasetDocParagraphs> docParagraphs = datasetDocParagraphsMapper.listByConversationId(messageInfoBo.getConversationId());

        //使用嵌入模型 mbed 段（将它们转换为表示含义的向量）
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//        EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
//            .apiKey("505fa1b8-d32d-4b4b-bee4-aa4a948ff167")
//            .environment("us-west4-gcp-free")
//            // Project ID can be found in the Pinecone url:
//            // https://app.pinecone.io/organizations/{organization}/projects/{environment}:{projectId}/indexes
//            .projectId("9bedf0a")
//            // Make sure the dimensions of the Pinecone index match the dimensions of the embedding model
//            // (384 for all-MiniLM-L6-v2, 1536 for text-embedding-ada-002, etc.)
//            .index("langc")
//            .build();



        for (DatasetDocParagraphs item : docParagraphs) {
            //将嵌入存储到嵌入存储中以供进一步搜索检索
            TextSegment segment1 = TextSegment.from(item.getContent());
            Embedding embedding1 = embeddingModel.embed(segment1).content();
            embeddingStore.add(embedding1, segment1);
        }





        String question = messageInfoBo.getQuery();

        // 嵌入问题
        Embedding questionEmbedding = embeddingModel.embed(question).content();
        // 通过语义相似度在嵌入存储中查找相关嵌入
        // 您可以使用下面的参数来找到适合您的特定用例的最佳位置
        int maxResults = 3;
        double minScore = 0.7;
        List<EmbeddingMatch<TextSegment>> relevantEmbeddings
            = embeddingStore.findRelevant(questionEmbedding, maxResults, minScore);
        //为包含问题和相关嵌入的模型创建提示
        PromptTemplate promptTemplate = PromptTemplate.from(
            "尽你所能回答以下问题:\n"
                + "\n"
                + "问题:\n"
                + "{{question}}\n"
                + "\n"
                + "如果以下信息中有答案你可以使用:\n"
                + "{{information}}");

        String information = relevantEmbeddings.stream()
            .map(match -> match.embedded().text())
            .collect(joining("\n\n"));

        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        variables.put("information", information);

        Prompt prompt = promptTemplate.apply(variables);

        // 将提示发送到 OpenAI 聊天模型
        ChatLanguageModel chatModel = MyCLLModel.getOpenAI();
        AiMessage aiMessage = chatModel.generate(prompt.toUserMessage()).content();
        //答案
        return aiMessage.text();
    }

    @Override
    public void addMsg(WebSocketSession session, TextMessage msg, Long userId) {

        MessageInfoBo messageInfoBo = JSONUtil.toBean(msg.getPayload(), MessageInfoBo.class);
        messageInfoBo.setCreateBy(userId);
        //把问题保存到数据库，先返回给前端，再进行回答
        Boolean b = insertByBo(messageInfoBo);
//        RedisUtils.setCacheObject(CacheConstants.MSG_WS_SESSION_KEY + messageInfoBo.getId(), session);
//        CacheUtils.put(CacheConstants.MSG_WS_SESSION_KEY, messageInfoBo.getId(), session);
        WebSocketUtils.sendMessage(session, JSONUtil.toJsonStr(BeanUtil.toBean(messageInfoBo, MessageInfoVo.class)));
        if (b) {
            //异步执行
            ThreadUtil.execAsync(() -> {
                //休眠2秒钟模拟调用chatGpt
//                ThreadUtil.sleep(2000);
//                messageInfoBo.setAnswer("自动回答" + IdUtil.simpleUUID());

                messageInfoBo.setAnswer(answer(messageInfoBo));
                messageInfoBo.setReDatetime(DateUtil.date());
                //发送回答内容
                MessageInfoVo vo = BeanUtil.toBean(messageInfoBo, MessageInfoVo.class);
//                WebSocketSession session1 = CacheUtils.get(CacheConstants.MSG_WS_SESSION_KEY, messageInfoBo.getId());
//                CacheUtils.evict(CacheConstants.MSG_WS_SESSION_KEY, messageInfoBo.getId());
//                System.out.println("b");
//                try {
//                    String str = RedisUtils.getCacheObject(CacheConstants.MSG_WS_SESSION_KEY + messageInfoBo.getId());
//                    if (str != null) {
//                        System.out.println("a" + str);
//                    } else {
//                        System.out.println("WebSocketSession is null.");
//                    }
//                    WebSocketSession bean = JSONUtil.toBean(str, WebSocketSession.class);
//
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
                WebSocketUtils.sendMessage(session, JSONUtil.toJsonStr(vo));
                //更新数据库
                updateByBo(messageInfoBo);
            });
        }
        System.out.println("addMsg结束");
    }
}
