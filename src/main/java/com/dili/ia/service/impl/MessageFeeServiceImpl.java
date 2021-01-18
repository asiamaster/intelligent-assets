package com.dili.ia.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.assets.sdk.dto.TypeMarketDto;
import com.dili.assets.sdk.rpc.TypeMarketRpc;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.domain.MessageFee;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundFeeItem;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.MessageFeeDto;
import com.dili.ia.domain.dto.MessageFeeQuery;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.domain.dto.printDto.MessageFeePayPrintDto;
import com.dili.ia.domain.dto.printDto.MessageFeeRefundPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.MessageFeeStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.MessageFeeMapper;
import com.dili.ia.rpc.MessageFeeRpc;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.BusinessLogService;
import com.dili.ia.service.MessageFeeService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundFeeItemService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.util.LoggerUtil;
import com.dili.ia.util.SettleOrderLinkUtils;
import com.dili.logger.sdk.component.MsgService;
import com.dili.rule.sdk.domain.input.QueryFeeInput;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EnableEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-08-24 16:16:50.
 */
@Service
public class MessageFeeServiceImpl extends BaseServiceImpl<MessageFee, Long> implements MessageFeeService,BusinessLogService {

	private final static Logger LOG = LoggerFactory.getLogger(MessageFeeServiceImpl.class);
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@Autowired
	private SettlementRpcResolver settlementRpcResolver;

	@SuppressWarnings("all")
	@Autowired
	private DepartmentRpc departmentRpc;
	
	@Autowired 
	private UidRpcResolver uidRpcResolver;
	
	@Autowired
	private RefundOrderService refundOrderService;
	
	@Autowired
	private ChargeRuleRpc chargeRuleRpc;
	
	@Autowired
	private TypeMarketRpc typeMarketRpc;
	
	@Autowired
	private BusinessChargeItemService businessChargeItemService;
	
	@Autowired
	private MessageFeeRpc messageFeeRpc;
	
	@Autowired
	private RefundFeeItemService refundFeeItemService;
	
	@Autowired
	private MsgService msgService;
	
	@Value("${settlement.app-id}")
	private Long settlementAppId;
	@Value("${messageFee.settlement.handler.url}")
	private String settlerHandlerUrl;
	@Value("${messageFee.settlement.view.url}")
	private String settleViewUrl;
	@Value("${messageFee.settlement.print.url}")
	private String settlerPrintUrl;
	
    public MessageFeeMapper getActualDao() {
        return (MessageFeeMapper)getDao();
    }
    
    @Override
    public MessageFee getMessageFeeByCode(String code) {
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
    
    private List<BusinessChargeItem> buildBusinessCharge(List<BusinessChargeItem> businessChargeItems,Long businessId,String businessCode){
		businessChargeItems.stream().forEach(item -> {
			item.setBizType(BizTypeEnum.MESSAGEFEE.getCode());
			item.setBusinessId(businessId);
			item.setBusinessCode(businessCode);
			item.setPaidAmount(0L);
			item.setWaitAmount(item.getAmount());
			item.setPaymentAmount(item.getAmount());
			item.setCreateTime(LocalDateTime.now());
			item.setVersion(0);
		});
		return businessChargeItems;
	}

	@Override
	@Transactional
	public void create(MessageFeeDto messageFeeDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = new MessageFee(userTicket);
		BeanUtil.copyProperties(messageFeeDto, messageFee);
		messageFee
				.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.MESSAGEFEE.getEnName()));
		messageFee.setCreatorId(userTicket.getId());
		messageFee.setCreatorName(userTicket.getRealName());
		messageFee.setMarketCode(userTicket.getFirmCode());
		messageFee.setMarketId(userTicket.getFirmId());
		messageFee.setCreateTime(LocalDateTime.now());
		messageFee.setVersion(1);
		messageFee.setState(MessageFeeStateEnum.CREATED.getCode());
		// 商户ID需要从基础信息中获取
		BaseOutput<List<TypeMarketDto>> baseOutput = typeMarketRpc.queryAll();
		if (baseOutput.isSuccess() && baseOutput.getData() != null) {
			for (TypeMarketDto typeMarketDto : baseOutput.getData()) {
				if (BizNumberTypeEnum.LABOR_VEST.getCode().equals(typeMarketDto.getType())) {
					messageFee.setMchId(typeMarketDto.getMarketId());
				}
			}
		}
		this.insertSelective(messageFee);
		// 动态收费项
		List<BusinessChargeItem> businessChargeItems = buildBusinessCharge(messageFeeDto.getBusinessChargeItems(),
				messageFee.getId(), messageFee.getCode());
		if (CollectionUtil.isNotEmpty(businessChargeItems)) {
			businessChargeItemService.batchInsert(businessChargeItems);
		}
		LoggerUtil.buildLoggerContext(messageFee.getId(), messageFee.getCode(), userTicket.getId(),
				userTicket.getRealName(), userTicket.getFirmId(), "新增信息费单");
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
		List<BusinessChargeItem> businessChargeItems = messageFeeDto.getBusinessChargeItems();
		businessChargeItemService.batchUpdateSelective(businessChargeItems);
		LoggerUtil.buildLoggerContext(messageFee.getId(), messageFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "修改信息费单");
	}

	@Override
	@GlobalTransactional
	public void submit(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = getMessageFeeByCode(code);
		if (messageFee.getState() != MessageFeeStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}


		// 提交结算单
		PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket, BizTypeEnum.MESSAGEFEE);
		paymentOrder.setBusinessCode(code);
		// 需要支付的金额 总金额-转抵
		paymentOrder.setAmount(messageFee.getAmount());
		paymentOrder.setBusinessId(messageFee.getId());
		paymentOrderService.insertSelective(paymentOrder);
		// 结算服务
		SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, messageFee,
				paymentOrder.getCode(), paymentOrder.getAmount(), BizTypeEnum.MESSAGEFEE);

		settlementRpcResolver.submit(settleOrderDto);
		
		MessageFee domain = new MessageFee(userTicket);
		domain.setSubmitorName(userTicket.getRealName());
		domain.setSubmitterId(userTicket.getId());
		domain.setPaymentOrderCode(paymentOrder.getCode());
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.SUBMITTED_PAY);
		LoggerUtil.buildLoggerContext(messageFee.getId(), messageFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "提交信息费单");

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
		LoggerUtil.buildLoggerContext(messageFee.getId(), messageFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "取消信息费单");

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

		LoggerUtil.buildLoggerContext(messageFee.getId(), messageFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "撤回信息费单");

	}

	@Override
	public MessageFeeDto view(String code) {
		MessageFee messageFee = getMessageFeeByCode(code);
		MessageFeeDto messageFeeDto = new MessageFeeDto();
		BeanUtil.copyProperties(messageFee, messageFeeDto);
		BusinessChargeItem condtion = new BusinessChargeItem();
		condtion.setBusinessCode(messageFee.getCode());
		messageFeeDto.setBusinessChargeItems(businessChargeItemService.list(condtion));
		return messageFeeDto;
	}

	@Override
	@GlobalTransactional
	public void refund(RefundInfoDto refundInfoDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		MessageFee messageFee = getMessageFeeByCode(refundInfoDto.getCode());
		if (messageFee.getState() != MessageFeeStateEnum.IN_EFFECTIVE.getCode()
				&& messageFee.getState() != MessageFeeStateEnum.NOT_STARTED.getCode()
				&& messageFee.getState() != MessageFeeStateEnum.EXPIRED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		MessageFee domain = new MessageFee(userTicket);
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.SUBMITTED_REFUND);
		// 获取结算单
		SettleOrder order = settlementRpcResolver.get(settlementAppId, messageFee.getPaymentOrderCode());
		RefundOrder refundOrder = buildRefundOrderDto(userTicket, refundInfoDto, messageFee, order);
		// 构建退款费用
		refundFeeItemService.batchInsert(buildRefundFeeItem(refundInfoDto.getRefundFeeItems(), refundOrder.getId(),refundOrder.getCode()));
		LoggerUtil.buildLoggerContext(messageFee.getId(), messageFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "退款申请信息费单");

	}

	/**
	 * 
	 * @Title buildRefundFeeItem
	 * @param businessChargeItems
	 * @param businessId
	 * @param businessCode
	 */
	private List<RefundFeeItem> buildRefundFeeItem(List<RefundFeeItem> businessChargeItems,Long businessId,String businessCode){
		businessChargeItems.stream().forEach(item -> {
			item.setRefundOrderId(businessId);
			item.setRefundOrderCode(businessCode);
			item.setAmount(item.getAmount());
			item.setCreateTime(LocalDateTime.now());
		});
		return businessChargeItems;
	}
	
	@Override
	@Transactional
	public void refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		LOG.info("信息费退款成功回调{}",settleOrder.getCode());
		MessageFee messageFee = getMessageFeeByCode(refundOrder.getBusinessCode());
		if(messageFee.getState() == MessageFeeStateEnum.REFUNDED.getCode()) {
			LOG.info("退款成功回调,退款单{}多次调用",refundOrder.getCode());
			return;
		}
		if (messageFee.getState() != MessageFeeStateEnum.SUBMITTED_REFUND.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		MessageFee domain = new MessageFee();
		domain.setSyncStatus(2);//默认同步信息中心失败
		update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.REFUNDED);
		
		// 通知消息系统
        try {
        	syncState(messageFee.getCode(), 2);
		} catch (Exception e) {
            LOG.error("【白名单推送】接口调用异常!");
		}
        msgService.sendBusinessLog(recordRefundLog(refundOrder,BizTypeEnum.MESSAGEFEE.getEnName()  ,messageFee.getId(), messageFee.getCode()));
	}

	@Override
	@Transactional
	public void settlementDealHandler(SettleOrder settleOrder) {
		LOG.info("信息费支付成功回调{}",settleOrder.getCode());
		MessageFee messageFee = getMessageFeeByCode(settleOrder.getBusinessCode());
		if(messageFee.getState() == MessageFeeStateEnum.NOT_STARTED.getCode()||
				messageFee.getState() == MessageFeeStateEnum.IN_EFFECTIVE.getCode()) {
			LOG.info("结算成功回调,结算单{}多次调用",settleOrder.getCode());
			return;
		}
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
		MessageFee mesFee = new MessageFee();
		mesFee.setSyncStatus(2); // 默认同步信息费失败
        if (LocalDateTime.now().isAfter(messageFee.getStartDate())) {
            update(mesFee, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.IN_EFFECTIVE);
		} else {
	        update(mesFee, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.NOT_STARTED);
		}
        

        // 通知消息系统
        try {
        	syncState(messageFee.getCode(), 1);
		} catch (Exception e) {
            LOG.error("【白名单推送】接口调用异常!");
		}
        msgService.sendBusinessLog(recordPayLog(settleOrder,BizTypeEnum.MESSAGEFEE.getEnName() ,messageFee.getId(), messageFee.getCode()));

	}
	
	@Override
	public void syncState(String code,Integer syncAction) {
		MessageFee messageFee = getMessageFeeByCode(code);
		if (messageFee.getState() != MessageFeeStateEnum.IN_EFFECTIVE.getCode()
				&& messageFee.getState() != MessageFeeStateEnum.NOT_STARTED.getCode()
				&& messageFee.getState() != MessageFeeStateEnum.EXPIRED.getCode()
				&& messageFee.getState() != MessageFeeStateEnum.REFUNDED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "信息单未缴费!");
		}
		// syncAction 1-缴费成功同步,2-退款同步
		if (messageFee.getState() == MessageFeeStateEnum.REFUNDED.getCode()) {
			syncAction = 2;
		}
		MessageFee domain = new MessageFee();
		domain.setSyncStatus(1);
		if (syncAction == 1) {
			// 通知消息系统
			messageFeeRpc.postPaySuccessMessageFeeCustomer(messageFee);
		}else {
			messageFeeRpc.postRefundMessageFeeCustomer(messageFee);
		}
        update(domain, messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.getMessageFeeStateEnum(messageFee.getState()));
	}
	
	private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, MessageFee messageFee, String orderCode,
			Long amount, BizTypeEnum bizTypeEnum) {
		SettleOrderInfoDto settleOrderInfoDto = new SettleOrderInfoDto(userTicket, bizTypeEnum, SettleTypeEnum.PAY,
				SettleStateEnum.WAIT_DEAL);
		settleOrderInfoDto.setBusinessCode(messageFee.getCode());
		settleOrderInfoDto.setOrderCode(orderCode);
		settleOrderInfoDto.setAmount(amount);
		settleOrderInfoDto.setBusinessDepId(messageFee.getDepartmentId());
		settleOrderInfoDto.setBusinessDepName(messageFee.getDepartmentName());
		settleOrderInfoDto.setCustomerId(messageFee.getCustomerId());
		settleOrderInfoDto.setCustomerName(messageFee.getCustomerName());
		settleOrderInfoDto.setCustomerPhone(messageFee.getCustomerCellphone());
		settleOrderInfoDto.setMchId(messageFee.getMchId());
		settleOrderInfoDto.setBusinessType(BizTypeEnum.MESSAGEFEE.getCode());
		settleOrderInfoDto.setDeductEnable(EnableEnum.NO.getCode());
		settleOrderInfoDto.setCustomerCertificate(messageFee.getCustomerCertificateNumber());
		if (userTicket.getDepartmentId() != null) {
			settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
			settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
		}
		// 结算费用项列表
		List<SettleFeeItem> settleFeeItemList = new ArrayList<>();
		List<BusinessChargeItem> items = businessChargeItemService.getByBizCode(messageFee.getCode());
		for (BusinessChargeItem item : items) {
			SettleFeeItem settleFeeItem = new SettleFeeItem();
			settleFeeItem.setChargeItemId(item.getChargeItemId());
			settleFeeItem.setChargeItemName(item.getChargeItemName());
			settleFeeItem.setAmount(item.getAmount());
			settleFeeItemList.add(settleFeeItem);
		}
		settleOrderInfoDto.setSettleFeeItemList(settleFeeItemList);
		// 结算单链接列表
		settleOrderInfoDto.setSettleOrderLinkList(SettleOrderLinkUtils.buildLinks(settlerPrintUrl, settleViewUrl,
				settlerHandlerUrl, messageFee.getCode(), orderCode));
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
		refundOrder.setBizType(BizTypeEnum.MESSAGEFEE.getCode());
		refundOrder.setPayeeId(refundInfoDto.getPayeeId());
		refundOrder.setPayee(refundInfoDto.getPayee());
		refundOrder.setRefundType(refundInfoDto.getRefundType());
		refundOrder.setMchId(messageFee.getMchId());
		refundOrder.setDepartmentName(messageFee.getDepartmentName());
		refundOrder.setDepartmentId(messageFee.getDepartmentId());
		/*if(SettleWayEnum.BANK.getCode() == refundInfoDto.getRefundType()) {
			refundOrder.setBank(refundInfoDto.getBank());
			refundOrder.setBankCardNo(refundInfoDto.getBankCardNo());
		}*/
		refundOrder.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode()+"_"+BizTypeEnum.MESSAGEFEE.getEnName()
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
		// 更新信息费单
		if(LocalDateTime.now().isAfter(messageFee.getEndDate())){
            update(new MessageFee(), messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.EXPIRED);
		} else if (LocalDateTime.now().isAfter(messageFee.getStartDate())) {
            update(new MessageFee(), messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.IN_EFFECTIVE);
		} else {
	        update(new MessageFee(), messageFee.getCode(), messageFee.getVersion(), MessageFeeStateEnum.NOT_STARTED);
		}
	}

	@Override
	public List<QueryFeeOutput> getCost(MessageFeeDto messageFeeDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		List<QueryFeeInput> queryFeeInputs = new ArrayList<>();
		long day = DateUtil.betweenDay(DateUtils.localDateTimeToUdate(messageFeeDto.getStartDate()), 
				DateUtils.localDateTimeToUdate(messageFeeDto.getEndDate()), true);
		messageFeeDto.getBusinessChargeItems().forEach(itme -> {
			QueryFeeInput queryFeeInput =new QueryFeeInput();
			queryFeeInput.setMarketId(userTicket.getFirmId());
			queryFeeInput.setBusinessType(BizTypeEnum.MESSAGEFEE.getCode());
			queryFeeInput.setChargeItem(itme.getChargeItemId());
			Map<String, Object> calcParams = new HashMap<String, Object>();
			calcParams.put("day", day);
			queryFeeInput.setCalcParams(calcParams);
			queryFeeInputs.add(queryFeeInput);
		});
		BaseOutput<List<QueryFeeOutput>> batchQueryFee = chargeRuleRpc.batchQueryFee(queryFeeInputs);
		return batchQueryFee.getData();
	}

	@Override
	public void scanEffective() {
		// 过期扫描
		MessageFeeQuery condition = new MessageFeeQuery();
		condition.setState(MessageFeeStateEnum.IN_EFFECTIVE.getCode());
		condition.setGtEndDate(LocalDateTime.now());
		MessageFeeQuery domain = new MessageFeeQuery();
		domain.setState(MessageFeeStateEnum.EXPIRED.getCode());
		this.updateSelectiveByExample(domain, condition);
		
		// 生效扫描
		MessageFeeQuery condition1 = new MessageFeeQuery();
		condition1.setState(MessageFeeStateEnum.NOT_STARTED.getCode());
		condition1.setGtStartDate(LocalDateTime.now());
		MessageFeeQuery domain1 = new MessageFeeQuery();
		domain1.setState(MessageFeeStateEnum.IN_EFFECTIVE.getCode());
		this.updateSelectiveByExample(domain1, condition1);
	}

	@Override
	public PrintDataDto<MessageFeePayPrintDto> receiptPaymentData(String orderCode, String reprint) {

		PaymentOrder paymentOrder = paymentOrderService.getByCode(orderCode);
		if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
			throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付!");
		}
		MessageFee messageFee = getMessageFeeByCode(paymentOrder.getBusinessCode());
		SettleOrder order = settlementRpcResolver.get(settlementAppId, orderCode);
		MessageFeePayPrintDto messageFeePrint = new MessageFeePayPrintDto();
		messageFeePrint.setPrintTime(LocalDateTime.now());
		messageFeePrint.setReprint("2".equals(reprint) ? "(补打)" : "");
		messageFeePrint.setCode(orderCode);
		messageFeePrint.setBusinessType(BizTypeEnum.MESSAGEFEE.getName());
		messageFeePrint.setTotalAmount(MoneyUtils.centToYuan(messageFee.getAmount()));
		messageFeePrint.setCustomerCellphone(messageFee.getCustomerCellphone());
		messageFeePrint.setCustomerName(messageFee.getCustomerName());
		messageFeePrint.setSettlementOperator(paymentOrder.getSettlementOperator());
		DateTimeFormatter sdf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		messageFeePrint.setEffectiveDate(sdf1.format(messageFee.getStartDate())+"至"+sdf1.format(messageFee.getEndDate()));
		messageFeePrint.setNotes(messageFee.getNotes());
		messageFeePrint.setSubmitter(messageFee.getSubmitorName());
		messageFeePrint.setSettlementOperator(order.getOperatorName());
		// 支付方式
        String settleDetails = "";
        if (SettleWayEnum.CARD.getCode() == order.getWay()) {
            // 园区卡支付
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【卡号：" + order.getTradeCardNo() +
                    "（" + order.getCustomerName() + "）】";
        } else {
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【流水号：" + (StringUtils.isEmpty(order.getSerialNumber())?"--":order.getSerialNumber()) + "  备注："
                    + (StringUtils.isEmpty(order.getNotes())?"--":order.getNotes()) + "】";
        }
        messageFeePrint.setSettleWayDetails(settleDetails);
		messageFeePrint.setSerialNumber(order.getSerialNumber());
		PrintDataDto<MessageFeePayPrintDto> printDataDto = new PrintDataDto<>();
		printDataDto.setName(PrintTemplateEnum.MESSAGEFEE_PAY.getCode());
		printDataDto.setItem(messageFeePrint);

		return printDataDto;	
	}

	@Override
	public PrintDataDto<MessageFeeRefundPrintDto> receiptRefundPrintData(RefundOrder refundOrder, String reprint) {

		MessageFee messageFee = getMessageFeeByCode(refundOrder.getBusinessCode());
		SettleOrder order = settlementRpcResolver.get(settlementAppId, refundOrder.getCode());
		MessageFeeRefundPrintDto printDto = new MessageFeeRefundPrintDto();
		printDto.setPrintTime(LocalDateTime.now());
		printDto.setBusinessType(BizTypeEnum.LABOR_VEST.getName());
		printDto.setTotalAmount(MoneyUtils.centToYuan(messageFee.getAmount()));
		printDto.setCustomerCellphone(messageFee.getCustomerCellphone());
		printDto.setCustomerName(messageFee.getCustomerName());
		printDto.setSettlementOperator(order.getOperatorName());
		printDto.setSubmitter(refundOrder.getSubmitter());
		printDto.setNotes(refundOrder.getRefundReason());
		printDto.setPayeeAmount(MoneyUtils.centToYuan(refundOrder.getPayeeAmount()));
		String settleDetails = "收款人：" + refundOrder.getPayee() + "金额：" + MoneyUtils.centToYuan(refundOrder.getPayeeAmount());
        if (SettleWayEnum.CARD.getCode() == order.getWay()) {
            // 园区卡支付
            settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     园区卡号：" + order.getTradeCardNo();
        } else if (SettleWayEnum.CASH.getCode() == order.getWay()) {
            // 现金
            settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay());
        } else if (SettleWayEnum.BANK.getCode() == order.getWay())  {
            // 银行卡
            settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "  开户行：" + order.getBankName() + "  银行卡号：" + order.getBankCardHolder();
        }
        printDto.setSettleWayDetails(settleDetails);
		// 获取转抵信息
//		printDto.setTransferDeductionItems(transferDeductionItemService.list(condtion));
		PrintDataDto<MessageFeeRefundPrintDto> printDataDto = new PrintDataDto<>();
		printDataDto.setName(PrintTemplateEnum.MESSAGEFEE_REFUND.getCode());
		printDataDto.setItem(printDto);
		return printDataDto;	
	
	}
	
	private RefundOrder getOrderByCode(String code) {
		RefundOrder condtion = new RefundOrder();
		condtion.setCode(code);
		List<RefundOrder> refundOrders = refundOrderService.list(condtion);
		if(CollectionUtil.isNotEmpty(refundOrders)) {
			return refundOrders.get(0);
		}
		throw new BusinessException(ResultCode.DATA_ERROR, "未查询到退款单!");

	}
	
}