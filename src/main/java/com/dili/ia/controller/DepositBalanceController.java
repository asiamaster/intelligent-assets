package com.dili.ia.controller;

import com.dili.ia.domain.dto.DepositBalanceQuery;
import com.dili.ia.service.DepositBalanceService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.MoneyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-29 15:15:26.
 */
@Controller
@RequestMapping("/depositBalance")
public class DepositBalanceController {
    @Autowired
    DepositBalanceService depositBalanceService;

    /**
     * 跳转到DepositBalance页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "depositBalance/index";
    }

    /**
     * 分页查询DepositBalance，返回easyui分页信息
     * @param depositBalance
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute DepositBalanceQuery depositBalance) throws Exception {
        return  depositBalanceService.listEasyuiPageByExample(depositBalance, true).toString();
    }

    /**
     * 计算【保证金余额】
     * @param depositBalanceQuery
     * @return String
     */
    @RequestMapping(value="/getTotalBalance.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput<String> getTotalBalance(DepositBalanceQuery depositBalanceQuery){
        Long totalBalance = depositBalanceService.sumBalance(depositBalanceQuery);
        return BaseOutput.success().setData(MoneyUtils.centToYuan(totalBalance));
    }
}