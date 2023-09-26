package org.dromara.witdock.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.witdock.domain.AppDataset;
import org.dromara.witdock.domain.SessionLog;
import org.dromara.witdock.domain.vo.SessionLogVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 会话日志表Mapper接口
 *
 * @author Lion Li
 * @date 2023-09-25
 */
public interface SessionLogMapper extends BaseMapperPlus<SessionLog, SessionLogVo> {
    @Select("SELECT COUNT(*) FROM session_log WHERE session_id = #{sessionId}")
    Integer countLogMsgBySessionId(@Param("sessionId") Long sessionId);
}
