package org.dromara.witdock.domain.bo;

import org.dromara.witdock.domain.SessionLog;
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
 * 会话日志表业务对象 session_log
 *
 * @author Lion Li
 * @date 2023-09-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SessionLog.class, reverseConvertGenerate = false)
public class SessionLogBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 对话id
     */
    @NotNull(message = "对话id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sessionId;

    /**
     * 提问内容
     */
    @NotBlank(message = "提问内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String query;

    /**
     * 回答内容
     */
    @NotBlank(message = "回答内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String answer;

    /**
     * 回答时间
     */
    @NotNull(message = "回答时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date reDatetime;

    /**
     * 花费token
     */
    @NotNull(message = "花费token不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal msgToken;


}
