package com.dili.ia.controller;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.AssetsLeaseOrderListDto;
import com.dili.ia.domain.dto.LeaseRefundOrderDto;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.glossary.LeaseOrderRefundTypeEnum;
import com.dili.ia.service.*;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Controller
@RequestMapping("/assetsLeaseOrder")
public class AssetsLeaseOrderController {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderController.class);
    @Autowired
    AssetsLeaseOrderService assetsLeaseOrderService;
    @Autowired
    AssetsLeaseOrderItemService assetsLeaseOrderItemService;
    @Autowired
    PaymentOrderService paymentOrderService;
    @Autowired
    BusinessLogRpc businessLogRpc;
    @Autowired
    DataAuthService dataAuthService;
    @Autowired
    private BusinessChargeItemService businessChargeItemService;


    /**
     * 跳转到LeaseOrder页面
     * @param modelMap
     * @param assetsType
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap,Integer assetsType) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.add(Calendar.DAY_OF_MONTH, -2);
        Date createdStart = c.getTime();

        Calendar ce = Calendar.getInstance();
        ce.set(ce.get(Calendar.YEAR), ce.get(Calendar.MONTH), ce.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date  createdEnd = ce.getTime();

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }

        modelMap.put("chargeItems", businessChargeItemService.queryBusinessChargeItemConfig(userTicket.getFirmId(), AssetsTypeEnum.getAssetsTypeEnum(assetsType).getBizType(), null));
        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        modelMap.put("assetsType", assetsType);
        return "assetsLeaseOrder/index";
    }

    /**
     * 跳转到LeaseOrder查看页面
     * @param modelMap
     * @param orderCode 缴费单CODE
     * @return String
     */
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap,Long id,String orderCode) {
        AssetsLeaseOrder leaseOrder = null;
        if(null != id) {
            leaseOrder = assetsLeaseOrderService.get(id);
        }else if(StringUtils.isNotBlank(orderCode)){
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setCode(orderCode);
            leaseOrder = assetsLeaseOrderService.get(paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null).getBusinessId());
            id = leaseOrder.getId();
        }

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }

        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(id);
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(condition);
        modelMap.put("leaseOrder",leaseOrder);
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemConfig(userTicket.getFirmId(), AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getBizType(), YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);
        modelMap.put("leaseOrderItems", assetsLeaseOrderItemService.leaseOrderItemListToDto(leaseOrderItems, AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getBizType(), chargeItemDtos));
        try{
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessId(id);
            businessLogQueryInput.setBusinessType(LogBizTypeConst.BOOTH_LEASE);
            BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if(businessLogOutput.isSuccess()){
                modelMap.put("logs",businessLogOutput.getData());
            }
        }catch (Exception e){
            LOG.error("日志服务查询异常",e);
        }
        return "assetsLeaseOrder/view";
    }

    /**
     * 跳转到LeaseOrder新增页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/preSave.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap,Long id,Integer assetsType,Integer isRenew) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemConfig(userTicket.getFirmId(), AssetsTypeEnum.getAssetsTypeEnum(assetsType).getBizType(), YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);
        if(null != id){
            AssetsLeaseOrder leaseOrder = assetsLeaseOrderService.get(id);
            modelMap.put("leaseOrder",leaseOrder);

            AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
            condition.setLeaseOrderId(id);
            List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(condition);

            modelMap.put("leaseOrderItems", assetsLeaseOrderItemService.leaseOrderItemListToDto(leaseOrderItems, AssetsTypeEnum.getAssetsTypeEnum(assetsType).getBizType(), chargeItemDtos));
        }
        modelMap.put("assetsType", assetsType);
        modelMap.put("isRenew", YesOrNoEnum.YES.getCode().equals(isRenew) ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
        return "assetsLeaseOrder/preSave";
    }

    /**
     *
     * @param modelMap
     * @param id 对应租赁单ID 或 订单项ID
     * @param type 1：租赁单退款 2： 子单退款
     * @return
     */
    @RequestMapping(value="/refundApply.html", method = RequestMethod.GET)
    public String refundApply(ModelMap modelMap,Long id,Integer type) {
        if(LeaseOrderRefundTypeEnum.LEASE_ORDER_REFUND.getCode().equals(type)){
            modelMap.put("leaseOrder",assetsLeaseOrderService.get(id));
        }else if(LeaseOrderRefundTypeEnum.LEASE_ORDER_ITEM_REFUND.getCode().equals(type)){
            AssetsLeaseOrderItem leaseOrderItem = assetsLeaseOrderItemService.get(id);
            modelMap.put("leaseOrderItem",leaseOrderItem);
            modelMap.put("leaseOrder",assetsLeaseOrderService.get(leaseOrderItem.getLeaseOrderId()));
        }
        return "assetsLeaseOrder/refundApply";
    }

    /**
     * 分页查询LeaseOrder，返回easyui分页信息
     * @param leaseOrder
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(AssetsLeaseOrderListDto leaseOrder) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
//        List<Long> departmentIdList = dataAuthService.getDepartmentDataAuth(userTicket);
//        if (CollectionUtils.isEmpty(departmentIdList)){
//            return new EasyuiPageOutput(0, Collections.emptyList()).toString();
//        }
//        leaseOrder.setMarketId(userTicket.getFirmId());
//        leaseOrder.setDepartmentIds(departmentIdList);
//
//        if (StringUtils.isNotBlank(leaseOrder.getAssetsName())) {
//            AssetsLeaseOrderItem leaseOrderItemCondition = new AssetsLeaseOrderItem();
//            leaseOrderItemCondition.setAssetsName(leaseOrder.getAssetsName());
//            leaseOrder.setIds(assetsLeaseOrderItemService.list(leaseOrderItemCondition).stream().map(AssetsLeaseOrderItem::getLeaseOrderId).collect(Collectors.toList()));
//            if(CollectionUtils.isEmpty(leaseOrder.getIds())){
//                return new EasyuiPageOutput(0, Collections.emptyList()).toString();
//            }
//        }
        return assetsLeaseOrderService.listEasyuiPageByExample(leaseOrder, true).toString();
    }

    /**
     * 租赁订单信息补录
     * @param leaseOrder
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE,content = "${contractNo}",operationType="reNumber",systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/supplement.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput supplement(AssetsLeaseOrder leaseOrder){
        try {
            return assetsLeaseOrderService.supplement(leaseOrder);
        }catch (BusinessException e){
            LOG.info("租赁订单信息补录异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("租赁订单信息补录异常！", e);
            return BaseOutput.failure(e.getMessage());
        }


    }

    /**
     * 租赁订单取消
     * @param id 订单ID
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, operationType="cancel",systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/cancelOrder.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput cancelOrder(Long id){
        try {
            return assetsLeaseOrderService.cancelOrder(id);
        }catch (BusinessException e){
            LOG.info("租赁订单取消异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("租赁订单取消异常！", e);
            return BaseOutput.failure(e.getMessage());
        }


    }

    /**
     * 租赁订单撤回
     * @param id 订单ID
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, operationType="withdraw",systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/withdrawOrder.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput withdrawOrder(Long id){
        try {
            return assetsLeaseOrderService.withdrawOrder(id);
        }catch (BusinessException e){
            LOG.info("租赁订单撤回异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("租赁订单撤回异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 资产租赁订单保存
     * @param leaseOrder
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, content="${logContent!}", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/saveLeaseOrder.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput saveLeaseOrder(@RequestBody AssetsLeaseOrderListDto leaseOrder){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59");
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        leaseOrder.setEndTime(LocalDateTime.parse(leaseOrder.getEndTime().format(formatter), formatterDateTime));
        try{
            BaseOutput output = assetsLeaseOrderService.saveLeaseOrder(leaseOrder);
            //写业务日志
            if (output.isSuccess()){
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, leaseOrder.getCode());
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, leaseOrder.getId());
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
                LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
                LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, null == leaseOrder.getId() ? "add" : "edit");
                LoggerContext.put("notes", leaseOrder.getNotes());
            }
            return output;
        }catch (BusinessException e){
            LOG.info("资产租赁订单保存异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("资产租赁订单保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }


    /**
     * 提交付款
     * @param id
     * @param amount
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE,content = "${amountFormatStr}",operationType="submitPayment",systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/submitPayment.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput submitPayment(@RequestParam Long id, @RequestParam Long amount, @RequestParam Long waitAmount, @RequestParam String amountFormatStr){
        try{
            if(waitAmount > 0L){
                if(amount <= 0L){
                    return BaseOutput.failure("支付金额必须大于0");
                }
            }else if(waitAmount.equals(0L)){
                if(!amount.equals(0L)){
                    return BaseOutput.failure("支付金额必须等于0");
                }
            }
            return assetsLeaseOrderService.submitPayment(id,amount,waitAmount);
        }catch (BusinessException e){
            LOG.info("资产租赁订单提交付款异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("资产租赁订单提交付款异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 资产租赁退款申请
     * @param refundOrderDto
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE,content = "${totalRefundAmountFormatStr}",operationType="refundApply",systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/createRefundOrder.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput createRefundOrder(@RequestBody LeaseRefundOrderDto refundOrderDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        try{
            BaseOutput output = assetsLeaseOrderService.createRefundOrder(refundOrderDto);
            if(output.isSuccess()){
                LoggerUtil.buildLoggerContext(refundOrderDto.getBusinessId(),refundOrderDto.getBusinessCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),refundOrderDto.getRefundReason());
            }
            return output;
        }catch (BusinessException e){
            LOG.info("资产租赁退款申请异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("资产租赁退款申请异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

}