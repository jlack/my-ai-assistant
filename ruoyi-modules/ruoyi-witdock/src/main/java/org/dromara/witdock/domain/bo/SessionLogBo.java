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
    private Long sessionId;

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
