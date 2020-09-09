package com.dili.ia.controller;

import com.dili.ia.domain.dto.BoutiqueFreeSetsDto;
import com.dili.ia.service.BoutiqueFreeSetsService;
import com.dili.ia.util.AssertUtils;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;

import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:       xiaosa
 * @date:         2020/7/15
 * @version:      农批业务系统重构
 * @description:  精品停车免费时长
 */
@Controller
@RequestMapping("/boutiqueFreeSets")
public class BoutiqueFreeSetsController {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueFreeSetsController.class);

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
     * 修改免费时长
     *
     * @param  boutiqueFreeSetsDto
     * @return BaseOutput
     * @date   2020/7/15
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute BoutiqueFreeSetsDto boutiqueFreeSetsDto) {
        try {
            // 参数校验
            AssertUtils.notNull(boutiqueFreeSetsDto.getTruck(), "柜车时长不能为空");
            AssertUtils.notNull(boutiqueFreeSetsDto.getTrailer(), "挂车时长不能为空");

            // 操作
            boutiqueFreeSetsService.updateFeeSets(boutiqueFreeSetsDto);

            return BaseOutput.success("修改成功");
        } catch (BusinessException e) {
            logger.info("修改免费时长失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

}