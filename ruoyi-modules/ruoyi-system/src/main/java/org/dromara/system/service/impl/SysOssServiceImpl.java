package org.dromara.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.dromara.common.core.constant.CacheNames;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.OssService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.file.FileUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.oss.core.OssClient;
import org.dromara.common.oss.entity.UploadResult;
import org.dromara.common.oss.enumd.AccessPolicyType;
import org.dromara.common.oss.factory.OssFactory;
import org.dromara.system.domain.SysOss;
import org.dromara.system.domain.bo.SysOssBo;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.mapper.SysOssMapper;
import org.dromara.system.service.ISysOssService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 文件上传 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysOssServiceImpl implements ISysOssService, OssService {

    private final SysOssMapper baseMapper;

    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        Page<SysOssVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SysOssVo> filterResult = StreamUtils.toList(result.getRecords(), this::matchingUrl);
        result.setRecords(filterResult);
        return TableDataInfo.build(result);
    }

    @Override
    public List<SysOssVo> listByIds(Collection<Long> ossIds) {
        List<SysOssVo> list = new ArrayList<>();
        for (Long id : ossIds) {
            SysOssVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                list.add(this.matchingUrl(vo));
            }
        }
        return list;
    }

    @Override
    public String selectUrlByIds(String ossIds) {
        List<String> list = new ArrayList<>();
        for (Long id : StringUtils.splitTo(ossIds, Convert::toLong)) {
            SysOssVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                list.add(this.matchingUrl(vo).getUrl());
            }
        }
        return String.join(StringUtils.SEPARATOR, list);
    }

    private LambdaQueryWrapper<SysOss> buildQueryWrapper(SysOssBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOss> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), SysOss::getFileName, bo.getFileName());
        lqw.like(StringUtils.isNotBlank(bo.getOriginalName()), SysOss::getOriginalName, bo.getOriginalName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSuffix()), SysOss::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), SysOss::getUrl, bo.getUrl());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            SysOss::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(ObjectUtil.isNotNull(bo.getCreateBy()), SysOss::getCreateBy, bo.getCreateBy());
        lqw.eq(StringUtils.isNotBlank(bo.getService()), SysOss::getService, bo.getService());
        lqw.orderByAsc(SysOss::getOssId);
        return lqw;
    }

    @Cacheable(cacheNames = CacheNames.SYS_OSS, key = "#ossId")
    @Override
    public SysOssVo getById(Long ossId) {
        return baseMapper.selectVoById(ossId);
    }

    @Override
    public void download(Long ossId, HttpServletResponse response) throws IOException {
        SysOssVo sysOss = SpringUtils.getAopProxy(this).getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        OssClient storage = OssFactory.instance(sysOss.getService());
        try(InputStream inputStream = storage.getObjectContent(sysOss.getUrl())) {
            int available = inputStream.available();
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            response.setContentLength(available);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public SysOssVo upload(MultipartFile file) {
        String originalfileName = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult);
    }

    @Override
    public SysOssVo upload(File file) {
        String originalfileName = file.getName();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult = storage.uploadSuffix(file, suffix);
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult);
    }

    @NotNull
    private SysOssVo buildResultEntity(String originalfileName, String suffix, String configKey, UploadResult uploadResult) {
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalfileName);
        oss.setService(configKey);
        baseMapper.insert(oss);
        SysOssVo sysOssVo = MapstructUtils.convert(oss, SysOssVo.class);
        return this.matchingUrl(sysOssVo);
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysOss> list = baseMapper.selectBatchIds(ids);
        for (SysOss sysOss : list) {
            OssClient storage = OssFactory.instance(sysOss.getService());
            storage.delete(sysOss.getUrl());
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Long calculateCharNum(Long ossId) {
        SysOssVo ossVo = baseMapper.selectVoById(ossId);
        Long charNum = 0L;
        try {
            OssClient storage = OssFactory.instance(ossVo.getService());
            try (InputStream inputStream = storage.getObjectContent(ossVo.getUrl())) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, outputStream);
                byte[] fileBytes = outputStream.toByteArray();
                String fileType = ossVo.getFileSuffix();

                // 根据文件类型进行字符数计算
                if (fileType.equalsIgnoreCase(".txt") || fileType.equalsIgnoreCase(".html") || fileType.equalsIgnoreCase(".md")) {
                    String fileContent = new String(fileBytes, StandardCharsets.UTF_8);
                    charNum = Long.valueOf(fileContent.length());
                } else if (fileType.equalsIgnoreCase(".pdf")) {
                    PDDocument document = PDDocument.load(fileBytes);
                    PDFTextStripper stripper = new PDFTextStripper();
                    String text = stripper.getText(document);

                    if (StringUtils.isNotEmpty(text)) {
                        charNum = Long.valueOf(text.length());
                    }

                    document.close();
                } else if (fileType.equalsIgnoreCase(".xlsx")) {
                    Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileBytes));
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
                } else if (fileType.equalsIgnoreCase(".csv")) {
                    CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream, StandardCharsets.UTF_8), CSVFormat.DEFAULT);

                    for (CSVRecord record : csvParser) {
                        for (String value : record) {
                            if (StringUtils.isNotEmpty(value)) {
                                charNum += value.length();
                            }
                        }
                    }
                } else if (fileType.equalsIgnoreCase(".docx")) {
                    XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(fileBytes));

                    for (XWPFParagraph paragraph : document.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            charNum += run.getText(0).length();
                        }
                    }
                    document.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return charNum;
    }

    /**
     * 匹配Url
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private SysOssVo matchingUrl(SysOssVo oss) {
        OssClient storage = OssFactory.instance(oss.getService());
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
            oss.setUrl(storage.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }


}
