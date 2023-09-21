package org.dromara.witdock.domain.bo;

import org.dromara.common.mybatis.core.domain.BaseEntityWithoutDept;
import org.dromara.witdock.domain.AppInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 构建应用业务对象 app_info
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AppInfo.class, reverseConvertGenerate = false)
public class AppInfoBo extends BaseEntityWithoutDept {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 应用名
     */
    @NotBlank(message = "应用名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String appName;

    /**
     * 应用描述
     */
    @NotBlank(message = "应用描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String appDesc;


}
