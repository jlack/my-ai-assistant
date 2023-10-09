package org.dromara.langChain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParagraphsResult {
    /**
     * 文档id
     */
    private Long docId;
    /**
     * 分段内容
     */
    private List<Paragraphs> paragraphs;

    public class Paragraphs {
        private String content;
        private List<String> keyword;
    }
}


