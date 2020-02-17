package com.dili.ia.controller;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.service.EarnestOrderService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Api("/earnestOrder")
@Controller
@RequestMapping("/earnestOrder")
public class EarnestOrderController {
    @Autowired
    EarnestOrderService earnestOrderService;

    /**
     * 跳转到EarnestOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到EarnestOrder页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        SessionContext sessionContext = SessionContext.getSessionContext();
        List<Map> typeDataAuthes = sessionContext.dataAuth(DataAuthType.DEPARTMENT.getCode());
        return "earnestOrder/index";
    }

    /**
     * 跳转到EarnestOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到EarnestOrder页面")
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        SessionContext sessionContext = SessionContext.getSessionContext();
        List<Map> typeDataAuthes = sessionContext.dataAuth(DataAuthType.DEPARTMENT.getCode());
        return "earnestOrder/add";
    }
    /**
     * 跳转到EarnestOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到EarnestOrder页面")
    @RequestMapping(value="/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap) {
        return "earnestOrder/view";
    }
    /**
     * 跳转到EarnestOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到EarnestOrder页面")
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap) {
        return "earnestOrder/update";
    }


    /**
     * 分页查询EarnestOrder，返回easyui分页信息
     * @param earnestOrder
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询EarnestOrder", notes = "分页查询EarnestOrder，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestOrder", paramType="form", value = "EarnestOrder的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(EarnestOrder earnestOrder) throws Exception {
        return earnestOrderService.listEasyuiPageByExample(earnestOrder, true).toString();
    }

    /**
     * 新增EarnestOrder
     * @param earnestOrder
     * @return BaseOutput
     */
    @ApiOperation("新增EarnestOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestOrder", paramType="form", value = "EarnestOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(EarnestOrder earnestOrder) {
        earnestOrderService.insertSelective(earnestOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改EarnestOrder
     * @param earnestOrder
     * @return BaseOutput
     */
    @ApiOperation("修改EarnestOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestOrder", paramType="form", value = "EarnestOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(EarnestOrder earnestOrder) {
        earnestOrderService.updateSelective(earnestOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除EarnestOrder
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除EarnestOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "EarnestOrder的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        earnestOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}