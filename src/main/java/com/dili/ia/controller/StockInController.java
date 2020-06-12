package com.dili.ia.controller;

import com.dili.ia.domain.StockIn;
import com.dili.ia.service.StockInService;
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
@RequestMapping("/stockIn")
public class StockInController {
    @Autowired
    StockInService stockInService;

    /**
     * 跳转到StockIn页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stockIn/index";
    }

    /**
     * 分页查询StockIn，返回easyui分页信息
     * @param stockIn
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockIn stockIn) throws Exception {
        return stockInService.listEasyuiPageByExample(stockIn, true).toString();
    }

    /**
     * 新增StockIn
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute StockIn stockIn) {
        stockInService.insertSelective(stockIn);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改StockIn
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute StockIn stockIn) {
        stockInService.updateSelective(stockIn);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除StockIn
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        stockInService.delete(id);
        return BaseOutput.success("删除成功");
    }
}