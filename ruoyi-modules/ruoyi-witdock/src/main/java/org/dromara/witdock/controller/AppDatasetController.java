package org.dromara.witdock.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.witdock.domain.AppDataset;
import org.dromara.witdock.domain.bo.AppDatasetBo;
import org.dromara.witdock.domain.vo.AppDatasetVo;
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
import org.dromara.witdock.service.IAppDatasetService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 应用数据集
 *
 * @author liaoyusheng
 * @date 2023-09-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/witdock/appDataset")
public class AppDatasetController extends BaseController {

    private final IAppDatasetService appDatesetService;

    /**
     * 查询应用数据集列表
     */
    @SaCheckPermission("witdock:appDataset:list")
    @GetMapping("/list")
    public TableDataInfo<AppDatasetVo> list(AppDatasetBo bo, PageQuery pageQuery) {
        return appDatesetService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出应用数据集列表
     */
    @SaCheckPermission("witdock:appDataset:export")
    @Log(title = "应用数据集", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AppDatasetBo bo, HttpServletResponse response) {
        List<AppDatasetVo> list = appDatesetService.queryList(bo);
        ExcelUtil.exportExcel(list, "应用数据集", AppDatasetVo.class, response);
    }

    /**
     * 获取应用数据集详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("witdock:appDataset:query")
    @GetMapping("/{id}")
    public R<AppDatasetVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(appDatesetService.queryById(id));
    }

    /**
     * 新增应用数据集
     */
    @SaCheckPermission("witdock:appDataset:add")
    @Log(title = "应用数据集", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AppDatasetBo bo) {
        return toAjax(appDatesetService.insertByBo(bo));
    }

    /**
     * 修改应用数据集
     */
    @SaCheckPermission("witdock:appDataset:edit")
    @Log(title = "应用数据集", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AppDatasetBo bo) {
        return toAjax(appDatesetService.updateByBo(bo));
    }

    /**
     * 删除应用数据集
     *
     * @param ids 主键串
     */
    @SaCheckPermission("witdock:appDataset:remove")
    @Log(title = "应用数据集", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(appDatesetService.deleteWithValidByIds(List.of(ids), true));
    }

    @SaCheckPermission("witdock:appDataset:remove")
    @DeleteMapping("delAppDatasetByBothId/{appId}/{datasetId}")
    public R<Void> delAppDatasetByBothId(@PathVariable String datasetId, @PathVariable String appId) {
        AppDatasetBo bo = new AppDatasetBo();
        bo.setAppId(Long.valueOf(appId));
        bo.setDatasetId(Long.valueOf(datasetId));
        appDatesetService.delByBothId(bo);
        return R.ok();
    }
}
