package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.bpmc.sdk.domain.ProcessInstanceMapping;
import com.dili.bpmc.sdk.domain.TaskMapping;
import com.dili.bpmc.sdk.rpc.RuntimeRpc;
import com.dili.bpmc.sdk.rpc.TaskRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.ApprovalProcess;
import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.Customer;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.ApprovalParam;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.RefundOrderPrintDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.AssetsLeaseOrderItemMapper;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.ApprovalProcessService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.util.BeanMapUtil;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EditEnableEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.redis.UserResourceRedis;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-12 18:45
 */
@Service
public class RefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderService {

    private final static Logger LOG = LoggerFactory.getLogger(RefundOrderServiceImpl.class);
    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }

    @SuppressWarnings("all")
    @Autowired
    private AssetsLeaseOrderItemMapper assetsLeaseOrderItemMapper;
    @Autowired
    private SettlementRpc settlementRpc;
    @SuppressWarnings("all")
    @Autowired
    private DepartmentRpc departmentRpc;
    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private RefundOrderService refundOrderService;
    @SuppressWarnings("all")
    @Autowired
    private RuntimeRpc runtimeRpc;
    @SuppressWarnings("all")
    @Autowired
    private TaskRpc taskRpc;
    @Autowired
    private AssetsRpc assetsRpc;
    @Autowired
    private ApprovalProcessService approvalProcessService;
    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${refundOrder.settlement.handler.url}")
    private String settlerHandlerUrl;
    @Autowired
    private UserResourceRedis userResourceRedis;
    @Autowired @Lazy
    private List<RefundOrderDispatcherService> refundBizTypes;
    private Map<String,RefundOrderDispatcherService> refundBiz = new HashMap<>();
    @PostConstruct
    public void init() {
        for(RefundOrderDispatcherService service : refundBizTypes) {
            for(String bizType : service.getBizType()) {
                this.refundBiz.put(bizType, service);
            }

        }
    }


    @Override
    @BusinessLogger(businessType = LogBizTypeConst.REFUND_ORDER,operationType="add",systemCode = "IA")
    public BaseOutput doAddHandler(RefundOrder order) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        BaseOutput checkResult = checkParams(order);
        if (!checkResult.isSuccess()){
            return checkResult;
        }
        order.setCreator(userTicket.getRealName());
        order.setCreatorId(userTicket.getId());
        order.setMarketId(userTicket.getFirmId());
        order.setMarketCode(userTicket.getFirmCode());
        order.setState(RefundOrderStateEnum.CREATED.getCode());
        order.setApprovalState(ApprovalStateEnum.WAIT_SUBMIT_APPROVAL.getCode());
        order.setVersion(0);
        refundOrderService.insertSelective(order);
        LoggerUtil.buildLoggerContext(order.getId(),order.getCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),null);
        return BaseOutput.success().setData(order);
    }
    private BaseOutput checkParams(RefundOrder order){
        if (null == order.getBusinessId()){//定金退款不是针对业务单，所以订单ID记录的是【客户账户ID】
            return BaseOutput.failure("退款单orderId不能为空！");
        }
        if (null == order.getCustomerId()){
            return BaseOutput.failure("客户ID不能为空！");
        }
        if (null == order.getCustomerName()){
            return BaseOutput.failure("客户名称不能为空！");
        }
        if (null == order.getCertificateNumber()){
            return BaseOutput.failure("客户证件号码不能为空！");
        }
        if (null == order.getCustomerCellphone()){
            return BaseOutput.failure("客户联系电话不能为空！");
        }
        if (null == order.getPayeeAmount()){
            return BaseOutput.failure("退款单金额不能为空！");
        }
        if (null == order.getTotalRefundAmount()){
            return BaseOutput.failure("退款单总金额不能为空！");
        }
        if (null == order.getBizType()){
            return BaseOutput.failure("退款单业务类型不能为空！");
        }

        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput doCancelDispatcher(RefundOrder refundOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null){
            return BaseOutput.failure("未登录！");
        }
        if (!refundOrder.getState().equals(RefundOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("取消失败，退款单状态已变更！");
        }
        refundOrder.setCancelerId(userTicket.getId());
        refundOrder.setCanceler(userTicket.getRealName());
        refundOrder.setState(EarnestOrderStateEnum.CANCELD.getCode());
        refundOrderService.updateSelective(refundOrder);
        //获取业务service,调用业务实现
        RefundOrderDispatcherService service = refundBiz.get(refundOrder.getBizType());
        if(service!=null){
            BaseOutput refundResult = service.cancelHandler(refundOrder);
            if (!refundResult.isSuccess()){
                LOG.info("提交回调业务返回失败！" + refundResult.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "提交回调业务返回失败！" + refundResult.getMessage());
            }
        }
        if(StringUtils.isNotBlank(refundOrder.getProcessInstanceId())) {
            //发送消息通知流程终止
            BaseOutput<String> baseOutput = taskRpc.messageEventReceived("terminate", refundOrder.getProcessInstanceId(), null);
            if (!baseOutput.isSuccess()) {
                throw new BusinessException(ResultCode.DATA_ERROR, "流程消息发送失败");
            }
        }
        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public BaseOutput doSubmitDispatcher(RefundOrder refundOrder) {
        if (!refundOrder.getState().equals(RefundOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        //记录当前审批状态，用于判断是否直接提交，而需要清空流程
        Integer approvalState = refundOrder.getApprovalState();
        //检查客户状态
        checkCustomerState(refundOrder.getCustomerId(), userTicket.getFirmId());
        //检查收款人客户状态
        checkCustomerState(refundOrder.getPayeeId(), userTicket.getFirmId());

        //提交时验证该租赁退款单业务的用户是否有跳过审批的权限
        if (refundOrder.getBizType().equals(BizTypeEnum.BOOTH_LEASE.getCode()) && !userResourceRedis.checkUserResourceRight(userTicket.getId(), "skipRefundApproval") && !ApprovalStateEnum.APPROVED.getCode().equals(refundOrder.getApprovalState())) {
            LOG.info("退款单编号【{}】 未审批，不可以进行提交操作", refundOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "退款单编号【" + refundOrder.getCode() + "】 未审批，不可以进行提交操作");
        }
        refundOrder.setState(RefundOrderStateEnum.SUBMITTED.getCode());
        refundOrder.setSubmitTime(LocalDateTime.now());
        refundOrder.setSubmitterId(userTicket.getId());
        refundOrder.setSubmitter(userTicket.getRealName());
        //提交审批后，默认为审批通过
        refundOrder.setApprovalState(ApprovalStateEnum.APPROVED.getCode());
        if (refundOrderService.updateSelective(refundOrder) == 0){
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作退款单，请重试！");
        }

        //如果有流程实例id，需要判断当前是否审批通过，非审批通过状态的直接提交需要清空流程
        if(StringUtils.isNotBlank(refundOrder.getProcessInstanceId()) && !ApprovalStateEnum.APPROVED.getCode().equals(approvalState)){
            RefundOrder refundOrder1 = new RefundOrder();
            RefundOrder dbRefundOrder = get(refundOrder.getId());
            refundOrder1.setId(refundOrder.getId());
            refundOrder1.setVersion(dbRefundOrder.getVersion());
            Map<String, Object> setForceParams = new HashMap<>();
            setForceParams.put("process_instance_id", null);
            setForceParams.put("process_definition_id", null);
            refundOrder1.setSetForceParams(setForceParams);
            if(0 == updateExact(refundOrder1)){
                LOG.info("退款单提交状态更新失败 乐观锁生效 【退款单ID {}】", refundOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
            }
            //清空流程
            runtimeRpc.stopProcessInstanceById(refundOrder.getProcessInstanceId(), "直接提交审批");
        }

        //获取业务service,调用业务实现
        RefundOrderDispatcherService service = refundBiz.get(refundOrder.getBizType());
        if(service!=null){
            BaseOutput refundResult = service.submitHandler(refundOrder);
            if (refundOrder != null && !refundResult.isSuccess()){
                LOG.info("提交回调业务返回失败！" + refundResult.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "提交回调业务返回失败！" + refundResult.getMessage());
            }
        }
        //提交到结算中心 --- 执行顺序不可调整！！因为异常只能回滚自己系统，无法回滚其它远程系统
        BaseOutput<SettleOrder> out= settlementRpc.submit(buildSettleOrderDto(userTicket, refundOrder));
        if (!out.isSuccess()){
            LOG.info("提交到结算中心失败！" + out.getMessage() + out.getErrorData());
            throw new BusinessException(ResultCode.DATA_ERROR, "提交到结算中心失败！" + out.getMessage());
        }
        return BaseOutput.success("提交成功");
    }

    /**
     * 检查客户状态
     * @param customerId
     * @param marketId
     */
    private void checkCustomerState(Long customerId,Long marketId){
        BaseOutput<Customer> output = customerRpc.get(customerId,marketId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户接口调用异常 "+output.getMessage());
        }
        Customer customer = output.getData();
        if(null == customer){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在，请核实！");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已禁用，请核实！");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已删除，请核实！");
        }
    }

    //组装 -- 结算中心缴费单 SettleOrder
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, RefundOrder ro){
        SettleOrderDto settleOrder = new SettleOrderDto();
        //以下是提交到结算中心的必填字段
        settleOrder.setMarketId(ro.getMarketId()); //市场ID
        settleOrder.setMarketCode(userTicket.getFirmCode());
        settleOrder.setOrderCode(ro.getCode());
        settleOrder.setBusinessCode(ro.getCode()); //业务单号
        //收款人信息
        settleOrder.setCustomerId(ro.getPayeeId());//客户ID
        Customer customer = customerRpc.get(ro.getPayeeId(), userTicket.getFirmId()).getData();
        settleOrder.setCustomerPhone(customer.getContactsPhone());//"客户手机号
        settleOrder.setCustomerName(customer.getName());// "客户姓名
        settleOrder.setAmount(ro.getPayeeAmount()); //金额
        settleOrder.setAccountNumber(ro.getBankCardNo());
        settleOrder.setBankName(ro.getBank());
        settleOrder.setBankCardHolder(ro.getPayee());
        settleOrder.setWay(ro.getRefundType());
        settleOrder.setTradeCardNo(ro.getTradeCardNo());

        settleOrder.setBusinessDepId(ro.getDepartmentId()); //"业务部门ID
        settleOrder.setBusinessDepName(ro.getDepartmentName());//"业务部门名称
        settleOrder.setSubmitterId(userTicket.getId());// "提交人ID
        settleOrder.setSubmitterName(userTicket.getRealName());// "提交人姓名
        if (userTicket.getDepartmentId() != null){
            settleOrder.setSubmitterDepId(userTicket.getDepartmentId()); //"提交人部门ID
            settleOrder.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
        settleOrder.setSubmitTime(LocalDateTime.now());
        //@TODO 结算单需要调整类型，为String
        settleOrder.setBusinessType(Integer.valueOf(ro.getBizType())); // 业务类型
        settleOrder.setAppId(settlementAppId);//应用ID
        settleOrder.setType(SettleTypeEnum.REFUND.getCode());// "结算类型  -- 退款
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrder.setEditEnable(EditEnableEnum.NO.getCode());
        settleOrder.setReturnUrl(settlerHandlerUrl); // 结算-- 缴费成功后回调路径

        return settleOrder;
    }


    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public BaseOutput doWithdrawDispatcher(RefundOrder refundOrder) {
        if (!refundOrder.getState().equals(RefundOrderStateEnum.SUBMITTED.getCode())){
            return BaseOutput.failure("撤回失败，状态已变更！请刷新");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        refundOrder.setState(RefundOrderStateEnum.CREATED.getCode());
        refundOrder.setWithdrawOperator(userTicket.getRealName());
        refundOrder.setWithdrawOperatorId(userTicket.getId());
        //摊位租赁业务撤回需要改审核状态为【待审核】
        if(refundOrder.getBizType().equals(BizTypeEnum.BOOTH_LEASE.getCode())){
            refundOrder.setApprovalState(ApprovalStateEnum.WAIT_SUBMIT_APPROVAL.getCode());
        }
        if (refundOrderService.updateSelective(refundOrder) == 0){
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作退款单，请重试！");
        }

        //获取业务service,调用业务实现
        RefundOrderDispatcherService service = refundBiz.get(refundOrder.getBizType());
        if(service!=null){
            BaseOutput refundResult = service.withdrawHandler(refundOrder);
            if (!refundResult.isSuccess()){
                LOG.info("撤回回调业务返回失败！" + refundResult.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "撤回回调业务返回失败！" + refundResult.getMessage());
            }
        }

        //提交到结算中心 --- 执行顺序不可调整！！因为异常只能回滚自己系统，无法回滚其它远程系统
        BaseOutput<String> out= settlementRpc.cancel(settlementAppId, refundOrder.getCode());
        if (!out.isSuccess()){
            LOG.info("撤回调用结算中心失败！" + out.getMessage() + out.getErrorData());
            throw new BusinessException(ResultCode.DATA_ERROR, "撤回调用结算中心失败！" + out.getMessage());
        }

        return BaseOutput.success("撤回成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public BaseOutput<RefundOrder> doRefundSuccessHandler(SettleOrder settleOrder) {
        RefundOrder condition = new RefundOrder();
        //结算单code唯一
        condition.setCode(settleOrder.getOrderCode());
        RefundOrder refundOrder = this.listByExample(condition).stream().findFirst().orElse(null);
        if (RefundOrderStateEnum.REFUNDED.getCode().equals(refundOrder.getState())) { //如果已退款，直接返回
            return BaseOutput.success().setData(refundOrder);
        }
        if (!refundOrder.getState().equals(RefundOrderStateEnum.SUBMITTED.getCode())){
            LOG.info("退款单状态已变更！状态为：" + RefundOrderStateEnum.getRefundOrderStateEnum(refundOrder.getState()).getName() );
            return BaseOutput.failure("退款单状态已变更！");
        }
        refundOrder.setState(RefundOrderStateEnum.REFUNDED.getCode());
        refundOrder.setRefundTime(DateUtils.localDateTimeToUdate(settleOrder.getOperateTime()));
        refundOrder.setSettlementCode(settleOrder.getCode());
        refundOrder.setRefundOperatorId(settleOrder.getOperatorId());
        refundOrder.setRefundOperator(settleOrder.getOperatorName());
        refundOrder.setRefundType(settleOrder.getWay());
        if (refundOrderService.updateSelective(refundOrder) == 0) {
            LOG.info("退款成功后--回调更新退款单状态记录数为0，多人操作，请重试！");
            throw new BusinessException(ResultCode.DATA_ERROR, "退款单多人操作，请重试！");
        }

        //获取业务service,调用业务实现
        RefundOrderDispatcherService service = refundBiz.get(refundOrder.getBizType());
        if(service!=null){
            BaseOutput refundResult = service.refundSuccessHandler(settleOrder, refundOrder);
            if (!refundResult.isSuccess()){
                LOG.info("退款成功后--回调业务返回失败！" + refundResult.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "退款成功回调业务返回失败！" + refundResult.getMessage());
            }
        }

        return BaseOutput.success("退款成功！").setData(refundOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<RefundOrder> doUpdatedHandler(RefundOrder refundOrder) {
        if (null == refundOrder || null == refundOrder.getId()){
            return BaseOutput.failure("退款单修改，必要参数（ID）不能为空");
        }
        RefundOrder oldOrder = this.get(refundOrder.getId());
        if (!oldOrder.getState().equals(RefundOrderStateEnum.CREATED.getCode())){
            LOG.info("修改失败，退款单状态已变更！状态为：" + RefundOrderStateEnum.getRefundOrderStateEnum(refundOrder.getState()).getName() );
            return BaseOutput.failure("退款单状态已变更！");
        }
        //检查客户状态
        checkCustomerState(refundOrder.getPayeeId(), oldOrder.getMarketId());
        refundOrder.setVersion(oldOrder.getVersion());
        //全部修改，为空字段会修改为空
        if (refundOrderService.update(refundOrder) == 0) {
            LOG.info("退款单修改--更新退款单状态记录数为0，多人操作，请重试！");
            throw new BusinessException(ResultCode.DATA_ERROR, "退款单多人操作，请重试！");
        }
        return BaseOutput.success("退款成功！").setData(refundOrder);
    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint) {
        try {
            RefundOrder refundOrderCondition = new RefundOrder();
            refundOrderCondition.setCode(orderCode);
            RefundOrder refundOrder = refundOrderService.list(refundOrderCondition).stream().findFirst().orElse(null);
            if (null == refundOrder){
                return BaseOutput.failure("没有获取到结算单【" + orderCode + "】");
            }
            if (!RefundOrderStateEnum.REFUNDED.getCode().equals(refundOrder.getState())) {
                return BaseOutput.failure("此单未退款");
            }

            RefundOrderPrintDto refundOrderPrintDto = buildCommonPrintDate(refundOrder, reprint);
            Map<String,Object> resultMap = BeanMapUtil.beanToMap(refundOrderPrintDto);
            //获取业务service,调用业务实现 ---- 业务专有的数据组装
            RefundOrderDispatcherService service=refundBiz.get(refundOrder.getBizType());
            if(service!=null){
                BaseOutput<Map<String,Object>> result = service.buildBusinessPrintData(refundOrder);
                if (!result.isSuccess()){
                    LOG.info("获取打印数据异常！获取组装业务数据异常！{}", result.getMessage());
                    return BaseOutput.failure("获取打印数据异常！获取组装业务数据异常！{}" + result.getMessage());
                }
                resultMap.putAll(result.getData());
            }

            PrintDataDto<Map<String,Object>> printDataDto = new PrintDataDto<>();
            printDataDto.setItem(resultMap);
            printDataDto.setName(resultMap.get("printTemplateCode").toString());
            return BaseOutput.success().setData(printDataDto);
        } catch (BusinessException e) {
            LOG.info("获取打印数据异常！{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("获取打印数据异常！{}", e1.getMessage());
            return BaseOutput.failure("获取打印数据异常");
        }
    }

    @Override
    public void submitForApproval(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new NotLoginException();
        }
        RefundOrder refundOrder = get(id);
        if (!refundOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())
                && (!ApprovalStateEnum.WAIT_SUBMIT_APPROVAL.getCode().equals(refundOrder.getApprovalState())
                || !ApprovalStateEnum.APPROVAL_DENIED.getCode().equals(refundOrder.getApprovalState()))) {
            throw new BusinessException(ResultCode.DATA_ERROR, "状态已流转不能提交审批，请刷新后再试");
        }
        //检查客户状态
        checkCustomerState(refundOrder.getCustomerId(), userTicket.getFirmId());
        //检查收款人客户状态
        checkCustomerState(refundOrder.getPayeeId(), userTicket.getFirmId());
        //获取业务service, 校验转抵人
        RefundOrderDispatcherService service = refundBiz.get(refundOrder.getBizType());
        if(service!=null){
            BaseOutput refundResult = service.submitHandler(refundOrder);
            if (refundOrder != null && !refundResult.isSuccess()){
                LOG.info("提交回调业务返回失败！" + refundResult.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "提交回调业务返回失败！" + refundResult.getMessage());
            }
        }
        if(StringUtils.isNotBlank(refundOrder.getProcessInstanceId())) {
            //发送消息通知流程
            BaseOutput<String> baseOutput = taskRpc.signal(refundOrder.getProcessInstanceId(), "reapply", null);
            if (!baseOutput.isSuccess()) {
                throw new BusinessException(ResultCode.DATA_ERROR, "流程消息发送失败");
            }
        }else {
            //根据第一个摊位的所属区域来确认审批人
//        AssetsLeaseOrderItem assetsLeaseOrderItem = assetsLeaseOrderItemMapper.selectByPrimaryKey(refundOrder.getBusinessItemId());
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("districtId", assetsLeaseOrderItem.getDistrictId().toString());
            /**
             * 启动租赁审批流程
             */
            Map<String, Object> variables = new HashMap<>(4);
            variables.put("businessKey", refundOrder.getCode());
            variables.put("firmId", userTicket.getFirmId().toString());
            BaseOutput<ProcessInstanceMapping> processInstanceMappingBaseOutput = runtimeRpc.startProcessInstanceByKey(BpmConstants.PK_REFUND_APPROVAL_PROCESS, refundOrder.getCode(), userTicket.getId().toString(), variables);
            if (!processInstanceMappingBaseOutput.isSuccess()) {
                throw new BusinessException(ResultCode.APP_ERROR, "流程启动失败，请联系管理员");
            }
            //设置流程定义和实例id，后面会更新到租赁单表
            refundOrder.setProcessDefinitionId(processInstanceMappingBaseOutput.getData().getProcessDefinitionId());
            refundOrder.setProcessInstanceId(processInstanceMappingBaseOutput.getData().getProcessInstanceId());
        }
        refundOrder.setApprovalState(ApprovalStateEnum.IN_REVIEW.getCode());
        if (updateSelective(refundOrder) == 0) {
            LOG.info("退款单提交状态更新失败 乐观锁生效 【退款单ID {}】", refundOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
        }
        //保存提交审批记录
        ApprovalParam approvalParam = DTOUtils.newInstance(ApprovalParam.class);
        approvalParam.setBusinessKey(refundOrder.getCode());
        approvalParam.setProcessInstanceId(refundOrder.getProcessInstanceId());
        saveApprovalProcess(approvalParam, userTicket);
        //写业务日志
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, refundOrder.getCode());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, refundOrder.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
        LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
    }

    @Override
    public void approvedHandler(ApprovalParam approvalParam) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new NotLoginException();
        }
        RefundOrder condition = new RefundOrder();
        condition.setCode(approvalParam.getBusinessKey());
        RefundOrder refundOrder = getActualDao().selectOne(condition);
        //只有创建状态的退款单才能提交审批任务
        if (!refundOrder.getState().equals(RefundOrderStateEnum.CREATED.getCode())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "退款单状态不正确，请刷新后再试");
        }
        //最后一次审批，更新审批状态、租赁单状态，并且全量提交退款单到结算
        //总经理审批通过需要更新审批状态
        if ("generalManagerApproval".equals(approvalParam.getTaskDefinitionKey())) {
            refundOrder.setApprovalState(ApprovalStateEnum.APPROVED.getCode());
            //提交退款单
            BaseOutput baseOutput = doSubmitDispatcher(refundOrder);
            if(!baseOutput.isSuccess()){
                throw new BusinessException(baseOutput.getCode(), baseOutput.getMessage());
            }
        }
        //保存流程审批记录
        saveApprovalProcess(approvalParam, userTicket);
        //摊位的区域id，用于获取一级区域名称，在流程中进行判断
        Long districtId = assetsLeaseOrderItemMapper.selectByPrimaryKey(refundOrder.getBusinessItemId()).getDistrictId();
        //写业务日志
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, approvalParam.getBusinessKey());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, refundOrder.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
        LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
        LoggerContext.put("logContent", approvalParam.getOpinion());
        //提交审批任务
        completeTask(approvalParam.getTaskId(), "true", getLevel1DistrictName(districtId));
    }

    @Override
    public void approvedDeniedHandler (ApprovalParam approvalParam) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new NotLoginException();
        }
        RefundOrder condition = new RefundOrder();
        condition.setCode(approvalParam.getBusinessKey());
        RefundOrder refundOrder = getActualDao().selectOne(condition);
        //只有创建状态的退款单才能提交审批任务
        if (!refundOrder.getState().equals(RefundOrderStateEnum.CREATED.getCode())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "退款单状态不正确，请刷新后再试");
        }
        //更新退款单审批状态为拒绝
        refundOrder.setApprovalState(ApprovalStateEnum.APPROVAL_DENIED.getCode());
        if (updateSelective(refundOrder) == 0) {
            LOG.info("退款单提交状态更新失败 乐观锁生效 【退款ID {}】", refundOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试");
        }
        //保存流程审批记录
        saveApprovalProcess(approvalParam, userTicket);
        //摊位的区域id，用于获取一级区域名称，在流程中进行判断
        Long districtId = assetsLeaseOrderItemMapper.selectByPrimaryKey(refundOrder.getBusinessItemId()).getDistrictId();
        //写业务日志
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, approvalParam.getBusinessKey());
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, refundOrder.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
        LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
        LoggerContext.put("logContent", approvalParam.getOpinion());
        //提交审批任务
        completeTask(approvalParam.getTaskId(), "false", getLevel1DistrictName(districtId));
    }

    /**
     * 提交审批任务
     *
     * @param taskId
     * @param agree
     * @param districtName 街区名称, 一区或二区。 用于流程判断
     */
    private void completeTask(String taskId, String agree, String districtName) {
        HashMap hashMap = new HashMap();
        hashMap.put("agree", agree);
        if(StringUtils.isNotEmpty(districtName)){
            hashMap.put("districtName", districtName);
        }
        //非最后一次审批，只更新流程状态
        BaseOutput baseOutput = taskRpc.complete(taskId, hashMap);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException(ResultCode.APP_ERROR, baseOutput.getMessage());
        }
    }

    /**
     * 获取一级区域名称,用于流程判断
     * @return
     */
    public String getLevel1DistrictName(Long districtId){
        BaseOutput<DistrictDTO> districtOutput = assetsRpc.getDistrictById(districtId);
        if(!districtOutput.isSuccess()){
            throw new AppException(ResultCode.DATA_ERROR, districtOutput.getMessage());
        }
        //构建一级区域名称，用于流程流转
        String districtName = null;
        if(districtOutput.getData().getParentId() == 0L || "0".equals(districtOutput.getData().getParentId())){
            return districtOutput.getData().getName();
        }else{
            BaseOutput<DistrictDTO> parentDistrictOutput = assetsRpc.getDistrictById(districtOutput.getData().getParentId());
            if(!parentDistrictOutput.isSuccess()){
                throw new AppException(ResultCode.DATA_ERROR, parentDistrictOutput.getMessage());
            }
            return parentDistrictOutput.getData().getName();
        }
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
            throw new AppException("获取任务信息失败");
        }
        approvalProcess.setTaskName(taskMappingBaseOutput.getData().getName());
        approvalProcess.setTaskTime(taskMappingBaseOutput.getData().getCreateTime());
        //每次审批通过，保存流程审批记录(目前考虑性能，没有保存流程名称)
        approvalProcessService.insertSelective(approvalProcess);
    }

    private RefundOrderPrintDto buildCommonPrintDate(RefundOrder refundOrder, Integer reprint){
        RefundOrderPrintDto roPrintDto = new RefundOrderPrintDto();
        roPrintDto.setPrintTime(DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
        roPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        roPrintDto.setCode(refundOrder.getCode());

        roPrintDto.setCustomerName(refundOrder.getCustomerName());
        roPrintDto.setCustomerCellphone(refundOrder.getCustomerCellphone());
        roPrintDto.setRefundReason(refundOrder.getRefundReason());
        roPrintDto.setAmount(MoneyUtils.centToYuan(refundOrder.getTotalRefundAmount()));
        roPrintDto.setSettlementOperator(refundOrder.getRefundOperator());
        roPrintDto.setSubmitter(refundOrder.getSubmitter());
        roPrintDto.setBusinessType(BizTypeEnum.getBizTypeEnum(refundOrder.getBizType()).getName());
        roPrintDto.setPayee(refundOrder.getPayee());
        roPrintDto.setPayeeAmount(MoneyUtils.centToYuan(refundOrder.getPayeeAmount()));
        roPrintDto.setBank(refundOrder.getBank());
        roPrintDto.setBankCardNo(refundOrder.getBankCardNo());
        roPrintDto.setRefundType(SettleWayEnum.getNameByCode(refundOrder.getRefundType()));
        return roPrintDto;
    }

    public Map<String, RefundOrderDispatcherService> getRefundBiz() {
        return refundBiz;
    }

    public void setRefundBiz(Map<String, RefundOrderDispatcherService> refundBiz) {
        this.refundBiz = refundBiz;
    }
}
