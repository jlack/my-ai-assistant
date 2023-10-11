package org.dromara.witdock.service;


import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.witdock.domain.bo.DatasetDocBo;
import org.dromara.witdock.domain.vo.DatasetDocVo;
import org.dromara.witdock.domain.vo.DatasetInfoVo;

import java.util.Collection;
import java.util.List;

/**
 * 数据集文档Service接口
 *
 * @author Lion Li
 * @date 2023-09-21
 */
public interface IDatasetDocService {


    /**
     * 查询数据集中文档总字符数
     */
    Integer countCharNumById(Long id);

    /**
     * 查询数据集文档
     */
    DatasetDocVo queryById(Long id);

    /**
     * 查询数据集文档
     */
    DatasetDocVo queryByOssId(Long ossId);

    /**
     * 查询数据集文档列表
     */
    TableDataInfo<DatasetDocVo> queryPageList(DatasetDocBo bo, PageQuery pageQuery);

    /**
     * 查询数据集文档列表
     */
    List<DatasetDocVo> queryList(DatasetDocBo bo);

    /**
     * 新增数据集文档
     */
    Boolean insertByBo(DatasetDocBo bo);

    /**
     * 修改数据集文档
     */
    Boolean updateByBo(DatasetDocBo bo);

    /**
     * 校验并批量删除数据集文档信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
