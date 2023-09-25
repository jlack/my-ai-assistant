package org.dromara.witdock.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 应用数据集对象 app_dateset
 *
 * @author liaoyusheng
 * @date 2023-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_dataset")
public class AppDataset extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 数据集id
     */
    private Long datasetId;


}
