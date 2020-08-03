package com.dili.ia.controller;

import com.dili.ia.domain.InvoiceRecord;
import com.dili.ia.service.InvoiceRecordService;
import com.dili.ss.domain.BaseOutput;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-30 11:20:01.
 */
@Controller
@RequestMapping("/invoiceRecord")
public class InvoiceRecordController {
    @Autowired
    InvoiceRecordService invoiceRecordService;

    /**
     * 跳转到InvoiceRecord页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "invoiceRecord/index";
    }

    /**
     * 分页查询InvoiceRecord，返回easyui分页信息
     * @param invoiceRecord
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute InvoiceRecord invoiceRecord) throws Exception {
        return invoiceRecordService.listEasyuiPageByExample(invoiceRecord, true).toString();
    }

    /**
     * 新增InvoiceRecord
     * @param invoiceRecord
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute InvoiceRecord invoiceRecord) {
        invoiceRecordService.insertSelective(invoiceRecord);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改InvoiceRecord
     * @param invoiceRecord
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute InvoiceRecord invoiceRecord) {
        invoiceRecordService.updateSelective(invoiceRecord);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除InvoiceRecord
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        invoiceRecordService.delete(id);
        return BaseOutput.success("删除成功");
    }
}