package com.dili.ia.controller;

import com.dili.ia.domain.Stock;
import com.dili.ia.service.StockService;
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
@RequestMapping("/stock")
public class StockController {
    @Autowired
    StockService stockService;

    /**
     * 跳转到Stock页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stock/index";
    }

    /**
     * 分页查询Stock，返回easyui分页信息
     * @param stock
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute Stock stock) throws Exception {
        return stockService.listEasyuiPageByExample(stock, true).toString();
    }

    /**
     * 新增Stock
     * @param stock
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute Stock stock) {
        stockService.insertSelective(stock);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改Stock
     * @param stock
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute Stock stock) {
        stockService.updateSelective(stock);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除Stock
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        stockService.delete(id);
        return BaseOutput.success("删除成功");
    }
}