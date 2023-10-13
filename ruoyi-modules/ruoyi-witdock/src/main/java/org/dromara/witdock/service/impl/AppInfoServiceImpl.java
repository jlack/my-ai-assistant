package org.dromara.witdock.service.impl;

import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.witdock.domain.AppDataset;
import org.dromara.witdock.domain.DatasetInfo;
import org.dromara.witdock.domain.vo.DatasetInfoVo;
import org.dromara.witdock.mapper.AppDatasetMapper;
import org.dromara.witdock.mapper.DatasetInfoMapper;
import org.dromara.witdock.service.IAppDatasetService;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.AppInfoBo;
import org.dromara.witdock.domain.vo.AppInfoVo;
import org.dromara.witdock.domain.AppInfo;
import org.dromara.witdock.mapper.AppInfoMapper;
import org.dromara.witdock.service.IAppInfoService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 构建应用Service业务层处理
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@RequiredArgsConstructor
@Service
public class AppInfoServiceImpl implements IAppInfoService {

    private final AppInfoMapper baseMapper;
    private final AppDatasetMapper appDatesetMapper;
    private final DatasetInfoMapper datasetInfoMapper;
    private final IAppDatasetService appDatesetService;

    /**
     * 查询构建应用
     */
    @Override
    public AppInfoVo queryById(Long id) {
        AppInfoVo appInfoVo = baseMapper.selectVoById(id);
        List<AppDataset> list = new LambdaQueryChainWrapper<>(appDatesetMapper)
            .eq(AppDataset::getAppId, id)
            .list();
        if (!list.isEmpty()) {
            Set<Long> datasetIds = list.stream().map(AppDataset::getDatasetId).collect(Collectors.toSet());
            List<DatasetInfoVo> datasetInfoVos = datasetInfoMapper.selectVoBatchIds(datasetIds);
            appInfoVo.setDatasetInfoVoList(datasetInfoVos);
        }
        return appInfoVo;
    }

    /**
     * 查询构建应用列表
     */
    @Override
    public TableDataInfo<AppInfoVo> queryPageList(AppInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AppInfo> lqw = buildQueryWrapper(bo);
        Page<AppInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询构建应用列表
     */
    @Override
    public List<AppInfoVo> queryList(AppInfoBo bo) {
        LambdaQueryWrapper<AppInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AppInfo> buildQueryWrapper(AppInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AppInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getAppName()), AppInfo::getAppName, bo.getAppName());
        lqw.like(StringUtils.isNotBlank(bo.getAppDesc()), AppInfo::getAppDesc, bo.getAppDesc());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), AppInfo::getCode, bo.getCode());
        lqw.eq(bo.getEnableSite() != null, AppInfo::getEnableSite, bo.getEnableSite());
        lqw.eq(bo.getEnableApi() != null, AppInfo::getEnableApi, bo.getEnableApi());
        return lqw;
    }

    /**
     * 新增构建应用
     */
    @Override
    public Boolean insertByBo(AppInfoBo bo) {
        AppInfo add = MapstructUtils.convert(bo, AppInfo.class);
        validEntityBeforeSave(add);
        add.setCode(IdUtil.simpleUUID());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改构建应用
     */
    @Override
    public Boolean updateByBo(AppInfoBo bo) {
        AppInfo update = MapstructUtils.convert(bo, AppInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AppInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除构建应用
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
//        删除app时同时删除 dataset_app 中间表的对应数据
        appDatesetMapper.deleteBatchAppIds(ids);

        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean resetCode(Long id) {
        return new LambdaUpdateChainWrapper<>(baseMapper)
            .set(AppInfo::getCode, IdUtil.objectId())
            .eq(AppInfo::getId, id).update();
    }

    @Override
    public Boolean addDatasetToApp(AppInfoBo bo) {
        AppInfo appInfo = baseMapper.selectById(bo.getId());
//        if (!LoginHelper.getUserId().equals(appInfo.getCreateBy())) {
//            throw new SecurityException("您无权编辑该应用");
//        }

        /**todo
         * 这里应该增加校验，只有自己的数据集才能添加。
         */
        List<DatasetInfo> datasetInfoList = new LambdaQueryChainWrapper<>(datasetInfoMapper)
            .select(DatasetInfo::getId)
            .in(DatasetInfo::getId, bo.getDatasetIds())
            .list();

        List<AppDataset> addList = new ArrayList<>();
        datasetInfoList.forEach(dataset -> {
            AppDataset appDateset = new AppDataset();
            appDateset.setAppId(bo.getId());
            appDateset.setDatasetId(dataset.getId());
            addList.add(appDateset);
        });


        //删除原有的，新增
        appDatesetMapper.deleteByAppId(bo.getId());
        return appDatesetMapper.insertBatch(addList);

    }

    @Override
    public AppInfoVo getConfig(Long appId) {
        AppInfoVo appInfoVo = baseMapper.selectVoById(appId);
        List<DatasetInfoVo> datasetInfoVos = appDatesetService.listDatasetVoByAppId(appId);
        appInfoVo.setDatasetInfoVoList(datasetInfoVos);
        List<Long> datasetIds = datasetInfoVos.stream().map(DatasetInfoVo::getId).collect(Collectors.toList());
        appInfoVo.setDatasetIds(datasetIds);
        return appInfoVo;
    }
}
