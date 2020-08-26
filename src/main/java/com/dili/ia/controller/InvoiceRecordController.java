package com.dili.ia.controller;

import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.InvoiceRecord;
import com.dili.ia.domain.dto.InvoiceRecordDto;
import com.dili.ia.service.AssetsLeaseOrderService;
import com.dili.ia.service.InvoiceRecordService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 开票记录
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-30 11:20:01.
 */
@Controller
@RequestMapping("/invoiceRecord")
public class InvoiceRecordController {
    @Autowired
    private InvoiceRecordService invoiceRecordService;

    /**
     * 跳转到InvoiceRecord页面
     * @param modelMap
     * @return String
     */
    @GetMapping(value="/index.html")
    public String index(ModelMap modelMap) {
        return "invoiceRecord/index";
    }

    /**
     * 分页查询InvoiceRecord，返回easyui分页信息
     * @param invoiceRecord
     * @return String
     * @throws Exception
     */
    @PostMapping(value="/listPage.action")
    public @ResponseBody String listPage(InvoiceRecordDto invoiceRecord) throws Exception {
        return invoiceRecordService.listEasyuiPageByExample(invoiceRecord, true).toString();
    }

    /**
     * 新增InvoiceRecord
     * @param invoiceRecord
     * @return BaseOutput
     */
    @PostMapping(value="/insert.action")
    public @ResponseBody BaseOutput insert(InvoiceRecord invoiceRecord) {
        try {
            invoiceRecordService.insertInvoiceRecord(invoiceRecord);
            return BaseOutput.success("新增成功");
        } catch (Exception e) {
            return BaseOutput.failure("新增失败:"+e.getMessage());
        }

    }

    /**
     * 修改InvoiceRecord
     * @param invoiceRecord
     * @return BaseOutput
     */
    @PostMapping(value="/update.action")
    public @ResponseBody BaseOutput update(InvoiceRecord invoiceRecord) {
        try {
            invoiceRecordService.updateSelective(invoiceRecord);
            return BaseOutput.success("修改成功");
        } catch (Exception e) {
            return BaseOutput.failure("修改失败:"+e.getMessage());
        }

    }

    /**
     * 删除InvoiceRecord
     * @param id
     * @return BaseOutput
     */
    @PostMapping(value="/delete.action")
    public @ResponseBody BaseOutput delete(@RequestParam Long id, @RequestParam String businessKey) {
        try {
            invoiceRecordService.delete(id, businessKey);
            return BaseOutput.success("删除成功");
        } catch (Exception e) {
            return BaseOutput.failure("删除失败:"+e.getMessage());
        }

    }
}