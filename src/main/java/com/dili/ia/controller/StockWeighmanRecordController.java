package com.dili.ia.controller;

import com.dili.ia.domain.StockWeighmanRecord;
import com.dili.ia.service.StockWeighmanRecordService;
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
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/stockWeighmanRecord")
public class StockWeighmanRecordController {
    @Autowired
    StockWeighmanRecordService stockWeighmanRecordService;

    /**
     * 跳转到StockWeighmanRecord页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stockWeighmanRecord/index";
    }

    /**
     * 分页查询StockWeighmanRecord，返回easyui分页信息
     * @param stockWeighmanRecord
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockWeighmanRecord stockWeighmanRecord) throws Exception {
        return stockWeighmanRecordService.listEasyuiPageByExample(stockWeighmanRecord, true).toString();
    }

    /**
     * 新增StockWeighmanRecord
     * @param stockWeighmanRecord
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute StockWeighmanRecord stockWeighmanRecord) {
        stockWeighmanRecordService.insertSelective(stockWeighmanRecord);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改StockWeighmanRecord
     * @param stockWeighmanRecord
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute StockWeighmanRecord stockWeighmanRecord) {
        stockWeighmanRecordService.updateSelective(stockWeighmanRecord);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除StockWeighmanRecord
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        stockWeighmanRecordService.delete(id);
        return BaseOutput.success("删除成功");
    }
}