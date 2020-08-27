package com.dili.ia.controller;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.bpmc.sdk.domain.TaskCenterParam;
import com.dili.bpmc.sdk.rpc.HistoryRpc;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.*;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.glossary.DepositOrderStateEnum;
import com.dili.ia.glossary.LeaseOrderStateEnum;
import com.dili.ia.glossary.LeaseRefundStateEnum;
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
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.IDTO;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Controller
@RequestMapping("/leaseOrder")
public class AssetsLeaseOrderController {
    private final static Logger LOG = LoggerFactory.getLogger(AssetsLeaseOrderController.class);
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
    @Autowired
    private AssetsRpc assetsRpc;
    @Autowired
    private DepositOrderService depositOrderService;
    @SuppressWarnings("all")
    @Autowired
    private HistoryRpc historyRpc;
    @Autowired
    private ApprovalProcessService approvalProcessService;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private RefundFeeItemService refundFeeItemService;
    @Autowired
    private TransferDeductionItemService transferDeductionItemService;


    /**
     * 跳转到LeaseOrder页面
     *
     * @param modelMap
     * @param assetsType
     * @return String
     */
    @GetMapping(value = "/{assetsType}/index.html")
    public String index(ModelMap modelMap, @PathVariable Integer assetsType) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.add(Calendar.DAY_OF_MONTH, -2);
        Date createdStart = c.getTime();

        Calendar ce = Calendar.getInstance();
        ce.set(ce.get(Calendar.YEAR), ce.get(Calendar.MONTH), ce.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date createdEnd = ce.getTime();

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new NotLoginException();
        }

        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        modelMap.put("assetsType", assetsType);
        return "assetsLeaseOrder/index";
    }

    /**
     * 跳转到资产审批页面，任务中心调用
     *
     * @param modelMap
     * @param assetsType 1：摊位， 2： 冷库， 3: 公寓, 4:其它
     * @return String
     */
    @GetMapping(value = "/{assetsType}/approval.html")
    public String assetsApproval(@PathVariable Integer assetsType, TaskCenterParam taskCenterParam, ModelMap modelMap) {
        //查询当前流程的历史任务实例，用于展示审批流程详情
//        BaseOutput<List<HistoricTaskInstanceMapping>> listBaseOutput = historyRpc.listHistoricTaskInstance(taskCenterParam.getProcessInstanceId(), true);
//        if(!listBaseOutput.isSuccess()){
//            throw new AppException("查询流程历史任务失败");
//        }
//        List<HistoricTaskInstanceMapping> historicTaskInstanceMappings = listBaseOutput.getData();
//        modelMap.put("historicTaskInstances", historicTaskInstanceMappings);
        modelMap.put("taskDefinitionKey", taskCenterParam.getTaskDefinitionKey());
        modelMap.put("processInstanceId", taskCenterParam.getProcessInstanceId());
        modelMap.put("taskId", taskCenterParam.getTaskId());
        modelMap.put("businessKey", taskCenterParam.getBusinessKey());
        modelMap.put("formKey", taskCenterParam.getFormKey());

        ApprovalProcess approvalProcess = new ApprovalProcess();
        approvalProcess.setProcessInstanceId(taskCenterParam.getProcessInstanceId());
        List<ApprovalProcess> approvalProcesses = approvalProcessService.list(approvalProcess);
        modelMap.put("approvalProcesses", approvalProcesses);
        return "assetsLeaseOrder/assetsApproval";
    }

    /**
     * 跳转到资产审批详情页面，用于查看归档记录
     *
     * @param modelMap
     * @param assetsType 1：摊位， 2： 冷库， 3: 公寓, 4:其它
     * @return String
     */
    @GetMapping(value = "/{assetsType}/approvalDetail.html")
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
        return "assetsLeaseOrder/assetsApprovalDetail";
    }

    /**
     * 审批通过处理
     *
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, operationType = "checkPass", content = "${logContent!}", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/approvedHandler.action")
    public @ResponseBody
    BaseOutput approvedHandler(@Validated ApprovalParam approvalParam) {
        try {
            if (StringUtils.isNotEmpty(approvalParam.aget(IDTO.ERROR_MSG_KEY).toString())) {
                return BaseOutput.failure(approvalParam.aget(IDTO.ERROR_MSG_KEY).toString());
            }
            assetsLeaseOrderService.approvedHandler(approvalParam);
            return BaseOutput.success();
        } catch (BusinessException e) {
            LOG.info("审批通过处理异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("审批通过处理异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 审批拒绝处理
     *
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, operationType = "checkFail", content = "${logContent!}", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/approvedDeniedHandler.action")
    public @ResponseBody
    BaseOutput approvedDeniedHandler(@Validated ApprovalParam approvalParam) {
        try {
            if (StringUtils.isNotEmpty(approvalParam.aget(IDTO.ERROR_MSG_KEY).toString())) {
                return BaseOutput.failure(approvalParam.aget(IDTO.ERROR_MSG_KEY).toString());
            }
            assetsLeaseOrderService.
                    approvedDeniedHandler(approvalParam);
            return BaseOutput.success();
        } catch (BusinessException e) {
            LOG.info("审批拒绝处理异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("审批拒绝处理异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 收费项meta信息
     *
     * @return
     */
    @GetMapping(value = "/queryBusinessChargeItemMeta.action")
    public @ResponseBody
    BaseOutput<List<BusinessChargeItemDto>> queryBusinessChargeItemMeta(Long leaseOrderId) {
        try {
            AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
            condition.setLeaseOrderId(leaseOrderId);
            List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(condition);
            return BaseOutput.success().setData(businessChargeItemService.queryBusinessChargeItemMeta(AssetsTypeEnum.getAssetsTypeEnum(leaseOrderItems.get(0).getAssetsType()).getBizType(), leaseOrderItems.stream().map(o -> o.getId()).collect(Collectors.toList())));
        } catch (BusinessException e) {
            LOG.info("收费项meta信息查询异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("收费项meta信息查询异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 跳转到LeaseOrder查看页面片段， 用于审批查看
     *
     * @param modelMap
     * @param orderCode 缴费单CODE
     * @param code
     * @return String
     */
    @GetMapping(value = "/viewFragment.action")
    public String viewFragment(ModelMap modelMap, Long id, String code, String orderCode) {
        view(modelMap, id, code, orderCode ,true);
        return "assetsLeaseOrder/viewFragment";
    }

    /**
     * 跳转到LeaseOrder查看页面
     *
     * @param modelMap
     * @param orderCode 缴费单CODE
     * @param code
     * @param isShowDepositAmount 是否显示保证金
     * @return String
     */
    @GetMapping(value = "/view.action")
    public String view(ModelMap modelMap, Long id, String code, String orderCode, boolean isShowDepositAmount) {
        AssetsLeaseOrder leaseOrder = null;
        if (null != id) {
            leaseOrder = assetsLeaseOrderService.get(id);
        } else if (StringUtils.isNotBlank(orderCode)) {
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setCode(orderCode);
            leaseOrder = assetsLeaseOrderService.get(paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null).getBusinessId());
        } else if (StringUtils.isNotBlank(code)) {
            AssetsLeaseOrder condition = new AssetsLeaseOrder();
            condition.setCode(code);
            leaseOrder = assetsLeaseOrderService.list(condition).stream().findFirst().orElse(null);
        }

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }

        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(condition);
        modelMap.put("leaseOrder", leaseOrder);
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemMeta(AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getBizType(), leaseOrderItems.stream().map(o -> o.getId()).collect(Collectors.toList()));
        modelMap.put("chargeItems", chargeItemDtos);
        modelMap.put("leaseOrderItems", assetsLeaseOrderItemService.leaseOrderItemListToDto(leaseOrderItems, AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getBizType(), chargeItemDtos));
        modelMap.put("isShowDepositAmount", isShowDepositAmount);
        try {
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessId(leaseOrder.getId());
            businessLogQueryInput.setBusinessType(LogBizTypeConst.BOOTH_LEASE);
            BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if (businessLogOutput.isSuccess()) {
                modelMap.put("logs", businessLogOutput.getData());
            }
        } catch (Exception e) {
            LOG.error("日志服务查询异常", e);
        }

        if(leaseOrder.getProcessInstanceId() != null) {
            //准备流程审批记录
            ApprovalProcess approvalProcess = new ApprovalProcess();
            approvalProcess.setProcessInstanceId(leaseOrder.getProcessInstanceId());
            List<ApprovalProcess> approvalProcesses = approvalProcessService.list(approvalProcess);
            if(!approvalProcesses.isEmpty()) {
                modelMap.put("approvalProcesses", approvalProcesses);
            }
        }
        return "assetsLeaseOrder/view";
    }

    /**
     * 跳转到LeaseOrder新增页面
     *
     * @param modelMap
     * @return String
     */
    @GetMapping(value = "/preSave.html")
    public String add(ModelMap modelMap, Long id, Integer assetsType, Integer isRenew) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemConfig(userTicket.getFirmId(), AssetsTypeEnum.getAssetsTypeEnum(assetsType).getBizType(), YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);
        if (null != id) {
            AssetsLeaseOrder leaseOrder = assetsLeaseOrderService.get(id);
            modelMap.put("leaseOrder", leaseOrder);

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
     * 租赁单退款预申请
     *
     * @param modelMap
     * @param leaseOrderItemId
     * @param refundOrderId
     * @return
     */
    @GetMapping(value = "/refundApply.html")
    public String refundApply(ModelMap modelMap, Long leaseOrderItemId, Long refundOrderId) {
        AssetsLeaseOrderItem leaseOrderItem = assetsLeaseOrderItemService.get(leaseOrderItemId);
        modelMap.put("leaseOrderItem", leaseOrderItem);
        BusinessChargeItem condition = new BusinessChargeItem();
        condition.setBusinessId(leaseOrderItem.getId());
        modelMap.put("businessChargeItems", businessChargeItemService.list(condition));
        modelMap.put("leaseOrder", assetsLeaseOrderService.get(leaseOrderItem.getLeaseOrderId()));
        if (null != refundOrderId) {
            modelMap.put("refundOrder", refundOrderService.get(refundOrderId));
            List<BusinessChargeItemDto> businessChargeItemDtos = businessChargeItemService.queryBusinessChargeItemMeta(AssetsTypeEnum.getAssetsTypeEnum(leaseOrderItem.getAssetsType()).getBizType(), List.of(leaseOrderItemId));
            modelMap.put("refundFeeItemMap", refundFeeItemService.queryRefundFeeItem(List.of(refundOrderId), businessChargeItemDtos).get(0));
            TransferDeductionItem transferDeductionItemCondition = new TransferDeductionItem();
            transferDeductionItemCondition.setRefundOrderId(refundOrderId);
            modelMap.put("transferDeductionItems", transferDeductionItemService.list(transferDeductionItemCondition));
        }

        return "assetsLeaseOrder/refundApply";
    }

    /**
     * 分页查询LeaseOrder，返回easyui分页信息
     *
     * @param leaseOrder
     * @return String
     * @throws Exception
     */
    @PostMapping(value = "/listPage.action")
    public @ResponseBody
    String listPage(AssetsLeaseOrderListDto leaseOrder) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }

        if (null != leaseOrder.getFirstDistrictId()) {
            //区域查询
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setParentId(leaseOrder.getFirstDistrictId());
            districtDTO.setMarketId(userTicket.getFirmId());
            List<Long> districtIds = assetsRpc.searchDistrict(districtDTO).getData().stream().map(o -> o.getId()).collect(Collectors.toList());
            districtIds.add(leaseOrder.getFirstDistrictId());

            AssetsLeaseOrderItemListDto assetsLeaseOrderItemListDto = new AssetsLeaseOrderItemListDto();
            assetsLeaseOrderItemListDto.setDistrictIds(districtIds);
            leaseOrder.setIds(new ArrayList<>(assetsLeaseOrderItemService.listByExample(assetsLeaseOrderItemListDto).stream().map(AssetsLeaseOrderItem::getLeaseOrderId).collect(Collectors.toSet())));
            if (CollectionUtils.isEmpty(leaseOrder.getIds())) {
                return new EasyuiPageOutput(0, Collections.emptyList()).toString();
            }
        }

        List<Long> departmentIdList = dataAuthService.getDepartmentDataAuth(userTicket);
        if (CollectionUtils.isEmpty(departmentIdList)) {
            return new EasyuiPageOutput(0, Collections.emptyList()).toString();
        }
        leaseOrder.setMarketId(userTicket.getFirmId());
        leaseOrder.setDepartmentIds(departmentIdList);

        if (StringUtils.isNotBlank(leaseOrder.getAssetsName())) {
            AssetsLeaseOrderItem leaseOrderItemCondition = new AssetsLeaseOrderItem();
            leaseOrderItemCondition.setAssetsName(leaseOrder.getAssetsName());
            leaseOrder.setIds(assetsLeaseOrderItemService.list(leaseOrderItemCondition).stream().map(AssetsLeaseOrderItem::getLeaseOrderId).collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(leaseOrder.getIds())) {
                return new EasyuiPageOutput(0, Collections.emptyList()).toString();
            }
        }
        return assetsLeaseOrderService.listEasyuiPageByExample(leaseOrder, true).toString();
    }

    /**
     * 租赁订单信息补录
     *
     * @param leaseOrder
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, content = "${contractNo}", operationType = "reNumber", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/supplement.action")
    public @ResponseBody
    BaseOutput supplement(AssetsLeaseOrder leaseOrder) {
        try {
            return assetsLeaseOrderService.supplement(leaseOrder);
        } catch (BusinessException e) {
            LOG.info("租赁订单信息补录异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("租赁订单信息补录异常！", e);
            return BaseOutput.failure(e.getMessage());
        }


    }

    /**
     * 租赁订单取消
     *
     * @param id 订单ID
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, operationType = "cancel", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/cancelOrder.action")
    public @ResponseBody
    BaseOutput cancelOrder(Long id) {
        try {
            return assetsLeaseOrderService.cancelOrder(id);
        } catch (BusinessException e) {
            LOG.info("租赁订单取消异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("租赁订单取消异常！", e);
            return BaseOutput.failure(e.getMessage());
        }


    }

    /**
     * 租赁订单撤回
     *
     * @param id 订单ID
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, operationType = "withdraw", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/withdrawOrder.action")
    public @ResponseBody
    BaseOutput withdrawOrder(Long id) {
        try {
            return assetsLeaseOrderService.withdrawOrder(id);
        } catch (BusinessException e) {
            LOG.info("租赁订单撤回异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("租赁订单撤回异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 资产租赁订单保存
     *
     * @param leaseOrder
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, content = "${logContent!}", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/saveLeaseOrder.action")
    public @ResponseBody
    BaseOutput saveLeaseOrder(@RequestBody AssetsLeaseOrderListDto leaseOrder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59");
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        leaseOrder.setEndTime(LocalDateTime.parse(leaseOrder.getEndTime().format(formatter), formatterDateTime));
        Long id = leaseOrder.getId();
        try {
            BaseOutput output = assetsLeaseOrderService.saveLeaseOrder(leaseOrder);
            //写业务日志
            if (output.isSuccess()) {
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, leaseOrder.getCode());
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, leaseOrder.getId());
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
                LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
                LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, null == id ? "add" : "edit");
                LoggerContext.put("notes", leaseOrder.getNotes());
            }
            return output;
        } catch (BusinessException e) {
            LOG.info("资产租赁订单保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("资产租赁订单保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * @param modelMap
     * @param id       对应租赁单ID 或 订单项ID
     * @return
     */
    @GetMapping(value = "/submitPayment.html")
    public String submitPayment(ModelMap modelMap, Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        AssetsLeaseOrder leaseOrder = assetsLeaseOrderService.get(id);
        modelMap.put("leaseOrder", leaseOrder);
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(id);
        condition.setRefundState(LeaseRefundStateEnum.WAIT_APPLY.getCode());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(condition);
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemMeta(AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getBizType(), leaseOrderItems.stream().map(o -> o.getId()).collect(Collectors.toList()));
        modelMap.put("chargeItems", chargeItemDtos);
        modelMap.put("leaseOrderItems", assetsLeaseOrderItemService.leaseOrderItemListToDto(leaseOrderItems, AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getBizType(), chargeItemDtos));
        //【已创建】状态或【已提交】状态是没有进行抵扣的，当从【已提交】状态流转到【未生效】或【已生效】时则完成了抵扣分摊
        modelMap.put("isNotDeducted", leaseOrder.getEarnestDeduction() + leaseOrder.getTransferDeduction() > 0 && (LeaseOrderStateEnum.CREATED.getCode().equals(leaseOrder.getState()) || LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())));
        return "assetsLeaseOrder/submitPayment";
    }


    /**
     * 提交付款
     *
     * @param assetsLeaseSubmitPaymentDto
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, content = "${leasePayAmountStr}", operationType = "submitPayment", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/submitPayment.action")
    public @ResponseBody
    BaseOutput submitPayment(@RequestBody AssetsLeaseSubmitPaymentDto assetsLeaseSubmitPaymentDto) {
        try {
            return assetsLeaseOrderService.submitPayment(assetsLeaseSubmitPaymentDto);
        } catch (BusinessException e) {
            LOG.info("资产租赁订单提交付款异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("资产租赁订单提交付款异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 提交审批
     *
     * @param id
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE, operationType = "submitForApproval", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/submitForApproval.action")
    public @ResponseBody
    BaseOutput submitForApproval(@RequestParam Long id) {
        try {
            assetsLeaseOrderService.submitForApproval(id);
            return BaseOutput.success();
        } catch (BusinessException e) {
            LOG.info("资产租赁订单提交审批异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("资产租赁订单提交审批异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 资产租赁退款单创建或修改
     *
     * @param refundOrderDto
     * @return BaseOutput
     */
    @BusinessLogger(content = "${content}", systemCode = "INTELLIGENT_ASSETS")
    @PostMapping(value = "/createOrUpdateRefundOrder.action")
    public @ResponseBody
    BaseOutput createRefundOrder(@RequestBody LeaseRefundOrderDto refundOrderDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        Long id = refundOrderDto.getId();
        try {
            BaseOutput output = assetsLeaseOrderService.createOrUpdateRefundOrder(refundOrderDto);
            if (output.isSuccess()) {
                if (null != id) {
                    LoggerContext.put("content", refundOrderDto.getLogContent());
                    LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "edit");
                    LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.REFUND_ORDER);
                    LoggerUtil.buildLoggerContext(refundOrderDto.getId(), refundOrderDto.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), refundOrderDto.getRefundReason());
                } else {
                    LoggerContext.put("content", MoneyUtils.centToYuan(refundOrderDto.getTotalRefundAmount()));
                    LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "refundApply");
                    LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.BOOTH_LEASE);
                    LoggerUtil.buildLoggerContext(refundOrderDto.getBusinessId(), refundOrderDto.getBusinessCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), refundOrderDto.getRefundReason());
                }
            }
            return output;
        } catch (BusinessException e) {
            LOG.info("资产租赁退款单创建或修改异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("资产租赁退款单创建或修改异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 客户批量资产保证金余额查询
     *
     * @param batchDepositBalanceQueryDto
     * @return String
     */
    @PostMapping(value = "/batchQueryDepositBalance.action")
    public @ResponseBody
    BaseOutput<List<DepositBalance>> batchQueryDepositBalance(@RequestBody BatchDepositBalanceQueryDto batchDepositBalanceQueryDto) {
        return depositOrderService.listDepositBalance(AssetsTypeEnum.getAssetsTypeEnum(batchDepositBalanceQueryDto.getAssetsType()).getBizType(), batchDepositBalanceQueryDto.getCustomerId(), batchDepositBalanceQueryDto.getAssetsIds());
    }

    /**
     * 批量订单项保证金（补交）查询
     *
     * @param depositOrderQuery
     * @return String
     */
    @PostMapping(value = "/batchQueryDepositOrder.action")
    public @ResponseBody
    BaseOutput<List<DepositOrder>> batchQueryDepositOrder(DepositOrderQuery depositOrderQuery) {
        try {
            depositOrderQuery.setIsRelated(YesOrNoEnum.YES.getCode());
            depositOrderQuery.setStateNotEquals(DepositOrderStateEnum.CANCELD.getCode());
            return BaseOutput.success().setData(depositOrderService.listByExample(depositOrderQuery));
        } catch (BusinessException e) {
            LOG.info("批量查询保证金单异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("批量查询保证金单异常！", e);
            return BaseOutput.failure(e.getMessage());
        }

    }

}