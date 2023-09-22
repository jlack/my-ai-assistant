package org.dromara.witdock.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.file.FileUtils;
import org.dromara.common.oss.core.OssClient;
import org.dromara.common.oss.factory.OssFactory;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.service.ISysOssService;
import org.dromara.witdock.domain.DatasetInfo;
import org.dromara.witdock.domain.bo.AddDocsBo;
import org.dromara.witdock.domain.bo.AddDsWithDocsBo;
import org.dromara.witdock.domain.bo.DatasetDocBo;
import org.dromara.witdock.enums.DocStatusEnum;
import org.dromara.witdock.service.IDatasetDocService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/witdock/dataset")
public class DatasetInfoController extends BaseController {

    private final IDatasetInfoService datasetInfoService;

    @Autowired
    private IDatasetDocService datasetDocService;

    @Autowired
    private ISysOssService ossService;

    /**
     * 查询数据集列表
     */
    @SaCheckPermission("witdock:dataset:list")
    @GetMapping("/list")
    public TableDataInfo<DatasetInfoVo> list(DatasetInfoBo bo, PageQuery pageQuery) {
        return datasetInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出数据集列表
     */
    @SaCheckPermission("witdock:dataset:export")
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
    @SaCheckPermission("witdock:dataset:query")
    @GetMapping("/{id}")
    public R<DatasetInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(datasetInfoService.queryById(id));
    }

    /**
     * 新增数据集
     */
    @SaCheckPermission("witdock:dataset:add")
    @Log(title = "数据集", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DatasetInfoBo bo) {
        return toAjax(datasetInfoService.insertByBo(bo));
    }

    /**
     * 新增数据集
     */
    @SaCheckPermission("witdock:dataset:add")
    @Log(title = "数据集", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/addWithDocs")
    public R<Void> addWithDocs(@Validated(AddGroup.class) @RequestBody AddDsWithDocsBo bo) {

        DatasetInfoBo insertBo = new DatasetInfoBo();
        BeanUtil.copyProperties(bo, insertBo);
        datasetInfoService.insertByBo(insertBo);

        Long datasetId = insertBo.getId();

        for (String ossId : bo.getOssIds().split(StringUtils.SEPARATOR)) {
            SysOssVo ossVo = ossService.getById(Long.parseLong(ossId));

            OssClient storage = OssFactory.instance(ossVo.getService());
            DatasetDocBo insertDocBo = new DatasetDocBo();
            BeanUtil.copyProperties(bo, insertDocBo);
            insertDocBo.setDatasetId(datasetId);
            insertDocBo.setOssId(Long.parseLong(ossId));
            insertDocBo.setDocName(ossVo.getOriginalName());
            insertDocBo.setStatus(DocStatusEnum.STATUS_0.getValue());

            try (InputStream inputStream = storage.getObjectContent(ossVo.getUrl())) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, outputStream);
                byte[] fileBytes = outputStream.toByteArray();

                String fileType = ossVo.getFileSuffix();
                if (fileType.equalsIgnoreCase(".txt") || fileType.equalsIgnoreCase(".html") || fileType.equalsIgnoreCase(".md")) {
                    String fileContent = new String(fileBytes, StandardCharsets.UTF_8);
                    insertDocBo.setCharNum((long) fileContent.length());
                } else if (fileType.equalsIgnoreCase(".pdf")) {
                    PDDocument document = PDDocument.load(fileBytes);
                    PDFTextStripper stripper = new PDFTextStripper();
                    String text = stripper.getText(document);

                    int charNum;
                    if (StringUtils.isNotEmpty(text)) {
                        charNum = text.length();
                    } else {
                        charNum = 0;
                    }
                    insertDocBo.setCharNum((long) charNum);

                    document.close();
                } else if (fileType.equalsIgnoreCase(".xlsx")) {
                    Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileBytes));
                    int charNum = 0;
                    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                        Sheet sheet = workbook.getSheetAt(i);
                        for (Row row : sheet) {
                            for (Cell cell : row) {
                                CellType cellType = cell.getCellType();
                                if (cellType == CellType.STRING) {
                                    String cellValue = cell.getStringCellValue();
                                    charNum += cellValue.length();
                                } else if (cellType == CellType.NUMERIC) {
                                    double numericCellValue = cell.getNumericCellValue();
                                    String cellValue = String.valueOf(numericCellValue);
                                    charNum += cellValue.length();
                                } else if (cellType == CellType.FORMULA) {
                                    String formulaValue = cell.getCellFormula();
                                    charNum += formulaValue.length();
                                }
                            }
                        }
                    }
                    workbook.close();
                    insertDocBo.setCharNum((long) charNum);
                } else if (fileType.equalsIgnoreCase(".csv")) {
                    CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream, StandardCharsets.UTF_8), CSVFormat.DEFAULT);

                    int charNum = 0;

                    for (CSVRecord record : csvParser) {
                        for (String value : record) {
                            if (StringUtils.isNotEmpty(value)) {
                                charNum += value.length();
                            }
                        }
                    }
                    insertDocBo.setCharNum((long) charNum);

                } else if (fileType.equalsIgnoreCase(".docx")) {
                    XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(fileBytes));
                    int charNum = 0;
                    for (XWPFParagraph paragraph : document.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            charNum += run.getText(0).length();
                        }
                    }
                    document.close();
                    insertDocBo.setCharNum((long) charNum);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            datasetDocService.insertByBo(insertDocBo);
        }
        return R.ok();
    }

    /**
     * 修改数据集
     */
    @SaCheckPermission("witdock:dataset:edit")
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
    @SaCheckPermission("witdock:dataset:remove")
    @Log(title = "数据集", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(datasetInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
