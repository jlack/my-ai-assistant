package org.dromara.witdock.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 段落关键字表对象 dataset_doc_paragraphs_keyword
 *
 * @author zyh
 * @date 2023-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dataset_doc_paragraphs_keyword")
public class DatasetDocParagraphsKeyword extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 段落id
     */
    private Long paragraphsId;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 0-正常，1-删除
     */
    @TableLogic
    private boolean isDeleted;


}
