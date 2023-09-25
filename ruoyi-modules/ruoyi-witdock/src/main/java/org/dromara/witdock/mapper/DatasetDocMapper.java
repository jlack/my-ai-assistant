package org.dromara.witdock.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.witdock.domain.DatasetDoc;
import org.dromara.witdock.domain.vo.DatasetDocVo;

/**
 * 数据集文档Mapper接口
 *
 * @author Lion Li
 * @date 2023-09-21
 */
public interface DatasetDocMapper extends BaseMapperPlus<DatasetDoc, DatasetDocVo> {
    Integer countCharNumByDatasetId(Long datasetId);
}
