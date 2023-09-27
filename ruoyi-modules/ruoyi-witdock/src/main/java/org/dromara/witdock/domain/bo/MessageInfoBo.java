package org.dromara.witdock.domain.bo;

import org.dromara.witdock.domain.MessageInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 对话消息业务对象 message_info
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MessageInfo.class, reverseConvertGenerate = false)
public class MessageInfoBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 对话id
     */
    private Long conversationId;

    /**
     * 提问内容
     */
    private String query;

    /**
     * 回答内容
     */
    private String answer;

    /**
     * 回答时间
     */
    private Date reDatetime;

    /**
     * 花费token
     */
    private BigDecimal msgToken;


}
