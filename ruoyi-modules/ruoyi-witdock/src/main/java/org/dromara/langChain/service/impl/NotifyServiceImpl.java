package org.dromara.langChain.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.langChain.dto.ParagraphsResult;
import org.dromara.langChain.service.INotifyService;
import org.dromara.witdock.domain.DatasetDoc;
import org.dromara.witdock.domain.DatasetDocParagraphs;
import org.dromara.witdock.mapper.DatasetDocMapper;
import org.dromara.witdock.mapper.DatasetDocParagraphsMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements INotifyService {

    private final DatasetDocMapper datasetDocMapper;
    private final DatasetDocParagraphsMapper datasetDocParagraphsMapper;

    @Override
    public void notifyParagraphs(Map map) {
        Long docId = Convert.toLong(map.get("doc_id"));
        DatasetDoc datasetDoc = datasetDocMapper.selectById(docId);
        if (ObjectUtil.isNull(datasetDoc)) {
            throw new SecurityException("文档不存在");
        }

        List<DatasetDocParagraphs> ddpList = new ArrayList<>();
        List<Map<String, Object>> paragraphs = (List<Map<String, Object>>) map.get("paragraphs");

        int sno = 0;
        for (Map<String, Object> item : paragraphs) {
            String content = item.get("content").toString();
            DatasetDocParagraphs ddp = new DatasetDocParagraphs();
            ddp.setDocId(docId);
            ddp.setSno(++sno);
            ddp.setStatus("active");
            ddp.setContent(content);
            ddp.setCharNum(Convert.toLong(content.length()));
            ddpList.add(ddp);
        }
        datasetDocParagraphsMapper.insertBatch(ddpList);
    }
}
