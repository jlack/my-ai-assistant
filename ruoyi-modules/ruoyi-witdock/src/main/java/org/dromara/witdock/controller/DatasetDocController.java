package org.dromara.witdock.controller;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.service.ISysOssService;
import org.dromara.witdock.domain.bo.AddDocsBo;
import org.dromara.witdock.domain.bo.DatasetDocBo;
import org.dromara.witdock.domain.vo.DatasetDocVo;
import org.dromara.witdock.enums.DocStatusEnum;
import org.dromara.witdock.service.IDatasetDocService;
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

import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 数据集文档
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/witdock/doc")
public class DatasetDocController extends BaseController {

    private final IDatasetDocService datasetDocService;

    private final ISysOssService ossService;

    /**
     * 查询数据集文档列表
     */
    @SaCheckPermission("witdock:doc:list")
    @GetMapping("/list")
    public TableDataInfo<DatasetDocVo> list(DatasetDocBo bo, PageQuery pageQuery) {
        return datasetDocService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出数据集文档列表
     */
    @SaCheckPermission("witdock:doc:export")
    @Log(title = "数据集文档", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DatasetDocBo bo, HttpServletResponse response) {
        List<DatasetDocVo> list = datasetDocService.queryList(bo);
        ExcelUtil.exportExcel(list, "数据集文档", DatasetDocVo.class, response);
    }

    /**
     * 获取数据集文档详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("witdock:doc:query")
    @GetMapping("/{id}")
    public R<DatasetDocVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        DatasetDocVo docVo = datasetDocService.queryById(id);
        docVo.setOssVo(ossService.getById(docVo.getOssId()));
        docVo.setFileSize(ossService.getFileSize(docVo.getOssId()));
        return R.ok(docVo);
    }

    /**
     * 新增数据集文档
     */
    @SaCheckPermission("witdock:doc:add")
    @Log(title = "数据集文档", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DatasetDocBo bo) {
        Long ossId = bo.getOssId();
        SysOssVo ossVo = ossService.getById(ossId);
        bo.setDocName(ossVo.getOriginalName());
        bo.setCharNum(ossService.calculateCharNum(ossId));
        return toAjax(datasetDocService.insertByBo(bo));
    }

    /**
     * 新增数据集文档
     */
    @SaCheckPermission("witdock:doc:add")
    @Log(title = "数据集文档", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/addDocs")
    public R<Void> addDocs(@Validated(AddGroup.class) @RequestBody AddDocsBo bo) {
        for (String ossId : bo.getOssIds().split(StringUtils.SEPARATOR)) {
            SysOssVo ossVo = ossService.getById(Long.parseLong(ossId));
            DatasetDocBo insertBo = new DatasetDocBo();
            BeanUtil.copyProperties(bo, insertBo);
            insertBo.setOssId(Long.parseLong(ossId));
            insertBo.setDocName(ossVo.getOriginalName());
            insertBo.setStatus(DocStatusEnum.STATUS_0.getValue());
            insertBo.setCharNum(ossService.calculateCharNum(Long.valueOf(ossId)));
            datasetDocService.insertByBo(insertBo);
        }
        return R.ok("数据集文档新增成功");
    }

    /**
     * 修改数据集文档
     */
    @SaCheckPermission("witdock:doc:edit")
    @Log(title = "数据集文档", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DatasetDocBo bo) {
        return toAjax(datasetDocService.updateByBo(bo));
    }

    /**
     * 删除数据集文档
     *
     * @param ids 主键串
     */
    @SaCheckPermission("witdock:doc:remove")
    @Log(title = "数据集文档", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(datasetDocService.deleteWithValidByIds(List.of(ids), true));
    }
}
