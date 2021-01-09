package com.dili.ia.controller;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ia.domain.dto.StockOutQuery;
import com.dili.ia.service.DataAuthService;
import com.dili.ia.service.StockOutService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/stock/stockOut")
public class StockOutController {
    @Autowired
    private StockOutService stockOutService;
    @Autowired
    private DataAuthService dataAuthService;

    /**
     * 跳转到StockOut页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stock/stockOut/index";
    }
    
    /**
     * 跳转到StockOut页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap,String code) {
    	modelMap.put("stockOut", stockOutService.getStockOut(code));
        return "stock/stockOut/view";
    }

    /**
     * 分页查询StockOut，返回easyui分页信息
     * @param stockOut
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockOutQuery stockOut) throws Exception {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		stockOut.setMarketId(userTicket.getFirmId());
		List<Long> departmentIdList = dataAuthService.getDepartmentDataAuth(userTicket);
		if (CollectionUtils.isEmpty(departmentIdList)) {
			return new EasyuiPageOutput(0L, Collections.emptyList()).toString();
		}
		stockOut.setDepIds(departmentIdList);
        return stockOutService.listEasyuiPageByExample(stockOut, true).toString();
    }

}