package org.dromara.witdock.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 数据集对象 dataset_info
 *
 * @author manascloud
 * @date 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dataset_info")
public class DatasetInfo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 数据集名称
     */
    private String datasetName;

    /**
     * 数据集描述
     */
    private String datasetDesc;

    /**
     * me\all
     */
    private String visiblePermission;

    /**
     * 是否删除
     */
    private Integer isDeleted;

}
