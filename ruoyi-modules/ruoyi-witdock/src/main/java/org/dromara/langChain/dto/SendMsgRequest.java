package org.dromara.langChain.dto;

import lombok.Data;

@Data
public class SendMsgRequest {
    private Long msgId;
    private Long appId;
    private Long conversationId;
    private String query;
    private boolean isFirst;
}
