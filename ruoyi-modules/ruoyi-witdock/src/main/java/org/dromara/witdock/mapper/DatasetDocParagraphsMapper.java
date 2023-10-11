package org.dromara.witdock.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.witdock.domain.DatasetDocParagraphs;
import org.dromara.witdock.domain.vo.DatasetDocParagraphsVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 文档段落表Mapper接口
 *
 * @author zyh
 * @date 2023-10-07
 */
public interface DatasetDocParagraphsMapper extends BaseMapperPlus<DatasetDocParagraphs, DatasetDocParagraphsVo> {
    //    查出sno字段最大值，返回最大值+1值
    @Select("SELECT MAX(sno) FROM dataset_doc_paragraphs WHERE doc_id = #{docId}")
    Integer getSnoMax(@Param("docId") Long docId);

    List<DatasetDocParagraphs> listByConversationId(Long conversationId);
}
