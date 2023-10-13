package org.dromara.witdock.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.BGE_SMALL_ZH_EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.websocket.service.MsgService;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.dromara.langchain4j.MyCLLModel;
import org.dromara.witdock.domain.DatasetDocParagraphs;
import org.dromara.witdock.mapper.AppInfoMapper;
import org.dromara.witdock.mapper.DatasetDocParagraphsMapper;
import org.dromara.langchain4j.MyResponseHandler;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.MessageInfoBo;
import org.dromara.witdock.domain.vo.MessageInfoVo;
import org.dromara.witdock.domain.MessageInfo;
import org.dromara.witdock.mapper.MessageInfoMapper;
import org.dromara.witdock.service.IMessageInfoService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

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
    public void answer(MessageInfoBo messageInfoBo, WebSocketSession session) {
        //根据会话查所属的APP，再根据APP查包含的数据集文档段落，再请求LLM
        List<DatasetDocParagraphs> docParagraphs = datasetDocParagraphsMapper.listByConversationId(messageInfoBo.getConversationId());

        //使用嵌入模型 mbed 段（将它们转换为表示含义的向量）
        EmbeddingModel embeddingModel = MyCLLModel.getOpenAiEmbeddingModel();
//        换中文分词向量模型
//        todo: 新建模型处线程挂起？？？？ embeddingStore.add(embedding1, segment1);卡住， 改为addAll试试
//        EmbeddingModel embeddingModel = new BGE_SMALL_ZH_EmbeddingModel();
        //将嵌入存储到嵌入存储中以供进一步搜索检索
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        for (DatasetDocParagraphs item : docParagraphs) {
            TextSegment segment1 = TextSegment.from(item.getContent());
            Embedding embedding1 = embeddingModel.embed(segment1).content();
            embeddingStore.add(embedding1, segment1);
        }

        String question = messageInfoBo.getQuery();
        //提示词
        Prompt prompt = null;
        // 嵌入问题
        Embedding questionEmbedding = embeddingModel.embed(question).content();
        // 通过语义相似度在嵌入存储中查找相关嵌入
        // 您可以使用下面的参数来找到适合您的特定用例的最佳位置
        int maxResults = 3;
        double minScore = 0.7;
        List<EmbeddingMatch<TextSegment>> relevantEmbeddings
            = embeddingStore.findRelevant(questionEmbedding, maxResults, minScore);

        //如果找到上下文，则通过提示词让chatGPT根据上下文回答，否则直接回答
        if (!relevantEmbeddings.isEmpty()) {
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
                .collect(joining("\n"));

            Map<String, Object> variables = new HashMap<>();
            variables.put("question", question);
            variables.put("information", information);

            prompt = promptTemplate.apply(variables);

        } else {
            prompt = new Prompt(question);
        }

        // 将提示发送到 OpenAI 聊天模型
        OpenAiStreamingChatModel chatModel = MyCLLModel.getOpenAiStreamingChatModel();
//        System.out.println("Nr of chars: " + prompt.toAiMessage().text());
//        System.out.println("Nr of tokens: " + chatModel.estimateTokenCount(prompt));

        chatModel.generate(prompt.text(), new MyResponseHandler(session, messageInfoBo, baseMapper));
    }


    @Override
    public void addMsg(WebSocketSession session, TextMessage msg, Long userId) {

        MessageInfoBo messageInfoBo = JSONUtil.toBean(msg.getPayload(), MessageInfoBo.class);
        messageInfoBo.setCreateBy(userId);
        //把问题保存到数据库，先返回给前端，再进行回答
        Boolean b = insertByBo(messageInfoBo);
        WebSocketUtils.sendMessage(session, JSONUtil.toJsonStr(BeanUtil.toBean(messageInfoBo, MessageInfoVo.class)));
        if (b) {
            //异步执行
            ThreadUtil.execAsync(() -> {

                answer(messageInfoBo, session);
                //休眠2秒钟模拟调用chatGpt
//                ThreadUtil.sleep(2000);
//                messageInfoBo.setAnswer("自动回答" + IdUtil.simpleUUID());

            });
        }
    }
}
