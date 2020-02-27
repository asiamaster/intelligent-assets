package com.dili.ia.controller;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.EarnestOrderDetail;
import com.dili.ia.domain.dto.EarnestOrderListDto;
import com.dili.ia.glossary.EarnestOrderStateEnum;
import com.dili.ia.service.EarnestOrderDetailService;
import com.dili.ia.service.EarnestOrderService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
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
    @Autowired
    EarnestOrderDetailService earnestOrderDetailService;

    /**
     * 跳转到定金管理页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到定金管理页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "earnestOrder/index";
    }

    /**
     * 跳转到定金管理-新增页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到定金管理-新增页面")
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "earnestOrder/add";
    }
    /**
     * 跳转到定金管理-查看页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到定金管理-查看页面")
    @RequestMapping(value="/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id) {
        if(null != id){
            EarnestOrder earnestOrder = earnestOrderService.get(id);
            EarnestOrderDetail condition = DTOUtils.newInstance(EarnestOrderDetail.class);
            condition.setEarnestOrderId(id);
            List<EarnestOrderDetail> earnestOrderDetails = earnestOrderDetailService.list(condition);
            modelMap.put("earnestOrder",earnestOrder);
            modelMap.put("earnestOrderDetails", earnestOrderDetails);
        }
        return "earnestOrder/view";
    }
    /**
     * 跳转到定金管理-修改页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到定金管理-修改页面")
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap) {
        return "earnestOrder/update";
    }


    /**
     * 分页查询EarnestOrder，返回easyui分页信息
     * @param earnestOrderListDto
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询EarnestOrder", notes = "分页查询EarnestOrder，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="EarnestOrder", paramType="form", value = "EarnestOrder的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(EarnestOrderListDto earnestOrderListDto) throws Exception {
        return earnestOrderService.listEasyuiPageByExample(earnestOrderListDto, true).toString();
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
        earnestOrderService.addEarnestOrder(earnestOrder);
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
        earnestOrderService.updateEarnestOrder(earnestOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 定金管理--提交
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("定金管理--提交")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "EarnestOrder的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(Long id) {
        try {
            earnestOrderService.submitEarnestOrder(id);
            return BaseOutput.success("提交成功！");
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure("提交出错！");
        }
    }

    /**
     * 定金管理--撤回
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("定金管理--撤回")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", paramType="form", value = "EarnestOrder的主键", required = true, dataType = "long")
    })
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(Long id) {
        try {
            earnestOrderService.withdrawEarnestOrder(id);
            return BaseOutput.success("撤回成功！");
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure("撤回出错！");
        }
    }

    /**
     * 定金管理--取消
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("定金管理--取消")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", paramType="form", value = "EarnestOrder的主键", required = true, dataType = "long")
    })
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancel(Long id) {
        EarnestOrder earnestOrder = earnestOrderService.get(id);
        if (!earnestOrder.getState().equals(EarnestOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("取消失败，定金单状态已变更！");
        }
        earnestOrder.setState(EarnestOrderStateEnum.CANCELD.getCode());
        earnestOrderService.updateSelective(earnestOrder);
        return BaseOutput.success("取消成功");
    }
}