package org.dromara.witdock.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 文档段落表对象 dataset_doc_paragraphs
 *
 * @author zyh
 * @date 2023-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dataset_doc_paragraphs")
public class DatasetDocParagraphs extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 文档id
     */
    private Long docId;

    /**
     * 段落编号
     */
    private Long sno;

    /**
     * 状态（active,inactive）可用、禁用
     */
    private String status;

    /**
     * 段落内容
     */
    private String content;

    /**
     * 字数
     */
    private Long charNum;


    /**
     * 是否删除
     */
    @TableLogic
    private boolean isDeleted;


}
