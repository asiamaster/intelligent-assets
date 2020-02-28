package com.dili.ia.controller;

import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.service.PaymentOrderService;
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
 * This file was generated on 2020-02-28 21:11:27.
 */
@Api("/paymentOrder")
@Controller
@RequestMapping("/paymentOrder")
public class PaymentOrderController {
    @Autowired
    PaymentOrderService paymentOrderService;

    /**
     * 跳转到PaymentOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到PaymentOrder页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "paymentOrder/index";
    }

    /**
     * 分页查询PaymentOrder，返回easyui分页信息
     * @param paymentOrder
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询PaymentOrder", notes = "分页查询PaymentOrder，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentOrder", paramType="form", value = "PaymentOrder的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(PaymentOrder paymentOrder) throws Exception {
        return paymentOrderService.listEasyuiPageByExample(paymentOrder, true).toString();
    }

    /**
     * 新增PaymentOrder
     * @param paymentOrder
     * @return BaseOutput
     */
    @ApiOperation("新增PaymentOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentOrder", paramType="form", value = "PaymentOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(PaymentOrder paymentOrder) {
        paymentOrderService.insertSelective(paymentOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改PaymentOrder
     * @param paymentOrder
     * @return BaseOutput
     */
    @ApiOperation("修改PaymentOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentOrder", paramType="form", value = "PaymentOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(PaymentOrder paymentOrder) {
        paymentOrderService.updateSelective(paymentOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除PaymentOrder
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除PaymentOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "PaymentOrder的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        paymentOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}