package com.dili.ia.controller;

import com.dili.ia.domain.ChargebackOrder;
import com.dili.ia.service.ChargebackOrderService;
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
 * This file was generated on 2020-06-18 11:36:18.
 */
@Controller
@RequestMapping("/chargebackOrder")
public class ChargebackOrderController {
    @Autowired
    ChargebackOrderService chargebackOrderService;

    /**
     * 跳转到ChargebackOrder页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "chargebackOrder/index";
    }

    /**
     * 分页查询ChargebackOrder，返回easyui分页信息
     * @param chargebackOrder
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute ChargebackOrder chargebackOrder) throws Exception {
        return chargebackOrderService.listEasyuiPageByExample(chargebackOrder, true).toString();
    }

    /**
     * 新增ChargebackOrder
     * @param chargebackOrder
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute ChargebackOrder chargebackOrder) {
        chargebackOrderService.insertSelective(chargebackOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改ChargebackOrder
     * @param chargebackOrder
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute ChargebackOrder chargebackOrder) {
        chargebackOrderService.updateSelective(chargebackOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除ChargebackOrder
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        chargebackOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}