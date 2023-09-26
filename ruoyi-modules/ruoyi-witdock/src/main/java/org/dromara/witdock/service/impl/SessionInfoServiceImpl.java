package org.dromara.witdock.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.witdock.domain.SessionLog;
import org.dromara.witdock.mapper.SessionLogMapper;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.SessionInfoBo;
import org.dromara.witdock.domain.vo.SessionInfoVo;
import org.dromara.witdock.domain.SessionInfo;
import org.dromara.witdock.mapper.SessionInfoMapper;
import org.dromara.witdock.service.ISessionInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 会话Service业务层处理
 *
 * @author liaoyusheng
 * @date 2023-09-25
 */
@RequiredArgsConstructor
@Service
public class SessionInfoServiceImpl implements ISessionInfoService {

    private final SessionInfoMapper baseMapper;
    private final SessionLogMapper logMapper;

    /**
     * 查询会话
     */
    @Override
    public SessionInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询会话列表
     */
    @Override
    public TableDataInfo<SessionInfoVo> queryPageList(SessionInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SessionInfo> lqw = buildQueryWrapper(bo);
        Page<SessionInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SessionInfoVo> vos = result.getRecords();
        for (SessionInfoVo vo : vos) {

         vo.setMsgNum(logMapper.countLogMsgBySessionId(vo.getId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询会话列表
     */
    @Override
    public List<SessionInfoVo> queryList(SessionInfoBo bo) {
        LambdaQueryWrapper<SessionInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SessionInfo> buildQueryWrapper(SessionInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SessionInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAppId() != null, SessionInfo::getAppId, bo.getAppId());
        lqw.eq(StringUtils.isNotBlank(bo.getSessionTitle()), SessionInfo::getSessionTitle, bo.getSessionTitle());
        lqw.eq(bo.getUserId() != null, SessionInfo::getUserId, bo.getUserId());
        lqw.eq(bo.getTopping() != null, SessionInfo::getTopping, bo.getTopping());
        return lqw;
    }

    /**
     * 新增会话
     */
    @Override
    public Boolean insertByBo(SessionInfoBo bo) {
        SessionInfo add = MapstructUtils.convert(bo, SessionInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改会话
     */
    @Override
    public Boolean updateByBo(SessionInfoBo bo) {
        SessionInfo update = MapstructUtils.convert(bo, SessionInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SessionInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除会话
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
