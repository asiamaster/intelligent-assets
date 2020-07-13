package com.dili.ia.controller;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.service.BoutiqueEntranceRecordService;
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
@RequestMapping("/boutiqueEntranceRecord")
public class BoutiqueEntranceRecordController {
    @Autowired
    BoutiqueEntranceRecordService boutiqueEntranceRecordService;

    /**
     * 跳转到BoutiqueEntranceRecord页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "boutiqueEntranceRecord/index";
    }

    /**
     * 分页查询BoutiqueEntranceRecord，返回easyui分页信息
     * @param boutiqueEntranceRecord
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) throws Exception {
        return boutiqueEntranceRecordService.listEasyuiPageByExample(boutiqueEntranceRecord, true).toString();
    }

    /**
     * 新增BoutiqueEntranceRecord
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) {
        boutiqueEntranceRecordService.insertSelective(boutiqueEntranceRecord);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改BoutiqueEntranceRecord
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) {
        boutiqueEntranceRecordService.updateSelective(boutiqueEntranceRecord);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除BoutiqueEntranceRecord
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        boutiqueEntranceRecordService.delete(id);
        return BaseOutput.success("删除成功");
    }
}