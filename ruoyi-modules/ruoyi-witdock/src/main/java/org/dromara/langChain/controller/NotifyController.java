package org.dromara.langChain.controller;


import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.langChain.dto.ParagraphsResult;
import org.dromara.langChain.service.INotifyService;
import org.dromara.witdock.domain.vo.DatasetDocVo;
import org.dromara.witdock.service.IDatasetDocParagraphsService;
import org.dromara.witdock.service.IDatasetDocService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lcapi/notify")
public class NotifyController extends BaseController {

    private final INotifyService notifyService;


    @PostMapping("/paragraphs")
    public R paragraphs(@RequestBody Map result) {

        notifyService.notifyParagraphs(result);
        return R.ok();
    }
}
