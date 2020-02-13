package com.dili.ia.controller;

import com.dili.ia.domain.RefundBill;
import com.dili.ia.service.RefundBillService;
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
@Api("/refundBill")
@Controller
@RequestMapping("/refundBill")
public class RefundBillController {
    @Autowired
    RefundBillService refundBillService;

    /**
     * 跳转到RefundBill页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到RefundBill页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "refundBill/index";
    }

    /**
     * 跳转到RefundBill修改页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到RefundBill页面")
    @RequestMapping(value="/modify.html", method = RequestMethod.GET)
    public String modify(ModelMap modelMap) {
        return "refundBill/modify";
    }

    /**
     * 跳转到RefundBill查看页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到RefundBill查看页面")
    @RequestMapping(value="/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap) {
        return "refundBill/view";
    }

    /**
     * 分页查询RefundBill，返回easyui分页信息
     * @param refundBill
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询RefundBill", notes = "分页查询RefundBill，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RefundBill", paramType="form", value = "RefundBill的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(RefundBill refundBill) throws Exception {
        return refundBillService.listEasyuiPageByExample(refundBill, true).toString();
    }

    /**
     * 新增RefundBill
     * @param refundBill
     * @return BaseOutput
     */
    @ApiOperation("新增RefundBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RefundBill", paramType="form", value = "RefundBill的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(RefundBill refundBill) {
        refundBillService.insertSelective(refundBill);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改RefundBill
     * @param refundBill
     * @return BaseOutput
     */
    @ApiOperation("修改RefundBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RefundBill", paramType="form", value = "RefundBill的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(RefundBill refundBill) {
        refundBillService.updateSelective(refundBill);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除RefundBill
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除RefundBill")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "RefundBill的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        refundBillService.delete(id);
        return BaseOutput.success("删除成功");
    }
}