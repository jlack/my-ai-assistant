package org.dromara.witdock.service;

import org.dromara.witdock.domain.DatasetDocParagraphs;
import org.dromara.witdock.domain.vo.DatasetDocParagraphsVo;
import org.dromara.witdock.domain.bo.DatasetDocParagraphsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 文档段落表Service接口
 *
 * @author zyh
 * @date 2023-10-07
 */
public interface IDatasetDocParagraphsService {

    /**
     * 查询文档段落表
     */
    DatasetDocParagraphsVo queryById(Long id);

    /**
     * 查询文档段落表列表
     */
    TableDataInfo<DatasetDocParagraphsVo> queryPageList(DatasetDocParagraphsBo bo, PageQuery pageQuery);

    /**
     * 查询文档段落表列表
     */
    List<DatasetDocParagraphsVo> queryList(DatasetDocParagraphsBo bo);

    /**
     * 新增文档段落表
     */
    Boolean insertByBo(DatasetDocParagraphsBo bo);

    List<DatasetDocParagraphs> listByDatesetIds(List<Long> ids);

    /**
     * 修改文档段落表
     */
    Boolean updateByBo(DatasetDocParagraphsBo bo);

    /**
     * 校验并批量删除文档段落表信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
