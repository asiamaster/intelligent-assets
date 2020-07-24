package com.dili.ia.controller;

import com.dili.bpmc.sdk.domain.TaskCenterParam;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.ApprovalParam;
import com.dili.ia.domain.dto.RefundOrderDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.service.*;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.IDTO;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款单控制器
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
    AssetsLeaseOrderItemService assetsLeaseOrderItemService;
    @Autowired
    TransferDeductionItemService transferDeductionItemService;
    @Autowired
    BusinessLogRpc businessLogRpc;
    @Autowired
    private ApprovalProcessService approvalProcessService;
    @Autowired
    private RefundFeeItemService refundFeeItemService;
    @Autowired
    CustomerRpc customerRpc;

    /**
     * 跳转到RefundOrder页面
     * @param modelMap
     * @param bizType 参考BizTypeEnum, 1：摊位租赁， 2:定金， 3:摊位保证金
     * @return String
     */
    @GetMapping(value="/{bizType}/index.html")
    public String index(ModelMap modelMap, @PathVariable("bizType") String bizType) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime createdStart = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth() -2 , 0, 0 ,0);
        LocalDateTime createdEnd = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth() , 23, 59 ,59);
        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        modelMap.put("bizType", bizType);
        return "refundOrder/index";
    }

    /**
     * 跳转到资产审批页面，任务中心调用
     * @param modelMap
     * @param bizType 参考BizTypeEnum, 1：摊位租赁， 2:定金， 3:摊位保证金
     * @return String
     */
    @GetMapping(value="/{bizType}/approval.html")
    public String assetsApproval(@PathVariable Integer bizType, TaskCenterParam taskCenterParam, ModelMap modelMap) {
        modelMap.put("taskDefinitionKey", taskCenterParam.getTaskDefinitionKey());
        modelMap.put("processInstanceId", taskCenterParam.getProcessInstanceId());
        modelMap.put("taskId", taskCenterParam.getTaskId());
        modelMap.put("businessKey", taskCenterParam.getBusinessKey());
        modelMap.put("formKey", taskCenterParam.getFormKey());
        ApprovalProcess approvalProcess = new ApprovalProcess();
        approvalProcess.setProcessInstanceId(taskCenterParam.getProcessInstanceId());
        //查询审批记录
        List<ApprovalProcess> approvalProcesses = approvalProcessService.list(approvalProcess);
        modelMap.put("approvalProcesses", approvalProcesses);
        return "refundOrder/approval";
    }

    /**
     * 跳转到退款单审批详情页面，用于查看归档记录
     * @param modelMap
     * @param assetsType 1：摊位， 2： 冷库， 3: 公寓, 4:其它
     * @return String
     */
    @GetMapping(value="/{assetsType}/approvalDetail.html")
    public String assetsApprovalDetail(@PathVariable Integer assetsType, TaskCenterParam taskCenterParam, ModelMap modelMap) {
        modelMap.put("taskDefinitionKey", taskCenterParam.getTaskDefinitionKey());
        modelMap.put("processInstanceId", taskCenterParam.getProcessInstanceId());
        modelMap.put("taskId", taskCenterParam.getTaskId());
        modelMap.put("businessKey", taskCenterParam.getBusinessKey());
        modelMap.put("formKey", taskCenterParam.getFormKey());
        ApprovalProcess approvalProcess = new ApprovalProcess();
        approvalProcess.setProcessInstanceId(taskCenterParam.getProcessInstanceId());
        List<ApprovalProcess> approvalProcesses = approvalProcessService.list(approvalProcess);
        modelMap.put("approvalProcesses", approvalProcesses);
        return "refundOrder/approvalDetail";
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
     * 跳转到退款单-查看页面版本，用于流程审批
     * @param modelMap
     * @return String
     */
    @GetMapping(value="/viewFragment.action")
    public String fragmentView(ModelMap modelMap, Long id, String orderCode){
        view(modelMap, id, orderCode);
        return "refundOrder/leaseRefundOrderViewFragment";
    }
    /**
     * 跳转到退款单-查看页面
     * @param modelMap
     * @return String
     */
    @GetMapping(value="/view.action")
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
                    AssetsLeaseOrderItem leaseOrderItem = assetsLeaseOrderItemService.get(refundOrder.getBusinessItemId());
                    modelMap.put("leaseOrderItem", leaseOrderItem);
                    RefundFeeItem condition = new RefundFeeItem();
                    condition.setRefundOrderId(refundOrder.getId());
                    modelMap.put("refundFeeItems", refundFeeItemService.list(condition));
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
     * 提交审批
     * @param id
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.REFUND_ORDER,operationType="submitForApproval",systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value="/submitForApproval.action")
    public @ResponseBody BaseOutput submitForApproval(@RequestParam Long id){
        try{
            refundOrderService.submitForApproval(id);
            return BaseOutput.success();
        }catch (BusinessException e){
            LOG.info("资产租赁订单提交审批异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("资产租赁订单提交审批异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     *
     * 审批通过处理
     * @return
     */
    @PostMapping(value="/approvedHandler.action")
    public @ResponseBody BaseOutput approvedHandler(@Validated ApprovalParam approvalParam){
        try{
            if(StringUtils.isNotEmpty(approvalParam.aget(IDTO.ERROR_MSG_KEY).toString())){
                return BaseOutput.failure(approvalParam.aget(IDTO.ERROR_MSG_KEY).toString());
            }
            refundOrderService.approvedHandler(approvalParam);
            return BaseOutput.success();
        }catch (BusinessException e){
            LOG.info("审批通过处理异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("审批通过处理异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     *
     * 审批拒绝处理
     * @return
     */
    @PostMapping(value="/approvedDeniedHandler.action")
    public @ResponseBody BaseOutput approvedDeniedHandler(@Validated ApprovalParam approvalParam){
        try{
            if(StringUtils.isNotEmpty(approvalParam.aget(IDTO.ERROR_MSG_KEY).toString())){
                return BaseOutput.failure(approvalParam.aget(IDTO.ERROR_MSG_KEY).toString());
            }
            refundOrderService.
                    approvedDeniedHandler(approvalParam);
            return BaseOutput.success();
        }catch (BusinessException e){
            LOG.info("审批拒绝处理异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("审批拒绝处理异常！", e);
            return BaseOutput.failure(e.getMessage());
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

    /**
     * 跳转到退款单-查看页面
     * @param modelMap
     * @return String
     */
    @GetMapping(value="/update.html")
    public String update(ModelMap modelMap, Long id) {
        RefundOrder refundOrder = refundOrderService.get(id);
        if (refundOrder != null){
            BaseOutput<Customer> payee = customerRpc.get(refundOrder.getPayeeId(), refundOrder.getMarketId());
            modelMap.put("refundOrder",refundOrder);
            modelMap.put("payee", payee.getData());
            if (refundOrder.getBizType().equals(BizTypeEnum.EARNEST.getCode())){
                return "refundOrder/updateView/refundApply";
            } else if (refundOrder.getBizType().equals(BizTypeEnum.DEPOSIT_ORDER.getCode())){
                return "refundOrder/updateView/refundApply";
            } else if (refundOrder.getBizType().equals(BizTypeEnum.BOOTH_LEASE.getCode())){
                return "forward:/assetsLeaseOrder/refundApply.html?refundOrderId=" + id + "&leaseOrderItemId=" + refundOrder.getBusinessItemId();
            }
        }
        return "refundOrder/updateView/refundApply";
    }

    /**
     * 修改DepositOrder
     * @param refundOrder
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.REFUND_ORDER, content="${logContent!}", operationType="edit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/doUpdate.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput doUpdate(RefundOrder refundOrder) {
        try{
            BaseOutput<RefundOrder> output = refundOrderService.doUpdateDispatcher(refundOrder);
            //写业务日志
            if (output.isSuccess()){
                RefundOrder order = refundOrderService.get(refundOrder.getId());
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), order.getRefundReason());
            }
            return output;
        }catch (BusinessException e){
            LOG.error("退款单修改异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("退款单修改异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}