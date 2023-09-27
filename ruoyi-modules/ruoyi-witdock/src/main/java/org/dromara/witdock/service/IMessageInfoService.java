package org.dromara.witdock.service;

import org.dromara.witdock.domain.MessageInfo;
import org.dromara.witdock.domain.vo.MessageInfoVo;
import org.dromara.witdock.domain.bo.MessageInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 对话消息Service接口
 *
 * @author Lion Li
 * @date 2023-09-27
 */
public interface IMessageInfoService {

    /**
     * 查询对话消息
     */
    MessageInfoVo queryById(Long id);

    /**
     * 查询对话消息列表
     */
    TableDataInfo<MessageInfoVo> queryPageList(MessageInfoBo bo, PageQuery pageQuery);

    /**
     * 查询对话消息列表
     */
    List<MessageInfoVo> queryList(MessageInfoBo bo);

    /**
     * 新增对话消息
     */
    Boolean insertByBo(MessageInfoBo bo);

    /**
     * 修改对话消息
     */
    Boolean updateByBo(MessageInfoBo bo);

    /**
     * 校验并批量删除对话消息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
