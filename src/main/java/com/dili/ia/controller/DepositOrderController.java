package com.dili.ia.controller;

import com.dili.ia.domain.DepositOrder;
import com.dili.ia.service.DepositOrderService;
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
 * This file was generated on 2020-05-20 17:29:10.
 */
@Api("/depositOrder")
@Controller
@RequestMapping("/depositOrder")
public class DepositOrderController {
    @Autowired
    DepositOrderService depositOrderService;

    /**
     * 跳转到DepositOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到DepositOrder页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "depositOrder/index";
    }

    /**
     * 分页查询DepositOrder，返回easyui分页信息
     * @param depositOrder
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询DepositOrder", notes = "分页查询DepositOrder，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DepositOrder", paramType="form", value = "DepositOrder的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(DepositOrder depositOrder) throws Exception {
        return depositOrderService.listEasyuiPageByExample(depositOrder, true).toString();
    }

    /**
     * 新增DepositOrder
     * @param depositOrder
     * @return BaseOutput
     */
    @ApiOperation("新增DepositOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DepositOrder", paramType="form", value = "DepositOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(DepositOrder depositOrder) {
        depositOrderService.insertSelective(depositOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改DepositOrder
     * @param depositOrder
     * @return BaseOutput
     */
    @ApiOperation("修改DepositOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DepositOrder", paramType="form", value = "DepositOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(DepositOrder depositOrder) {
        depositOrderService.updateSelective(depositOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除DepositOrder
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除DepositOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "DepositOrder的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        depositOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}