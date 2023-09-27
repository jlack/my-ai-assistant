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
import org.dromara.witdock.domain.bo.MessageInfoBo;
import org.dromara.witdock.domain.vo.MessageInfoVo;
import org.dromara.witdock.domain.MessageInfo;
import org.dromara.witdock.mapper.MessageInfoMapper;
import org.dromara.witdock.service.IMessageInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 对话消息Service业务层处理
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@RequiredArgsConstructor
@Service
public class MessageInfoServiceImpl implements IMessageInfoService {

    private final MessageInfoMapper baseMapper;

    /**
     * 查询对话消息
     */
    @Override
    public MessageInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询对话消息列表
     */
    @Override
    public TableDataInfo<MessageInfoVo> queryPageList(MessageInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MessageInfo> lqw = buildQueryWrapper(bo);
        Page<MessageInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询对话消息列表
     */
    @Override
    public List<MessageInfoVo> queryList(MessageInfoBo bo) {
        LambdaQueryWrapper<MessageInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MessageInfo> buildQueryWrapper(MessageInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MessageInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getConversationId() != null, MessageInfo::getConversationId, bo.getConversationId());
        lqw.like(StringUtils.isNotBlank(bo.getQuery()), MessageInfo::getQuery, bo.getQuery());
        lqw.eq(StringUtils.isNotBlank(bo.getAnswer()), MessageInfo::getAnswer, bo.getAnswer());
        lqw.eq(bo.getReDatetime() != null, MessageInfo::getReDatetime, bo.getReDatetime());
        lqw.eq(bo.getMsgToken() != null, MessageInfo::getMsgToken, bo.getMsgToken());
        return lqw;
    }

    /**
     * 新增对话消息
     */
    @Override
    public Boolean insertByBo(MessageInfoBo bo) {
        MessageInfo add = MapstructUtils.convert(bo, MessageInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改对话消息
     */
    @Override
    public Boolean updateByBo(MessageInfoBo bo) {
        MessageInfo update = MapstructUtils.convert(bo, MessageInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MessageInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除对话消息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
