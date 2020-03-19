package com.dili.ia.controller;

import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.domain.dto.TransactionDetailsListDto;
import com.dili.ia.service.TransactionDetailsService;
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
@Api("/transactionDetails")
@Controller
@RequestMapping("/transactionDetails")
public class TransactionDetailsController {
    @Autowired
    TransactionDetailsService transactionDetailsService;

    /**
     * 跳转到TransactionDetails页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到TransactionDetails页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "transactionDetails/index";
    }

    /**
     * 分页查询TransactionDetails，返回easyui分页信息
     * @param transactionDetails
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询TransactionDetails", notes = "分页查询TransactionDetails，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="TransactionDetails", paramType="form", value = "TransactionDetails的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(TransactionDetailsListDto transactionDetails) throws Exception {
        return transactionDetailsService.listEasyuiPageByExample(transactionDetails, true).toString();
    }

    /**
     * 新增TransactionDetails
     * @param transactionDetails
     * @return BaseOutput
     */
    @ApiOperation("新增TransactionDetails")
    @ApiImplicitParams({
		@ApiImplicitParam(name="TransactionDetails", paramType="form", value = "TransactionDetails的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(TransactionDetails transactionDetails) {
        transactionDetailsService.insertSelective(transactionDetails);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改TransactionDetails
     * @param transactionDetails
     * @return BaseOutput
     */
    @ApiOperation("修改TransactionDetails")
    @ApiImplicitParams({
		@ApiImplicitParam(name="TransactionDetails", paramType="form", value = "TransactionDetails的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(TransactionDetails transactionDetails) {
        transactionDetailsService.updateSelective(transactionDetails);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除TransactionDetails
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除TransactionDetails")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "TransactionDetails的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        transactionDetailsService.delete(id);
        return BaseOutput.success("删除成功");
    }
}