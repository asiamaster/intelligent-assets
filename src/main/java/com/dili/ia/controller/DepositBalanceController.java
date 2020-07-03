package com.dili.ia.controller;

import com.dili.ia.domain.DepositBalance;
import com.dili.ia.service.DepositBalanceService;
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
    public @ResponseBody String listPage(@ModelAttribute DepositBalance depositBalance) throws Exception {
        return depositBalanceService.listEasyuiPageByExample(depositBalance, true).toString();
    }

    /**
     * 新增DepositBalance
     * @param depositBalance
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute DepositBalance depositBalance) {
        depositBalanceService.insertSelective(depositBalance);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改DepositBalance
     * @param depositBalance
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute DepositBalance depositBalance) {
        depositBalanceService.updateSelective(depositBalance);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除DepositBalance
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        depositBalanceService.delete(id);
        return BaseOutput.success("删除成功");
    }
}