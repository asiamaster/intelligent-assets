package com.dili.ia.controller;

import com.dili.ia.domain.PaymentBill;
import com.dili.ia.service.PaymentBillService;
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
 * This file was generated on 2020-02-11 15:54:49.
 */
@Api("/paymentBill")
@Controller
@RequestMapping("/paymentBill")
public class PaymentBillController {
    @Autowired
    PaymentBillService paymentBillService;

    /**
     * 跳转到PaymentBill页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到PaymentBill页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "paymentBill/index";
    }

    /**
     * 分页查询PaymentBill，返回easyui分页信息
     * @param paymentBill
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询PaymentBill", notes = "分页查询PaymentBill，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentBill", paramType="form", value = "PaymentBill的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(PaymentBill paymentBill) throws Exception {
        return paymentBillService.listEasyuiPageByExample(paymentBill, true).toString();
    }

    /**
     * 新增PaymentBill
     * @param paymentBill
     * @return BaseOutput
     */
    @ApiOperation("新增PaymentBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentBill", paramType="form", value = "PaymentBill的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(PaymentBill paymentBill) {
        paymentBillService.insertSelective(paymentBill);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改PaymentBill
     * @param paymentBill
     * @return BaseOutput
     */
    @ApiOperation("修改PaymentBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentBill", paramType="form", value = "PaymentBill的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(PaymentBill paymentBill) {
        paymentBillService.updateSelective(paymentBill);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除PaymentBill
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除PaymentBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "PaymentBill的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        paymentBillService.delete(id);
        return BaseOutput.success("删除成功");
    }
}