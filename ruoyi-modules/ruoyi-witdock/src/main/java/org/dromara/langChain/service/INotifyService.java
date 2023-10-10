package org.dromara.langChain.service;

import org.dromara.langChain.dto.ParagraphsResult;

import java.util.Map;

public interface INotifyService {
    void notifyParagraphs(Map result);
}
