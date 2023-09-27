package org.dromara.witdock.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 会话对象 conversation_info
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("conversation_info")
public class ConversationInfo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 会话标题
     */
    private String conversationTitle;

    /**
     * 发起用户
     */
    private Long userId;

    /**
     * 是否置顶
     */
    private Boolean topping;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean isDeleted;


}
