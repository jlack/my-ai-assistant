package org.dromara.witdock.service;

import org.dromara.witdock.domain.ConversationInfo;
import org.dromara.witdock.domain.vo.ConversationInfoVo;
import org.dromara.witdock.domain.bo.ConversationInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 会话Service接口
 *
 * @author Lion Li
 * @date 2023-09-27
 */
public interface IConversationInfoService {

    /**
     * 查询会话
     */
    ConversationInfoVo queryById(Long id);

    /**
     * 查询用户列表
     */
    TableDataInfo<ConversationInfoVo> queryUserPageList(ConversationInfoBo bo, PageQuery pageQuery);
    /**
     * 查询会话列表
     */
    TableDataInfo<ConversationInfoVo> queryPageList(ConversationInfoBo bo, PageQuery pageQuery);

    /**
     * 查询会话列表
     */
    List<ConversationInfoVo> queryList(ConversationInfoBo bo);

    /**
     * 新增会话
     */
    Boolean insertByBo(ConversationInfoBo bo);

    /**
     * 修改会话
     */
    Boolean updateByBo(ConversationInfoBo bo);

    /**
     * 校验并批量删除会话信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
