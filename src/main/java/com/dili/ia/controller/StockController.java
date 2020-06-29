package com.dili.ia.controller;

import com.dili.ia.domain.Stock;
import com.dili.ia.service.StockService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/stock/stock")
public class StockController {
    @Autowired
    StockService stockService;

    /**
     * 跳转到出库页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/out.html", method = RequestMethod.GET)
    public String out(ModelMap modelMap) {
        return "stock/stock/out";
    }
    
    /**
     * 客户库存
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stock/stock/index";
    }

    /**
     * 分页查询Stock，返回easyui分页信息
     * @param stock
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute Stock stock) throws Exception {
    	System.err.println(stockService.listEasyuiPageByExample(stock, true).toString());
        return stockService.listEasyuiPageByExample(stock, true).toString();
    }
    
    /**
     * 提交出库单
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/stockOut.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput stockOut(Long stockId,Long weight,Long quantity,String notes) {
    	stockService.stockOut(stockId, weight, quantity,notes);
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("新增成功");
    }
    
    /**
     * 冷库出库单列表
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/outList.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String outList(ModelMap modelMap, Long customerId,Long categoryId,Long assetsId) {
    	
    	//stockService.stockOut(stockId, weight, quantity,notes);
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return "stock/stock/outList";
    }

}