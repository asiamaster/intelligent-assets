package com.dili.ia.controller;

import com.dili.ia.domain.LeaseChargeItem;
import com.dili.ia.service.LeaseChargeItemService;
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
 * This file was generated on 2020-05-29 16:13:04.
 */
@Api("/leaseChargeItem")
@Controller
@RequestMapping("/leaseChargeItem")
public class LeaseChargeItemController {
    @Autowired
    LeaseChargeItemService leaseChargeItemService;

    /**
     * 跳转到LeaseChargeItem页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到LeaseChargeItem页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "leaseChargeItem/index";
    }

    /**
     * 分页查询LeaseChargeItem，返回easyui分页信息
     * @param leaseChargeItem
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询LeaseChargeItem", notes = "分页查询LeaseChargeItem，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LeaseChargeItem", paramType="form", value = "LeaseChargeItem的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute LeaseChargeItem leaseChargeItem) throws Exception {
        return leaseChargeItemService.listEasyuiPageByExample(leaseChargeItem, true).toString();
    }

    /**
     * 新增LeaseChargeItem
     * @param leaseChargeItem
     * @return BaseOutput
     */
    @ApiOperation("新增LeaseChargeItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LeaseChargeItem", paramType="form", value = "LeaseChargeItem的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute LeaseChargeItem leaseChargeItem) {
        leaseChargeItemService.insertSelective(leaseChargeItem);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改LeaseChargeItem
     * @param leaseChargeItem
     * @return BaseOutput
     */
    @ApiOperation("修改LeaseChargeItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LeaseChargeItem", paramType="form", value = "LeaseChargeItem的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute LeaseChargeItem leaseChargeItem) {
        leaseChargeItemService.updateSelective(leaseChargeItem);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除LeaseChargeItem
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除LeaseChargeItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "LeaseChargeItem的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        leaseChargeItemService.delete(id);
        return BaseOutput.success("删除成功");
    }
}