package com.dili.ia.controller;

import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.domain.dto.AssetsRentalItemDto;
import com.dili.ia.service.AssetsRentalItemService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设 中间表
 */
@Controller
@RequestMapping("/assetsRentalItem")
public class AssetsRentalItemController {
    @Autowired
    AssetsRentalItemService assetsRentalItemService;

    /**
     * 跳转到assetsRentalItem页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "assetsRentalItem/index";
    }

    /**
     * 分页查询assetsRentalItem，返回easyui分页信息
     * @param assetsRentalItem
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute AssetsRentalItem assetsRentalItem) throws Exception {
        return assetsRentalItemService.listEasyuiPageByExample(assetsRentalItem, true).toString();
    }

    /**
     * 新增assetsRentalItem
     * @param assetsRentalItem
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute AssetsRentalItem assetsRentalItem) {
        assetsRentalItemService.insertSelective(assetsRentalItem);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改assetsRentalItem
     * @param assetsRentalItem
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute AssetsRentalItem assetsRentalItem) {
        assetsRentalItemService.updateSelective(assetsRentalItem);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除assetsRentalItem
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        assetsRentalItemService.delete(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 根据条件搜索摊位，过滤掉预设池已有的摊位，返回没有添加到预设池的摊位集合，用于预设
     *
     * @param  assetsRentalDto
     * @return BaseOutput
     * @date   2020/12/8
     */
    @RequestMapping(value="/viewAssetsByNoTable.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput viewAssetsByNoTable(@RequestBody AssetsRentalDto assetsRentalDto) {
        List<AssetsDTO> assetsDTOList = assetsRentalItemService.filterAssets(assetsRentalDto);
        return BaseOutput.success().setData(assetsDTOList);
    }
}