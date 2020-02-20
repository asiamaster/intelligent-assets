package com.dili.ia.controller;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.service.CustomerAccountService;
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
@Api("/customerAccount")
@Controller
@RequestMapping("/customerAccount")
public class CustomerAccountController {
    @Autowired
    CustomerAccountService customerAccountService;

    /**
     * 跳转到CustomerAccount页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到CustomerAccount页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerAccount/index";
    }
    /**
     * 跳转到CustomerAccount页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到CustomerAccount页面")
    @RequestMapping(value="/earnestRefund.html", method = RequestMethod.GET)
    public String earnestRefund(ModelMap modelMap) {
        return "customerAccount/earnestRefund";
    }
    /**
     * 跳转到CustomerAccount页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到CustomerAccount页面")
    @RequestMapping(value="/earnestTransfer.html", method = RequestMethod.GET)
    public String earnestTransfer(ModelMap modelMap) {
        return "customerAccount/earnestTransfer";
    }

    /**
     * 分页查询CustomerAccount，返回easyui分页信息
     * @param customerAccount
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询CustomerAccount", notes = "分页查询CustomerAccount，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerAccount", paramType="form", value = "CustomerAccount的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(CustomerAccount customerAccount) throws Exception {
        return customerAccountService.listEasyuiPageByExample(customerAccount, true).toString();
    }

    /**
     * 新增CustomerAccount
     * @param customerAccount
     * @return BaseOutput
     */
    @ApiOperation("新增CustomerAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerAccount", paramType="form", value = "CustomerAccount的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(CustomerAccount customerAccount) {
        customerAccountService.insertSelective(customerAccount);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改CustomerAccount
     * @param customerAccount
     * @return BaseOutput
     */
    @ApiOperation("修改CustomerAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerAccount", paramType="form", value = "CustomerAccount的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(CustomerAccount customerAccount) {
        customerAccountService.updateSelective(customerAccount);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除CustomerAccount
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除CustomerAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "CustomerAccount的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        customerAccountService.delete(id);
        return BaseOutput.success("删除成功");
    }
}