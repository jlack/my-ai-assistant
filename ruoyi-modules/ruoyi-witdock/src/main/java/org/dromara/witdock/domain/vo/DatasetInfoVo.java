package org.dromara.witdock.domain.vo;

import org.dromara.witdock.domain.DatasetInfo;
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
 * 数据集视图对象 dataset_info
 *
 * @author manascloud
 * @date 2023-09-21
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DatasetInfo.class)
public class DatasetInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 数据集名称
     */
    @ExcelProperty(value = "数据集名称")
    private String datasetName;

    /**
     * 数据集描述
     */
    @ExcelProperty(value = "数据集描述")
    private String datasetDesc;

    /**
     * me\all
     */
    @ExcelProperty(value = "me,all")
    private String visiblePermission;

    /**
     * 所含文档数
     */
    @ExcelProperty(value = "所含文档数")
    private Integer docNum;

    /**
     * 总字符数
     */
    @ExcelProperty(value = "总字符数")
    private Integer charNum;
}
