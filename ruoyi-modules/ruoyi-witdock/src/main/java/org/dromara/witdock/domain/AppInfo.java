package org.dromara.witdock.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 构建应用对象 app_info
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_info")
public class AppInfo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 应用描述
     */
    private String appDesc;

    /**
     * 是否删除
     */
    @TableLogic
    private boolean isDeleted;

    /**
     * 应用Code
     */
    private String code;

    /**
     * 启用webapp
     */
    private Boolean enableSite;

    /**
     * 启用服务API
     */
    private Boolean enableApi;

    /**
     * 开场白
     */
    private String prolog;
}
