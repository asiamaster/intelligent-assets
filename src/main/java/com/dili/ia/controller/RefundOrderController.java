package com.dili.ia.controller;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.service.RefundOrderService;
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
@Api("/refundOrder")
@Controller
@RequestMapping("/refundOrder")
public class RefundOrderController {
    @Autowired
    RefundOrderService refundOrderService;

    /**
     * 跳转到RefundOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到RefundOrder页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "refundOrder/index";
    }

    /**
     * 分页查询RefundOrder，返回easyui分页信息
     * @param refundOrder
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询RefundOrder", notes = "分页查询RefundOrder，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RefundOrder", paramType="form", value = "RefundOrder的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(RefundOrder refundOrder) throws Exception {
        return refundOrderService.listEasyuiPageByExample(refundOrder, true).toString();
    }

    /**
     * 新增RefundOrder
     * @param refundOrder
     * @return BaseOutput
     */
    @ApiOperation("新增RefundOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RefundOrder", paramType="form", value = "RefundOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(RefundOrder refundOrder) {
        refundOrderService.insertSelective(refundOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改RefundOrder
     * @param refundOrder
     * @return BaseOutput
     */
    @ApiOperation("修改RefundOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RefundOrder", paramType="form", value = "RefundOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(RefundOrder refundOrder) {
        refundOrderService.updateSelective(refundOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除RefundOrder
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除RefundOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "RefundOrder的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        refundOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}