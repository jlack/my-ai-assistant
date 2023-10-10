package org.dromara.witdock.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.witdock.mapper.MessageInfoMapper;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.ConversationInfoBo;
import org.dromara.witdock.domain.vo.ConversationInfoVo;
import org.dromara.witdock.domain.ConversationInfo;
import org.dromara.witdock.mapper.ConversationInfoMapper;
import org.dromara.witdock.service.IConversationInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 会话Service业务层处理
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@RequiredArgsConstructor
@Service
public class ConversationInfoServiceImpl implements IConversationInfoService {

    private final ConversationInfoMapper baseMapper;

    private final MessageInfoMapper msgInfoMapper;
    /**
     * 查询会话
     */
    @Override
    public ConversationInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<ConversationInfoVo> queryUserPageList(ConversationInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ConversationInfo> lqw = buildQueryWrapper(bo);
        lqw.ne(bo.getChatToken() != null, ConversationInfo::getChatToken, bo.getChatToken());
        lqw.isNull(bo.getChatToken() == null, ConversationInfo::getChatToken);
        Page<ConversationInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (ConversationInfoVo vo : result.getRecords()) {
            vo.setMsgNum(msgInfoMapper.countMsgNumByConversationId(vo.getId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询会话列表
     */
    @Override
    public TableDataInfo<ConversationInfoVo> queryPageList(ConversationInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ConversationInfo> lqw = buildQueryWrapper(bo);
        Page<ConversationInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (ConversationInfoVo vo : result.getRecords()) {
            vo.setMsgNum(msgInfoMapper.countMsgNumByConversationId(vo.getId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询会话列表
     */
    @Override
    public List<ConversationInfoVo> queryList(ConversationInfoBo bo) {
        LambdaQueryWrapper<ConversationInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ConversationInfo> buildQueryWrapper(ConversationInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ConversationInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAppId() != null, ConversationInfo::getAppId, bo.getAppId());
        lqw.like(StringUtils.isNotBlank(bo.getConversationTitle()), ConversationInfo::getConversationTitle, bo.getConversationTitle());
        lqw.eq(bo.getUserId() != null, ConversationInfo::getUserId, bo.getUserId());
        lqw.eq(bo.getTopping() != null, ConversationInfo::getTopping, bo.getTopping());
        lqw.eq(bo.getChatToken() != null, ConversationInfo::getChatToken, bo.getChatToken());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            ConversationInfo::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增会话
     */
    @Override
    public Boolean insertByBo(ConversationInfoBo bo) {
        ConversationInfo add = MapstructUtils.convert(bo, ConversationInfo.class);
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
    public Boolean updateByBo(ConversationInfoBo bo) {
        ConversationInfo update = MapstructUtils.convert(bo, ConversationInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ConversationInfo entity){
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
