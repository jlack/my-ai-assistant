package org.dromara.witdock.domain.vo;

import org.dromara.witdock.domain.DatasetDocParagraphs;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 文档段落表视图对象 dataset_doc_paragraphs
 *
 * @author zyh
 * @date 2023-10-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DatasetDocParagraphs.class)
public class DatasetDocParagraphsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 文档id
     */
    @ExcelProperty(value = "文档id")
    private Long docId;

    /**
     * 段落编号
     */
    @ExcelProperty(value = "段落编号")
    private int sno;

    /**
     * 状态（active,inactive）可用、禁用
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "a=ctive,inactive")
    private String status;

    /**
     * 段落内容
     */
    @ExcelProperty(value = "段落内容")
    private String content;

    /**
     * 字数
     */
    @ExcelProperty(value = "字数")
    private Long charNum;


}
