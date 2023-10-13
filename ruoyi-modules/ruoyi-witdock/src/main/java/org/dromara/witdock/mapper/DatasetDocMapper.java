package org.dromara.witdock.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.witdock.domain.DatasetDoc;
import org.dromara.witdock.domain.vo.DatasetDocVo;

import java.util.Collection;

/**
 * 数据集文档Mapper接口
 *
 * @author Lion Li
 * @date 2023-09-21
 */
public interface DatasetDocMapper extends BaseMapperPlus<DatasetDoc, DatasetDocVo> {

    @Select("select sum(char_num) from dataset_doc where dataset_id = #{datasetId}")
    Integer countCharNumByDatasetId(Long datasetId);

    @Select("select * from dataset_doc where oss_id  = #{ossId}")
    DatasetDocVo selectVoByOssId(Long ossId);

    @Delete("<script>" +
        "DELETE FROM dataset_doc WHERE dataset_id IN " +
        "<foreach item='item' index='index' collection='datasetIds' open='(' separator=',' close=')'>" +
        "#{item}" +
        "</foreach>" +
        "</script>")
    Integer deleteByDatasetIds(Collection<Long> datasetIds);

    @Select("<script>" +
        "SELECT id FROM dataset_doc WHERE dataset_id IN " +
        "<foreach item='item' index='index' collection='listDocIdsByDatasetIds' open='(' separator=',' close=')'>" +
        "#{item}" +
        "</foreach>" +
        "</script>")
    Collection<Long> listDocIdsByDatasetIds(Collection<Long> listDocIdsByDatasetIds);

}
