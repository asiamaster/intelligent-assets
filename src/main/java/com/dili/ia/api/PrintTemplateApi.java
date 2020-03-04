package com.dili.ia.api;

import com.dili.ia.domain.PrintTemplate;
import com.dili.ia.service.PrintTemplateService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * api
 */
@RestController
@RequestMapping("/api/printTemplate")
public class PrintTemplateApi {

    @Autowired
    private PrintTemplateService printTemplateService;

    /**
     *
     * @param printTemplate
     * @return
     */
    @RequestMapping("getTemplate")
    public BaseOutput get(PrintTemplate printTemplate) {
        BaseOutput<Object> success = BaseOutput.success();
        List<PrintTemplate> printTemplates = printTemplateService.listByExample(printTemplate);
        if (printTemplates != null && printTemplates.size() > 0) {
            success.setData(printTemplates.get(0));
        }
        return success;
    }
}
