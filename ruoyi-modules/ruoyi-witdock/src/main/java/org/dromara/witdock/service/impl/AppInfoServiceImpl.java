package org.dromara.witdock.service.impl;

import cn.hutool.core.util.IdUtil;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.AppInfoBo;
import org.dromara.witdock.domain.vo.AppInfoVo;
import org.dromara.witdock.domain.AppInfo;
import org.dromara.witdock.mapper.AppInfoMapper;
import org.dromara.witdock.service.IAppInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

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

    /**
     * 查询构建应用
     */
    @Override
    public AppInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
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
    private void validEntityBeforeSave(AppInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除构建应用
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
