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
import org.dromara.witdock.domain.vo.AppInfoVo;
import org.dromara.witdock.domain.bo.AppInfoBo;
import org.dromara.witdock.service.IAppInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 构建应用
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/witdock/app")
public class AppInfoController extends BaseController {

    private final IAppInfoService appInfoService;

    /**
     * 查询构建应用列表
     */
    @SaCheckPermission("witdock:app:list")
    @GetMapping("/list")
    public TableDataInfo<AppInfoVo> list(AppInfoBo bo, PageQuery pageQuery) {
        return appInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出构建应用列表
     */
    @SaCheckPermission("witdock:app:export")
    @Log(title = "构建应用", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AppInfoBo bo, HttpServletResponse response) {
        List<AppInfoVo> list = appInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "构建应用", AppInfoVo.class, response);
    }

    /**
     * 获取构建应用详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("witdock:app:query")
    @GetMapping("/{id}")
    public R<AppInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(appInfoService.queryById(id));
    }

    /**
     * 新增构建应用
     */
    @SaCheckPermission("witdock:app:add")
    @Log(title = "构建应用", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AppInfoBo bo) {
        return toAjax(appInfoService.insertByBo(bo));
    }

    /**
     * 修改构建应用
     */
    @SaCheckPermission("witdock:app:edit")
    @Log(title = "构建应用", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AppInfoBo bo) {
        return toAjax(appInfoService.updateByBo(bo));
    }

    /**
     * 删除构建应用
     *
     * @param ids 主键串
     */
    @SaCheckPermission("witdock:app:remove")
    @Log(title = "构建应用", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(appInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
