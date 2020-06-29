package com.dili.ia.controller;

import com.dili.ia.domain.StockInDetail;
import com.dili.ia.service.StockInDetailService;
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
@RequestMapping("/stock/stockInDetail")
public class StockInDetailController {
    @Autowired
    StockInDetailService stockInDetailService;

    /**
     * 跳转到StockInDetail页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stock/stockInDetail/index";
    }

    /**
     * 分页查询StockInDetail，返回easyui分页信息
     * @param stockInDetail
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockInDetail stockInDetail) throws Exception {
        return stockInDetailService.listEasyuiPageByExample(stockInDetail, true).toString();
    }

   
}