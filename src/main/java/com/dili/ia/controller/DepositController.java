package com.dili.ia.controller;

import com.dili.ia.domain.Deposit;
import com.dili.ia.service.DepositService;
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
 * This file was generated on 2020-05-19 18:20:05.
 */
@Api("/deposit")
@Controller
@RequestMapping("/deposit")
public class DepositController {
    @Autowired
    DepositService depositService;

    /**
     * 跳转到Deposit页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到Deposit页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "deposit/index";
    }

    /**
     * 分页查询Deposit，返回easyui分页信息
     * @param deposit
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询Deposit", notes = "分页查询Deposit，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Deposit", paramType="form", value = "Deposit的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Deposit deposit) throws Exception {
        return depositService.listEasyuiPageByExample(deposit, true).toString();
    }

    /**
     * 新增Deposit
     * @param deposit
     * @return BaseOutput
     */
    @ApiOperation("新增Deposit")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Deposit", paramType="form", value = "Deposit的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Deposit deposit) {
        depositService.insertSelective(deposit);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改Deposit
     * @param deposit
     * @return BaseOutput
     */
    @ApiOperation("修改Deposit")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Deposit", paramType="form", value = "Deposit的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Deposit deposit) {
        depositService.updateSelective(deposit);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除Deposit
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除Deposit")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Deposit的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        depositService.delete(id);
        return BaseOutput.success("删除成功");
    }
}