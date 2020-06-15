package com.dili.ia.controller;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
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
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的相关业务 web 层
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
     * @param meterDto
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute MeterDto meterDto) throws Exception {
        return meterService.listEasyuiPageByExample(meterDto, true).toString();
    }

    /**
     * 新增Meter
     * @param meterDto
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute MeterDto meterDto) {
//        meterService.insertSelective(meterDto);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改Meter
     * @param meterDto
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute MeterDto meterDto) {
        meterService.updateSelective(meterDto);
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