package org.dromara.witdock.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.witdock.domain.ConversationInfo;
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
 * 会话视图对象 conversation_info
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ConversationInfo.class)
public class ConversationInfoVo implements Serializable {

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
    private String conversationTitle;

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

    /**
     * 会话创建人id
     */
    private Long createBy;

    /**
     * 会话创建人名字
     */
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "createBy")
    private String createByName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 消息数
     */
    private Integer msgNum;
}
