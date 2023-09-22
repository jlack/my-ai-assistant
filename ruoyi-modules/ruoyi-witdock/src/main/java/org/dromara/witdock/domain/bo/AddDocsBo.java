package org.dromara.witdock.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.witdock.domain.DatasetDoc;

/**
 * @author zyh
 */

@Data
@AutoMapper(target = DatasetDoc.class, reverseConvertGenerate = false)
public class AddDocsBo extends BaseEntity {

    private Long datasetId;

    /**
     * 对象存储ids
     */
    @NotNull(message = "对象存储id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ossIds;
}
