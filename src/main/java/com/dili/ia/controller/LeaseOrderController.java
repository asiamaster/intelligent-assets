package com.dili.ia.controller;

import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.service.LeaseOrderService;
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
 * This file was generated on 2020-02-10 12:31:39.
 */
@Api("/leaseOrder")
@Controller
@RequestMapping("/leaseOrder")
public class LeaseOrderController {
    @Autowired
    LeaseOrderService leaseOrderService;

    /**
     * 跳转到LeaseOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到LeaseOrder页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "leaseOrder/index";
    }

    /**
     * 分页查询LeaseOrder，返回easyui分页信息
     * @param leaseOrder
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询LeaseOrder", notes = "分页查询LeaseOrder，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LeaseOrder", paramType="form", value = "LeaseOrder的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(LeaseOrder leaseOrder) throws Exception {
        return leaseOrderService.listEasyuiPageByExample(leaseOrder, true).toString();
    }

    /**
     * 新增LeaseOrder
     * @param leaseOrder
     * @return BaseOutput
     */
    @ApiOperation("新增LeaseOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LeaseOrder", paramType="form", value = "LeaseOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(LeaseOrder leaseOrder) {
        leaseOrderService.insertSelective(leaseOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改LeaseOrder
     * @param leaseOrder
     * @return BaseOutput
     */
    @ApiOperation("修改LeaseOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LeaseOrder", paramType="form", value = "LeaseOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(LeaseOrder leaseOrder) {
        leaseOrderService.updateSelective(leaseOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除LeaseOrder
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除LeaseOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "LeaseOrder的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        leaseOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}