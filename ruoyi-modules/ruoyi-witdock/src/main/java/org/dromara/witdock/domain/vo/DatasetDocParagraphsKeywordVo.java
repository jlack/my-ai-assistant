package org.dromara.witdock.domain.vo;

import org.dromara.witdock.domain.DatasetDocParagraphsKeyword;
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
 * 段落关键字表视图对象 dataset_doc_paragraphs_keyword
 *
 * @author zyh
 * @date 2023-10-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DatasetDocParagraphsKeyword.class)
public class DatasetDocParagraphsKeywordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 段落id
     */
    @ExcelProperty(value = "文档id")
    private Long paragraphsId;

    /**
     * 关键字
     */
    @ExcelProperty(value = "关键字")
    private String keyword;


}
