package org.dromara.witdock.domain.bo;

import org.dromara.witdock.domain.DatasetDocParagraphsKeyword;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 段落关键字表业务对象 dataset_doc_paragraphs_keyword
 *
 * @author zyh
 * @date 2023-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DatasetDocParagraphsKeyword.class, reverseConvertGenerate = false)
public class DatasetDocParagraphsKeywordBo extends BaseEntity {

    /**
     * 段落id
     */
    @NotNull(message = "文档id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long paragraphsId;

    /**
     * 关键字
     */
    @NotBlank(message = "关键字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String keyword;


}
