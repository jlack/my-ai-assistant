package org.dromara.common.websocket.service;

import cn.hutool.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public interface MsgService {
    void addMsg(WebSocketSession session, TextMessage msg, Long userId);
}
