package org.dromara.witdock.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 会话日志表对象 session_log
 *
 * @author Lion Li
 * @date 2023-09-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("session_log")
public class SessionLog extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
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

    /**
     * 是否删除
     */
    private Integer isDeleted;


}
