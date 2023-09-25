package org.dromara.witdock.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.witdock.domain.SessionLog;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 会话日志表视图对象 session_log
 *
 * @author Lion Li
 * @date 2023-09-25
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SessionLog.class)
public class SessionLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 对话id
     */
    @ExcelProperty(value = "对话id")
    private Long sessionId;

    /**
     * 提问内容
     */
    @ExcelProperty(value = "提问内容")
    private String query;

    /**
     * 回答内容
     */
    @ExcelProperty(value = "回答内容")
    private String answer;

    /**
     * 回答时间
     */
    @ExcelProperty(value = "回答时间")
    private Date reDatetime;

    /**
     * 花费token
     */
    @ExcelProperty(value = "花费token")
    private BigDecimal msgToken;


}
