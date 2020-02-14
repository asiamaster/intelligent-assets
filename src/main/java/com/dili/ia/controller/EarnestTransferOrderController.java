package com.dili.ia.controller;

import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.service.EarnestTransferOrderService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Api("/earnestTransferOrder")
@Controller
@RequestMapping("/earnestTransferOrder")
public class EarnestTransferOrderController {
    @Autowired
    EarnestTransferOrderService earnestTransferOrderService;

    /**
     * 跳转到EarnestTransferOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到EarnestTransferOrder页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "earnestTransferOrder/index";
    }

    /**
     * 分页查询EarnestTransferOrder，返回easyui分页信息
     * @param earnestTransferOrder
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询EarnestTransferOrder", notes = "分页查询EarnestTransferOrder，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestTransferOrder", paramType="form", value = "EarnestTransferOrder的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(EarnestTransferOrder earnestTransferOrder) throws Exception {
        return earnestTransferOrderService.listEasyuiPageByExample(earnestTransferOrder, true).toString();
    }

    /**
     * 新增EarnestTransferOrder
     * @param earnestTransferOrder
     * @return BaseOutput
     */
    @ApiOperation("新增EarnestTransferOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestTransferOrder", paramType="form", value = "EarnestTransferOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(EarnestTransferOrder earnestTransferOrder) {
        earnestTransferOrderService.insertSelective(earnestTransferOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改EarnestTransferOrder
     * @param earnestTransferOrder
     * @return BaseOutput
     */
    @ApiOperation("修改EarnestTransferOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestTransferOrder", paramType="form", value = "EarnestTransferOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(EarnestTransferOrder earnestTransferOrder) {
        earnestTransferOrderService.updateSelective(earnestTransferOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除EarnestTransferOrder
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除EarnestTransferOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "EarnestTransferOrder的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        earnestTransferOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}