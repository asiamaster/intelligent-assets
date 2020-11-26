package com.dili.ia.controller;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.service.AssetsRentalService;
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
    public @ResponseBody String listPage(@ModelAttribute AssetsRental assetsRental) throws Exception {
        return assetsRentalService.listEasyuiPageByExample(assetsRental, true).toString();
    }

    /**
     * 新增资产出租预设
     *
     * @param assetsRentalDto
     * @return BaseOutput
     * @date   2020/11/26
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute AssetsRentalDto assetsRentalDto) {
        try {
            // 校验参数，名称和摊位不能为空
            AssertUtils.notEmpty(assetsRentalDto.getName(), "预设名称不能为空");
            AssertUtils.notNull(assetsRentalDto.getAssetsRentalItemList(), "预设名称不能为空");

            AssetsRental assetsRental = assetsRentalService.addAssetsRental(assetsRentalDto);
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
    public @ResponseBody BaseOutput update(@ModelAttribute AssetsRentalDto assetsRentalDto) {
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
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        assetsRentalService.delete(id);
        return BaseOutput.success("删除成功");
    }
}