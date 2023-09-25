package org.dromara.witdock.service;

import org.dromara.witdock.domain.SessionLog;
import org.dromara.witdock.domain.vo.SessionLogVo;
import org.dromara.witdock.domain.bo.SessionLogBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 会话日志表Service接口
 *
 * @author Lion Li
 * @date 2023-09-25
 */
public interface ISessionLogService {

    /**
     * 查询会话日志表
     */
    SessionLogVo queryById(Long id);

    /**
     * 查询会话日志表列表
     */
    TableDataInfo<SessionLogVo> queryPageList(SessionLogBo bo, PageQuery pageQuery);

    /**
     * 查询会话日志表列表
     */
    List<SessionLogVo> queryList(SessionLogBo bo);

    /**
     * 新增会话日志表
     */
    Boolean insertByBo(SessionLogBo bo);

    /**
     * 修改会话日志表
     */
    Boolean updateByBo(SessionLogBo bo);

    /**
     * 校验并批量删除会话日志表信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
