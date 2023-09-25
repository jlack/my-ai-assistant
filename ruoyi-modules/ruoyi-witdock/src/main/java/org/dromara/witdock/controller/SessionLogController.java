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
import org.dromara.witdock.domain.vo.SessionLogVo;
import org.dromara.witdock.domain.bo.SessionLogBo;
import org.dromara.witdock.service.ISessionLogService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 会话日志表
 *
 * @author Lion Li
 * @date 2023-09-25
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/log")
public class SessionLogController extends BaseController {

    private final ISessionLogService sessionLogService;

    /**
     * 查询会话日志表列表
     */
    @SaCheckPermission("system:log:list")
    @GetMapping("/list")
    public TableDataInfo<SessionLogVo> list(SessionLogBo bo, PageQuery pageQuery) {
        return sessionLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出会话日志表列表
     */
    @SaCheckPermission("system:log:export")
    @Log(title = "会话日志表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SessionLogBo bo, HttpServletResponse response) {
        List<SessionLogVo> list = sessionLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "会话日志表", SessionLogVo.class, response);
    }

    /**
     * 获取会话日志表详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:log:query")
    @GetMapping("/{id}")
    public R<SessionLogVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(sessionLogService.queryById(id));
    }

    /**
     * 新增会话日志表
     */
    @SaCheckPermission("system:log:add")
    @Log(title = "会话日志表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SessionLogBo bo) {
        return toAjax(sessionLogService.insertByBo(bo));
    }

    /**
     * 修改会话日志表
     */
    @SaCheckPermission("system:log:edit")
    @Log(title = "会话日志表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SessionLogBo bo) {
        return toAjax(sessionLogService.updateByBo(bo));
    }

    /**
     * 删除会话日志表
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:log:remove")
    @Log(title = "会话日志表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(sessionLogService.deleteWithValidByIds(List.of(ids), true));
    }
}
