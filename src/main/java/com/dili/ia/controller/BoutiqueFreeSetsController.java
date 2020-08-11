package com.dili.ia.controller;

import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.domain.dto.BoutiqueFreeSetsDto;
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
    @RequestMapping(value="/set.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {

        BoutiqueFreeSetsDto boutiqueFreeSetsDto = boutiqueFreeSetsService.getHour();
        modelMap.put("boutiqueFreeSets", boutiqueFreeSetsDto);

        return "boutiqueEntranceRecord/set";
    }

    /**
     * 修改BoutiqueFreeSets
     * @param boutiqueFreeSetsDto
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute BoutiqueFreeSetsDto boutiqueFreeSetsDto) {
        boutiqueFreeSetsService.updateFeeSets(boutiqueFreeSetsDto);
        return BaseOutput.success("修改成功");
    }

}