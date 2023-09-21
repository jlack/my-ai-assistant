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
import org.dromara.witdock.domain.vo.DatasetInfoVo;
import org.dromara.witdock.domain.bo.DatasetInfoBo;
import org.dromara.witdock.service.IDatasetInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 数据集
 *
 * @author manascloud
 * @date 2023-09-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dataset/dataset")
public class DatasetInfoController extends BaseController {

    private final IDatasetInfoService datasetInfoService;

    /**
     * 查询数据集列表
     */
    @SaCheckPermission("dataset:dataset:list")
    @GetMapping("/list")
    public TableDataInfo<DatasetInfoVo> list(DatasetInfoBo bo, PageQuery pageQuery) {
        return datasetInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出数据集列表
     */
    @SaCheckPermission("dataset:dataset:export")
    @Log(title = "数据集", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DatasetInfoBo bo, HttpServletResponse response) {
        List<DatasetInfoVo> list = datasetInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "数据集", DatasetInfoVo.class, response);
    }

    /**
     * 获取数据集详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("dataset:dataset:query")
    @GetMapping("/{id}")
    public R<DatasetInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(datasetInfoService.queryById(id));
    }

    /**
     * 新增数据集
     */
    @SaCheckPermission("dataset:dataset:add")
    @Log(title = "数据集", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DatasetInfoBo bo) {
        return toAjax(datasetInfoService.insertByBo(bo));
    }

    /**
     * 修改数据集
     */
    @SaCheckPermission("dataset:dataset:edit")
    @Log(title = "数据集", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DatasetInfoBo bo) {
        return toAjax(datasetInfoService.updateByBo(bo));
    }

    /**
     * 删除数据集
     *
     * @param ids 主键串
     */
    @SaCheckPermission("dataset:dataset:remove")
    @Log(title = "数据集", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(datasetInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
