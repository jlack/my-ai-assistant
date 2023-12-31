package org.dromara.witdock.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.witdock.domain.bo.DatasetDocBo;
import org.dromara.witdock.domain.vo.DatasetDocVo;
import org.dromara.witdock.mapper.AppDatasetMapper;
import org.dromara.witdock.service.IDatasetDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.DatasetInfoBo;
import org.dromara.witdock.domain.vo.DatasetInfoVo;
import org.dromara.witdock.domain.DatasetInfo;
import org.dromara.witdock.mapper.DatasetInfoMapper;
import org.dromara.witdock.service.IDatasetInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 数据集Service业务层处理
 *
 * @author manascloud
 * @date 2023-09-21
 */
@RequiredArgsConstructor
@Service
public class DatasetInfoServiceImpl implements IDatasetInfoService {

    private final DatasetInfoMapper baseMapper;
    private final AppDatasetMapper appDatesetMapper;

    @Autowired
    private IDatasetDocService docService;

    /**
     * 查询数据集
     */
    @Override
    public DatasetInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询数据集列表
     */
    @Override
    public TableDataInfo<DatasetInfoVo> queryPageList(DatasetInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DatasetInfo> lqw = buildQueryWrapper(bo);
        Page<DatasetInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);

        // 遍历result中的每一个数据集的所含文档数量和总字符数量
        for (DatasetInfoVo datasetVo : result.getRecords()) {
            DatasetDocBo docBo = new DatasetDocBo();
            docBo.setDatasetId(datasetVo.getId());
            List<DatasetDocVo> datasetDocVos = docService.queryList(docBo);
            Integer charNum = docService.countCharNumById(datasetVo.getId());
            int totalCharNum = ObjectUtil.isNotNull(charNum) ? charNum : 0;
            datasetVo.setDocNum(datasetDocVos.size());
            datasetVo.setCharNum(totalCharNum);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询数据集列表
     */
    @Override
    public List<DatasetInfoVo> queryList(DatasetInfoBo bo) {
        LambdaQueryWrapper<DatasetInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DatasetInfo> buildQueryWrapper(DatasetInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DatasetInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getDatasetName()), DatasetInfo::getDatasetName, bo.getDatasetName());
        lqw.eq(StringUtils.isNotBlank(bo.getDatasetDesc()), DatasetInfo::getDatasetDesc, bo.getDatasetDesc());
        lqw.eq(StringUtils.isNotBlank(bo.getVisiblePermission()), DatasetInfo::getVisiblePermission, bo.getVisiblePermission());
        return lqw;
    }

    /**
     * 新增数据集
     */
    @Override
    public Boolean insertByBo(DatasetInfoBo bo) {
        DatasetInfo add = MapstructUtils.convert(bo, DatasetInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改数据集
     */
    @Override
    public Boolean updateByBo(DatasetInfoBo bo) {
        DatasetInfo update = MapstructUtils.convert(bo, DatasetInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DatasetInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除数据集
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
//        删除dataset时同时删除 dataset_app 中间表的对应数据 , 删除对应doc，以及doc对应的paras
        appDatesetMapper.deleteBatchDatasetIds(ids);
        docService.deleteByDatasetIds(ids);
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
