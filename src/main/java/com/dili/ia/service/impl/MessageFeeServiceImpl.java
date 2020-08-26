package com.dili.ia.service.impl;

import com.dili.ia.domain.MessageFee;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.domain.dto.MessageFeeDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.MessageFeeStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.mapper.MessageFeeMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.MessageFeeService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.ia.util.ResultCodeConst;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import io.seata.spring.annotation.GlobalTransactional;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-08-24 16:16:50.
 */
@Service
public class MessageFeeServiceImpl extends BaseServiceImpl<MessageFee, Long> implements MessageFeeService {

	private final static Logger LOG = LoggerFactory.getLogger(MessageFeeServiceImpl.class);

	
	@Autowired
	private CustomerAccountService customerAccountService;
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@Autowired
	private SettlementRpcResolver settlementRpcResolver;
	
	@Autowired
	private DepartmentRpc departmentRpc;
	
	@Autowired 
	private UidRpcResolver uidRpcResolver;
	
	@Autowired
	private RefundOrderService refundOrderService;
	
	@Autowired
	private TransferDeductionItemService transferDeductionItemService;
	
	private String settlerHandlerUrl;
	
	private Long settlementAppId;
	
    public MessageFeeMapper getActualDao() {
        return (MessageFeeMapper)getDao();
    }
    
    private MessageFee getMessageFeeByCode(String code) {
    	MessageFee condition = new MessageFee();
		condition.setCode(code);
		List<MessageFee> messageFees = this.list(condition);
		if(CollectionUtil.isNotEmpty(messageFees)) {
			return messageFees.get(0);
		}
		throw new BusinessException(ResultCode.DATA_ERROR, "马甲单不存在!");
	}
    
    private void update(MessageFee domain,String code,Integer version,MessageFeeStateEnum state) {
		domain.setVersion(version+1);
		domain.setState(state.getCode());
		MessageFee condition = new MessageFee();
		condition.setCode(code);
		condition.setVersion(version);
		int row = updateSelectiveByExample(domain, condition);
		if(row != 1) {
			throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
		}
	}

	@Override
	@Transactional
	public void create(MessageFeeDto messageFeeDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = new MessageFee(userTicket);
		BeanUtil.copyProperties(messageFeeDto, messageFee);
		messageFee.setCreatorId(userTicket.getId());
		messageFee.setCreatorName(userTicket.getRealName());
		messageFee.setMarketCode(userTicket.getFirmCode());
		messageFee.setMarketId(userTicket.getFirmId());
		messageFee.setCreateTime(LocalDateTime.now());
		messageFee.setVersion(1);
		messageFee.setState(MessageFeeStateEnum.CREATED.getCode());
		// 转抵信息
		this.insertSelective(messageFee);
	}

	@Override
	@Transactional
	public void update(MessageFeeDto messageFeeDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = getMessageFeeByCode(messageFeeDto.getCode());
		if(messageFee.getState() != MessageFeeStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		messageFee.setOperatorId(userTicket.getId());
		messageFee.setOperatorName(userTicket.getRealName());
		BeanUtil.copyProperties(messageFeeDto, messageFee, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
		update(messageFee, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.CREATED);
	}

	@Override
	@GlobalTransactional
	public void submit(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = getMessageFeeByCode(code);
		if (messageFee.getState() != MessageFeeStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		MessageFee domain = new MessageFee(userTicket);
		domain.setSubmitorName(userTicket.getRealName());
		domain.setSubmitterId(userTicket.getId());
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.CREATED);

		// 冻结定金和转抵
		BaseOutput customerAccountOutput = customerAccountService.submitLeaseOrderCustomerAmountFrozen(
				messageFee.getId(), messageFee.getCode(), messageFee.getCustomerId(), 0L,
				messageFee.getTransactionAmount(), messageFee.getMarketId(), userTicket.getId(),
				userTicket.getRealName());
		if (!customerAccountOutput.isSuccess()) {
			LOG.info("冻结定金和转抵异常【编号：{}】", messageFee.getCode());
			if (ResultCodeConst.TRANSFER_ERROR.equals(customerAccountOutput.getCode())) {
				throw new BusinessException(ResultCode.DATA_ERROR, "客户转抵可用金额不足，请核实修改后重新保存");
			} else {
				throw new BusinessException(ResultCode.DATA_ERROR, customerAccountOutput.getMessage());
			}
		}

		// 提交结算单
		PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket, BizTypeEnum.LABOR_VEST);
		paymentOrder.setBusinessCode(code);
		paymentOrder.setAmount(messageFee.getAmount());
		paymentOrder.setBusinessId(messageFee.getId());
		paymentOrderService.insertSelective(paymentOrder);
		// 结算服务
		SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, messageFee,
				paymentOrder.getCode(), paymentOrder.getAmount(), BizTypeEnum.LABOR_VEST);
		settleOrderDto.setReturnUrl(settlerHandlerUrl);
		settlementRpcResolver.submit(settleOrderDto);

	}

	@Override
	@Transactional
	public void cancel(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = getMessageFeeByCode(code);
		if (messageFee.getState() != MessageFeeStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		MessageFee domain = new MessageFee(userTicket);
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.CANCELLED);
	}

	@Override
	@Transactional
	public void withdraw(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = getMessageFeeByCode(code);
		if (messageFee.getState() != MessageFeeStateEnum.SUBMITTED_PAY.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		MessageFee domain = new MessageFee(userTicket);
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.CREATED);

		 //解冻定金、转抵
        BaseOutput customerAccountOutput = customerAccountService.withdrawLeaseOrderCustomerAmountUnFrozen(
        		messageFee.getId(), messageFee.getCode(), messageFee.getCustomerId(),
        		0L, messageFee.getTransactionAmount(),
        		messageFee.getMarketId(), userTicket.getId(), userTicket.getRealName());
        if (!customerAccountOutput.isSuccess()) {
            LOG.info("租赁单撤回 解冻定金、转抵异常【编号：{},MSG:{}】", messageFee.getCode(), customerAccountOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, customerAccountOutput.getMessage());
        }
		
	}

	@Override
	public MessageFee view(String code) {
		return getMessageFeeByCode(code);
	}

	@Override
	@GlobalTransactional
	public void refund(RefundInfoDto refundInfoDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = getMessageFeeByCode(refundInfoDto.getBusinessCode());
		if (messageFee.getState() != MessageFeeStateEnum.IN_EFFECTIVE.getCode()
				|| messageFee.getState() != MessageFeeStateEnum.NOT_STARTED.getCode()
				|| messageFee.getState() != MessageFeeStateEnum.EXPIRED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		MessageFee domain = new MessageFee(userTicket);
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.SUBMITTED_REFUND);
		// 获取结算单
		SettleOrder order = settlementRpcResolver.get(settlementAppId, messageFee.getCode());
		RefundOrder refundOrder = buildRefundOrderDto(userTicket, refundInfoDto, messageFee, order);
		
		// 转抵信息
		if (CollectionUtils.isNotEmpty(refundInfoDto.getTransferDeductionItems())) {
			refundInfoDto.getTransferDeductionItems().forEach(o -> {
				o.setRefundOrderId(refundOrder.getId());
				transferDeductionItemService.insertSelective(o);
			});
		}
		
	}

	@Override
	@Transactional
	public void refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = getMessageFeeByCode(refundOrder.getBusinessCode());
		if (messageFee.getState() != MessageFeeStateEnum.SUBMITTED_REFUND.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		MessageFee domain = new MessageFee(userTicket);
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.REFUNDED);
		
		//转抵扣充值
        TransferDeductionItem transferDeductionItemCondition = new TransferDeductionItem();
        transferDeductionItemCondition.setRefundOrderId(refundOrder.getId());
        List<TransferDeductionItem> transferDeductionItems = transferDeductionItemService.list(transferDeductionItemCondition);
        if (CollectionUtils.isNotEmpty(transferDeductionItems)) {
            transferDeductionItems.forEach(o -> {
                BaseOutput accountOutput = customerAccountService.leaseOrderRechargTransfer(
                        refundOrder.getId(), refundOrder.getCode(), o.getPayeeId(), o.getPayeeAmount(),
                        refundOrder.getMarketId(), refundOrder.getRefundOperatorId(), refundOrder.getRefundOperator());
                if (!accountOutput.isSuccess()) {
                    LOG.info("退款单转抵异常，【退款编号:{},收款人:{},收款金额:{},msg:{}】", refundOrder.getCode(), o.getPayee(), o.getPayeeAmount(), accountOutput.getMessage());
                    throw new BusinessException(ResultCode.DATA_ERROR, accountOutput.getMessage());
                }
            });
        }
		
	}

	@Override
	@Transactional
	public void settlementDealHandler(SettleOrder settleOrder) {
		MessageFee messageFee = getMessageFeeByCode(settleOrder.getBusinessCode());
		if(messageFee.getState() != MessageFeeStateEnum.SUBMITTED_PAY.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		// 获取缴费单
		PaymentOrder paymentOrder = paymentOrderService.getByCode(messageFee.getPaymentOrderCode());
		paymentOrder.setState(PaymentOrderStateEnum.PAID.getCode());
		// 填充缴费单的结算单信息变更缴费单状态
		paymentOrder.setSettlementCode(settleOrder.getCode());
		paymentOrder.setSettlementOperator(settleOrder.getOperatorName());
		paymentOrder.setSettlementWay(settleOrder.getWay());
		paymentOrderService.updateSelective(paymentOrder);
		
		// 更新信息费单
        if (LocalDateTime.now().isAfter(messageFee.getStartDate())) {
            update(new MessageFee(), messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.IN_EFFECTIVE);
		} else {
	        update(new MessageFee(), messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.NOT_STARTED);
		}
        
		// 转抵扣除
		BaseOutput customerAccountOutput = customerAccountService.paySuccessLeaseOrderCustomerAmountConsume(messageFee.getId(), messageFee.getCode(), messageFee.getCustomerId(), 0L, messageFee.getTransactionAmount(), messageFee.getMarketId(), settleOrder.getOperatorId(), settleOrder.getOperatorName());
        if (!customerAccountOutput.isSuccess()) {
            LOG.info("结算成功，消费定金、转抵接口异常 【租赁单编号:{},定金:{},转抵:{}】", messageFee.getCode(), 0L, messageFee.getTransactionAmount());
            throw new BusinessException(ResultCode.DATA_ERROR, customerAccountOutput.getMessage());
        }
        
        // TODO 通知消息系统
	}
	
	private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, MessageFee messageFee,String orderCode,Long amount,BizTypeEnum bizTypeEnum) {
		SettleOrderInfoDto settleOrderInfoDto = 
				new SettleOrderInfoDto(userTicket, bizTypeEnum,SettleTypeEnum.PAY,SettleStateEnum.WAIT_DEAL);
		settleOrderInfoDto.setBusinessCode(messageFee.getCode());
		settleOrderInfoDto.setOrderCode(orderCode);
		settleOrderInfoDto.setAmount(amount);
		settleOrderInfoDto.setBusinessDepId(messageFee.getDepartmentId());
		settleOrderInfoDto.setBusinessDepName(messageFee.getDepartmentName());
		settleOrderInfoDto.setCustomerId(messageFee.getCustomerId());
		settleOrderInfoDto.setCustomerName(messageFee.getCustomerName());
		settleOrderInfoDto.setCustomerPhone(messageFee.getCustomerCellphone());
		if (userTicket.getDepartmentId() != null){
            settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
		return settleOrderInfoDto;
	}
	
	private RefundOrder buildRefundOrderDto(UserTicket userTicket, RefundInfoDto refundInfoDto, MessageFee messageFee,SettleOrder order) {
		//退款单
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setBusinessCode(messageFee.getCode());
		refundOrder.setBusinessId(messageFee.getId());
		refundOrder.setCustomerId(messageFee.getCustomerId());
		refundOrder.setCustomerName(messageFee.getCustomerName());
		refundOrder.setCustomerCellphone(messageFee.getCustomerCellphone());
		refundOrder.setCertificateNumber(refundInfoDto.getPayeeCertificateNumber());
		refundOrder.setTotalRefundAmount(refundInfoDto.getTotalRefundAmount());
		refundOrder.setPayeeAmount(refundInfoDto.getPayeeAmount());
		refundOrder.setRefundReason(refundInfoDto.getNotes());
		refundOrder.setBizType(BizTypeEnum.LABOR_VEST.getCode());
		refundOrder.setPayeeId(refundInfoDto.getPayeeId());
		refundOrder.setPayee(refundInfoDto.getPayee());
		refundOrder.setRefundType(refundInfoDto.getRefundType());
		refundOrder.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode()+"_"+BizTypeEnum.LABOR_VEST.getEnName()
				+"_"+BizNumberTypeEnum.REFUND_ORDER.getCode()));
		if (!refundOrderService.doAddHandler(refundOrder).isSuccess()) {
			LOG.info("入库单【编号：{}】退款申请接口异常", refundOrder.getBusinessCode());
			throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常");
		}
		return refundOrder;
	}

	@Override
	public void cancleRefund(RefundOrder refundOrder) {
		MessageFee messageFee = getMessageFeeByCode(refundOrder.getBusinessCode());
		if (messageFee.getState() != MessageFeeStateEnum.SUBMITTED_REFUND.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		MessageFee domain = new MessageFee();
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.REFUNDED);
		// 更新信息费单
		if(LocalDateTime.now().isAfter(messageFee.getEndDate())){
            update(new MessageFee(), messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.EXPIRED);
		} else if (LocalDateTime.now().isAfter(messageFee.getStartDate())) {
            update(new MessageFee(), messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.IN_EFFECTIVE);
		} else {
	        update(new MessageFee(), messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.NOT_STARTED);
		}
	}
}