package com.dili.ia.controller;

import com.dili.ia.domain.StockOut;
import com.dili.ia.service.StockOutService;
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
@RequestMapping("/stockOut")
public class StockOutController {
    @Autowired
    StockOutService stockOutService;

    /**
     * 跳转到StockOut页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stockOut/index";
    }

    /**
     * 分页查询StockOut，返回easyui分页信息
     * @param stockOut
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockOut stockOut) throws Exception {
        return stockOutService.listEasyuiPageByExample(stockOut, true).toString();
    }

    /**
     * 新增StockOut
     * @param stockOut
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute StockOut stockOut) {
        stockOutService.insertSelective(stockOut);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改StockOut
     * @param stockOut
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute StockOut stockOut) {
        stockOutService.updateSelective(stockOut);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除StockOut
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        stockOutService.delete(id);
        return BaseOutput.success("删除成功");
    }
}