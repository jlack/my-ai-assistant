package org.dromara.witdock.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.witdock.domain.AppDataset;
import org.dromara.witdock.domain.DatasetInfo;
import org.dromara.witdock.domain.vo.DatasetInfoVo;
import org.dromara.witdock.mapper.DatasetInfoMapper;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.AppDatasetBo;
import org.dromara.witdock.domain.vo.AppDatasetVo;
import org.dromara.witdock.mapper.AppDatasetMapper;
import org.dromara.witdock.service.IAppDatasetService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 应用数据集Service业务层处理
 *
 * @author liaoyusheng
 * @date 2023-09-22
 */
@RequiredArgsConstructor
@Service
public class AppDatasetServiceImpl implements IAppDatasetService {

    private final AppDatasetMapper baseMapper;
    private final DatasetInfoMapper datasetInfoMapper;


    /**
     * 查询应用数据集
     */
    @Override
    public AppDatasetVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询应用数据集列表
     */
    @Override
    public TableDataInfo<AppDatasetVo> queryPageList(AppDatasetBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AppDataset> lqw = buildQueryWrapper(bo);
        Page<AppDatasetVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询应用数据集列表
     */
    @Override
    public List<AppDatasetVo> queryList(AppDatasetBo bo) {
        LambdaQueryWrapper<AppDataset> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AppDataset> buildQueryWrapper(AppDatasetBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AppDataset> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAppId() != null, AppDataset::getAppId, bo.getAppId());
        lqw.eq(bo.getDatasetId() != null, AppDataset::getDatasetId, bo.getDatasetId());
        return lqw;
    }

    /**
     * 新增应用数据集
     */
    @Override
    public Boolean insertByBo(AppDatasetBo bo) {
        AppDataset add = MapstructUtils.convert(bo, AppDataset.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改应用数据集
     */
    @Override
    public Boolean updateByBo(AppDatasetBo bo) {
        AppDataset update = MapstructUtils.convert(bo, AppDataset.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AppDataset entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除应用数据集
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<DatasetInfoVo> listDatasetVoByAppId(Long appId) {
        List<AppDataset> appDatasets = baseMapper.listByAppId(appId);
        Set<Long> datasetIds = appDatasets.stream().map(AppDataset::getDatasetId).collect(Collectors.toSet());
        if (!datasetIds.isEmpty()) {
            List<DatasetInfo> list = new LambdaQueryChainWrapper<>(datasetInfoMapper)
                .in(DatasetInfo::getId, datasetIds).list();
            return BeanUtil.copyToList(list, DatasetInfoVo.class);
        }
        return Collections.emptyList();
    }

    @Override
    public void delByBothId(AppDatasetBo bo) {
        baseMapper.delete(buildQueryWrapper(bo));
    }
}
