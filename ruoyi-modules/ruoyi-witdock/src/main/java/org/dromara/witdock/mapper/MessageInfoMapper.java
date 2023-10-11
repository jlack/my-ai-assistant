package org.dromara.witdock.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.witdock.domain.DatasetDocParagraphs;
import org.dromara.witdock.domain.MessageInfo;
import org.dromara.witdock.domain.vo.MessageInfoVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 对话消息Mapper接口
 *
 * @author Lion Li
 * @date 2023-09-27
 */
public interface MessageInfoMapper extends BaseMapperPlus<MessageInfo, MessageInfoVo> {
    @Select("SELECT COUNT(*) FROM message_info WHERE conversation_id = #{conversationId}")
    Integer countMsgNumByConversationId(@Param("conversationId") Long conversationId);

    List<DatasetDocParagraphs> listDatasetDocParagraphsByMessageId(Long messageId);
}
