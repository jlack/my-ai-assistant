package org.dromara.witdock.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.witdock.domain.DatasetDocParagraphs;

@Data
@AutoMapper(target = DatasetDocParagraphs.class, reverseConvertGenerate = false)
public class DocParaSplitBo {
    /**
     * 已上传的待分段的文件的ossId
     */
    String ossIds;


    /**
     * 分段最大尺寸，按token数计算
     */
    Integer maxSegmentSizeInTokens;
}
