package org.dromara.witdock.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.dromara.witdock.domain.AppDataset;
import org.dromara.witdock.domain.vo.AppDatasetVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.Collection;
import java.util.List;

/**
 * 应用数据集Mapper接口
 *
 * @author liaoyusheng
 * @date 2023-09-22
 */
public interface AppDatasetMapper extends BaseMapperPlus<AppDataset, AppDatasetVo> {
    @Delete("delete from app_dataset where app_id=#{appId}")
    Integer deleteByAppId(Long appId);

    @Select("select * from app_dataset where app_id=#{appId}")
    List<AppDataset> listByAppId(Long appId);

    @Delete("<script>" +
        "DELETE FROM app_dataset WHERE dataset_id IN " +
        "<foreach item='item' index='index' collection='datasetIds' open='(' separator=',' close=')'>" +
        "#{item}" +
        "</foreach>" +
        "</script>")
    int deleteBatchDatasetIds(Collection<Long> datasetIds);

    @Delete("<script>" +
        "DELETE FROM app_dataset WHERE app_id IN " +
        "<foreach item='item' index='index' collection='appIds' open='(' separator=',' close=')'>" +
        "#{item}" +
        "</foreach>" +
        "</script>")
    int deleteBatchAppIds(Collection<Long> appIds);
}
