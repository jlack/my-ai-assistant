package org.dromara.witdock.domain.vo;

import org.dromara.witdock.domain.SessionInfo;
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
 * 会话视图对象 session_info
 *
 * @author liaoyusheng
 * @date 2023-09-25
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SessionInfo.class)
public class SessionInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 应用id
     */
    @ExcelProperty(value = "应用id")
    private Long appId;

    /**
     * 会话标题
     */
    @ExcelProperty(value = "会话标题")
    private String sessionTitle;

    /**
     * 发起用户
     */
    @ExcelProperty(value = "发起用户")
    private Long userId;

    /**
     * 是否置顶
     */
    @ExcelProperty(value = "是否置顶")
    private Boolean topping;


}
