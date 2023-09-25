package org.dromara.witdock.service;

import org.dromara.witdock.domain.bo.AppDatasetBo;
import org.dromara.witdock.domain.vo.AppDatasetVo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.witdock.domain.vo.DatasetInfoVo;

import java.util.Collection;
import java.util.List;

/**
 * 应用数据集Service接口
 *
 * @author liaoyusheng
 * @date 2023-09-22
 */
public interface IAppDatasetService {


    /**
     * 查询应用数据集
     */
    AppDatasetVo queryById(Long id);

    /**
     * 查询应用数据集列表
     */
    TableDataInfo<AppDatasetVo> queryPageList(AppDatasetBo bo, PageQuery pageQuery);

    /**
     * 查询应用数据集列表
     */
    List<AppDatasetVo> queryList(AppDatasetBo bo);

    /**
     * 新增应用数据集
     */
    Boolean insertByBo(AppDatasetBo bo);

    /**
     * 修改应用数据集
     */
    Boolean updateByBo(AppDatasetBo bo);

    /**
     * 校验并批量删除应用数据集信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<DatasetInfoVo> listDatasetVoByAppId(Long appId);
}
