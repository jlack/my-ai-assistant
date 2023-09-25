package org.dromara.witdock.domain.bo;

import org.dromara.witdock.domain.DatasetInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 数据集业务对象 dataset_info
 *
 * @author manascloud
 * @date 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DatasetInfo.class, reverseConvertGenerate = false)
public class DatasetInfoBo extends BaseEntity {
    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 数据集名称
     */
    @NotBlank(message = "数据集名称不能为空", groups = { AddGroup.class })
    private String datasetName;

    /**
     * 数据集描述
     */
    @NotBlank(message = "数据集描述不能为空", groups = { AddGroup.class })
    private String datasetDesc;

    /**
     * me\all
     */
//    @NotBlank(message = "me,all不能为空", groups = { AddGroup.class, EditGroup.class })
    private String visiblePermission;

    /**
     * 是否删除
     */
    private Integer isDeleted;

}
