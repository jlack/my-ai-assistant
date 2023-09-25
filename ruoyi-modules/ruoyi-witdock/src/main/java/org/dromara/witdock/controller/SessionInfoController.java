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
import org.dromara.witdock.domain.vo.SessionInfoVo;
import org.dromara.witdock.domain.bo.SessionInfoBo;
import org.dromara.witdock.service.ISessionInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 会话
 *
 * @author liaoyusheng
 * @date 2023-09-25
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/witdock/session")
public class SessionInfoController extends BaseController {

    private final ISessionInfoService sessionInfoService;

    /**
     * 查询会话列表
     */
    @SaCheckPermission("witdock:session:list")
    @GetMapping("/list")
    public TableDataInfo<SessionInfoVo> list(SessionInfoBo bo, PageQuery pageQuery) {
        return sessionInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出会话列表
     */
    @SaCheckPermission("witdock:session:export")
    @Log(title = "会话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SessionInfoBo bo, HttpServletResponse response) {
        List<SessionInfoVo> list = sessionInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "会话", SessionInfoVo.class, response);
    }

    /**
     * 获取会话详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("witdock:session:query")
    @GetMapping("/{id}")
    public R<SessionInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(sessionInfoService.queryById(id));
    }

    /**
     * 新增会话
     */
    @SaCheckPermission("witdock:session:add")
    @Log(title = "会话", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SessionInfoBo bo) {
        return toAjax(sessionInfoService.insertByBo(bo));
    }

    /**
     * 修改会话
     */
    @SaCheckPermission("witdock:session:edit")
    @Log(title = "会话", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SessionInfoBo bo) {
        return toAjax(sessionInfoService.updateByBo(bo));
    }

    /**
     * 删除会话
     *
     * @param ids 主键串
     */
    @SaCheckPermission("witdock:session:remove")
    @Log(title = "会话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(sessionInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
