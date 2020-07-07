package com.dili.ia.controller;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.domain.dto.RefundOrderDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Controller
@RequestMapping("/refundOrder")
public class RefundOrderController {
    private final static Logger LOG = LoggerFactory.getLogger(RefundOrderController.class);
    @Autowired
    RefundOrderService refundOrderService;
    @Autowired
    LeaseOrderItemService leaseOrderItemService;
    @Autowired
    TransferDeductionItemService transferDeductionItemService;
    @Autowired
    BusinessLogRpc businessLogRpc;

    /**
     * 跳转到RefundOrder页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime createdStart = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth() -2 , 0, 0 ,0);
        LocalDateTime createdEnd = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth() , 23, 59 ,59);
        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);

        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        return "refundOrder/index";
    }

    /**
     * 分页查询RefundOrder，返回easyui分页信息
     * @param refundOrder
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(RefundOrderDto refundOrder) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        refundOrder.setMarketId(userTicket.getFirmId());
        return refundOrderService.listEasyuiPageByExample(refundOrder, true).toString();
    }

    /**
     * 跳转到退款单-查看页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id, String orderCode) {
        RefundOrder refundOrder = null;
        if(null != id) {
            refundOrder = refundOrderService.get(id);
        }else if(StringUtils.isNotBlank(orderCode)){
            RefundOrder query = new RefundOrder();
            query.setCode(orderCode);
            refundOrder = refundOrderService.get(refundOrderService.listByExample(query).stream().findFirst().orElse(null).getId());
            id = refundOrder.getId();
        }
        if(null != refundOrder){
            modelMap.put("refundOrder",refundOrder);
            try{
                //日志查询
                BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
                businessLogQueryInput.setBusinessId(id);
                businessLogQueryInput.setBusinessType(LogBizTypeConst.REFUND_ORDER);
                BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
                if(businessLogOutput.isSuccess()){
                    modelMap.put("logs",businessLogOutput.getData());
                }
            }catch (Exception e){
                LOG.error("日志服务查询异常",e);
            }

            if (refundOrder.getBizType().equals(BizTypeEnum.EARNEST.getCode())){
                return "refundOrder/earnestRefundOrderView";
            } else if (refundOrder.getBizType().equals(BizTypeEnum.DEPOSIT_ORDER.getCode())){
                return "refundOrder/depositRefundOrderView";
            }else if (refundOrder.getBizType().equals(BizTypeEnum.BOOTH_LEASE.getCode())){
                TransferDeductionItem transferDeductionItemCondition = new TransferDeductionItem();
                transferDeductionItemCondition.setRefundOrderId(id);
                modelMap.put("transferDeductionItems",transferDeductionItemService.list(transferDeductionItemCondition));
                if(null != refundOrder.getBusinessItemId()){
                    modelMap.put("leaseOrderItem",leaseOrderItemService.get(refundOrder.getBusinessItemId()));
                }
                return "refundOrder/leaseRefundOrderView";
            }
        }
        return "refundOrder/leaseRefundOrderView";
    }

    /**
     * 退款单--提交
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.REFUND_ORDER, content="${businessCode!}", operationType="submit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(Long id) {
        try {
            RefundOrder refundOrder = refundOrderService.get(id);
            if (refundOrder == null){
                return BaseOutput.failure("未查询到退款单！");
            }
            BaseOutput out = refundOrderService.doSubmitDispatcher(refundOrder);

            if (out.isSuccess()){
                //记录业务日志
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(refundOrder.getId(), refundOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), refundOrder.getRefundReason());
            }

            return out;
        } catch (BusinessException e) {
            LOG.error("退款单提交失败！", e);
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("退款单提交出错！", e);
            return BaseOutput.failure("提交出错！");
        }
    }

    /**
     * 退款单--撤回
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.REFUND_ORDER, content="${businessCode!}", operationType="withdraw", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(Long id) {
        try {
            RefundOrder refundOrder = refundOrderService.get(id);
            if (refundOrder == null){
                return BaseOutput.failure("未查询到退款单！");
            }
            BaseOutput out = refundOrderService.doWithdrawDispatcher(refundOrder);

            if (out.isSuccess()){
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(refundOrder.getId(), refundOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return out;
        } catch (BusinessException e) {
            LOG.error("退款单测回失败！", e);
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("退款单测回出错！", e);
            return BaseOutput.failure("撤回出错！");
        }
    }

    /**
     * 退款单--取消
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.REFUND_ORDER, content="${businessCode!}", operationType="cancel", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancel(Long id) {
        try {
            RefundOrder refundOrder = refundOrderService.get(id);
            BaseOutput output = refundOrderService.doCancelDispatcher(refundOrder);

            if (output.isSuccess()){
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(refundOrder.getId(), refundOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return output;
        } catch (BusinessException e) {
            LOG.error("退款单取消失败！", e);
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("退款单取消出错！", e);
            return BaseOutput.failure("取消出错！");
        }
    }
}