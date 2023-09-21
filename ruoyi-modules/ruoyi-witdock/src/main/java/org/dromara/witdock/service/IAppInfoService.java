package org.dromara.witdock.service;


import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.witdock.domain.bo.AppInfoBo;
import org.dromara.witdock.domain.vo.AppInfoVo;

import java.util.Collection;
import java.util.List;

/**
 * 构建应用Service接口
 *
 * @author Lion Li
 * @date 2023-09-21
 */
public interface IAppInfoService {

    /**
     * 查询构建应用
     */
    AppInfoVo queryById(Long id);

    /**
     * 查询构建应用列表
     */
    TableDataInfo<AppInfoVo> queryPageList(AppInfoBo bo, PageQuery pageQuery);

    /**
     * 查询构建应用列表
     */
    List<AppInfoVo> queryList(AppInfoBo bo);

    /**
     * 新增构建应用
     */
    Boolean insertByBo(AppInfoBo bo);

    /**
     * 修改构建应用
     */
    Boolean updateByBo(AppInfoBo bo);

    /**
     * 校验并批量删除构建应用信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
