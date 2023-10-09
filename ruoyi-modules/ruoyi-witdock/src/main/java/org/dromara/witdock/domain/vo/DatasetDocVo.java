package org.dromara.witdock.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.witdock.domain.DatasetDoc;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 数据集文档视图对象 dataset_doc
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DatasetDoc.class)
public class DatasetDocVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 数据集id
     */
    @ExcelProperty(value = "数据集id")
    private Long datasetId;

    /**
     * 文档名称
     */
    @ExcelProperty(value = "文档名称")
    private String docName;

    /**
     * 对象存储id
     */
    @ExcelProperty(value = "对象存储id")
    private Long ossId;

    /**
     * 字符数
     */
    @ExcelProperty(value = "字符数")
    private Long charNum;

    /**
     * 状态（active,inactive,archived）可用、禁用、归档
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "a=ctive,inactive,archived")
    private String status;

    /**
     * 上传人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "createBy")
    private String createByName;

    private Long createBy;

    private Date createTime;

    /**
     * 更新人账号
     */
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "updateBy")
    private String updateByName;

    private Long updateBy;

    private Date updateTime;

    private Long fileSize;

    private SysOssVo ossVo;
}
