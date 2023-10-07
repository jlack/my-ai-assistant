package org.dromara.witdock.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
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
import org.dromara.witdock.domain.vo.DatasetDocParagraphsKeywordVo;
import org.dromara.witdock.domain.bo.DatasetDocParagraphsKeywordBo;
import org.dromara.witdock.service.IDatasetDocParagraphsKeywordService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 段落关键字表
 *
 * @author zyh
 * @date 2023-10-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/witdock/docParagraphsKeyword")
public class DatasetDocParagraphsKeywordController extends BaseController {

    private final IDatasetDocParagraphsKeywordService datasetDocParagraphsKeywordService;

    /**
     * 查询段落关键字表列表
     */
    @SaCheckPermission("witdock:docParagraphsKeyword:list")
    @GetMapping("/list")
    public TableDataInfo<DatasetDocParagraphsKeywordVo> list(DatasetDocParagraphsKeywordBo bo, PageQuery pageQuery) {
        return datasetDocParagraphsKeywordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出段落关键字表列表
     */
    @SaCheckPermission("witdock:docParagraphsKeyword:export")
    @Log(title = "段落关键字表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DatasetDocParagraphsKeywordBo bo, HttpServletResponse response) {
        List<DatasetDocParagraphsKeywordVo> list = datasetDocParagraphsKeywordService.queryList(bo);
        ExcelUtil.exportExcel(list, "段落关键字表", DatasetDocParagraphsKeywordVo.class, response);
    }

    /**
     * 获取段落关键字表详细信息
     *
     * @param paragraphsId 主键
     */
    @SaCheckPermission("witdock:docParagraphsKeyword:query")
    @GetMapping("/{paragraphsId}")
    public R<DatasetDocParagraphsKeywordVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long paragraphsId) {
        return R.ok(datasetDocParagraphsKeywordService.queryById(paragraphsId));
    }

    /**
     * 新增段落关键字表
     */
    @SaCheckPermission("witdock:docParagraphsKeyword:add")
    @Log(title = "段落关键字表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DatasetDocParagraphsKeywordBo bo) {
        return toAjax(datasetDocParagraphsKeywordService.insertByBo(bo));
    }

    /**
     * 修改段落关键字表
     */
    @SaCheckPermission("witdock:docParagraphsKeyword:edit")
    @Log(title = "段落关键字表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DatasetDocParagraphsKeywordBo bo) {
        return toAjax(datasetDocParagraphsKeywordService.updateByBo(bo));
    }

    /**
     * 删除段落关键字表
     *
     * @param paragraphsIds 主键串
     */
    @SaCheckPermission("witdock:docParagraphsKeyword:remove")
    @Log(title = "段落关键字表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{paragraphsIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] paragraphsIds) {
        return toAjax(datasetDocParagraphsKeywordService.deleteWithValidByIds(List.of(paragraphsIds), true));
    }
}
