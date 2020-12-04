package com.dili.ia.controller;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.service.AssetsRentalService;
import com.dili.ia.util.AssertUtils;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设
 */
@Controller
@RequestMapping("/assetsRental")
public class AssetsRentalController {

    private final static Logger logger = LoggerFactory.getLogger(AssetsRentalController.class);

    @Autowired
    AssetsRentalService assetsRentalService;

    /**
     * 跳转到assetsRental页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/11/26
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "assetsRental/index";
    }

    /**
     * 跳转到新增页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/11/26
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "assetsRental/add";
    }

    /**
     * 跳转到修改页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/11/26
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        if (id != null) {
            AssetsRental assetsRental = assetsRentalService.get(id);
            modelMap.put("assetsRental", assetsRental);
        }
        return "assetsRental/update";
    }

    /**
     * 查询资产出租预设的集合(分页)
     *
     * @param  assetsRental
     * @return
     * @date   2020/11/26
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@RequestBody AssetsRental assetsRental) throws Exception {
        return assetsRentalService.listEasyuiPageByExample(assetsRental, true).toString();
    }

    /**
     * 新增资产出租预设
     *
     * @param assetsRentalDto
     * @return BaseOutput
     * @date   2020/11/26
     */
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput add(@RequestBody AssetsRentalDto assetsRentalDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 校验参数，名称和摊位不能为空
            AssertUtils.notEmpty(assetsRentalDto.getName(), "预设名称不能为空");
            AssertUtils.notNull(assetsRentalDto.getAssetsRentalItemList(), "预设名称不能为空");

            AssetsRental assetsRental = assetsRentalService.addAssetsRental(assetsRentalDto, userTicket);
            return BaseOutput.success().setData(assetsRental);
        } catch (BusinessException e) {
            logger.info("新增资产出租预设失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 修改资产出租预设
     *
     * @param assetsRentalDto
     * @return BaseOutput
     * @date   2020/11/26
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@RequestBody AssetsRentalDto assetsRentalDto) {
        try {
            // 校验参数，名称和摊位不能为空
            AssertUtils.notEmpty(assetsRentalDto.getName(), "预设名称不能为空");
            AssertUtils.notNull(assetsRentalDto.getAssetsRentalItemList(), "预设名称不能为空");

            AssetsRental assetsRental = assetsRentalService.updateAssetsRental(assetsRentalDto);
            return BaseOutput.success().setData(assetsRental);
        } catch (BusinessException e) {
            logger.info("修改资产出租预设失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 启用或者禁用
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/11/26
     */
    @RequestMapping(value="/enableOrDisable.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput enableOrDisable(@RequestBody Long id) {
        assetsRentalService.enableOrDisable(id);
        return BaseOutput.success();
    }

    /**
     * 根据摊位 id 查询相关的预设信息
     *
     * @param  assetsId
     * @return BaseOutput
     * @date   2020/12/2
     */
    @RequestMapping(value="/getRentalByAssetsId.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getRentalByAssetsId(@RequestParam("assetsId") Long assetsId) {
        AssetsRentalDto assetsRentalDto = assetsRentalService.getRentalByAssetsId(assetsId);
        return BaseOutput.success().setData(assetsRentalDto);
    }

    /**
     * 根据摊位 ids 查询是否属于一个批次
     *
     * @param  assetsRentalDto
     * @return BaseOutput
     * @date   2020/12/2
     */
    @RequestMapping(value="/belongsOneBatchByAssetsIds.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput belongsBatchByAssetsIds(@RequestBody AssetsRentalDto assetsRentalDto) {
        boolean belongsBatchByAssetsIds = assetsRentalService.belongsBatchByAssetsIds(assetsRentalDto.getAssetsIds());
        return BaseOutput.success().setData(belongsBatchByAssetsIds);
    }
}