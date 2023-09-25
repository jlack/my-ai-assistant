package org.dromara.witdock.service;

import org.dromara.witdock.domain.DatasetInfo;
import org.dromara.witdock.domain.vo.DatasetInfoVo;
import org.dromara.witdock.domain.bo.DatasetInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 数据集Service接口
 *
 * @author manascloud
 * @date 2023-09-21
 */
public interface IDatasetInfoService {


    /**
     * 查询数据集
     */
    DatasetInfoVo queryById(Long id);

    /**
     * 查询数据集列表
     */
    TableDataInfo<DatasetInfoVo> queryPageList(DatasetInfoBo bo, PageQuery pageQuery);

    /**
     * 查询数据集列表
     */
    List<DatasetInfoVo> queryList(DatasetInfoBo bo);

    /**
     * 新增数据集
     */
    Boolean insertByBo(DatasetInfoBo bo);

    /**
     * 修改数据集
     */
    Boolean updateByBo(DatasetInfoBo bo);

    /**
     * 校验并批量删除数据集信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
