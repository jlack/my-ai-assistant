package org.dromara.langchain4j;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.dromara.witdock.domain.MessageInfo;
import org.dromara.witdock.domain.bo.MessageInfoBo;
import org.dromara.witdock.domain.vo.MessageInfoVo;
import org.dromara.witdock.mapper.MessageInfoMapper;
import org.springframework.web.socket.WebSocketSession;

public class MyResponseHandler implements StreamingResponseHandler<AiMessage> {
    private WebSocketSession session;
    private MessageInfoBo messageInfoBo;
    private MessageInfoMapper messageInfoMapper;

    public MyResponseHandler(WebSocketSession session, MessageInfoBo messageInfoBo, MessageInfoMapper baseMapper) {
        this.session = session;
        this.messageInfoBo = messageInfoBo;
        this.messageInfoMapper = baseMapper;
    }

    @Override
    public void onNext(String s) {
        //发送回答内容
        MessageInfoVo vo = new MessageInfoVo();
        vo.setStreamText(s);

        vo.setMsgLocalId(messageInfoBo.getMsgLocalId());
        WebSocketUtils.sendMessage(session, JSONUtil.toJsonStr(vo));
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete(Response<AiMessage> response) {
        messageInfoBo.setReDatetime(DateUtil.date());
        messageInfoBo.setAnswer(response.content().text());
        //发送回答内容
        MessageInfoVo vo = BeanUtil.toBean(messageInfoBo, MessageInfoVo.class);
        WebSocketUtils.sendMessage(session, JSONUtil.toJsonStr(vo));
        //更新数据库
        MessageInfo mi = BeanUtil.toBean(messageInfoBo, MessageInfo.class);
        messageInfoMapper.updateById(mi);
    }
}
