package org.dromara.witdock.controller;

import java.util.List;

import cn.hutool.core.thread.ThreadUtil;
import com.dtflys.forest.utils.AsyncUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.witdock.domain.MessageInfo;
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
import org.dromara.witdock.domain.vo.MessageInfoVo;
import org.dromara.witdock.domain.bo.MessageInfoBo;
import org.dromara.witdock.service.IMessageInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 对话消息
 *
 * @author Lion Li
 * @date 2023-09-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/witdock/messageInfo")
public class MessageInfoController extends BaseController {

    private final IMessageInfoService messageInfoService;

    /**
     * 查询对话消息列表
     */
    @SaCheckPermission("witdock:messageInfo:list")
    @GetMapping("/list")
    public TableDataInfo<MessageInfoVo> list(MessageInfoBo bo, PageQuery pageQuery) {
        TableDataInfo<MessageInfoVo> messageInfoVoTableDataInfo = messageInfoService.queryPageList(bo, pageQuery);
        //测试用 异步2秒后回复
        AsyncUtil.execute(() -> {
            ThreadUtil.sleep(3000L);
        });
        return messageInfoVoTableDataInfo;
    }

    /**
     * 导出对话消息列表
     */
    @SaCheckPermission("witdock:messageInfo:export")
    @Log(title = "对话消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MessageInfoBo bo, HttpServletResponse response) {
        List<MessageInfoVo> list = messageInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "对话消息", MessageInfoVo.class, response);
    }

    /**
     * 获取对话消息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("witdock:messageInfo:query")
    @GetMapping("/{id}")
    public R<MessageInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(messageInfoService.queryById(id));
    }

    /**
     * 新增对话消息
     */
    @SaCheckPermission("witdock:messageInfo:add")
    @Log(title = "对话消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MessageInfoBo bo) {
        return toAjax(messageInfoService.insertByBo(bo));
    }

    /**
     * 修改对话消息
     */
    @SaCheckPermission("witdock:messageInfo:edit")
    @Log(title = "对话消息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MessageInfoBo bo) {
        return toAjax(messageInfoService.updateByBo(bo));
    }

    /**
     * 删除对话消息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("witdock:messageInfo:remove")
    @Log(title = "对话消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(messageInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
