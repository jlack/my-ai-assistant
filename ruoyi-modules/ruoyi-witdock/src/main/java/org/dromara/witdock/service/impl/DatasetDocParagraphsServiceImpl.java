package org.dromara.witdock.service.impl;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.service.ISysOssService;
import org.dromara.witdock.domain.bo.DocParaSplitBo;
import org.dromara.witdock.service.IDatasetDocService;
import org.dromara.witdock.domain.DatasetDoc;
import org.dromara.witdock.enums.DatasetDocParagraphsStatusEnum;
import org.dromara.witdock.mapper.DatasetDocMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.DatasetDocParagraphsBo;
import org.dromara.witdock.domain.vo.DatasetDocParagraphsVo;
import org.dromara.witdock.domain.DatasetDocParagraphs;
import org.dromara.witdock.mapper.DatasetDocParagraphsMapper;
import org.dromara.witdock.service.IDatasetDocParagraphsService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 文档段落表Service业务层处理
 *
 * @author zyh
 * @date 2023-10-07
 */
@RequiredArgsConstructor
@Service
public class DatasetDocParagraphsServiceImpl implements IDatasetDocParagraphsService {

    private final DatasetDocParagraphsMapper baseMapper;
    private final DatasetDocMapper datasetDocMapper;

    private final IDatasetDocService docService;

    private final ISysOssService ossService;

    /**
     * 查询文档段落表
     */
    @Override
    public DatasetDocParagraphsVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询文档段落表列表
     */
    @Override
    public TableDataInfo<DatasetDocParagraphsVo> queryPageList(DatasetDocParagraphsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DatasetDocParagraphs> lqw = buildQueryWrapper(bo);
        Page<DatasetDocParagraphsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询文档段落表列表
     */
    @Override
    public List<DatasetDocParagraphsVo> queryList(DatasetDocParagraphsBo bo) {
        LambdaQueryWrapper<DatasetDocParagraphs> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DatasetDocParagraphs> buildQueryWrapper(DatasetDocParagraphsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DatasetDocParagraphs> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDocId() != null, DatasetDocParagraphs::getDocId, bo.getDocId());
        lqw.eq(bo.getSno() != null, DatasetDocParagraphs::getSno, bo.getSno());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), DatasetDocParagraphs::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), DatasetDocParagraphs::getContent, bo.getContent());
        lqw.eq(bo.getCharNum() != null, DatasetDocParagraphs::getCharNum, bo.getCharNum());
        return lqw;
    }

    /**
     * 新增文档段落表
     */
    @Override
    public Boolean insertByBo(DatasetDocParagraphsBo bo) {
        DatasetDocParagraphs add = MapstructUtils.convert(bo, DatasetDocParagraphs.class);
        validEntityBeforeSave(add);
        add.setSno(baseMapper.getSnoMax(bo.getDocId()) + 1);
        add.setCharNum((long) bo.getContent().length());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public List<DatasetDocParagraphs> listByDatesetIds(List<Long> ids) {
        List<DatasetDoc> list = new LambdaQueryChainWrapper<>(datasetDocMapper)
            .select(DatasetDoc::getId)
            .in(DatasetDoc::getDatasetId, ids)
            .list();
        List<Long> DatasetDocIds = list.stream().map(DatasetDoc::getId).collect(Collectors.toList());
        if (ArrayUtil.isNotEmpty(DatasetDocIds)) {
            List<DatasetDocParagraphs> paragraphsList = new LambdaQueryChainWrapper<>(baseMapper)
                .in(DatasetDocParagraphs::getDocId, ids)
                .eq(DatasetDocParagraphs::getStatus, DatasetDocParagraphsStatusEnum.ACTIVE)
                .list();
            return paragraphsList;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 修改文档段落表
     */
    @Override
    public Boolean updateByBo(DatasetDocParagraphsBo bo) {
        DatasetDocParagraphs update = MapstructUtils.convert(bo, DatasetDocParagraphs.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DatasetDocParagraphs entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除文档段落表
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void insertSplitedParas(DocParaSplitBo docParaSplitBo) {
        List<Long> ossIdList = getIdLongList(docParaSplitBo.getOssIds());

        DocumentSplitter splitter = DocumentSplitters.recursive(
            docParaSplitBo.getMaxSegmentSizeInTokens(),
            0
        );

        List<SysOssVo> ossVoList = ossService.listByIds(ossIdList);



        for (SysOssVo oss : ossVoList) {
            List<TextSegment> segments = getSegsByOssId(oss.getOssId(), splitter);
            List<DatasetDocParagraphs> paraList = new ArrayList<>();

            Long docId = docService.queryByOssId(oss.getOssId()).getId();
            Integer snoMax = baseMapper.getSnoMax(docId);
            for (int i = 0; i < segments.size(); i++) {
                DatasetDocParagraphs para = new DatasetDocParagraphs();
                para.setContent(segments.get(i).text());
                para.setDocId(docId);
                para.setSno(snoMax + i + 1);
                para.setCharNum((long) segments.get(i).text().length());
                paraList.add(para);
            }

            baseMapper.insertBatch(paraList);
        }

    }

    public List<Long> getIdLongList(String ossIds) {
        String[] ossIdArr = StringUtils.split(ossIds, ",");

        Long[] ossIdLongArr = new Long[ossIdArr.length];

        for (int i = 0; i < ossIdArr.length; i++) {
            ossIdLongArr[i] = Long.parseLong(ossIdArr[i]);
        }

        List<Long> ossIdList = Arrays.asList(ossIdLongArr);
        return ossIdList;
    }

    @Override
    public List<TextSegment> getSegsByOssId(Long ossId, DocumentSplitter splitter) {
        Document doc = ossService.getDocumentById(ossId);
        return splitter.split(doc);
    }
}
