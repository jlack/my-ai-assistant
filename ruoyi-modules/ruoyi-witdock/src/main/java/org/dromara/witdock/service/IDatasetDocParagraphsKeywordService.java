package org.dromara.witdock.service;

import org.dromara.witdock.domain.DatasetDocParagraphsKeyword;
import org.dromara.witdock.domain.vo.DatasetDocParagraphsKeywordVo;
import org.dromara.witdock.domain.bo.DatasetDocParagraphsKeywordBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 段落关键字表Service接口
 *
 * @author zyh
 * @date 2023-10-07
 */
public interface IDatasetDocParagraphsKeywordService {

    /**
     * 查询段落关键字表
     */
    DatasetDocParagraphsKeywordVo queryById(Long paragraphsId);

    /**
     * 查询段落关键字表列表
     */
    TableDataInfo<DatasetDocParagraphsKeywordVo> queryPageList(DatasetDocParagraphsKeywordBo bo, PageQuery pageQuery);

    /**
     * 查询段落关键字表列表
     */
    List<DatasetDocParagraphsKeywordVo> queryList(DatasetDocParagraphsKeywordBo bo);

    /**
     * 新增段落关键字表
     */
    Boolean insertByBo(DatasetDocParagraphsKeywordBo bo);

    /**
     * 修改段落关键字表
     */
    Boolean updateByBo(DatasetDocParagraphsKeywordBo bo);

    /**
     * 校验并批量删除段落关键字表信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
