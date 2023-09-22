package org.dromara.witdock.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntityWithoutDept;

import java.io.Serial;

/**
 * 数据集文档对象 dataset_doc
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dataset_doc")
public class DatasetDoc extends BaseEntityWithoutDept {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 数据集id
     */
    private Long datasetId;

    /**
     * 文档名称
     */
    private String docName;

    /**
     * 对象存储id
     */
    private Long ossId;

    /**
     * 字符数
     */
    private Long charNum;

    /**
     * 状态（active,inactive,archived）可用、禁用、归档
     */
    private String status;

    /**
     * 是否删除
     */
    @TableLogic
    private boolean isDeleted;


}
