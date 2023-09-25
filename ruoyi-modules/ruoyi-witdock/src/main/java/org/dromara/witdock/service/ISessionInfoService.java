package org.dromara.witdock.service;

import org.dromara.witdock.domain.SessionInfo;
import org.dromara.witdock.domain.vo.SessionInfoVo;
import org.dromara.witdock.domain.bo.SessionInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 会话Service接口
 *
 * @author liaoyusheng
 * @date 2023-09-25
 */
public interface ISessionInfoService {

    /**
     * 查询会话
     */
    SessionInfoVo queryById(Long id);

    /**
     * 查询会话列表
     */
    TableDataInfo<SessionInfoVo> queryPageList(SessionInfoBo bo, PageQuery pageQuery);

    /**
     * 查询会话列表
     */
    List<SessionInfoVo> queryList(SessionInfoBo bo);

    /**
     * 新增会话
     */
    Boolean insertByBo(SessionInfoBo bo);

    /**
     * 修改会话
     */
    Boolean updateByBo(SessionInfoBo bo);

    /**
     * 校验并批量删除会话信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
