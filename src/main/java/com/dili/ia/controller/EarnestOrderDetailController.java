package com.dili.ia.controller;

import com.dili.ia.domain.EarnestOrderDetail;
import com.dili.ia.service.EarnestOrderDetailService;
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
@Api("/earnestOrderDetail")
@Controller
@RequestMapping("/earnestOrderDetail")
public class EarnestOrderDetailController {
    @Autowired
    EarnestOrderDetailService earnestOrderDetailService;

    /**
     * 跳转到EarnestOrderDetail页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到EarnestOrderDetail页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "earnestOrderDetail/index";
    }

    /**
     * 分页查询EarnestOrderDetail，返回easyui分页信息
     * @param earnestOrderDetail
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询EarnestOrderDetail", notes = "分页查询EarnestOrderDetail，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestOrderDetail", paramType="form", value = "EarnestOrderDetail的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(EarnestOrderDetail earnestOrderDetail) throws Exception {
        return earnestOrderDetailService.listEasyuiPageByExample(earnestOrderDetail, true).toString();
    }

    /**
     * 新增EarnestOrderDetail
     * @param earnestOrderDetail
     * @return BaseOutput
     */
    @ApiOperation("新增EarnestOrderDetail")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestOrderDetail", paramType="form", value = "EarnestOrderDetail的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(EarnestOrderDetail earnestOrderDetail) {
        earnestOrderDetailService.insertSelective(earnestOrderDetail);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改EarnestOrderDetail
     * @param earnestOrderDetail
     * @return BaseOutput
     */
    @ApiOperation("修改EarnestOrderDetail")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestOrderDetail", paramType="form", value = "EarnestOrderDetail的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(EarnestOrderDetail earnestOrderDetail) {
        earnestOrderDetailService.updateSelective(earnestOrderDetail);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除EarnestOrderDetail
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除EarnestOrderDetail")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "EarnestOrderDetail的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        earnestOrderDetailService.delete(id);
        return BaseOutput.success("删除成功");
    }
}