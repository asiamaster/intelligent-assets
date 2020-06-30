package com.dili.ia.service.impl;

import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.Customer;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.RefundOrderPrintDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.EarnestOrderStateEnum;
import com.dili.ia.glossary.RefundOrderStateEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.util.BeanMapUtil;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
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
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;
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
    @Autowired
    SettlementRpc settlementRpc;
    @Autowired
    DepartmentRpc departmentRpc;
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    RefundOrderService refundOrderService;

    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${refundOrder.settlement.handler.url}")
    private String settlerHandlerUrl;

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
    @BusinessLogger(businessType = LogBizTypeConst.REFUND_ORDER,operationType="add",systemCode = "INTELLIGENT_ASSETS")
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
        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput doSubmitDispatcher(RefundOrder refundOrder) {
        if (!refundOrder.getState().equals(RefundOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        //检查客户状态
        checkCustomerState(refundOrder.getCustomerId(), userTicket.getFirmId());
        //检查收款人客户状态
        checkCustomerState(refundOrder.getPayeeId(), userTicket.getFirmId());
        refundOrder.setState(RefundOrderStateEnum.SUBMITTED.getCode());
        refundOrder.setSubmitTime(new Date());
        refundOrder.setSubmitterId(userTicket.getId());
        refundOrder.setSubmitter(userTicket.getRealName());
        if (refundOrderService.updateSelective(refundOrder) == 0){
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作退款单，请重试！");
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
            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在，请核实和修改后再保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已禁用，请核实和修改后再保存");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已删除，请核实和修改后再保存");
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
//        settleOrder.setBusinessType(ro.getBizType()); // 业务类型
        settleOrder.setAppId(settlementAppId);//应用ID
        settleOrder.setType(SettleTypeEnum.REFUND.getCode());// "结算类型  -- 退款
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrder.setEditEnable(EditEnableEnum.NO.getCode());
        settleOrder.setReturnUrl(settlerHandlerUrl); // 结算-- 缴费成功后回调路径

        return settleOrder;
    }


    @Transactional(rollbackFor = Exception.class)
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
        RefundOrder condition = DTOUtils.newInstance(RefundOrder.class);
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

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint) {
        try {
            RefundOrder refundOrderCondition = DTOUtils.newDTO(RefundOrder.class);
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
            LOG.info("获取打印数据异常！{}", e.getErrorMsg());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("获取打印数据异常！{}", e1.getMessage());
            return BaseOutput.failure("获取打印数据异常");
        }
    }

    private RefundOrderPrintDto buildCommonPrintDate(RefundOrder refundOrder, Integer reprint){
        RefundOrderPrintDto roPrintDto = new RefundOrderPrintDto();
        roPrintDto.setPrintTime(new Date());
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
