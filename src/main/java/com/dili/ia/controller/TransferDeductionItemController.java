package com.dili.ia.controller;

import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.service.TransferDeductionItemService;
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
@Api("/transferDeductionItem")
@Controller
@RequestMapping("/transferDeductionItem")
public class TransferDeductionItemController {
    @Autowired
    TransferDeductionItemService transferDeductionItemService;

    /**
     * 跳转到TransferDeductionItem页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到TransferDeductionItem页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "transferDeductionItem/index";
    }

    /**
     * 分页查询TransferDeductionItem，返回easyui分页信息
     * @param transferDeductionItem
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询TransferDeductionItem", notes = "分页查询TransferDeductionItem，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="TransferDeductionItem", paramType="form", value = "TransferDeductionItem的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(TransferDeductionItem transferDeductionItem) throws Exception {
        return transferDeductionItemService.listEasyuiPageByExample(transferDeductionItem, true).toString();
    }

    /**
     * 新增TransferDeductionItem
     * @param transferDeductionItem
     * @return BaseOutput
     */
    @ApiOperation("新增TransferDeductionItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="TransferDeductionItem", paramType="form", value = "TransferDeductionItem的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(TransferDeductionItem transferDeductionItem) {
        transferDeductionItemService.insertSelective(transferDeductionItem);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改TransferDeductionItem
     * @param transferDeductionItem
     * @return BaseOutput
     */
    @ApiOperation("修改TransferDeductionItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="TransferDeductionItem", paramType="form", value = "TransferDeductionItem的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(TransferDeductionItem transferDeductionItem) {
        transferDeductionItemService.updateSelective(transferDeductionItem);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除TransferDeductionItem
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除TransferDeductionItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "TransferDeductionItem的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        transferDeductionItemService.delete(id);
        return BaseOutput.success("删除成功");
    }
}