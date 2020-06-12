package com.dili.ia.controller;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.service.MeterDetailService;
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
@RequestMapping("/meterDetail")
public class MeterDetailController {
    @Autowired
    MeterDetailService meterDetailService;

    /**
     * 跳转到MeterDetail页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "meterDetail/index";
    }

    /**
     * 分页查询MeterDetail，返回easyui分页信息
     * @param meterDetail
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute MeterDetail meterDetail) throws Exception {
        return meterDetailService.listEasyuiPageByExample(meterDetail, true).toString();
    }

    /**
     * 新增MeterDetail
     * @param meterDetail
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute MeterDetail meterDetail) {
        meterDetailService.insertSelective(meterDetail);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改MeterDetail
     * @param meterDetail
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute MeterDetail meterDetail) {
        meterDetailService.updateSelective(meterDetail);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除MeterDetail
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        meterDetailService.delete(id);
        return BaseOutput.success("删除成功");
    }
}