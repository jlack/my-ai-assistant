package org.dromara.witdock.domain.bo;

import org.dromara.common.mybatis.core.domain.BaseEntityWithoutDept;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.dromara.witdock.domain.DatasetDoc;

/**
 * 数据集文档业务对象 dataset_doc
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DatasetDoc.class, reverseConvertGenerate = false)
public class DatasetDocBo extends BaseEntityWithoutDept {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 数据集id
     */
    @NotNull(message = "数据集id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long datasetId;

    /**
     * 文档名称
     */
    @NotBlank(message = "文档名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String docName;

    /**
     * 对象存储id
     */
    @NotNull(message = "对象存储id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long ossId;

    /**
     * 字符数
     */
    @NotNull(message = "字符数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long charNum;

    /**
     * 状态（active,inactive,archived）可用、禁用、归档
     */
    @NotBlank(message = "状态（active,inactive,archived）可用、禁用、归档不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;


}
