package org.dromara.witdock.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.witdock.domain.AppInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 构建应用视图对象 app_info
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AppInfo.class)
public class AppInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 应用名
     */
    @ExcelProperty(value = "应用名")
    private String appName;

    /**
     * 应用描述
     */
    @ExcelProperty(value = "应用描述")
    private String appDesc;

    /**
     * 应用Code
     */
    @ExcelProperty(value = "应用Code")
    private String code;

    /**
     * 启用webapp
     */
    @ExcelProperty(value = "启用webapp")
    private boolean enableSite;

    /**
     * 启用服务API
     */
    @ExcelProperty(value = "启用服务API")
    private boolean enableApi;

    /**
     * 开场白
     */
    private String prolog;

    private List<DatasetInfoVo> datasetInfoVoList;
    private List<Long> datasetIds;
}
