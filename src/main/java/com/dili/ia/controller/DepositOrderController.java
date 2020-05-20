package com.dili.ia.controller;

import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.dto.EarnestOrderListDto;
import com.dili.ia.service.DepositOrderService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-20 17:29:10.
 */
@Api("/depositOrder")
@Controller
@RequestMapping("/depositOrder")
public class DepositOrderController {
    private final static Logger LOG = LoggerFactory.getLogger(DepositOrderController.class);

    @Autowired
    DepositOrderService depositOrderService;

    /**
     * 跳转到DepositOrder页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到DepositOrder页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.add(Calendar.DAY_OF_MONTH, -2);
        Date createdStart = c.getTime();

        Calendar ce = Calendar.getInstance();
        ce.set(ce.get(Calendar.YEAR), ce.get(Calendar.MONTH), ce.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date  createdEnd = ce.getTime();

        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        return "depositOrder/index";
    }

    /**
     * 分页查询DepositOrder，返回easyui分页信息
     * @param depositOrder
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询DepositOrder", notes = "分页查询DepositOrder，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DepositOrder", paramType="form", value = "DepositOrder的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(DepositOrder depositOrder) throws Exception {
        return depositOrderService.listEasyuiPageByExample(depositOrder, true).toString();
    }

    /**
     * 跳转到保证金管理-新增页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "depositOrder/add";
    }

    /**
     * 新增DepositOrder
//     * @param depositOrder
     * @return BaseOutput
     */
    @ApiOperation("新增DepositOrder")
    @BusinessLogger(businessType = LogBizTypeConst.EARNEST_ORDER, content="${businessCode!}", operationType="add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/doAdd.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput doAdd(EarnestOrderListDto earnestOrder) {

        try{
            return BaseOutput.success();
//            BaseOutput<EarnestOrder> output = earnestOrderService.addEarnestOrder(earnestOrder);
//            //写业务日志
//            if (output.isSuccess()){
//                EarnestOrder order = output.getData();
//                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
//                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
//            }
//            return output;
        }catch (BusinessException e){
            LOG.error("定金单保存异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("定金单保存异常！", e);
            return BaseOutput.failure("定金单保存异常！");
        }
    }


    /**
     * 跳转到定金管理-修改页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        if(null != id){
//            EarnestOrder earnestOrder = earnestOrderService.get(id);
//            EarnestOrderDetail condition = DTOUtils.newInstance(EarnestOrderDetail.class);
//            condition.setEarnestOrderId(id);
//            List<EarnestOrderDetail> earnestOrderDetails = earnestOrderDetailService.list(condition);
//            modelMap.put("earnestOrder",earnestOrder);
//            modelMap.put("earnestOrderDetails", JSON.toJSONString(earnestOrderDetails));
        }
        return "depositOrder/update";
    }

    /**
     * 修改DepositOrder
     * @param depositOrder
     * @return BaseOutput
     */
    @ApiOperation("修改DepositOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="DepositOrder", paramType="form", value = "DepositOrder的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(DepositOrder depositOrder) {
        depositOrderService.updateSelective(depositOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除DepositOrder
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除DepositOrder")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "DepositOrder的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        depositOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 跳转到定金管理-查看页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap,Long id,String orderCode) {
//        EarnestOrder earnestOrder = null;
//        if(null != id) {
//            earnestOrder = earnestOrderService.get(id);
//        }else if(StringUtils.isNotBlank(orderCode)){
//            PaymentOrder paymentOrder = DTOUtils.newInstance(PaymentOrder.class);
//            paymentOrder.setCode(orderCode);
//            paymentOrder.setBizType(BizTypeEnum.EARNEST.getCode());
//            earnestOrder = earnestOrderService.get(paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null).getBusinessId());
//            id = earnestOrder.getId();
//        }
//        EarnestOrderDetail condition = DTOUtils.newInstance(EarnestOrderDetail.class);
//        condition.setEarnestOrderId(id);
//        List<EarnestOrderDetail> earnestOrderDetails = earnestOrderDetailService.list(condition);
//        modelMap.put("earnestOrder",earnestOrder);
//        modelMap.put("earnestOrderDetails", earnestOrderDetails);
        try{
            //日志查询
//            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
//            businessLogQueryInput.setBusinessId(id);
//            businessLogQueryInput.setBusinessType(LogBizTypeConst.EARNEST_ORDER);
//            BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
//            if(businessLogOutput.isSuccess()){
//                modelMap.put("logs",businessLogOutput.getData());
//            }
        }catch (Exception e){
            LOG.error("日志服务查询异常",e);
        }
        return "earnestOrder/view";
    }
}