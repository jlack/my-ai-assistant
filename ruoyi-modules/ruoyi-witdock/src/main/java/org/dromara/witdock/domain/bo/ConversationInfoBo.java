package org.dromara.witdock.domain.bo;

import org.dromara.witdock.domain.ConversationInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 会话业务对象 conversation_info
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ConversationInfo.class, reverseConvertGenerate = false)
public class ConversationInfoBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 应用id
     */
    @NotNull(message = "应用id不能为空", groups = { AddGroup.class })
    private Long appId;

    /**
     * 会话token
     */
    private String chatToken;

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
    private Boolean isDeleted;


}
