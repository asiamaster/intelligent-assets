package com.dili.ia.controller;

import com.alibaba.fastjson.JSONArray;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.dto.StockInDto;
import com.dili.ia.domain.dto.StockInQueryDto;
import com.dili.ia.domain.dto.StockInRefundDto;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.service.StockInService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/stock/stockIn")
public class StockInController {
    @Autowired
    StockInService stockInService;
    
    /**
     * 跳转到StockIn页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap,Integer type) {
    	modelMap.put("type", type == null ? 1:type);
        return "stock/add";
    }
    
    /**
     * 跳转到StockIn页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stock/index";
    }
    //http://ia.diligrp.com:8381/stock/stockIn/indetail.html
    
    /**
     * 分页查询StockIn，返回easyui分页信息
     * @param stockIn
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockInQueryDto stockIn) throws Exception {
    	
        return stockInService.listPageAction(stockIn);
    }

    /**
     * 新增StockIn
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    //@BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput insert(@RequestBody @Validated StockInDto stockInDto) {
        stockInService.createStockIn(stockInDto);
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("新增成功");
    }
    
    /**
     * 查看
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/view.html", method = {RequestMethod.GET, RequestMethod.POST})
    //@BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    public String view(ModelMap modelMap,String code) {
    	modelMap.put("stockIn",stockInService.view(code));
        return "stock/view";
    }

    /**
     * 修改StockIn
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/update.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String update(ModelMap modelMap,String code) {
    	modelMap.put("stockIn",stockInService.view(code));
        return "stock/update";
    }
    
    /**
     * 修改StockIn
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@RequestBody @Validated StockInDto stockIn) {
    	stockInService.updateStockIn(stockIn);
        return BaseOutput.success();
    }
    
    /**
     * 取消(逻辑删除)StockIn
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancel(String code) {
    	//throw new BusinessException("", "");
        stockInService.cancel(code);
        return BaseOutput.success("取消成功");
    }
    
    /**
     * 提交入库单
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput submit(String code) {
        stockInService.submit(code);
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("提交成功");
    }
    
    /**
     * 撤回入库单
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/remove.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput remove(String code) {
        stockInService.withdraw(code);
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("撤回成功");
    }
    
    /**
     * 结算入库单
     * @param stockIn
     * @return BaseOutput
     */
	/*@RequestMapping(value="/pay.action", method = {RequestMethod.GET, RequestMethod.POST})
	@BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
	public @ResponseBody BaseOutput pay(@Validated PayInfoDto payInfoDto) {
	    stockInService.pay(payInfoDto);
	    return BaseOutput.success("支付成功");
	}*/
    
    /**
     * 退款
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/refund.action", method = {RequestMethod.GET, RequestMethod.POST})
    //@BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput refund(@Validated StockInRefundDto stockInRefundDto) {	        //throw new BusinessException("2000", "errorCode");
    	stockInService.refund(stockInRefundDto);
    	return BaseOutput.success("退款成功");
    }
}