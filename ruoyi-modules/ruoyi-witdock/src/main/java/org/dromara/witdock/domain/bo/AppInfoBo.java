package org.dromara.witdock.domain.bo;

import org.dromara.witdock.domain.AppInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 构建应用业务对象 app_info
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AppInfo.class, reverseConvertGenerate = false)
public class AppInfoBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 应用名
     */
    @NotBlank(message = "应用名不能为空", groups = {AddGroup.class})
    private String appName;

    /**
     * 应用描述
     */
    private String appDesc;

    /**
     * 应用Code
     */
    private String code;

    /**
     * 启用webapp
     */
    private Boolean enableSite;

    /**
     * 启用服务API
     */
    private Boolean enableApi;

    /**
     * 开场白
     */
    private String prolog;
    private List<Long> datasetIds;
}
