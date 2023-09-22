package org.dromara.witdock.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;

/**
 * @author zyh
 */
@Data
public class AddDsWithDocsBo {
    /**
     *
     */
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
    private String visiblePermission;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    private Long datasetId;

    /**
     * 对象存储ids
     */
    @NotNull(message = "对象存储id不能为空", groups = { AddGroup.class })
    private String ossIds;
}
