package com.dili.ia.controller;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.service.CustomerMeterService;
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
 * This file was generated on 2020-06-12 11:35:07.
 */
@Controller
@RequestMapping("/customerMeter")
public class CustomerMeterController {
    @Autowired
    CustomerMeterService customerMeterService;

    /**
     * 跳转到CustomerMeter页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerMeter/index";
    }

    /**
     * 分页查询CustomerMeter，返回easyui分页信息
     * @param customerMeter
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute CustomerMeter customerMeter) throws Exception {
        return customerMeterService.listEasyuiPageByExample(customerMeter, true).toString();
    }

    /**
     * 新增CustomerMeter
     * @param customerMeter
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute CustomerMeter customerMeter) {
        customerMeterService.insertSelective(customerMeter);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改CustomerMeter
     * @param customerMeter
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute CustomerMeter customerMeter) {
        customerMeterService.updateSelective(customerMeter);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除CustomerMeter
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        customerMeterService.delete(id);
        return BaseOutput.success("删除成功");
    }
}