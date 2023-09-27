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
import org.dromara.witdock.domain.vo.ConversationInfoVo;
import org.dromara.witdock.domain.bo.ConversationInfoBo;
import org.dromara.witdock.service.IConversationInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 会话
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/witdock/conversationInfo")
public class ConversationInfoController extends BaseController {

    private final IConversationInfoService conversationInfoService;

    /**
     * 查询会话列表
     */
    @SaCheckPermission("witdock:conversationInfo:list")
    @GetMapping("/list")
    public TableDataInfo<ConversationInfoVo> list(ConversationInfoBo bo, PageQuery pageQuery) {
        return conversationInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出会话列表
     */
    @SaCheckPermission("witdock:conversationInfo:export")
    @Log(title = "会话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ConversationInfoBo bo, HttpServletResponse response) {
        List<ConversationInfoVo> list = conversationInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "会话", ConversationInfoVo.class, response);
    }

    /**
     * 获取会话详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("witdock:conversationInfo:query")
    @GetMapping("/{id}")
    public R<ConversationInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(conversationInfoService.queryById(id));
    }

    /**
     * 新增会话
     */
    @SaCheckPermission("witdock:conversationInfo:add")
    @Log(title = "会话", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ConversationInfoBo bo) {
        return toAjax(conversationInfoService.insertByBo(bo));
    }

    /**
     * 修改会话
     */
    @SaCheckPermission("witdock:conversationInfo:edit")
    @Log(title = "会话", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ConversationInfoBo bo) {
        return toAjax(conversationInfoService.updateByBo(bo));
    }

    /**
     * 删除会话
     *
     * @param ids 主键串
     */
    @SaCheckPermission("witdock:conversationInfo:remove")
    @Log(title = "会话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(conversationInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
