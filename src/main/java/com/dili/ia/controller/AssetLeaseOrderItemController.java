package com.dili.ia.controller;

import com.dili.ia.domain.AssetLeaseOrderItem;
import com.dili.ia.service.AssetLeaseOrderItemService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * This file was generated on 2020-05-29 14:40:05.
 */
@Api("/assetLeaseOrderItem")
@Controller
@RequestMapping("/assetLeaseOrderItem")
public class AssetLeaseOrderItemController {
    @Autowired
    AssetLeaseOrderItemService assetLeaseOrderItemService;

    /**
     * 跳转到AssetLeaseOrderItem页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到AssetLeaseOrderItem页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "assetLeaseOrderItem/index";
    }

    /**
     * 分页查询AssetLeaseOrderItem，返回easyui分页信息
     * @param assetLeaseOrderItem
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询AssetLeaseOrderItem", notes = "分页查询AssetLeaseOrderItem，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="AssetLeaseOrderItem", paramType="form", value = "AssetLeaseOrderItem的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute AssetLeaseOrderItem assetLeaseOrderItem) throws Exception {
        return assetLeaseOrderItemService.listEasyuiPageByExample(assetLeaseOrderItem, true).toString();
    }

    /**
     * 新增AssetLeaseOrderItem
     * @param assetLeaseOrderItem
     * @return BaseOutput
     */
    @ApiOperation("新增AssetLeaseOrderItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="AssetLeaseOrderItem", paramType="form", value = "AssetLeaseOrderItem的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute AssetLeaseOrderItem assetLeaseOrderItem) {
        assetLeaseOrderItemService.insertSelective(assetLeaseOrderItem);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改AssetLeaseOrderItem
     * @param assetLeaseOrderItem
     * @return BaseOutput
     */
    @ApiOperation("修改AssetLeaseOrderItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="AssetLeaseOrderItem", paramType="form", value = "AssetLeaseOrderItem的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute AssetLeaseOrderItem assetLeaseOrderItem) {
        assetLeaseOrderItemService.updateSelective(assetLeaseOrderItem);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除AssetLeaseOrderItem
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除AssetLeaseOrderItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "AssetLeaseOrderItem的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        assetLeaseOrderItemService.delete(id);
        return BaseOutput.success("删除成功");
    }
}