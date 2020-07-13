package com.dili.ia.controller;

import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.service.BoutiqueFeeOrderService;
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
 * This file was generated on 2020-07-13 10:49:05.
 */
@Controller
@RequestMapping("/boutiqueFeeOrder")
public class BoutiqueFeeOrderController {
    @Autowired
    BoutiqueFeeOrderService boutiqueFeeOrderService;

    /**
     * 跳转到BoutiqueFeeOrder页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "boutiqueFeeOrder/index";
    }

    /**
     * 分页查询BoutiqueFeeOrder，返回easyui分页信息
     * @param boutiqueFeeOrder
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute BoutiqueFeeOrder boutiqueFeeOrder) throws Exception {
        return boutiqueFeeOrderService.listEasyuiPageByExample(boutiqueFeeOrder, true).toString();
    }

    /**
     * 新增BoutiqueFeeOrder
     * @param boutiqueFeeOrder
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute BoutiqueFeeOrder boutiqueFeeOrder) {
        boutiqueFeeOrderService.insertSelective(boutiqueFeeOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改BoutiqueFeeOrder
     * @param boutiqueFeeOrder
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute BoutiqueFeeOrder boutiqueFeeOrder) {
        boutiqueFeeOrderService.updateSelective(boutiqueFeeOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除BoutiqueFeeOrder
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        boutiqueFeeOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}