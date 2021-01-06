package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.bpmc.sdk.domain.ProcessInstanceMapping;
import com.dili.bpmc.sdk.domain.TaskMapping;
import com.dili.bpmc.sdk.dto.EventReceivedDto;
import com.dili.bpmc.sdk.dto.StartProcessInstanceDto;
import com.dili.bpmc.sdk.dto.TaskCompleteDto;
import com.dili.bpmc.sdk.rpc.restful.EventRpc;
import com.dili.bpmc.sdk.rpc.restful.RuntimeRpc;
import com.dili.bpmc.sdk.rpc.restful.TaskRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ia.cache.BpmDefKeyConfig;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.*;
import com.dili.ia.domain.dto.printDto.ContractDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.AssetsLeaseOrderMapper;
import com.dili.uid.sdk.rpc.feign.UidFeignRpc;

import cn.hutool.core.convert.Convert;

import com.dili.ia.service.*;
import com.dili.ia.util.LoggerUtil;
import com.dili.ia.util.SpringUtil;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.component.MsgService;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.rule.sdk.domain.input.QueryFeeInput;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EnableEnum;
import com.dili.settlement.enums.LinkTypeEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.rpc.SettleOrderRpc;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.exception.RemoteException;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.math.BigDecimal;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Service
public class AssetsLeaseOrderServiceImpl extends BaseServiceImpl<AssetsLeaseOrder, Long> implements AssetsLeaseOrderService {
    private final static Logger LOG = LoggerFactory.getLogger(AssetsLeaseOrderServiceImpl.class);

    public AssetsLeaseOrderMapper getActualDao() {
        return (AssetsLeaseOrderMapper) getDao();
    }

    @SuppressWarnings("all")
    @Autowired
    private DepartmentRpc departmentRpc;
    @Autowired
    private AssetsLeaseOrderItemService assetsLeaseOrderItemService;
    @Autowired
    private SettleOrderRpc settleOrderRpc;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${leaseOrder.settlement.handler.url}")
    private String settlerHandlerUrl;
    @Value("${leaseOrder.settlement.view.url}")
    private String viewUrl;
    @Value("${leaseOrder.settlement.print.url}")
    private String printUrl;

    @Autowired
    private UidFeignRpc uidFeignRpc;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private MsgService msgService;
    @Autowired
    private BusinessChargeItemService businessChargeItemService;
    @Autowired
    private RefundFeeItemService refundFeeItemService;
    @SuppressWarnings("all")
    @Autowired
    DataDictionaryRpc dataDictionaryRpc;
    @Autowired
    DepositOrderService depositOrderService;
    @Autowired
    AssetsRpc assetsRpc;
    @SuppressWarnings("all")
    @Autowired
    private RuntimeRpc runtimeRpc;
    @SuppressWarnings("all")
    @Autowired
    private TaskRpc taskRpc;
    @SuppressWarnings("all")
    @Autowired
    private EventRpc eventRpc;
    @Autowired
    private ApprovalProcessService approvalProcessService;
    @Autowired
    private ApportionRecordService apportionRecordService;
    @Autowired
    ChargeRuleRpc chargeRuleRpc;
    @Autowired
    private FirmRpc firmRpc;

    @Resource
    BpmDefKeyConfig bpmDefKeyConfig;
    @Autowired
    @Lazy
    private List<AssetsLeaseService> assetsLeaseServices;

    private Map<Integer, AssetsLeaseService> assetsLeaseServiceMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (AssetsLeaseService service : assetsLeaseServices) {
            this.assetsLeaseServiceMap.put(service.getAssetsType(), service);
        }
    }

    @Override
    @Transactional
    public BaseOutput saveLeaseOrder(AssetsLeaseOrderListDto dto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        //检查客户状态
        checkCustomerState(dto.getCustomerId(), userTicket.getFirmId());
        AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(dto.getAssetsType());
        //检查资产合法性
        Long latestMchId = assetsLeaseService.checkAssets(dto.getLeaseOrderItems().stream().map(lo -> lo.getAssetsId()).collect(Collectors.toList()), dto.getMchId(), dto.getBatchId());
        dto.setMchId(latestMchId);

        dto.setMarketId(userTicket.getFirmId());
        dto.setMarketCode(userTicket.getFirmCode());
        dto.setCreatorId(userTicket.getId());
        dto.setCreator(userTicket.getRealName());
        if (CollectionUtils.isNotEmpty(dto.getCategorys())) {
            dto.setCategoryId(dto.getCategorys().stream().map(o -> o.getId()).collect(Collectors.joining(",")));
            dto.setCategoryName(dto.getCategorys().stream().map(o -> o.getText()).collect(Collectors.joining(",")));
        }

        if (null == dto.getId()) {
            //租赁单新增
            checkContractNo(null, dto.getContractNo(), true);//线下合同号验证重复
            BaseOutput<String> bizNumberOutput = uidFeignRpc.getBizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.getBizTypeEnum(dto.getBizType()).getEnName() + "_" + BizNumberTypeEnum.LEASE_ORDER.getCode());
            if (!bizNumberOutput.isSuccess()) {
                LOG.info("租赁单编号生成异常");
                throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
            }
            /**
             * wm:启动租赁业务流程
             */
            StartProcessInstanceDto startProcessInstanceDto = DTOUtils.newInstance(StartProcessInstanceDto.class);
            startProcessInstanceDto.setProcessDefinitionKey(bpmDefKeyConfig.getLeaseBizDefKey(dto.getBizType(), dto.getMarketCode()));
            startProcessInstanceDto.setBusinessKey(bizNumberOutput.getData());
            startProcessInstanceDto.setUserId(userTicket.getId().toString());
            BaseOutput<ProcessInstanceMapping> processInstanceMappingBaseOutput = runtimeRpc.startProcessInstanceByKey(startProcessInstanceDto);
            if (!processInstanceMappingBaseOutput.isSuccess()) {
                throw new BusinessException(ResultCode.DATA_ERROR, "启动租赁业务流程异常" + processInstanceMappingBaseOutput.getMessage());
            }
            //wm:设置流程定义和实例id，后面会更新到租赁单表
            dto.setBizProcessDefinitionId(processInstanceMappingBaseOutput.getData().getProcessDefinitionId());
            dto.setBizProcessInstanceId(processInstanceMappingBaseOutput.getData().getProcessInstanceId());
            dto.setCode(bizNumberOutput.getData());
            dto.setState(LeaseOrderStateEnum.CREATED.getCode());
            dto.setPayState(PayStateEnum.NOT_PAID.getCode());
            dto.setApprovalState(ApprovalStateEnum.WAIT_SUBMIT_APPROVAL.getCode());
            dto.setWaitAmount(dto.getTotalAmount());
            insertSelective(dto);
            insertLeaseOrderItems(dto);
        } else {
            //租赁单修改
            checkDepositOrderPayState(dto); //检查保证金单交费状态 （已交费的单子，不能修改）
            checkContractNo(dto.getId(), dto.getContractNo(), false);//线下合同号验证重复
            AssetsLeaseOrder oldLeaseOrder = get(dto.getId());
            if (!LeaseOrderStateEnum.CREATED.getCode().equals(oldLeaseOrder.getState())
                    || ApprovalStateEnum.APPROVED.getCode().equals(oldLeaseOrder.getApprovalState())
                    || ApprovalStateEnum.IN_REVIEW.getCode().equals(oldLeaseOrder.getApprovalState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + oldLeaseOrder.getCode() + "】 状态已变更，不可以进行修改操作");
            }
            dto.setWaitAmount(dto.getTotalAmount());
            SpringUtil.copyPropertiesIgnoreNull(dto, oldLeaseOrder);
            oldLeaseOrder.setBatchId(dto.getBatchId());
            oldLeaseOrder.setContractNo(dto.getContractNo());
            oldLeaseOrder.setNotes(dto.getNotes());
            oldLeaseOrder.setCategoryId(dto.getCategoryId());
            oldLeaseOrder.setCategoryName(dto.getCategoryName());
            oldLeaseOrder.setManagerId(dto.getManagerId());
            oldLeaseOrder.setManager(dto.getManager());
            if (update(oldLeaseOrder) == 0) {
                LOG.info("摊位租赁单修改异常,乐观锁生效 【租赁单编号:{}】", dto.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
            }
            deleteLeaseOrderItems(dto.getId());
            insertLeaseOrderItems(dto);
        }

        //保证金保存
        saveOrUpdateDepositOrders(dto);
        return BaseOutput.success().setData(dto);
    }

    /**
     * 检查保证金单交费状态 （已交费的单子，不能修改）
     *
     * @param dto
     */
    public void checkDepositOrderPayState(AssetsLeaseOrderListDto dto) {
        DepositOrderQuery depositOrderQuery = new DepositOrderQuery();
        depositOrderQuery.setMarketId(dto.getMarketId());
        depositOrderQuery.setStateNotEquals(DepositOrderStateEnum.CANCELD.getCode());
        depositOrderQuery.setBusinessId(dto.getId());
        depositOrderQuery.setBizType(dto.getBizType());
        List<DepositOrder> depositOrders = depositOrderService.listByExample(depositOrderQuery);
        if (CollectionUtils.isNotEmpty(depositOrders)) {
            depositOrders.forEach(o -> {
                if (!DepositPayStateEnum.UNPAID.getCode().equals(o.getPayState())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "对应补交保证金已交费不能修改，请取消后重新录入");
                }
            });
        }
    }

    /**
     * 检查客户状态
     *
     * @param customerId
     * @param marketId
     */
    @Override
    public void checkCustomerState(Long customerId, Long marketId) {
        BaseOutput<CustomerExtendDto> output = customerRpc.get(customerId, marketId);
        if (!output.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户接口调用异常 " + output.getMessage());
        }
        CustomerExtendDto customer = output.getData();
        if (null == customer) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在，请重新修改后保存");
        } else if (EnabledStateEnum.DISABLED.getCode().equals(customer.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户【" + customer.getName() + "】已禁用，请重新修改后保存");
        } else if (YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户【" + customer.getName() + "】已删除，请重新修改后保存");
        }
    }


    @Override
    @Transactional
    public void submitForApproval(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new NotLoginException();
        }
        AssetsLeaseOrder leaseOrder = get(id);
//        if (!leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())
//                && (!ApprovalStateEnum.WAIT_SUBMIT_APPROVAL.getCode().equals(leaseOrder.getApprovalState())
//                || !ApprovalStateEnum.APPROVAL_DENIED.getCode().equals(leaseOrder.getApprovalState()))) {
//            throw new BusinessException(ResultCode.DATA_ERROR, "状态已流转不能提交审批，请刷新后再试");
//        }
        /**
         * 检查资产占用状态
         */
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(condition);
        AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(leaseOrder.getAssetsType());
        //每个摊位补交的保证金合计
        Long depositAmount = 0L;
        DepositOrderQuery depositOrderQuery = new DepositOrderQuery();
        depositOrderQuery.setIsRelated(YesOrNoEnum.YES.getCode());
        depositOrderQuery.setBusinessId(leaseOrder.getId());
        depositOrderQuery.setStateNotEquals(DepositOrderStateEnum.CANCELD.getCode());
        List<DepositOrder> depositOrders = depositOrderService.listByExample(depositOrderQuery);
        for (DepositOrder depositOrder : depositOrders) {
            depositAmount += depositOrder.getAmount();
        }

        if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {
            //检查客户状态
            checkCustomerState(leaseOrder.getCustomerId(), leaseOrder.getMarketId());
            //检查资产合法性
            Long latestMchId = assetsLeaseService.checkAssets(leaseOrderItems.stream().map(lo -> lo.getAssetsId()).collect(Collectors.toList()),leaseOrder.getMchId(),leaseOrder.getBatchId());
            leaseOrder.setMchId(latestMchId);
        }

        //wm:重新提交审批时清空旧的审批流程
        if(StringUtils.isNotBlank(leaseOrder.getProcessInstanceId())) {
            BaseOutput output = runtimeRpc.stopProcessInstanceById(leaseOrder.getProcessInstanceId(), "重新提交审批");
            if (!output.isSuccess()) {
                throw new BusinessException(ResultCode.DATA_ERROR, "重新提交审批时清空旧的审批流程失败:"+ output.getMessage());
            }
        }
        //构建审批流程参数
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", leaseOrder.getCustomerName());
        //wm:支付金额 = 总金额 + 摊位保证金合计, 用于任务标题展示
        Long payAmount = leaseOrder.getTotalAmount() + depositAmount;
        variables.put("payAmount", String.valueOf(payAmount / 100));
        variables.put("businessKey", leaseOrder.getCode());
        //市场id用于决定市场任务审批人
        variables.put("firmId", leaseOrder.getMarketId());

        /**
         * 冻结摊位
         */
        assetsLeaseService.frozenAsset(leaseOrder, leaseOrderItems);

        /**
         * wm:启动租赁审批子流程
         */
        if(StringUtils.isNotBlank(leaseOrder.getBizProcessInstanceId())) {
            EventReceivedDto eventReceivedDto = DTOUtils.newInstance(EventReceivedDto.class);
            eventReceivedDto.setEventName(BpmEventConstants.SUBMIT_APPROVAL_EVENT);
            eventReceivedDto.setProcessInstanceId(leaseOrder.getBizProcessInstanceId());
            eventReceivedDto.setVariables(variables);
            eventReceivedDto.setUserId(userTicket.getId().toString());
            BaseOutput<String> submitApprovalOutput = eventRpc.messageEventReceived(eventReceivedDto);
            if (!submitApprovalOutput.isSuccess()) {
                throw new BusinessException(ResultCode.DATA_ERROR, "审批子流程启动失败:" + submitApprovalOutput.getMessage());
            }
            //wm:获取审批子流程实例
            BaseOutput<ProcessInstanceMapping> subApprovalProcessInstance = runtimeRpc.findActiveProcessInstance(null, leaseOrder.getCode(), leaseOrder.getBizProcessInstanceId());
            ProcessInstanceMapping processInstanceMapping = subApprovalProcessInstance.getData();
            if (!subApprovalProcessInstance.isSuccess() || processInstanceMapping == null) {
                throw new BusinessException(ResultCode.APP_ERROR, "获取审批流程失败");
            }
            //wm:设置审批子流程定义和实例id，后面会更新到租赁单表
            leaseOrder.setProcessDefinitionId(subApprovalProcessInstance.getData().getProcessDefinitionId());
            leaseOrder.setProcessInstanceId(subApprovalProcessInstance.getData().getProcessInstanceId());
        }else {
            //没有业务流程实例id，需要直接启动审批流程
            StartProcessInstanceDto startProcessInstanceDto = DTOUtils.newInstance(StartProcessInstanceDto.class);
            startProcessInstanceDto.setProcessDefinitionKey(bpmDefKeyConfig.getLeaseApprovalDefKey(leaseOrder.getBizType(), leaseOrder.getMarketCode()));
            startProcessInstanceDto.setBusinessKey(leaseOrder.getCode());
            startProcessInstanceDto.setUserId(userTicket.getId().toString());
            startProcessInstanceDto.setVariables(variables);
            BaseOutput<ProcessInstanceMapping> processInstanceMappingBaseOutput = runtimeRpc.startProcessInstanceByKey(startProcessInstanceDto);
            if (!processInstanceMappingBaseOutput.isSuccess()) {
                throw new RemoteException(processInstanceMappingBaseOutput.getMessage());
            }
            //设置流程定义和实例id，后面会更新到租赁单表
            leaseOrder.setProcessDefinitionId(processInstanceMappingBaseOutput.getData().getProcessDefinitionId());
            leaseOrder.setProcessInstanceId(processInstanceMappingBaseOutput.getData().getProcessInstanceId());
        }
        leaseOrder.setApprovalState(ApprovalStateEnum.IN_REVIEW.getCode());
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
        }
        ApprovalParam approvalParam = DTOUtils.newInstance(ApprovalParam.class);
        approvalParam.setBusinessKey(leaseOrder.getCode());
        approvalParam.setProcessInstanceId(leaseOrder.getProcessInstanceId());
        approvalParam.setResult(ApprovalResultEnum.SUBMIT.getCode());
        saveApprovalProcess(approvalParam, userTicket);
        //写业务日志
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE,BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, leaseOrder.getCode());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, leaseOrder.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
        LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
    }

    @Override
    @Transactional
    @GlobalTransactional
    public void approvedHandler(ApprovalParam approvalParam) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new NotLoginException();
        }
        AssetsLeaseOrder condition = new AssetsLeaseOrder();
        condition.setCode(approvalParam.getBusinessKey());
        AssetsLeaseOrder leaseOrder = getActualDao().selectOne(condition);

        //wm:只有创建状态的订单才能提交审批任务
        if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {
            //wm:最后一次审批，更新审批状态、租赁单状态，并且全量提交租赁单到结算
            //wm:总经理审批通过需要更新审批状态
            if ("generalManagerApproval".equals(approvalParam.getTaskDefinitionKey())) {
                AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(leaseOrder.getAssetsType());
                //检查客户状态
                checkCustomerState(leaseOrder.getCustomerId(), leaseOrder.getMarketId());
                AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
                itemCondition.setLeaseOrderId(leaseOrder.getId());
                List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition);
                //检查资产合法性
                Long latestMchId = assetsLeaseService.checkAssets(leaseOrderItems.stream().map(lo -> lo.getAssetsId()).collect(Collectors.toList()),leaseOrder.getMchId(),leaseOrder.getBatchId());
                leaseOrder.setMchId(latestMchId);
                //wm:保存流程审批记录
                saveApprovalProcess(approvalParam, userTicket);
                //wm:提交审批任务(现在不需要根据区域名称来判断流程)
                completeTask(approvalParam.getTaskId(), "true");

                //构建收费项支付金额（全额提交）
                List<BusinessChargeItem> allBusinessChargeItems = new ArrayList<>();
                leaseOrderItems.forEach(l -> {
                    BusinessChargeItem chargeItemCondition = new BusinessChargeItem();
                    chargeItemCondition.setBusinessId(l.getId());
                    chargeItemCondition.setBizType(l.getBizType());
                    List<BusinessChargeItem> businessChargeItems = businessChargeItemService.list(chargeItemCondition);
                    businessChargeItems.forEach(bci -> {
                        bci.setPaymentAmount(bci.getAmount());
                    });
                    if (businessChargeItemService.batchUpdateSelective(businessChargeItems) != businessChargeItems.size()) {
                        LOG.info("批量更新收费项支付中金额 【businessId:{},bizType:{}】", l.getId(), l.getBizType());
                        throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
                    }
                    allBusinessChargeItems.addAll(businessChargeItems);
                });

                //提交付款
                Long paymentId = submitPay(leaseOrder, leaseOrder.getTotalAmount(), allBusinessChargeItems);
                leaseOrder.setState(LeaseOrderStateEnum.SUBMITTED.getCode());
                leaseOrder.setPaymentId(paymentId);
                leaseOrder.setApprovalState(ApprovalStateEnum.APPROVED.getCode());
                //更新摊位租赁单状态
                cascadeUpdateLeaseOrderState(leaseOrder, leaseOrderItems, true, LeaseOrderItemStateEnum.SUBMITTED);

                //保证金全额提交
                BaseOutput depositOutput = depositOrderService.batchSubmitDepositOrderFull(leaseOrder.getBizType(), leaseOrder.getId());
                if (!depositOutput.isSuccess()) {
                    LOG.info("保证金审批全额提交付款接口异常 【租赁单编号:{}】", leaseOrder.getCode());
                    throw new BusinessException(ResultCode.DATA_ERROR, depositOutput.getMessage());
                }
            }else{
                //wm:保存流程审批记录
                saveApprovalProcess(approvalParam, userTicket);
                //wm:提交审批任务(现在不需要根据区域名称来判断流程)
                completeTask(approvalParam.getTaskId(), "true");
            }

            //wm:写业务日志
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE,BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, approvalParam.getBusinessKey());
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, leaseOrder.getId());
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
            LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
            LoggerContext.put("logContent", approvalParam.getOpinion());

        } else {
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单状态不正确");
        }
    }

    @Override
    @Transactional
    @GlobalTransactional
    public void approvedDeniedHandler(ApprovalParam approvalParam) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new NotLoginException();
        }
        AssetsLeaseOrder condition = new AssetsLeaseOrder();
        condition.setCode(approvalParam.getBusinessKey());
        AssetsLeaseOrder leaseOrder = getActualDao().selectOne(condition);
        if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {//第一次发起付款，相关业务实现
            leaseOrder.setApprovalState(ApprovalStateEnum.APPROVAL_DENIED.getCode());
            if (updateSelective(leaseOrder) == 0) {
                LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
            }
        } else {
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单状态不正确");
        }
        //解冻摊位
        AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(leaseOrder.getAssetsType());
        assetsLeaseService.unFrozenAllAsset(leaseOrder.getId());
        //保存流程审批记录
        saveApprovalProcess(approvalParam, userTicket);

        //查第一个摊位，用于获取一级区域，进行流程判断
//        AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
//        itemCondition.setLeaseOrderId(leaseOrder.getId());
//        itemCondition.setPage(1);
//        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition);
        //第一个摊位的区域id，用于获取一级区域名称，在流程中进行判断
//        Long districtId = leaseOrderItems.get(0).getDistrictId();
        //写业务日志
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE,BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, approvalParam.getBusinessKey());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, leaseOrder.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
        LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
        LoggerContext.put("logContent", approvalParam.getOpinion());
        //提交审批任务(现在不需要根据区域名称来判断流程)
        completeTask(approvalParam.getTaskId(), "false");
    }


    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput submitPayment(AssetsLeaseSubmitPaymentDto assetsLeaseSubmitPaymentDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "未登录");
        }

        AssetsLeaseOrder leaseOrder = get(assetsLeaseSubmitPaymentDto.getLeaseOrderId());
        //记录当前审批状态，用于判断是否直接提交，而需要清空流程
        Integer approvalState = leaseOrder.getApprovalState();
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(condition);

        //租赁付款金额提交前提条件：1.大于0 2.等于0时，要么实付金额为0，要么有抵扣时摊位项分摊总金额等于抵扣额
        if (assetsLeaseSubmitPaymentDto.getLeasePayAmount() > 0L) {
            AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(leaseOrder.getAssetsType());
            /***************************检查是否可以提交付款 begin*********************/
            if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {
                //检查客户状态
                checkCustomerState(leaseOrder.getCustomerId(), leaseOrder.getMarketId());
                //检查资产合法性
                Long latestMchId = assetsLeaseService.checkAssets(leaseOrderItems.stream().map(lo -> lo.getAssetsId()).collect(Collectors.toList()),leaseOrder.getMchId(),leaseOrder.getBatchId());
                leaseOrder.setMchId(latestMchId);
            }
            //检查是否可以进行提交付款
            checkSubmitPayment(assetsLeaseSubmitPaymentDto.getLeaseOrderId(), assetsLeaseSubmitPaymentDto.getLeasePayAmount(), leaseOrder);
            /***************************检查是否可以提交付款 end*********************/

            //判断缴费单是否需要撤回 需要撤回则撤回
            if (null != leaseOrder.getPaymentId() && 0 != leaseOrder.getPaymentId()) {
                //撤回
                withdrawPaymentOrder(leaseOrder.getPaymentId());
                //撤回正在分摊中收费项金额
                withdrawBusinessChargeItemPaymentAmount(leaseOrderItems);
            }

            //提交付款
            Long paymentId = submitPay(leaseOrder, assetsLeaseSubmitPaymentDto.getLeasePayAmount(), assetsLeaseSubmitPaymentDto.getBusinessChargeItems());
            leaseOrder.setPaymentId(paymentId);
            if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {//第一次发起付款，相关业务实现
                //冻结摊位
                assetsLeaseService.frozenAsset(leaseOrder, leaseOrderItems);
                //提交付款
                leaseOrder.setState(LeaseOrderStateEnum.SUBMITTED.getCode());
                //提交时判断如果没有审批过则是待提审批，否则不处理审批状态
                if(leaseOrder.getApprovalState() == null) {
                    leaseOrder.setApprovalState(ApprovalStateEnum.WAIT_SUBMIT_APPROVAL.getCode());
                }
                //更新摊位租赁单状态
                cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.SUBMITTED);
            } else {
                //更新摊位租赁单状态
                if (updateSelective(leaseOrder) == 0) {
                    LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", assetsLeaseSubmitPaymentDto.getLeaseOrderId());
                    throw new BusinessException(ResultCode.DATA_ERROR, "摊位租赁单提交状态更新失败");
                }
            }

            assetsLeaseSubmitPaymentDto.getBusinessChargeItems().forEach(bci -> {
                BusinessChargeItem businessChargeItem = businessChargeItemService.get(bci.getId());
                bci.setVersion(businessChargeItem.getVersion());
            });
            if (businessChargeItemService.batchUpdateSelective(assetsLeaseSubmitPaymentDto.getBusinessChargeItems()) != assetsLeaseSubmitPaymentDto.getBusinessChargeItems().size()) {
                LOG.info("批量更新收费项支付中金额 【订单CODE{}】", leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
            }
        }

        if (!assetsLeaseSubmitPaymentDto.getDepositAmountMap().isEmpty()) {
            //保证金提交付款
            BaseOutput depositOutput = depositOrderService.batchSubmitDepositOrder(leaseOrder.getBizType(), leaseOrder.getId(), assetsLeaseSubmitPaymentDto.getDepositAmountMap());
            if (!depositOutput.isSuccess()) {
                LOG.info("保证金提交付款接口异常 【租赁单编号:{},数据:{}】", leaseOrder.getCode(), JSON.toJSONString(assetsLeaseSubmitPaymentDto.getDepositAmountMap()));
                throw new BusinessException(ResultCode.DATA_ERROR, depositOutput.getMessage());
            }
        }

        //wm:如果有审批流程实例id，需要判断当前是否审批通过，非审批通过状态的直接提交需要清空审批流程
        if(StringUtils.isNotBlank(leaseOrder.getProcessInstanceId()) && !ApprovalStateEnum.APPROVED.getCode().equals(approvalState)){
            AssetsLeaseOrder assetsLeaseOrder = new AssetsLeaseOrder();
            AssetsLeaseOrder dbAssetsLeaseOrder = get(leaseOrder.getId());
            assetsLeaseOrder.setId(leaseOrder.getId());
            assetsLeaseOrder.setVersion(dbAssetsLeaseOrder.getVersion());
            Map<String, Object> setForceParams = new HashMap<>();
            setForceParams.put("process_instance_id", null);
            setForceParams.put("process_definition_id", null);
            assetsLeaseOrder.setSetForceParams(setForceParams);
            if(0 == updateExact(assetsLeaseOrder)){
                LOG.info("清空审批流程实例id失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
            }
            //清空流程
            runtimeRpc.stopProcessInstanceById(leaseOrder.getProcessInstanceId(), "直接提交审批，清空流程");
        }

        //wm: 触发流程消息事件
        if (StringUtils.isNotBlank(leaseOrder.getBizProcessInstanceId())) {
            EventReceivedDto eventReceivedDto = DTOUtils.newInstance(EventReceivedDto.class);
            eventReceivedDto.setEventName(BpmEventConstants.SUBMIT_EVENT);
            eventReceivedDto.setProcessInstanceId(leaseOrder.getBizProcessInstanceId());
            BaseOutput<String> output = eventRpc.messageEventReceived(eventReceivedDto);
            if (!output.isSuccess()) {
                LOG.info("提交付款订单边界事件异常 【租赁单编号:{}】", leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
            }
        }

        //日志上下文构建
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE,BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
        LoggerContext.put("leasePayAmountStr",MoneyUtils.centToYuan(assetsLeaseSubmitPaymentDto.getLeasePayAmount()));
        LoggerUtil.buildLoggerContext(leaseOrder.getId(), leaseOrder.getCode(), userTicket.getId(), userTicket.getRealName(), leaseOrder.getMarketId(), null);
        return BaseOutput.success();
    }

    /**
     * 取消摊位租赁订单
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput cancelOrder(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        AssetsLeaseOrder leaseOrder = get(id);

        AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
        itemCondition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition);
        //仅有创建状态或已提交状态才可执行取消
//        if (!(LeaseOrderStateEnum.CREATED.getCode().equals(leaseOrder.getState())
//                || LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState()))) {
//            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
//            LOG.info("租赁单【编号：{}】状态为【{}】，不可以进行取消操作", leaseOrder.getCode(), stateName);
//            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单状态为【" + stateName + "】，不可以进行取消操作");
//        }
        int currentState = leaseOrder.getState();
        if (null != leaseOrder.getPaymentId() && 0L != leaseOrder.getPaymentId()) {
            withdrawPaymentOrder(leaseOrder.getPaymentId());
            leaseOrder.setPaymentId(0L);
            //撤回正在分摊中收费项金额
            withdrawBusinessChargeItemPaymentAmount(leaseOrderItems);
        }

        AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(leaseOrder.getAssetsType());
        //释放租赁
        assetsLeaseService.unFrozenAllAsset(id);

        leaseOrder.setState(LeaseOrderStateEnum.CANCELD.getCode());
        leaseOrder.setCancelerId(userTicket.getId());
        leaseOrder.setCanceler(userTicket.getRealName());
        String formatNow = DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        if (StringUtils.isNotBlank(leaseOrder.getContractNo())) {
            leaseOrder.setContractNo(leaseOrder.getContractNo() + "_" + formatNow);
        }

        //保证金取消
        BaseOutput depositOutput = depositOrderService.batchCancelDepositOrder(leaseOrder.getBizType(), id);
        if (!depositOutput.isSuccess()) {
            LOG.info("取消保证金单接口异常 【租赁单编号:{}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, depositOutput.getMessage());
        }

        //联动摊位租赁单项状态 取消
        cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.CANCELD);

        //wm: 触发流程消息事件
        if (StringUtils.isNotBlank(leaseOrder.getBizProcessInstanceId())) {
            EventReceivedDto eventReceivedDto = DTOUtils.newInstance(EventReceivedDto.class);
            eventReceivedDto.setEventName(BpmEventConstants.CANCEL_EVENT);
            eventReceivedDto.setProcessInstanceId(leaseOrder.getBizProcessInstanceId());
            BaseOutput<String> output = eventRpc.messageEventReceived(eventReceivedDto);
            if (!output.isSuccess()) {
                LOG.info("取消订单边界事件异常 【租赁单编号:{}】", leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
            }
        }

        //日志上下文构建
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE,BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
        LoggerUtil.buildLoggerContext(leaseOrder.getId(), leaseOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success();
    }

    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput invalidOrder(Long id, String invalidReason) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        AssetsLeaseOrder leaseOrder = get(id);
        PaymentOrder paymentOrderCondition = new PaymentOrder();
        paymentOrderCondition.setBusinessId(id);
        paymentOrderCondition.setBizType(leaseOrder.getBizType());
        List<PaymentOrder> paymentOrders = paymentOrderService.listByExample(paymentOrderCondition);
        //检查作废
        checkInvalid(leaseOrder, paymentOrders);


        //检查同步状态
        List<String> notPaidPaymentOrders = paymentOrders.stream().filter(o -> PaymentOrderStateEnum.NOT_PAID.getCode().equals(o.getState())).map(o -> o.getCode()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(notPaidPaymentOrders)) {
            SettleOrderDto notPaidOrderCondition = new SettleOrderDto();
            notPaidOrderCondition.setAppId(settlementAppId);
            notPaidOrderCondition.setMarketId(leaseOrder.getMarketId());
            notPaidOrderCondition.setOrderCodeList(notPaidPaymentOrders);
            List<SettleOrder> settleOrders = settleOrderRpc.list(notPaidOrderCondition).getData();
            if (CollectionUtils.isNotEmpty(settleOrders.stream().filter(s -> SettleStateEnum.DEAL.getCode() == s.getState()).collect(Collectors.toList()))) {
                LOG.error("缴费状态未同步 【租赁单CODE {}】", leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "缴费状态未同步，不能进行此操作");
            }
        }

        //构建缴费红冲单
        List<PaymentOrder> rerverpaymentOrders = new ArrayList<>();
        paymentOrders.stream().forEach(o -> {
            if (PaymentOrderStateEnum.PAID.getCode().equals(o.getState())) {
                PaymentOrder newPaymentOrder = o.clone();
                newPaymentOrder.setCreateTime(LocalDateTime.now());
                newPaymentOrder.setModifyTime(LocalDateTime.now());
                newPaymentOrder.setCreatorId(userTicket.getId());
                newPaymentOrder.setCreator(userTicket.getRealName());
                newPaymentOrder.setIsReverse(YesOrNoEnum.YES.getCode());
                newPaymentOrder.setParentId(o.getId());
                newPaymentOrder.setId(null);

                BaseOutput<String> bizNumberOutput = uidFeignRpc.getBizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.getBizTypeEnum(o.getBizType()).getEnName() + "_" + BizNumberTypeEnum.PAYMENT_ORDER.getCode());
                if (!bizNumberOutput.isSuccess()) {
                    LOG.info("租赁单【编号：{}】,缴费单编号生成异常", leaseOrder.getCode());
                    throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
                }
                newPaymentOrder.setCode(bizNumberOutput.getData());
                rerverpaymentOrders.add(newPaymentOrder);
            } else if (PaymentOrderStateEnum.NOT_PAID.getCode().equals(o.getState())) {
                withdrawPaymentOrder(o.getId());
            }

        });
        if (!rerverpaymentOrders.isEmpty() && paymentOrderService.batchInsert(rerverpaymentOrders) != rerverpaymentOrders.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "缴费单红冲写入失败！");
        }

        //更新作废人信息及状态
        leaseOrder.setState(LeaseOrderStateEnum.INVALIDATED.getCode());
        leaseOrder.setInvalidReason(invalidReason);
        leaseOrder.setInvalidTime(LocalDateTime.now());
        leaseOrder.setInvalidOperatorId(userTicket.getId());
        leaseOrder.setInvalidOperator(userTicket.getRealName());
        String formatNow = DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        if (StringUtils.isNotBlank(leaseOrder.getContractNo())) {
            leaseOrder.setContractNo(leaseOrder.getContractNo() + "_" + formatNow);
        }
        cascadeUpdateLeaseOrderState(leaseOrder,true,LeaseOrderItemStateEnum.INVALIDATED);

        //释放租赁时间段
        AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(leaseOrder.getAssetsType());
        assetsLeaseService.unFrozenAllAsset(leaseOrder.getId());

        //释放关联保证金，让其单飞
        BaseOutput depositOutput = depositOrderService.batchReleaseRelated(leaseOrder.getBizType(), leaseOrder.getId(),null);
        if (!depositOutput.isSuccess()) {
            LOG.info("释放关联保证金，让其单飞接口异常 【租赁单编号:{}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, depositOutput.getMessage());
        }

        //作废结算单
        InvalidRequestDto invalidRequestDto = new InvalidRequestDto();
        invalidRequestDto.setAppId(settlementAppId);
        invalidRequestDto.setMarketId(leaseOrder.getMarketId());
        invalidRequestDto.setMarketCode(leaseOrder.getMarketCode());
        invalidRequestDto.setOperatorId(userTicket.getId());
        invalidRequestDto.setOperatorName(userTicket.getRealName());
        invalidRequestDto.setOrderCodeList(paymentOrders.stream().map(o -> o.getCode()).collect(Collectors.toList()));
        BaseOutput settlementInvalidOutput = settleOrderRpc.invalid(invalidRequestDto);
        if (!settlementInvalidOutput.isSuccess()) {
            LOG.error("租赁单作废调用结算单作废异常 【租赁单CODE {}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, settlementInvalidOutput.getMessage());
        }
        //wm: 触发流程消息事件
        if (StringUtils.isNotBlank(leaseOrder.getBizProcessInstanceId())) {
            EventReceivedDto eventReceivedDto = DTOUtils.newInstance(EventReceivedDto.class);
            eventReceivedDto.setEventName(BpmEventConstants.OBSOLETE_EVENT);
            eventReceivedDto.setProcessInstanceId(leaseOrder.getBizProcessInstanceId());
            BaseOutput<String> output = eventRpc.messageEventReceived(eventReceivedDto);
            if (!output.isSuccess()) {
                LOG.info("作废订单边界事件异常 【租赁单编号:{}】", leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
            }
        }
        //日志上下文构建
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE,BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
        LoggerUtil.buildLoggerContext(leaseOrder.getId(), leaseOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), invalidReason);
        return BaseOutput.success();
    }

    /**
     * 撤回摊位租赁订单
     *
     * @param id
     * @return
     */
    @Transactional
    @GlobalTransactional
    @Override
    public BaseOutput withdrawOrder(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        AssetsLeaseOrder leaseOrder = get(id);
        AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
        itemCondition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition);
        if (!LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState()) || ApprovalStateEnum.APPROVED.getCode().equals(leaseOrder.getApprovalState())) {
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.info("租赁单【编号：{}】状态为【{}】或审批通过，不可以进行撤回操作", leaseOrder.getCode(), stateName);
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单状态为【" + stateName + "】或审批通过，不可以进行撤回操作");
        }
        if (null != leaseOrder.getPaymentId() && 0L != leaseOrder.getPaymentId()) {
            withdrawPaymentOrder(leaseOrder.getPaymentId());
            leaseOrder.setPaymentId(0L);
            //撤回正在分摊中收费项金额
            withdrawBusinessChargeItemPaymentAmount(leaseOrderItems);
        }
        leaseOrder.setState(LeaseOrderStateEnum.CREATED.getCode());
        leaseOrder.setApprovalState(ApprovalStateEnum.WAIT_SUBMIT_APPROVAL.getCode());
        cascadeUpdateLeaseOrderState(leaseOrder, leaseOrderItems, true, LeaseOrderItemStateEnum.CREATED);

        //保证金撤回
        BaseOutput depositOutput = depositOrderService.batchWithdrawDepositOrder(leaseOrder.getBizType(), id);
        if (!depositOutput.isSuccess()) {
            LOG.info("撤回保证金单接口异常 【租赁单编号:{}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, depositOutput.getMessage());
        }

        //解冻摊位
        AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(leaseOrder.getAssetsType());
        assetsLeaseService.unFrozenAllAsset(leaseOrder.getId());
        //发送流程消息通知撤回
        if (StringUtils.isNotBlank(leaseOrder.getBizProcessInstanceId())) {
            EventReceivedDto eventReceivedDto = DTOUtils.newInstance(EventReceivedDto.class);
            eventReceivedDto.setEventName(BpmEventConstants.WITHDRAW_EVENT);
            eventReceivedDto.setProcessInstanceId(leaseOrder.getBizProcessInstanceId());
            BaseOutput<String> output = eventRpc.messageEventReceived(eventReceivedDto);
            if (!output.isSuccess()) {
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
            }
        }

        //日志上下文构建
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE,BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
        LoggerUtil.buildLoggerContext(leaseOrder.getId(), leaseOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success();
    }


    /**
     * 结算成功，同步更新租赁单相关信息
     *
     * @param settleOrder
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput<Boolean> updateLeaseOrderBySettleInfo(SettleOrder settleOrder) {
        /*****************************更新缴费单相关字段 begin***********************/
        PaymentOrder condition = new PaymentOrder();
        condition.setCode(settleOrder.getOrderCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        AssetsLeaseOrder leaseOrder = get(paymentOrderPO.getBusinessId());
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) {
            return BaseOutput.success();
        }

        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setSettlementWay(settleOrder.getWay());
        paymentOrderPO.setSettlementOperator(settleOrder.getOperatorName());
        paymentOrderPO.setPayedTime(settleOrder.getOperateTime());
        if (leaseOrder.getWaitAmount().equals(paymentOrderPO.getAmount())) {
            paymentOrderPO.setIsSettle(YesOrNoEnum.YES.getCode());
        }
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            LOG.info("结算成功，缴费单同步数据异常,乐观锁生效 【缴费单编号:{}】", settleOrder.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        /*****************************更新缴费单相关字段 end*************************/


        //第一次消费，需要解冻出租摊位
        if (LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())) {
            //解冻出租摊位
            AssetsLeaseService assetsLeaseService = assetsLeaseServiceMap.get(leaseOrder.getAssetsType());
            assetsLeaseService.leaseAsset(leaseOrder);
        }
        //记录原始状态，在后面流程引擎用到
        Integer leaseOrderState = leaseOrder.getState();
        /***************************更新租赁单及其订单项相关字段 begin*********************/
        //根据租赁时间和当前时间比对，单子是未生效、已生效、还是已过期
        if (!LeaseOrderStateEnum.RENTED_OUT.getCode().equals(leaseOrderState)) { //单子已停租 状态不做变化
            LocalDateTime now = LocalDateTime.now();
            if (!now.isBefore(leaseOrder.getStartTime()) && !now.isAfter(leaseOrder.getEndTime())) {
                leaseOrder.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
            } else if (now.isBefore(leaseOrder.getStartTime())) {
                leaseOrder.setState(LeaseOrderStateEnum.NOT_ACTIVE.getCode());
            } else if (now.isAfter(leaseOrder.getEndTime())) {
                leaseOrder.setState(LeaseOrderStateEnum.EXPIRED.getCode());
            }
        }

        if ((leaseOrder.getWaitAmount() - paymentOrderPO.getAmount()) == 0L) {
            leaseOrder.setPayState(PayStateEnum.PAID.getCode());
            //释放关联保证金，让其单飞
            BaseOutput depositOutput = depositOrderService.batchReleaseRelated(leaseOrder.getBizType(), leaseOrder.getId(),null);
            if (!depositOutput.isSuccess()) {
                LOG.info("释放关联保证金，让其单飞接口异常 【租赁单编号:{}】", leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, depositOutput.getMessage());
            }
        }
        leaseOrder.setWaitAmount(leaseOrder.getWaitAmount() - paymentOrderPO.getAmount());
        leaseOrder.setPaidAmount(leaseOrder.getPaidAmount() + paymentOrderPO.getAmount());
        leaseOrder.setPaymentId(0L);
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
        }
        AssetsLeaseOrderItem leaseOrderItemCondition = new AssetsLeaseOrderItem();
        leaseOrderItemCondition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(leaseOrderItemCondition);
        String bizType = leaseOrder.getBizType();
        List<ApportionRecord> apportionRecords = new ArrayList<>();
        leaseOrderItems.stream().forEach(o -> {
            BusinessChargeItem chargeItemCondition = new BusinessChargeItem();
            chargeItemCondition.setBusinessId(o.getId());
            chargeItemCondition.setBizType(bizType);
            List<BusinessChargeItem> businessChargeItems = businessChargeItemService.list(chargeItemCondition);

            Long itemPaymentAmount = businessChargeItems.stream().mapToLong(BusinessChargeItem::getPaymentAmount).sum();
            o.setState(leaseOrder.getState());
            if ((o.getWaitAmount() - itemPaymentAmount) == 0L) {
                o.setPayState(PayStateEnum.PAID.getCode());
            }

            if (itemPaymentAmount > 0L) {
                o.setWaitAmount(o.getWaitAmount() - itemPaymentAmount);
                o.setPaidAmount(o.getPaidAmount() + itemPaymentAmount);

                //业务收费项完成分摊
                businessChargeItems.stream().filter(bci -> bci.getPaymentAmount() > 0L).forEach(bci -> {
                    ApportionRecord apportionRecord = buildApportionRecord( o, bci);
                    apportionRecord.setPaymentOrderId(paymentOrderPO.getId());
                    apportionRecords.add(apportionRecord);

                    bci.setWaitAmount(bci.getWaitAmount() - bci.getPaymentAmount());
                    bci.setPaidAmount(bci.getPaidAmount() + bci.getPaymentAmount());
                    bci.setPaymentAmount(0L);
                });
                if (businessChargeItemService.batchUpdateSelective(businessChargeItems) != businessChargeItems.size()) {
                    LOG.info("结算成功完成分摊 【businessId:{},bizType:{}】", o.getId(), bizType);
                    throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
                }
            }
        });
        if (assetsLeaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        if (!apportionRecords.isEmpty() && apportionRecordService.batchInsert(apportionRecords) != apportionRecords.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "分摊明细写入失败！");
        }
        /***************************更新租赁单及其订单项相关字段 end*********************/
        //如果是提交状态的租赁单(第一次提交付款)，并且有流程实例id，则通知流程结束
//        if(StringUtils.isNotBlank(leaseOrder.getProcessInstanceId()) && leaseOrderState.equals(LeaseOrderStateEnum.SUBMITTED.getCode())) {
//            //发送消息通知流程
//            BaseOutput<String> baseOutput = eventRpc.signal(leaseOrder.getProcessInstanceId(), "confirmReceipt", null);
//            if (!baseOutput.isSuccess()) {
//                throw new BusinessException(ResultCode.DATA_ERROR, "流程结束消息发送失败");
//            }
//        }
        //wm: 触发java接收任务流程事件
        if (StringUtils.isNotBlank(leaseOrder.getBizProcessInstanceId())) {
            HashMap<String, Object> param = new HashMap<>();
            //根据订单状态流转到未生效4、已生效5或已到期8
            param.put("leaseOrderState", leaseOrder.getState());
            EventReceivedDto eventReceivedDto = DTOUtils.newInstance(EventReceivedDto.class);
            eventReceivedDto.setEventName(BpmEventConstants.SUBMITTED_RECEIVE_TASK);
            eventReceivedDto.setProcessInstanceId(leaseOrder.getBizProcessInstanceId());
            eventReceivedDto.setVariables(param);
            BaseOutput<String> output = eventRpc.signal(eventReceivedDto);
            if (!output.isSuccess()) {
                LOG.info("确认付款事件异常 【租赁单编号:{}】", leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
            }
        }
        msgService.sendBusinessLog(recordPayLog(settleOrder, leaseOrder));
        return BaseOutput.success();
    }

    /**
     * 租赁单生效处理
     *
     * @param o
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void leaseOrderEffectiveHandler(AssetsLeaseOrder o) {
        o.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
        if (updateSelective(o) == 0) {
            LOG.info("租赁单生效处理异常,乐观锁生效 【租赁单ID {}】", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
        }

        AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
        itemCondition.setLeaseOrderId(o.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition);

        List<AssetsLeaseOrderItem> waitItems = new ArrayList<>();
        leaseOrderItems.stream().forEach(leaseOrderItem -> {
            //只更新待生效的订单项
            if (LeaseOrderItemStateEnum.NOT_ACTIVE.getCode().equals(leaseOrderItem.getState())) {
                leaseOrderItem.setState(LeaseOrderItemStateEnum.EFFECTIVE.getCode());
                waitItems.add(leaseOrderItem);
            }
        });
        if (assetsLeaseOrderItemService.batchUpdateSelective(waitItems) != waitItems.size()) {
            LOG.info("租赁单生效处理异常,级联批量处理摊位订单项乐观锁生效【订单ID:{}", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        //wm: 触发java接收任务流程事件
        if (StringUtils.isNotBlank(o.getBizProcessInstanceId())) {
            EventReceivedDto eventReceivedDto = DTOUtils.newInstance(EventReceivedDto.class);
            eventReceivedDto.setEventName(BpmEventConstants.NOT_ACTIVE_RECEIVE_TASK);
            eventReceivedDto.setProcessInstanceId(o.getBizProcessInstanceId());
            BaseOutput<String> output = eventRpc.signal(eventReceivedDto);
            if (!output.isSuccess()) {
//                LOG.info("租赁单生效事件异常 【租赁单编号:{}】", o.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
            }
        }

    }

    /**
     * 租赁单到期处理
     *
     * @param o
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void leaseOrderExpiredHandler(AssetsLeaseOrder o) {
        o.setState(LeaseOrderStateEnum.EXPIRED.getCode());
        if (updateSelective(o) == 0) {
            LOG.info("租赁单到期处理异常 乐观锁生效 【租赁单ID {}】", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
        }

        AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
        itemCondition.setLeaseOrderId(o.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition);

        List<AssetsLeaseOrderItem> waitItems = new ArrayList<>();
        leaseOrderItems.stream().forEach(leaseOrderItem -> {
            //只更新待到期的订单项
            if (LeaseOrderItemStateEnum.EFFECTIVE.getCode().equals(leaseOrderItem.getState())) {
                leaseOrderItem.setState(LeaseOrderItemStateEnum.EXPIRED.getCode());
                waitItems.add(leaseOrderItem);
            }
        });
        if (assetsLeaseOrderItemService.batchUpdateSelective(waitItems) != waitItems.size()) {
            LOG.info("租赁单到期处理异常,级联批量处理摊位订单项乐观锁生效【订单ID:{}", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        //wm: 触发java接收任务流程事件
        if (StringUtils.isNotBlank(o.getBizProcessInstanceId())) {
            EventReceivedDto eventReceivedDto = DTOUtils.newInstance(EventReceivedDto.class);
            eventReceivedDto.setEventName(BpmEventConstants.ACTIVE_RECEIVE_TASK);
            eventReceivedDto.setProcessInstanceId(o.getBizProcessInstanceId());
            BaseOutput<String> output = eventRpc.signal(eventReceivedDto);
            if (!output.isSuccess()) {
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
//                LOG.info("租赁单过期事件异常 【租赁单编号:{}】", o.getCode());
            }
        }
    }


    @Override
    @Transactional
    public BaseOutput createOrUpdateRefundOrder(LeaseRefundOrderDto refundOrderDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        refundOrderDto.setPayeeAmount(refundOrderDto.getTotalRefundAmount());
        //订单项退款申请
        AssetsLeaseOrderItem leaseOrderItem = assetsLeaseOrderItemService.get(refundOrderDto.getBusinessItemId());
        //摊位订单项退款申请条件检查
        checkRufundApplyWithLeaseOrderItem(refundOrderDto, leaseOrderItem, userTicket);

        //新增
        if (null == refundOrderDto.getId()) {
            AssetsLeaseOrder leaseOrder = get(leaseOrderItem.getLeaseOrderId());
            AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
            itemCondition.setLeaseOrderId(leaseOrder.getId());
            List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition);
            //判断缴费单是否需要撤回 需要撤回则撤回
            if (null != leaseOrder.getPaymentId() && 0 != leaseOrder.getPaymentId()) {
                withdrawPaymentOrder(leaseOrder.getPaymentId());
                //撤回正在分摊中收费项金额
                withdrawBusinessChargeItemPaymentAmount(leaseOrderItems);
            }
            //同步更新主单退款状态为【退款中】
            leaseOrder.setRefundState(LeaseRefundStateEnum.REFUNDING.getCode());
            if (updateSelective(leaseOrder) == 0) {
                LOG.info("摊位租赁单退款状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
            }

            BaseOutput<String> bizNumberOutput = uidFeignRpc.getBizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.getBizTypeEnum(refundOrderDto.getBizType()).getEnName() + "_" + BizNumberTypeEnum.REFUND_ORDER.getCode());
            if (!bizNumberOutput.isSuccess()) {
                LOG.info("租赁单【编号：{}】退款单编号生成异常", refundOrderDto.getBusinessCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
            }
            refundOrderDto.setCode(bizNumberOutput.getData());
            refundOrderDto.setDistrictId(null != leaseOrderItem.getSecondDistrictId() ? leaseOrderItem.getSecondDistrictId() : leaseOrderItem.getFirstDistrictId());

            //根据退款单业务类型启动流程
            StartProcessInstanceDto startProcessInstanceDto = DTOUtils.newInstance(StartProcessInstanceDto.class);
            startProcessInstanceDto.setProcessDefinitionKey(bpmDefKeyConfig.getRefundBizDefKey(refundOrderDto.getBizType(), userTicket.getFirmCode()));
            startProcessInstanceDto.setBusinessKey(refundOrderDto.getCode());
            startProcessInstanceDto.setUserId(userTicket.getId().toString());
            BaseOutput<ProcessInstanceMapping> output = runtimeRpc.startProcessInstanceByKey(startProcessInstanceDto);
            if(!output.isSuccess()){
                throw new RemoteException(output.getMessage());
            }
            //设置流程实例id和流程定义id到退款单对象
            refundOrderDto.setBizProcessInstanceId(output.getData().getProcessInstanceId());
            refundOrderDto.setBizProcessDefinitionId(output.getData().getProcessDefinitionId());
            if (!refundOrderService.doAddHandler(refundOrderDto).isSuccess()) {
                LOG.info("租赁单【编号：{}】退款申请接口异常", refundOrderDto.getBusinessCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常");
            }

        } else { //修改
            RefundOrder oldRefundOrder = refundOrderService.get(refundOrderDto.getId());
            if (ApprovalStateEnum.APPROVED.getCode().equals(oldRefundOrder.getApprovalState())
                    || ApprovalStateEnum.IN_REVIEW.getCode().equals(oldRefundOrder.getApprovalState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "退款单编号【" + oldRefundOrder.getCode() + "】 状态已变更，不可以进行修改操作");
            }

            SpringUtil.copyPropertiesIgnoreNull(refundOrderDto, oldRefundOrder);
            if (!refundOrderService.doUpdatedHandler(oldRefundOrder).isSuccess()) {
                LOG.info("租赁单【编号：{}】退款修改接口异常", refundOrderDto.getBusinessCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "退款修改接口异常");
            }
            //删除退款费用项的数据
            RefundFeeItem refundFeeItemCondtion = new RefundFeeItem();
            refundFeeItemCondtion.setRefundOrderId(refundOrderDto.getId());
            refundFeeItemService.deleteByExample(refundFeeItemCondtion);

        }

        leaseOrderItem.setExitTime(refundOrderDto.getExitTime());
        leaseOrderItem.setRefundState(LeaseRefundStateEnum.REFUNDING.getCode());
        if (assetsLeaseOrderItemService.update(leaseOrderItem) == 0) {
            LOG.info("摊位租赁订单项退款申请异常 更新乐观锁生效 【租赁单项ID {}】", leaseOrderItem.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //退款费用项设置
        refundOrderDto.getRefundFeeItems().forEach(o -> {
            o.setRefundOrderId(refundOrderDto.getId());
            o.setRefundOrderCode(refundOrderDto.getCode());
            refundFeeItemService.insertSelective(o);
        });

        return BaseOutput.success();
    }

    @Override
    @Transactional
    public BaseOutput cancelRefundOrderHandler(Long leaseOrderItemId) {
        //订单项退款申请
        AssetsLeaseOrderItem leaseOrderItem = assetsLeaseOrderItemService.get(leaseOrderItemId);
        AssetsLeaseOrder leaseOrder = get(leaseOrderItem.getLeaseOrderId());
        if (!LeaseRefundStateEnum.REFUNDING.getCode().equals(leaseOrderItem.getRefundState())) {
            LOG.info("租赁单【编号：{}】退款状态已发生变更，不能取消退款", leaseOrderItem.getLeaseOrderCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "退款状态已发生变更，不能取消退款");
        }

        //级联检查其他订单项状态，如果全部为已退款，则需联动更新订单状态为已退款
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrderItem.getLeaseOrderId());
        condition.setRefundState(LeaseRefundStateEnum.REFUNDED.getCode());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(condition);
        if (CollectionUtils.isNotEmpty(leaseOrderItems)) {
            leaseOrder.setRefundState(LeaseRefundStateEnum.PARTIAL_REFUND.getCode());
        } else {
            leaseOrder.setRefundState(LeaseRefundStateEnum.WAIT_APPLY.getCode());
        }
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单订单项取消退款 主单退款状态同步 乐观锁生效 【订单编号 {}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        leaseOrderItem.setRefundState(LeaseRefundStateEnum.WAIT_APPLY.getCode());
        if (assetsLeaseOrderItemService.updateSelective(leaseOrderItem) == 0) {
            LOG.info("摊位租赁单订单项取消退款申请异常 更新租赁单订单项乐观锁生效 【摊位租赁订单项ID {}】", leaseOrderItem.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }


        return BaseOutput.success();
    }

    @Override
    @Transactional
    public BaseOutput settleSuccessRefundOrderHandler(RefundOrder refundOrder) {
        AssetsLeaseOrder leaseOrder = get(refundOrder.getBusinessId());
        //订单项退款申请
        AssetsLeaseOrderItem leaseOrderItem = assetsLeaseOrderItemService.get(refundOrder.getBusinessItemId());
        if (LeaseRefundStateEnum.REFUNDED.getCode().equals(leaseOrderItem.getRefundState())) {
            LOG.info("此单已退款【leaseOrderItemId={}】", refundOrder.getBusinessItemId());
            return BaseOutput.success();
        }
        leaseOrderItem.setRefundState(LeaseRefundStateEnum.REFUNDED.getCode());
        if (assetsLeaseOrderItemService.updateSelective(leaseOrderItem) == 0) {
            LOG.info("摊位租赁单订单项退款申请结算退款成功 更新租赁单订单项乐观锁生效 【租赁单订单项ID {}】", leaseOrderItem.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //级联检查其他订单项状态，如果全部为已退款，则需联动更新订单状态为已退款
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(condition);
        Set<Integer> refundStates = leaseOrderItems.stream().filter(o -> !o.getId().equals(leaseOrderItem.getId())).map(o -> o.getRefundState()).collect(Collectors.toSet());
        if (refundStates.contains(LeaseRefundStateEnum.REFUNDING.getCode())) {
            leaseOrder.setRefundState(LeaseRefundStateEnum.REFUNDING.getCode());
        } else {
            if (CollectionUtils.isEmpty(refundStates) || (refundStates.size() == 1 && refundStates.contains(LeaseRefundStateEnum.REFUNDED.getCode()))) {
                leaseOrder.setRefundState(LeaseRefundStateEnum.REFUNDED.getCode());
            } else {
                leaseOrder.setRefundState(LeaseRefundStateEnum.PARTIAL_REFUND.getCode());
            }
        }
        leaseOrder.setWaitAmount(leaseOrder.getWaitAmount() - leaseOrderItem.getWaitAmount());
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单订单项退款申请结算退款成功 级联更新租赁单乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //释放关联保证金，让其单飞
        BaseOutput depositOutput = depositOrderService.batchReleaseRelated(leaseOrder.getBizType(), leaseOrder.getId(),leaseOrderItem.getAssetsId());
        if (!depositOutput.isSuccess()) {
            LOG.info("释放关联保证金，让其单飞接口异常 【租赁单编号:{},资产编号:{}】", leaseOrder.getCode(),leaseOrderItem.getAssetsName());
            throw new BusinessException(ResultCode.DATA_ERROR, depositOutput.getMessage());
        }
        //记录退款日志
        msgService.sendBusinessLog(recordRefundLog(refundOrder, leaseOrder));
        return BaseOutput.success();
    }

    @Override
    public BaseOutput supplement(AssetsLeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        checkContractNo(leaseOrder.getId(), leaseOrder.getContractNo(), false);
        AssetsLeaseOrder oldLeaseOrder = get(leaseOrder.getId());
        leaseOrder.setVersion(oldLeaseOrder.getVersion());
        if (updateSelective(leaseOrder) == 0) {
            return BaseOutput.failure("多人操作，请稍后重试");
        }

        LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE,BizTypeEnum.getBizTypeEnum(oldLeaseOrder.getBizType()).getEnName());
        LoggerUtil.buildLoggerContext(oldLeaseOrder.getId(), oldLeaseOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success();
    }

    @Override
    public BaseOutput<List<QueryFeeOutput>> batchQueryFeeWithoutShortcut(List<QueryFeeInput> queryFeeInputList) {
        return chargeRuleRpc.batchQueryFeeWithoutShortcut(queryFeeInputList);
    }

    /**
     * 提交审批任务
     *
     * @param taskId
     * @param agree
     */
    private void completeTask(String taskId, String agree) {
        HashMap hashMap = new HashMap();
        hashMap.put("agree", agree);
        TaskCompleteDto taskCompleteDto = DTOUtils.newInstance(TaskCompleteDto.class);
        taskCompleteDto.setTaskId(taskId);
        taskCompleteDto.setVariables(hashMap);
        //非最后一次审批，只更新流程状态
        BaseOutput baseOutput = taskRpc.complete(taskCompleteDto);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException(ResultCode.APP_ERROR, baseOutput.getMessage());
        }
    }

    /**
     * 冗余结算编号到缴费单
     *
     * @param paymentId
     * @param settlementCode
     */
    private void saveSettlementCode(Long paymentId, String settlementCode) {
        PaymentOrder paymentOrderPo = paymentOrderService.get(paymentId);
        paymentOrderPo.setSettlementCode(settlementCode);
        paymentOrderService.updateSelective(paymentOrderPo);
    }

    /**
     * 提交到付款
     *
     * @param leaseOrder
     * @param amount
     * @param businessChargeItems
     * @return
     */
    private Long submitPay(AssetsLeaseOrder leaseOrder, Long amount , List<BusinessChargeItem> businessChargeItems) {
        //新增缴费单
        PaymentOrder paymentOrder = buildPaymentOrder(leaseOrder);
        paymentOrder.setAmount(amount);
        paymentOrder.setWaitAmount(leaseOrder.getWaitAmount() - amount);
        paymentOrderService.insertSelective(paymentOrder);

        BaseOutput<SettleOrder> settlementOutput = settleOrderRpc.submit(buildSettleOrderDto(leaseOrder, paymentOrder, amount, buildSettleFeeItems(businessChargeItems)));
        if (settlementOutput.isSuccess()) {
            try {
                saveSettlementCode(paymentOrder.getId(), settlementOutput.getData().getCode());
            } catch (Exception e) {
                LOG.error("结算编号冗余异常 租赁单【编号：{}】缴费单【编号：{}】 异常信息{}", leaseOrder.getCode(), paymentOrder.getCode(), e.getMessage());
            }
        } else {
            LOG.info("提交付款调用结算异常【编号：{}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, settlementOutput.getMessage());
        }
        return paymentOrder.getId();
    }

    /**
     * 构建结算所需收费项信息
     * @param businessChargeItems
     * @return
     */
    private List<SettleFeeItem> buildSettleFeeItems (List<BusinessChargeItem> businessChargeItems) {
        List<SettleFeeItem> settleFeeItems = new ArrayList<>();
        businessChargeItems.forEach(bc -> {
            SettleFeeItem settleFeeItem = new SettleFeeItem();
            settleFeeItem.setChargeItemId(bc.getChargeItemId());
            settleFeeItem.setChargeItemName(bc.getChargeItemName());
            settleFeeItem.setAmount(bc.getPaymentAmount());
            settleFeeItems.add(settleFeeItem);
        });
        return settleFeeItems;
    }

    /**
     * 保存流程审批记录
     *
     * @param approvalParam
     */
    private void saveApprovalProcess(ApprovalParam approvalParam, UserTicket userTicket) {
        //构建流程审批记录
        ApprovalProcess approvalProcess = new ApprovalProcess();
        approvalProcess.setAssignee(userTicket.getId());
        approvalProcess.setAssigneeName(userTicket.getRealName());
        approvalProcess.setFirmId(userTicket.getFirmId());
        approvalProcess.setProcessInstanceId(approvalParam.getProcessInstanceId());
        approvalProcess.setBusinessKey(approvalParam.getBusinessKey());
        approvalProcess.setOpinion(approvalParam.getOpinion());
        approvalProcess.setTaskId(approvalParam.getTaskId());
        approvalProcess.setResult(approvalParam.getResult());
        //提交审批时没有任务id，直接保存
        if(approvalParam.getTaskId() == null){
            approvalProcessService.insertSelective(approvalProcess);
            return;
        }
        BaseOutput<TaskMapping> taskMappingBaseOutput = taskRpc.getById(approvalParam.getTaskId());
        if (!taskMappingBaseOutput.isSuccess()) {
            throw new AppException(taskMappingBaseOutput.getMessage());
        }
        approvalProcess.setTaskName(taskMappingBaseOutput.getData().getName());
        approvalProcess.setTaskTime(taskMappingBaseOutput.getData().getCreateTime());
        //每次审批通过，保存流程审批记录(目前考虑性能，没有保存流程名称)
        approvalProcessService.insertSelective(approvalProcess);
    }

    /**
     * 检查是否可以进行提交付款
     *
     * @param id
     * @param amount
     * @param leaseOrder
     */
    private void checkSubmitPayment(Long id, Long amount, AssetsLeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        if (PayStateEnum.PAID.getCode().equals(leaseOrder.getPayState())) {
            LOG.info("租赁单编号【{}】 已交清，不可以进行提交付款操作", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + leaseOrder.getCode() + "】 已交清，不可以进行提交付款操作");
        }
        if (LeaseRefundStateEnum.REFUNDED.getCode().equals(leaseOrder.getRefundState()) || LeaseRefundStateEnum.REFUNDING.getCode().equals(leaseOrder.getRefundState())) {
            LOG.info("租赁单编号【{}】退款中或已退款，不可以进行提交付款操作", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + leaseOrder.getCode() + "】退款中或已退款，不可以进行提交付款操作");
        }
        if (LeaseOrderStateEnum.CANCELD.getCode().equals(leaseOrder.getState())) {
            LOG.info("租赁单编号【{}】已取消，不可以进行提交付款操作", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + leaseOrder.getCode() + "】 已取消，不可以进行提交付款操作");
        }
        if (amount > leaseOrder.getWaitAmount()) {
            LOG.info("摊位租赁单【ID {}】 支付金额【{}】大于待付金额【{}】", id, amount, leaseOrder.getWaitAmount());
            throw new BusinessException(ResultCode.DATA_ERROR, "支付金额大于待付金额");
        }
    }

    /**
     * 构建缴费单数据
     *
     * @param leaseOrder
     * @return
     */
    private PaymentOrder buildPaymentOrder(AssetsLeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "未登录");
        }
        PaymentOrder paymentOrder = new PaymentOrder();
        BaseOutput<String> bizNumberOutput = uidFeignRpc.getBizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName() + "_" + BizNumberTypeEnum.PAYMENT_ORDER.getCode());
        if (!bizNumberOutput.isSuccess()) {
            LOG.info("租赁单【编号：{}】,缴费单编号生成异常", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
        }
        paymentOrder.setCode(bizNumberOutput.getData());
        paymentOrder.setBusinessCode(leaseOrder.getCode());
        paymentOrder.setBusinessId(leaseOrder.getId());
        paymentOrder.setMarketId(userTicket.getFirmId());
        paymentOrder.setMarketCode(userTicket.getFirmCode());
        paymentOrder.setCreatorId(userTicket.getId());
        paymentOrder.setCreator(userTicket.getRealName());
        paymentOrder.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        paymentOrder.setIsSettle(YesOrNoEnum.NO.getCode());
        paymentOrder.setBizType(leaseOrder.getBizType());
        paymentOrder.setCustomerId(leaseOrder.getCustomerId());
        paymentOrder.setCustomerName(leaseOrder.getCustomerName());
        return paymentOrder;
    }

    /**
     * 摊位订单项退款申请条件检查
     *
     * @param refundOrderDto
     * @param leaseOrderItem
     */
    private void checkRufundApplyWithLeaseOrderItem(LeaseRefundOrderDto refundOrderDto, AssetsLeaseOrderItem leaseOrderItem, UserTicket userTicket) {
        //收款人和转抵扣收款人客户状态验证
        checkCustomerState(refundOrderDto.getPayeeId(), userTicket.getFirmId());

        if (LeaseRefundStateEnum.REFUNDED.getCode().equals(leaseOrderItem.getRefundState())
                || (null == refundOrderDto.getId() && LeaseRefundStateEnum.REFUNDING.getCode().equals(leaseOrderItem.getRefundState()))) {
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位项状态已发生变更，不能发起退款申请");
        }

        BusinessChargeItem chargeItemCondition = new BusinessChargeItem();
        chargeItemCondition.setBusinessId(leaseOrderItem.getId());
        Map<Long, Long> businessChargeItemMap = businessChargeItemService.list(chargeItemCondition).stream().collect(Collectors.toMap(BusinessChargeItem::getChargeItemId, BusinessChargeItem::getPaidAmount));
        Map<Long, Long> refundFeeItemMap = refundOrderDto.getRefundFeeItems().stream().collect(Collectors.toMap(RefundFeeItem::getChargeItemId, RefundFeeItem::getAmount));
        for (Long chargeItemId : refundFeeItemMap.keySet()) {
            if (refundFeeItemMap.get(chargeItemId) > businessChargeItemMap.get(chargeItemId)) {
                throw new BusinessException(ResultCode.DATA_ERROR, "存在收费项退款额大于可退款额");
            }
        }
    }

    /**
     * 构造结算单数据
     *
     * @param leaseOrder
     * @param paymentOrder
     * @param submitTotalAmount 提交总金额
     * @param settleFeeItems 结算收费项
     * @return
     */
    private SettleOrderDto buildSettleOrderDto(AssetsLeaseOrder leaseOrder, PaymentOrder paymentOrder, Long submitTotalAmount, List<SettleFeeItem> settleFeeItems) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "未登录");
        }
        SettleOrderDto settleOrder = new SettleOrderDto();
        settleOrder.setAppId(settlementAppId);
        settleOrder.setBusinessCode(leaseOrder.getCode());
        settleOrder.setBusinessDepId(leaseOrder.getDepartmentId());
        settleOrder.setBusinessDepName(leaseOrder.getDepartmentName());
        settleOrder.setBusinessType(leaseOrder.getBizType());
        settleOrder.setCustomerId(leaseOrder.getCustomerId());
        settleOrder.setCustomerName(leaseOrder.getCustomerName());
        settleOrder.setCustomerPhone(leaseOrder.getCustomerCellphone());
        settleOrder.setCustomerCertificate(leaseOrder.getCertificateNumber());
        settleOrder.setMarketId(userTicket.getFirmId());
        settleOrder.setMarketCode(userTicket.getFirmCode());
        settleOrder.setSettleOrderLinkList(buildSettleOrderLinks(paymentOrder));
        settleOrder.setSubmitterDepId(userTicket.getDepartmentId());
        settleOrder.setSubmitterDepName(null == userTicket.getDepartmentId() ? null : departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        settleOrder.setSubmitterId(userTicket.getId());
        settleOrder.setSubmitterName(userTicket.getRealName());
        settleOrder.setSubmitTime(LocalDateTime.now());
        settleOrder.setType(SettleTypeEnum.PAY.getCode());
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrder.setMchId(leaseOrder.getMchId());
        settleOrder.setDeductEnable(EnableEnum.YES.getCode());

        settleOrder.setAmount(submitTotalAmount);
        settleOrder.setOrderCode(paymentOrder.getCode());//订单号
        settleOrder.setBusinessCode(paymentOrder.getBusinessCode());//业务单号
        settleOrder.setSettleFeeItemList(settleFeeItems);
        return settleOrder;
    }

    /**
     * 构建结算链接
     * @return
     */
    private List<SettleOrderLink> buildSettleOrderLinks(PaymentOrder paymentOrder) {
        List<SettleOrderLink> settleOrderLinks = new ArrayList<>();
        //回调
        SettleOrderLink callBackLink = new SettleOrderLink();
        callBackLink.setType(LinkTypeEnum.CALLBACK.getCode());
        callBackLink.setUrl(settlerHandlerUrl);
        settleOrderLinks.add(callBackLink);

        //详情
        SettleOrderLink detailLink = new SettleOrderLink();
        detailLink.setType(LinkTypeEnum.DETAIL.getCode());
        detailLink.setUrl(viewUrl + "?orderCode=" + paymentOrder.getCode());
        settleOrderLinks.add(detailLink);

        //打印
        SettleOrderLink printLink = new SettleOrderLink();
        printLink.setType(LinkTypeEnum.PRINT.getCode());
        printLink.setUrl(printUrl + "?orderCode=" + paymentOrder.getCode());
        settleOrderLinks.add(printLink);
        return settleOrderLinks;
    }

    /**
     * 撤回缴费单 判断缴费单是否需要撤回 需要撤回则撤回
     * 如果撤回时发现缴费单状态为及时同步结算状态 则抛出异常 提示用户带结算同步后再操作
     *
     * @param paymentId
     */
    private void withdrawPaymentOrder(Long paymentId) {
        PaymentOrder payingOrder = paymentOrderService.get(paymentId);
        if (PaymentOrderStateEnum.NOT_PAID.getCode().equals(payingOrder.getState())) {
            String paymentCode = payingOrder.getCode();
            BaseOutput output = settleOrderRpc.cancel(settlementAppId, paymentCode);
            if (!output.isSuccess()) {
                LOG.info("结算单撤回异常 【缴费单CODE {}】", paymentCode);
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
            }

            payingOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
            if (paymentOrderService.updateSelective(payingOrder) == 0) {
                LOG.info("撤回缴费单异常，乐观锁生效，【缴费单编号:{}】", payingOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
            }
        }
    }

    /**
     * 保证金单保存
     *
     * @param dto
     */
    private void saveOrUpdateDepositOrders(AssetsLeaseOrderListDto dto) {
        List<DepositOrder> depositOrders = new ArrayList<>();
        dto.getLeaseOrderItems().stream().filter(o->o.getDepositMakeUpAmount() > 0).forEach(l -> {
            DepositOrder depositOrder = new DepositOrder();
            depositOrder.setCustomerId(dto.getCustomerId());
            depositOrder.setCustomerName(dto.getCustomerName());
            depositOrder.setCertificateNumber(dto.getCertificateNumber());
            depositOrder.setCustomerCellphone(dto.getCustomerCellphone());
            depositOrder.setDepartmentId(dto.getDepartmentId());
            depositOrder.setDepartmentName(dto.getDepartmentName());
            DepositTypeEnum dtEnum = DepositTypeEnum.getAssetsTypeEnum(AssetsTypeEnum.getAssetsTypeEnum(dto.getAssetsType()).getTypeCode());
            depositOrder.setTypeCode(dtEnum.getTypeCode());
            depositOrder.setTypeName(dtEnum.getTypeName());
            depositOrder.setAssetsType(dto.getAssetsType());
            depositOrder.setAssetsId(l.getAssetsId());
            depositOrder.setAssetsName(l.getAssetsName());
            depositOrder.setAmount(l.getDepositMakeUpAmount());
            depositOrder.setIsRelated(YesOrNoEnum.YES.getCode());
            depositOrder.setBusinessId(dto.getId());
            depositOrder.setBizType(dto.getBizType());
            depositOrder.setFirstDistrictId(l.getFirstDistrictId());
            depositOrder.setSecondDistrictId(l.getSecondDistrictId());
            depositOrders.add(depositOrder);
        });
        depositOrderService.batchAddOrUpdateDepositOrder(dto.getBizType(), dto.getId(), depositOrders);
    }

    /**
     * 删除订单项
     *
     * @param leaseOrderId
     */
    private void deleteLeaseOrderItems(Long leaseOrderId) {
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrderId);
        List<AssetsLeaseOrderItem> assetsLeaseOrderItems = assetsLeaseOrderItemService.listByExample(condition);
        assetsLeaseOrderItemService.deleteByExample(condition);
        assetsLeaseOrderItems.forEach(o -> {
            BusinessChargeItem bciCondition = new BusinessChargeItem();
            bciCondition.setBusinessId(o.getId());
            bciCondition.setBizType(o.getBizType());
            businessChargeItemService.deleteByExample(bciCondition);
        });

    }


    /**
     * 批量插入租赁单项
     *
     * @param dto
     */
    private void insertLeaseOrderItems(AssetsLeaseOrderListDto dto) {
        dto.getLeaseOrderItems().forEach(o -> {
            o.setLeaseOrderId(dto.getId());
            o.setLeaseOrderCode(dto.getCode());
            o.setCustomerId(dto.getCustomerId());
            o.setCustomerName(dto.getCustomerName());
            o.setState(LeaseOrderStateEnum.CREATED.getCode());
            o.setPayState(PayStateEnum.NOT_PAID.getCode());
            o.setStopRentState(StopRentStateEnum.NO_APPLY.getCode());
            //收费项新增
            if (CollectionUtils.isNotEmpty(o.getBusinessChargeItems())) {
                Long totalAmount = o.getBusinessChargeItems().stream().mapToLong(BusinessChargeItem::getAmount).sum();
                o.setTotalAmount(totalAmount);
                o.setWaitAmount(totalAmount);
            }
            assetsLeaseOrderItemService.insertSelective(o);

            //收费项新增
            if (CollectionUtils.isNotEmpty(o.getBusinessChargeItems())) {
                o.getBusinessChargeItems().stream().forEach(bci -> {
                    bci.setBusinessId(o.getId());
                    bci.setWaitAmount(bci.getAmount());
                    bci.setBizType(dto.getBizType());
                    businessChargeItemService.insertSelective(bci);
                });
            }
        });
    }

    /**
     * 线下合同号验重
     *
     * @param leaseOrderId 待修改的租赁单Id
     * @param contractNo
     * @param isAdd
     */
    private void checkContractNo(Long leaseOrderId, String contractNo, Boolean isAdd) {
        if (StringUtils.isNotBlank(contractNo)) {
            AssetsLeaseOrder condition = new AssetsLeaseOrder();
            condition.setContractNo(contractNo);
            List<AssetsLeaseOrder> leaseOrders = list(condition);
            if (isAdd && CollectionUtils.isNotEmpty(leaseOrders)) {
                throw new BusinessException(ResultCode.DATA_ERROR, "线下合同号不允许重复使用，请修改");
            } else {
                if (leaseOrders.size() == 1) {
                    AssetsLeaseOrder leaseOrder = leaseOrders.get(0);
                    if (!leaseOrder.getId().equals(leaseOrderId)) {
                        throw new BusinessException(ResultCode.DATA_ERROR, "线下合同号不允许重复使用，请修改");
                    }
                }
            }
        }
    }

    /**
     * 级联更新摊位租赁订单状态 订单项状态级联发生变化
     *
     * @param leaseOrder
     * @param isCascade  false不级联更新订单项 true 级联更新订单项
     * @param stateEnum  isCascade为false时，此处可以传null
     */
    private void cascadeUpdateLeaseOrderState(AssetsLeaseOrder leaseOrder, boolean isCascade, LeaseOrderItemStateEnum stateEnum) {
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
        }

        if (isCascade) {
            AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
            condition.setLeaseOrderId(leaseOrder.getId());
            List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.listByExample(condition);
            leaseOrderItems.stream().forEach(o -> o.setState(stateEnum.getCode()));
            if (assetsLeaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                LOG.info("级联更新摊位租赁订单状态失败,乐观锁生效 【租赁单编号:{},变更目标状态:{}】", leaseOrder.getCode(), stateEnum.getName());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
            }
        }
    }

    /**
     * 级联更新摊位租赁订单状态 订单项状态级联发生变化
     *
     * @param leaseOrder
     * @param leaseOrderItems
     * @param isCascade       false不级联更新订单项 true 级联更新订单项
     * @param stateEnum       isCascade为false时，此处可以传null
     */
    private void cascadeUpdateLeaseOrderState(AssetsLeaseOrder leaseOrder, List<AssetsLeaseOrderItem> leaseOrderItems, boolean isCascade, LeaseOrderItemStateEnum stateEnum) {
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
        }

        if (isCascade) {
            leaseOrderItems.stream().forEach(o -> o.setState(stateEnum.getCode()));
            if (assetsLeaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                LOG.info("级联更新摊位租赁订单状态失败,乐观锁生效 【租赁单编号:{},变更目标状态:{}】", leaseOrder.getCode(), stateEnum.getName());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
            }
        }
    }

    /**
     * 记录交费日志
     *
     * @param settleOrder
     * @param leaseOrder
     */
    private BusinessLog recordPayLog(SettleOrder settleOrder, AssetsLeaseOrder leaseOrder) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.setBusinessId(leaseOrder.getId());
        businessLog.setBusinessCode(leaseOrder.getCode());
        businessLog.setContent(settleOrder.getCode());
        businessLog.setOperationType("pay");
        businessLog.setMarketId(settleOrder.getMarketId());
        businessLog.setOperatorId(settleOrder.getOperatorId());
        businessLog.setOperatorName(settleOrder.getOperatorName());
        businessLog.setBusinessType(BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
        businessLog.setSystemCode("IA");
        return businessLog;
    }

    /**
     * 记录退款日志
     *
     * @param refundOrder
     * @param leaseOrder
     */
    private BusinessLog recordRefundLog(RefundOrder refundOrder, AssetsLeaseOrder leaseOrder) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.setBusinessId(leaseOrder.getId());
        businessLog.setBusinessCode(leaseOrder.getCode());
        businessLog.setContent(refundOrder.getSettlementCode());
        businessLog.setOperationType("refund");
        businessLog.setMarketId(refundOrder.getMarketId());
        businessLog.setOperatorId(refundOrder.getRefundOperatorId());
        businessLog.setOperatorName(refundOrder.getRefundOperator());
        businessLog.setBusinessType(BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getEnName());
        businessLog.setSystemCode("IA");
        return businessLog;
    }

    /**
     * 构建分摊明细
     * @param o
     * @param bci
     * @return
     */
    private ApportionRecord buildApportionRecord(AssetsLeaseOrderItem o, BusinessChargeItem bci) {
        ApportionRecord apportionRecord = new ApportionRecord();
        apportionRecord.setLeaseOrderId(o.getLeaseOrderId());
        apportionRecord.setLeaseOrderItemId(o.getId());
        apportionRecord.setAmount(bci.getPaymentAmount());
        apportionRecord.setChargeItemId(bci.getChargeItemId());
        apportionRecord.setChargeItemName(bci.getChargeItemName());
        apportionRecord.setCreateTime(LocalDateTime.now());
        return apportionRecord;
    }

    /**
     * 撤回正在分摊中收费项金额
     * @param leaseOrderItems
     */
    private void withdrawBusinessChargeItemPaymentAmount(List<AssetsLeaseOrderItem> leaseOrderItems) {
        BusinessChargeItemListDto businessChargeItemCondition = new BusinessChargeItemListDto();
        businessChargeItemCondition.setBusinessIds(leaseOrderItems.stream().filter(o -> PayStateEnum.NOT_PAID.getCode().equals(o.getPayState())).map(o -> o.getId()).collect(Collectors.toList()));
        businessChargeItemCondition.setBizType(leaseOrderItems.get(0).getBizType());
        List<BusinessChargeItem> businessChargeItems = businessChargeItemService.listByExample(businessChargeItemCondition)
                .stream().filter(bci -> bci.getPaymentAmount() > 0).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(businessChargeItems)) {
            businessChargeItems.forEach(bci -> {
                bci.setPaymentAmount(0L);
            });
            if (businessChargeItemService.batchUpdateSelective(businessChargeItems) != businessChargeItems.size()) {
                LOG.info("批量撤回正在分摊中的金额 【订单CODE{}】", leaseOrderItems.get(0).getLeaseOrderCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
            }
        }
    }


    /**
     * 作废前检查
     * @param leaseOrder
     * @param paymentOrders
     */
    private void checkInvalid(AssetsLeaseOrder leaseOrder, List<PaymentOrder> paymentOrders) {
        //检查是否已经作废
        if (LeaseOrderStateEnum.INVALIDATED.getCode().equals(leaseOrder.getState())) {
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.info("租赁单【编号：{}】状态为【{}】，不可以进行作废操作", leaseOrder.getCode(), stateName);
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单状态为【" + stateName + "】，不可以进行作废操作");
        }
        //检查是否交过费
        if (CollectionUtils.isEmpty(paymentOrders.stream().filter(o -> PaymentOrderStateEnum.PAID.getCode().equals(o.getState())).collect(Collectors.toList()))) {
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.info("租赁单【编号：{}】没有缴过费，不可以进行作废操作", leaseOrder.getCode(), stateName);
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单没有缴过费，不可以进行作废操作");
        }

        //检查是否已退款或正在退款中
        if (!LeaseRefundStateEnum.WAIT_APPLY.getCode().equals(leaseOrder.getRefundState())) {
            LOG.info("租赁单【编号：{}】已退款或正在退款，不可以进行作废操作", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单已退款或正在退款，不可以进行作废操作");
        }
    }

    @Override
	public ContractDto getPrintData(Long id) {
    	UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
             throw new BusinessException(ResultCode.CSRF_ERROR, "登录已过期!");
        }
		ContractDto contractDto = new ContractDto();
		AssetsLeaseOrder leaseOrder = this.get(id);
		// 合同市场获取,客户地址获取
		BaseOutput<CustomerExtendDto> output = customerRpc.get(leaseOrder.getCustomerId(), leaseOrder.getMarketId());
        if (!output.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户接口调用异常 " + output.getMessage());
        }
        CustomerExtendDto customer = output.getData();
		// 获取基本数据
		contractDto.setPartya(firmRpc.getById(leaseOrder.getMarketId()).getData().getName());
		contractDto.setPartyb(leaseOrder.getCustomerName());
		contractDto.setAddress(customer.getCertificateAddr());
		contractDto.setCertificateNo(leaseOrder.getCertificateNumber());
		contractDto.setPhone(leaseOrder.getCustomerCellphone());
		contractDto.setAssetsType(AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getName());

		contractDto.setsTime(leaseOrder.getStartTime());
		contractDto.seteTime(leaseOrder.getEndTime());
		contractDto.setDays(String.valueOf(leaseOrder.getDays()));

		AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
		condition.setLeaseOrderId(leaseOrder.getId());
		List<Map<String, String>> items = new ArrayList<>();
		List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(condition);
		BigDecimal big = new BigDecimal(0);
		StringBuilder unitName = new StringBuilder();
		leaseOrderItems.stream().forEach(it -> {
			big.add(it.getNumber());
			StringBuilder str = new StringBuilder();
			str.append(String.format("【%s】【%s】【%s】", it.getFirstDistrictName(), it.getSecondDistrictName(),
					it.getAssetsName())).append(" ").append(it.getNumber()).append(it.getUnitName());
			Map<String, String> map = new HashMap<>();
			map.put("position", str.toString());
			items.add(map);
			unitName.append(it.getUnitName());
		});
		// 面积
		contractDto.setArea(big.toString()+unitName.toString());
		contractDto.setItems(items);
		List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemMeta(
				leaseOrder.getBizType(), leaseOrderItems.stream().map(o -> o.getId()).collect(Collectors.toList()));
		List<AssetsLeaseOrderItemListDto> itmesAssetsLeaseOrderItemListDtos = assetsLeaseOrderItemService
				.leaseOrderItemListToDto(leaseOrderItems, leaseOrder.getBizType(), chargeItemDtos);
		List<Map<String, String>> feeItems = new ArrayList<>();
		itmesAssetsLeaseOrderItemListDtos.stream().forEach(it -> {
			StringBuilder str = new StringBuilder();
			str.append(String.format("【%s】【%s】【%s】:", it.getFirstDistrictName(), it.getSecondDistrictName(),
					it.getAssetsName()));
			chargeItemDtos.stream().forEach(it1 -> {
				str.append(it1.getChargeItem()).append(it.getBusinessChargeItem().get("chargeItemYuan" + it1.getId()))
						.append(";");
				;
			});
			Map<String, String> map = new HashMap<>();
			map.put("feeList", str.toString());
			feeItems.add(map);
		});
		contractDto.setAmount(MoneyUtils.centToYuan(leaseOrder.getTotalAmount()));
		contractDto.setAmountCn(Convert.digitToChinese(new BigDecimal(MoneyUtils.centToYuan(leaseOrder.getTotalAmount()))));
		contractDto.setFeeItems(feeItems);
		return contractDto;
	}

}