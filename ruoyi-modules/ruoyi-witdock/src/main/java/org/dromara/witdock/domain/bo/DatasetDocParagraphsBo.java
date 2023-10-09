package org.dromara.witdock.domain.bo;

import org.dromara.witdock.domain.DatasetDocParagraphs;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 文档段落表业务对象 dataset_doc_paragraphs
 *
 * @author zyh
 * @date 2023-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DatasetDocParagraphs.class, reverseConvertGenerate = false)
public class DatasetDocParagraphsBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 文档id
     */
    @NotNull(message = "文档id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long docId;

    /**
     * 段落编号
     */
    @NotNull(message = "段落编号不能为空", groups = { EditGroup.class })
    private Long sno;

    /**
     * 状态（active,inactive）可用、禁用
     */
    @NotBlank(message = "状态（active,inactive）可用、禁用不能为空", groups = { EditGroup.class })
    private String status;

    /**
     * 段落内容
     */
    @NotBlank(message = "段落内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 字数
     */
    @NotNull(message = "字数不能为空", groups = { EditGroup.class })
    private Long charNum;


}
