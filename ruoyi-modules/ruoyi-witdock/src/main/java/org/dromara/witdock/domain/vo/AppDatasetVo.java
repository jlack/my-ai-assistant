package org.dromara.witdock.domain.vo;

import org.dromara.witdock.domain.AppDataset;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 应用数据集视图对象 app_dateset
 *
 * @author liaoyusheng
 * @date 2023-09-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AppDataset.class)
public class AppDatasetVo implements Serializable {

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
     * 数据集id
     */
    @ExcelProperty(value = "数据集id")
    private Long datasetId;

}
