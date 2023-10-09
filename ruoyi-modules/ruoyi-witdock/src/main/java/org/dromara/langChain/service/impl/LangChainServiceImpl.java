package org.dromara.langChain.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.BeanToMapCopier;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.langChain.dto.SendMsgRequest;
import org.dromara.langChain.service.ILangChainService;
import org.dromara.witdock.domain.ConversationInfo;
import org.dromara.witdock.domain.MessageInfo;
import org.dromara.witdock.mapper.ConversationInfoMapper;
import org.dromara.witdock.mapper.MessageInfoMapper;
import org.joda.time.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LangChainServiceImpl implements ILangChainService {

    private final MessageInfoMapper messageInfoMapper;
    private final ConversationInfoMapper conversationInfoMapper;

    @Override
    public MessageInfo sendMsg(SendMsgRequest req) {
        Map<String, Object> params = BeanUtil.beanToMap(req);
        /**
         * 入参  msg_id,app_id,conversation_id,query,is_first
         */
        String resultStr = HttpUtil.post("/lcapi/senMsg", params);
        R r = JSONUtil.toBean(resultStr, R.class);
        if (200 != r.getCode()) {
            throw new SecurityException(r.toString());
        }
        JSONObject data = JSONUtil.parseObj(r.getData());
        MessageInfo msgInfo = new MessageInfo();
        msgInfo.setId(data.getLong("msg_id"));
        msgInfo.setAnswer(data.getStr("answer"));
        msgInfo.setMsgToken(data.getBigDecimal("msg_token"));
        msgInfo.setConversationId(data.getLong("conversation_id"));
        msgInfo.setReDatetime(DateUtil.date());
        //如果返回了会话标题，直接更新标题
        if (data.getStr("conversation_title") != null) {
            ConversationInfo ci = new ConversationInfo();
            ci.setId(data.getLong("conversation_id"));
            ci.setConversationTitle(data.getStr("conversation_title"));
            conversationInfoMapper.updateById(ci);
        }

        //返回给webSocket处理
        return msgInfo;
    }
}
