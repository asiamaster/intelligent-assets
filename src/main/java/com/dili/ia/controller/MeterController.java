package com.dili.ia.controller;

import com.dili.ia.domain.Meter;
import com.dili.ia.service.MeterService;
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
@RequestMapping("/meter")
public class MeterController {
    @Autowired
    MeterService meterService;

    /**
     * 跳转到Meter页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "meter/index";
    }

    /**
     * 分页查询Meter，返回easyui分页信息
     * @param meter
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute Meter meter) throws Exception {
        return meterService.listEasyuiPageByExample(meter, true).toString();
    }

    /**
     * 新增Meter
     * @param meter
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute Meter meter) {
        meterService.insertSelective(meter);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改Meter
     * @param meter
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute Meter meter) {
        meterService.updateSelective(meter);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除Meter
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        meterService.delete(id);
        return BaseOutput.success("删除成功");
    }
}