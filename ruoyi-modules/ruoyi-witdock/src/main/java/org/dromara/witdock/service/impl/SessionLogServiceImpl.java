package org.dromara.witdock.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.SessionLogBo;
import org.dromara.witdock.domain.vo.SessionLogVo;
import org.dromara.witdock.domain.SessionLog;
import org.dromara.witdock.mapper.SessionLogMapper;
import org.dromara.witdock.service.ISessionLogService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 会话日志表Service业务层处理
 *
 * @author Lion Li
 * @date 2023-09-25
 */
@RequiredArgsConstructor
@Service
public class SessionLogServiceImpl implements ISessionLogService {

    private final SessionLogMapper baseMapper;

    /**
     * 查询会话日志表
     */
    @Override
    public SessionLogVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询会话日志表列表
     */
    @Override
    public TableDataInfo<SessionLogVo> queryPageList(SessionLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SessionLog> lqw = buildQueryWrapper(bo);
        Page<SessionLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询会话日志表列表
     */
    @Override
    public List<SessionLogVo> queryList(SessionLogBo bo) {
        LambdaQueryWrapper<SessionLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SessionLog> buildQueryWrapper(SessionLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SessionLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSessionId() != null, SessionLog::getSessionId, bo.getSessionId());
        lqw.eq(StringUtils.isNotBlank(bo.getQuery()), SessionLog::getQuery, bo.getQuery());
        lqw.eq(StringUtils.isNotBlank(bo.getAnswer()), SessionLog::getAnswer, bo.getAnswer());
        lqw.eq(bo.getReDatetime() != null, SessionLog::getReDatetime, bo.getReDatetime());
        lqw.eq(bo.getMsgToken() != null, SessionLog::getMsgToken, bo.getMsgToken());
        return lqw;
    }

    /**
     * 新增会话日志表
     */
    @Override
    public Boolean insertByBo(SessionLogBo bo) {
        SessionLog add = MapstructUtils.convert(bo, SessionLog.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改会话日志表
     */
    @Override
    public Boolean updateByBo(SessionLogBo bo) {
        SessionLog update = MapstructUtils.convert(bo, SessionLog.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SessionLog entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除会话日志表
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
