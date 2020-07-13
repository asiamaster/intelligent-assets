package com.dili.ia.controller;

import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.service.BoutiqueFreeSetsService;
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
@RequestMapping("/boutiqueFreeSets")
public class BoutiqueFreeSetsController {
    @Autowired
    BoutiqueFreeSetsService boutiqueFreeSetsService;

    /**
     * 跳转到BoutiqueFreeSets页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "boutiqueFreeSets/index";
    }

    /**
     * 分页查询BoutiqueFreeSets，返回easyui分页信息
     * @param boutiqueFreeSets
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute BoutiqueFreeSets boutiqueFreeSets) throws Exception {
        return boutiqueFreeSetsService.listEasyuiPageByExample(boutiqueFreeSets, true).toString();
    }

    /**
     * 新增BoutiqueFreeSets
     * @param boutiqueFreeSets
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute BoutiqueFreeSets boutiqueFreeSets) {
        boutiqueFreeSetsService.insertSelective(boutiqueFreeSets);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改BoutiqueFreeSets
     * @param boutiqueFreeSets
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute BoutiqueFreeSets boutiqueFreeSets) {
        boutiqueFreeSetsService.updateSelective(boutiqueFreeSets);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除BoutiqueFreeSets
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        boutiqueFreeSetsService.delete(id);
        return BaseOutput.success("删除成功");
    }
}