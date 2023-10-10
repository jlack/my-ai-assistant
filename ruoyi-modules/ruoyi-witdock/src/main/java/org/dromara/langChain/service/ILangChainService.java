package org.dromara.langChain.service;

import org.dromara.langChain.dto.SendMsgRequest;
import org.dromara.witdock.domain.MessageInfo;

public interface ILangChainService {

    /**
     * 发送消息给LangChain， 返回一个回复内容
     *
     * @return
     */
    MessageInfo sendMsg(SendMsgRequest req);
}
