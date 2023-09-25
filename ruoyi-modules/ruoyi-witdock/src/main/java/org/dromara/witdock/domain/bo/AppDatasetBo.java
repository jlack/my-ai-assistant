package org.dromara.witdock.domain.bo;

import org.dromara.witdock.domain.AppDataset;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 应用数据集业务对象 app_dateset
 *
 * @author liaoyusheng
 * @date 2023-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AppDataset.class, reverseConvertGenerate = false)
public class AppDatasetBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 应用id
     */
    @NotNull(message = "应用id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long appId;

    /**
     * 数据集id
     */
    @NotNull(message = "数据集id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long datasetId;


}
