package org.dromara.witdock.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.witdock.domain.bo.DocParaSplitBo;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.witdock.domain.vo.DatasetDocParagraphsVo;
import org.dromara.witdock.domain.bo.DatasetDocParagraphsBo;
import org.dromara.witdock.service.IDatasetDocParagraphsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import javax.swing.text.Segment;

/**
 * 文档段落表
 *
 * @author zyh
 * @date 2023-10-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/witdock/docParagraphs")
public class DatasetDocParagraphsController extends BaseController {

    private final IDatasetDocParagraphsService datasetDocParagraphsService;

    /**
     * 查询文档段落表列表
     */
    @SaCheckPermission("witdock:docParagraphs:list")
    @GetMapping("/list")
    public TableDataInfo<DatasetDocParagraphsVo> list(DatasetDocParagraphsBo bo, PageQuery pageQuery) {
        return datasetDocParagraphsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出文档段落表列表
     */
    @SaCheckPermission("witdock:docParagraphs:export")
    @Log(title = "文档段落表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DatasetDocParagraphsBo bo, HttpServletResponse response) {
        List<DatasetDocParagraphsVo> list = datasetDocParagraphsService.queryList(bo);
        ExcelUtil.exportExcel(list, "文档段落表", DatasetDocParagraphsVo.class, response);
    }

    /**
     * 获取文档段落表详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("witdock:docParagraphs:query")
    @GetMapping("/{id}")
    public R<DatasetDocParagraphsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(datasetDocParagraphsService.queryById(id));
    }

    /**
     * 新增文档段落表
     */
    @SaCheckPermission("witdock:docParagraphs:add")
    @Log(title = "文档段落表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DatasetDocParagraphsBo bo) {
        return toAjax(datasetDocParagraphsService.insertByBo(bo));
    }

    /**
     * 修改文档段落表
     */
    @SaCheckPermission("witdock:docParagraphs:edit")
    @Log(title = "文档段落表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DatasetDocParagraphsBo bo) {
        return toAjax(datasetDocParagraphsService.updateByBo(bo));
    }

    /**
     * 删除文档段落表
     *
     * @param ids 主键串
     */
    @SaCheckPermission("witdock:docParagraphs:remove")
    @Log(title = "文档段落表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(datasetDocParagraphsService.deleteWithValidByIds(List.of(ids), true));
    }


    /**
     * doc分段后批量新增段落
     */
    @SaCheckPermission("witdock:docParagraphs:add")
    @Log(title = "文档段落表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/splitDocToPara")
    public R<Void> splitDocToPara(@RequestBody DocParaSplitBo docParaSplitBo) throws Exception {
        datasetDocParagraphsService.insertSplitedParas(docParaSplitBo);
        return R.ok("分段成功");
    }

    /**
     * 返回doc分段预览
     */
    @SaCheckPermission("witdock:docParagraphs:add")
    @Log(title = "文档段落表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/getDocSegResult")
    public R<List<List<String>>> getDocSegResult(@RequestBody DocParaSplitBo docParaSplitBo) throws Exception {
        List<List<String>> result = new ArrayList<>();
        DocumentSplitter splitter = DocumentSplitters.recursive(
            docParaSplitBo.getMaxSegmentSizeInTokens(),
            0
        );
        for (Long ossId : datasetDocParagraphsService.getIdLongList(docParaSplitBo.getOssIds())) {
            List<String> segContents = new ArrayList<>();
            for (TextSegment seg : datasetDocParagraphsService.getSegsByOssId(ossId, splitter)) {
                segContents.add(seg.text());
            }
            result.add(segContents);
        }
        return R.ok(result);
    }
}
