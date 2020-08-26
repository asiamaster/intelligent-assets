package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.domain.Labor;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.domain.dto.LaborDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.domain.dto.printDto.LaborPayPrintDto;
import com.dili.ia.domain.dto.printDto.LaborRefundPrintDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.LaborStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.LaborMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.LaborService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.rule.sdk.domain.input.QueryFeeInput;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-27 14:50:45.
 */
@Service
public class LaborServiceImpl extends BaseServiceImpl<Labor, Long> implements LaborService {

	private final static Logger LOG = LoggerFactory.getLogger(LaborServiceImpl.class);

	
	@Autowired
	private UidRpcResolver uidRpcResolver;
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@Autowired
	private BusinessChargeItemService businessChargeItemService;
	
	@Autowired
	private SettlementRpcResolver settlementRpcResolver;
	
	@Autowired
	private RefundOrderService refundOrderService;
	
	@Autowired
	private TransferDeductionItemService transferDeductionItemService;
	
	@Autowired
	private ChargeRuleRpc chargeRuleRpc;
	
	@Autowired
	private CustomerAccountService customerAccountService;
	
	@Autowired
	private DepartmentRpc departmentRpc;
	
	@Value("${settlement.app-id}")
    private Long settlementAppId;
    
	private String settlerHandlerUrl = "http://10.28.1.187:8381/api/labor/vest/settlementDealHandler";
	
    public LaborMapper getActualDao() {
        return (LaborMapper)getDao();
    }

	@Override
	@Transactional
	public void create(LaborDto laborDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = buildLabor(laborDto, userTicket);
		this.insertSelective(labor);
		List<BusinessChargeItem> businessChargeItems = buildBusinessCharge(laborDto.getBusinessChargeItems(), labor.getId(), labor.getCode());
		if(CollectionUtil.isNotEmpty(businessChargeItems)) {
			businessChargeItemService.batchInsert(businessChargeItems);
		}
		LoggerUtil.buildLoggerContext(labor.getId(), labor.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "新增马甲单");
	}
	
	private List<BusinessChargeItem> buildBusinessCharge(List<BusinessChargeItem> businessChargeItems,Long businessId,String businessCode){
		businessChargeItems.stream().forEach(item -> {
			item.setBizType(BizTypeEnum.LABOR_VEST.getCode());
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
	
	private Labor buildLabor(LaborDto laborDto, UserTicket userTicket) {
		Labor labor = new Labor(userTicket);
		BeanUtil.copyProperties(laborDto, labor);
		labor.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode()+"_"+BizNumberTypeEnum.LABOR_VEST.getCode()));
		labor.setCreateTime(LocalDateTime.now());
		labor.setState(LaborStateEnum.CREATED.getCode());
		labor.setMarketCode(userTicket.getFirmCode());
		labor.setMarketId(userTicket.getFirmId());
		labor.setCreator(userTicket.getRealName());
		labor.setCreatorId(userTicket.getId());
		labor.setVersion(1);
		return labor;
	}

	@Override
	@Transactional
	public void update(LaborDto laborDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(laborDto.getCode());
		if(labor.getState() != LaborStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		Labor domain = new Labor(userTicket);
		BeanUtil.copyProperties(laborDto, domain, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.CREATED);
		List<BusinessChargeItem> businessChargeItems = laborDto.getBusinessChargeItems();
		businessChargeItemService.batchUpdateSelective(businessChargeItems);
		LoggerUtil.buildLoggerContext(labor.getId(), labor.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "修改马甲单");
	}
	
	

	@Override
	@Transactional
	public void cancel(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor laborRename = getLaborByCode(code);
		if(laborRename.getState() != LaborStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		Labor domain = new Labor(userTicket);
		update(domain, laborRename.getCode(), laborRename.getVersion(), LaborStateEnum.CANCELLED);
		//判断是否有主单(更名,型)
		String oldCode = StringUtils.isNotEmpty(laborRename.getRenameCode())?laborRename.getRenameCode():laborRename.getRemodelCode();
		if(StringUtils.isNotEmpty(oldCode)) {
			Labor labor = getLaborByCode(oldCode);
			Labor laborDomain = new Labor(userTicket);
			if(LocalDateTime.now().isAfter(labor.getStartDate())) {
				update(laborDomain, oldCode, labor.getVersion(), LaborStateEnum.IN_EFFECTIVE);
			}else {
				update(laborDomain, oldCode, labor.getVersion(), LaborStateEnum.NOT_STARTED);
			}
		}
		LoggerUtil.buildLoggerContext(laborRename.getId(), laborRename.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "取消马甲单");

	}

	@Override
	@GlobalTransactional
	public void submit(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(code);
		if(labor.getState() != LaborStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		
		// 结算单
		PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket,BizTypeEnum.LABOR_VEST);
		paymentOrder.setBusinessCode(code);
		paymentOrder.setAmount(labor.getAmount());
		paymentOrder.setBusinessId(labor.getId());
		paymentOrderService.insertSelective(paymentOrder);
		// 结算服务
		SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket,
				labor, paymentOrder.getCode(), paymentOrder.getAmount(), BizTypeEnum.LABOR_VEST);
		settleOrderDto.setReturnUrl(settlerHandlerUrl);
		settlementRpcResolver.submit(settleOrderDto);
		
		Labor domain = new Labor(userTicket);
		domain.setPaymentOrderCode(paymentOrder.getCode());
		domain.setSubmitter(userTicket.getRealName());
		domain.setSubmitterId(userTicket.getId());
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.SUBMITTED_PAY);
		LoggerUtil.buildLoggerContext(labor.getId(), labor.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "提交马甲单");

	}

	@Override
	@GlobalTransactional
	public void withdraw(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(code);
		if(labor.getState() != LaborStateEnum.SUBMITTED_PAY.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		Labor domain = new Labor(userTicket);
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.CREATED);
		PaymentOrder paymentOrder = paymentOrderService.getByCode(labor.getPaymentOrderCode());
		paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
		paymentOrderService.updateSelective(paymentOrder);
		settlementRpcResolver.cancel(paymentOrder.getCode());
		LoggerUtil.buildLoggerContext(labor.getId(), labor.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "撤销马甲单");
	}

	@Override
	@Transactional
	public void renew(LaborDto laborDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(laborDto.getRenewCode());
		if(labor.getState() != LaborStateEnum.IN_EFFECTIVE.getCode() && labor.getState() != LaborStateEnum.NOT_STARTED.getCode()
				&& labor.getState() != LaborStateEnum.EXPIRED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		// Labor odlLabor = new Labor(userTicket);
		// update(odlLabor, labor.getCode(), labor.getVersion(), LaborStateEnum.IN_RENAME);
		Labor domain = buildLabor(laborDto, userTicket);
		domain.setRenewCode(labor.getCode());
		this.insertSelective(domain);
		List<BusinessChargeItem> businessChargeItems = buildBusinessCharge(laborDto.getBusinessChargeItems(), domain.getId(), domain.getCode());
		businessChargeItemService.batchInsert(businessChargeItems);
		LoggerUtil.buildLoggerContext(labor.getId(), labor.getCode(), userTicket.getId(),
				userTicket.getRealName(), userTicket.getFirmId(),  String.format("马甲单%s续费%s", labor.getCode(),domain.getCode()));

	}

	@Override
	@Transactional
	public void rename(LaborDto laborDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(laborDto.getRenameCode());
		if(labor.getState() != LaborStateEnum.IN_EFFECTIVE.getCode() && labor.getState() != LaborStateEnum.NOT_STARTED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		Labor odlLabor = new Labor(userTicket);
		update(odlLabor, labor.getCode(), labor.getVersion(), LaborStateEnum.IN_RENAME);
		Labor domain = buildLabor(laborDto, userTicket);
		domain.setRenameCode(labor.getCode());
		this.insertSelective(domain);
		List<BusinessChargeItem> businessChargeItems = buildBusinessCharge(laborDto.getBusinessChargeItems(), domain.getId(), domain.getCode());
		businessChargeItemService.batchInsert(businessChargeItems);
		LoggerUtil.buildLoggerContext(labor.getId(), labor.getCode(), userTicket.getId(),
				userTicket.getRealName(), userTicket.getFirmId(), String.format("马甲单%s更名%s", labor.getCode(),domain.getCode()));
		// LoggerContext.put("content", String.format("马甲单%s更名%s", labor.getCode(),domain.getCode()));
	}

	@Override
	@Transactional
	public void remodel(LaborDto laborDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(laborDto.getRemodelCode());
		if(labor.getState() != LaborStateEnum.IN_EFFECTIVE.getCode() && labor.getState() != LaborStateEnum.NOT_STARTED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		Labor odlLabor = new Labor(userTicket);
		update(odlLabor, labor.getCode(), labor.getVersion(), LaborStateEnum.IN_REMODEL);
		Labor domain = buildLabor(laborDto, userTicket);
		domain.setRemodelCode(labor.getCode());
		this.insertSelective(domain);
		List<BusinessChargeItem> businessChargeItems = buildBusinessCharge(laborDto.getBusinessChargeItems(), domain.getId(), domain.getCode());
		businessChargeItemService.batchInsert(businessChargeItems);
		LoggerUtil.buildLoggerContext(labor.getId(), labor.getCode(), userTicket.getId(),
				userTicket.getRealName(), userTicket.getFirmId(), String.format("马甲单%s更型%s", labor.getCode(),domain.getCode()));
	}

	@Override
	@GlobalTransactional
	public void refund(RefundInfoDto refundInfoDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(refundInfoDto.getCode());
		checkRefund(labor);
		Labor domain = new Labor(userTicket);
		domain.setPreState(labor.getState());
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.SUBMITTED_REFUND);
		// 获取结算单
		SettleOrder order = settlementRpcResolver.get(settlementAppId, labor.getCode());
		RefundOrder refundOrder = buildRefundOrderDto(userTicket, refundInfoDto, labor, order);
		// refundOrderService.doAddHandler(refundOrder);
		if (CollectionUtils.isNotEmpty(refundInfoDto.getTransferDeductionItems())) {
			refundInfoDto.getTransferDeductionItems().forEach(o -> {
                o.setRefundOrderId(refundOrder.getId());
                transferDeductionItemService.insertSelective(o);
            });
        }
        LoggerUtil.buildLoggerContext(labor.getId(), labor.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
	}
		
	@Override
	@Transactional
	public void refundSubmitHandler(RefundOrder refundOrder) {
		Labor labor = getLaborByCode(refundOrder.getBusinessCode());
		checkRefund(labor);
		Labor domain = new Labor();
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.SUBMITTED_REFUND);
	}
	
	@Override
	@GlobalTransactional
	public void refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		String code = refundOrder.getBusinessCode();
		Labor labor = getLaborByCode(code);
		if(labor.getState() != LaborStateEnum.SUBMITTED_REFUND.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		Labor domain = new Labor();
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.REFUNDED);
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
		String code = settleOrder.getBusinessCode();
		Labor labor = getLaborByCode(code);
		if (labor.getState() != LaborStateEnum.SUBMITTED_PAY.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		// 获取缴费单
		PaymentOrder paymentOrder = paymentOrderService.getByCode(labor.getPaymentOrderCode());
		paymentOrder.setState(PaymentOrderStateEnum.PAID.getCode());
		// 填充缴费单的结算单信息
		paymentOrder.setSettlementCode(settleOrder.getCode());
		paymentOrder.setSettlementOperator(settleOrder.getOperatorName());
		paymentOrder.setSettlementWay(settleOrder.getWay());
		// 变更缴费单状态
		paymentOrderService.updateSelective(paymentOrder);
		Labor domain = new Labor();
		// 生成马甲号
		String workCard	= buildVestCode(labor.getMarketCode(), labor.getModels());
		domain.setLicensePlate(workCard);
		domain.setWorkCard(workCard);
		if (LocalDateTime.now().isAfter(labor.getStartDate())) {
			update(domain, code, labor.getVersion(), LaborStateEnum.IN_EFFECTIVE);
		} else {
			update(domain, code, labor.getVersion(), LaborStateEnum.NOT_STARTED);
		}
		
		// 获取更名 更型单信息
		//String oldCode = StringUtils.isNotEmpty(labor.getRenameCode())?labor.getRenameCode():labor.getRemodelCode();
		if(StringUtils.isNotEmpty(labor.getRenameCode())) {
			Labor reLabor = getLaborByCode(labor.getRenameCode());
			Labor laborDomain = new Labor();
			update(laborDomain, reLabor.getCode(), reLabor.getVersion(), LaborStateEnum.RENAME);
		} else if (StringUtils.isNotEmpty(labor.getRemodelCode())) {
			Labor reLabor = getLaborByCode(labor.getRemodelCode());
			Labor laborDomain = new Labor();
			update(laborDomain, reLabor.getCode(), reLabor.getVersion(), LaborStateEnum.REMODEL);
		}
	}
	
	private String buildVestCode(String firmCode,String models) {
		String type = "";
		switch (models) {
		case "RL": {
			type = BizNumberTypeEnum.VEST_RL.getCode();	
			break;
		}
		case "DD": {
			type = BizNumberTypeEnum.VEST_DD.getCode();	
			break;
		}
		case "JZ": {
			type = BizNumberTypeEnum.VEST_JZ.getCode();	
			break;
		}
		case "GX": {
			type = BizNumberTypeEnum.VEST_GX.getCode();	
			break;
		}
		case "GD": {
			type = BizNumberTypeEnum.VEST_GD.getCode();	
			break;
		}
		default:
			break;
		}
		String code = uidRpcResolver.bizNumber(firmCode+"_"+type);
		if(StringUtils.isEmpty(code)) {
			throw new BusinessException(ResultCode.DATA_ERROR, "马甲号生成失败");
		}
		// HZSCRL200001
		// 重新拼接规则号HZSC20RL0001
		return code;
	}
	
	private Labor getLaborByCode(String code) {
		Labor condition = new Labor();
		condition.setCode(code);
		List<Labor> labors = this.list(condition);
		if(CollectionUtil.isNotEmpty(labors)) {
			return labors.get(0);
		}
		throw new BusinessException(ResultCode.DATA_ERROR, "马甲单不存在!");
	}
	
	private void update(Labor domain,String code,Integer version,LaborStateEnum state) {
		domain.setVersion(version+1);
		domain.setState(state.getCode());
		Labor condition = new Labor();
		condition.setCode(code);
		condition.setVersion(version);
		int row = updateSelectiveByExample(domain, condition);
		if(row != 1) {
			throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
		}
	}
	
	private RefundOrder buildRefundOrderDto(UserTicket userTicket, RefundInfoDto refundInfoDto, Labor labor,SettleOrder order) {
		//退款单
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setBusinessCode(labor.getCode());
		refundOrder.setBusinessId(labor.getId());
		refundOrder.setCustomerId(labor.getCustomerId());
		refundOrder.setCustomerName(labor.getCustomerName());
		refundOrder.setCustomerCellphone(labor.getCustomerCellphone());
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
	
	private void checkRefund(Labor labor) {
		if(labor.getState() != LaborStateEnum.IN_EFFECTIVE.getCode() 
				&& labor.getState() != LaborStateEnum.NOT_STARTED.getCode()
				&& labor.getState() != LaborStateEnum.RENAME.getCode()
				&& labor.getState() != LaborStateEnum.REMODEL.getCode()
				&& labor.getState() != LaborStateEnum.EXPIRED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
	}

	@Override
	public LaborDto getLabor(String code) {
		Labor labor = getLaborByCode(code);
		LaborDto laborDto = new LaborDto();
		BeanUtil.copyProperties(labor, laborDto);
		BusinessChargeItem condtion = new BusinessChargeItem();
		condtion.setBusinessCode(labor.getCode());
		laborDto.setBusinessChargeItems(businessChargeItemService.list(condtion));
		return laborDto;
	}
	
	// 生效
	private void reEffective() {
		Labor condtion = new Labor();
		condtion.setStartDate(LocalDate.now().atStartOfDay());
		condtion.setState(LaborStateEnum.NOT_STARTED.getCode());
		Labor domain = new Labor();
		domain.setState(LaborStateEnum.IN_EFFECTIVE.getCode());
		updateSelectiveByExample(domain, condtion);
		
	}
	
	// 过期
	private void reExpire() {
		Labor condtion = new Labor();
		condtion.setEndDate(LocalDate.now().atStartOfDay());
		Labor domain = new Labor();
		domain.setState(LaborStateEnum.EXPIRED.getCode());
		updateSelectiveByExample(domain, condtion);
	}

	@Override
	public void scanLaborVest() {
		try {
			reEffective();
		} catch (Exception e) {
			LOG.error("生效订单扫描失败{}",LocalDateTime.now(),e.getMessage());
		}
		try {
			reExpire();
		} catch (Exception e) {
			LOG.error("过期订单扫描失败{}",LocalDateTime.now(),e.getMessage());
		}
	}

	@Override
	public PrintDataDto<LaborPayPrintDto> receiptPaymentData(String orderCode, String reprint) {
		PaymentOrder paymentOrder = paymentOrderService.getByCode(orderCode);
		if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
			throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付!");
		}
		Labor labor = getLaborByCode(paymentOrder.getBusinessCode());
		SettleOrder order = settlementRpcResolver.get(settlementAppId, labor.getCode());
		LaborPayPrintDto laborPrintDto = new LaborPayPrintDto();
		laborPrintDto.setPrintTime(LocalDateTime.now());
		laborPrintDto.setReprint(reprint);
		laborPrintDto.setBusinessType(BizTypeEnum.LABOR_VEST.getName());
		laborPrintDto.setWorkCard(labor.getWorkCard());
		laborPrintDto.setModels(labor.getModels());
		laborPrintDto.setTotalAmount(String.valueOf(paymentOrder.getAmount()));
		laborPrintDto.setCustomerCellphone(labor.getCustomerCellphone());
		laborPrintDto.setCustomerName(labor.getCustomerName());
		laborPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
		laborPrintDto.setSubmitter(labor.getSubmitter());
		DateTimeFormatter sdf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		laborPrintDto.setEffectiveDate(sdf1.format(labor.getStartDate())+"至"+sdf1.format(labor.getEndDate()));
		laborPrintDto.setNotes(labor.getNotes());
		
		laborPrintDto.setPayWay(order.getWayName());
		//TODO 判断支付方式
		//园区卡号
		laborPrintDto.setCardNo(order.getAccountNumber());
		//流水号
		laborPrintDto.setSerialNumber(order.getSerialNumber());
		PrintDataDto<LaborPayPrintDto> printDataDto = new PrintDataDto<>();
		printDataDto.setName(PrintTemplateEnum.LABOR_VEST_PAY.getName());
		printDataDto.setItem(laborPrintDto);
		return printDataDto;	
	}

	@Override
	public PrintDataDto<LaborRefundPrintDto> receiptRefundPrintData(String orderCode, String reprint) {
		RefundOrder refundOrder = getOrderByCode(orderCode);
		Labor labor = getLaborByCode(refundOrder.getBusinessCode());
		SettleOrder order = settlementRpcResolver.get(settlementAppId, labor.getCode());
		LaborRefundPrintDto printDto = new LaborRefundPrintDto();
		printDto.setPrintTime(LocalDateTime.now());
		printDto.setReprint(reprint);
		printDto.setBusinessType(BizTypeEnum.LABOR_VEST.getName());
		printDto.setWorkCard(labor.getWorkCard());
		printDto.setTotalAmount(String.valueOf(labor.getAmount()));
		printDto.setCustomerCellphone(labor.getCustomerCellphone());
		printDto.setCustomerName(labor.getCustomerName());
		printDto.setSettlementOperator(order.getOperatorName());
		printDto.setSubmitter(labor.getSubmitter());
		printDto.setNotes(labor.getNotes());
		printDto.setPayeeAmount(refundOrder.getPayeeAmount());
		//TODO 判断支付方式
		//园区卡号
		printDto.setAccountCardNo(order.getAccountNumber());
		//银行卡号
		printDto.setBankName(refundOrder.getBank());
		printDto.setBankNo(refundOrder.getBankCardNo());
		// 获取转抵信息
		TransferDeductionItem condtion = new TransferDeductionItem();
		condtion.setRefundOrderId(refundOrder.getId());
		printDto.setTransferDeductionItems(transferDeductionItemService.list(condtion));
		PrintDataDto<LaborRefundPrintDto> printDataDto = new PrintDataDto<>();
		printDataDto.setName(PrintTemplateEnum.LABOR_VEST_PAY.getName());
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

	@Override
	public void cancleRefund(RefundOrder refundOrder) {
		Labor labor = getLaborByCode(refundOrder.getBusinessCode());
		Labor domain = new Labor();
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.getLaborStateEnum(labor.getPreState()));
	}

	@Override
	public List<QueryFeeOutput> getCost(LaborDto laborDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		List<QueryFeeInput> queryFeeInputs = new ArrayList<>();
		long day = DateUtil.betweenDay(DateUtils.localDateTimeToUdate(laborDto.getStartDate()), 
				DateUtils.localDateTimeToUdate(laborDto.getEndDate()), true);
		laborDto.getBusinessChargeItems().forEach(itme -> {
			QueryFeeInput queryFeeInput =new QueryFeeInput();
			queryFeeInput.setMarketId(userTicket.getFirmId());
			queryFeeInput.setBusinessType(laborDto.getBusinessChargeType());
			queryFeeInput.setChargeItem(itme.getChargeItemId());
			Map<String, Object> calcParams = new HashMap<String, Object>();
			calcParams.put("laborType", laborDto.getLaborType());
			calcParams.put("models", laborDto.getModels());
			calcParams.put("day", day);
			queryFeeInput.setCalcParams(calcParams);
			queryFeeInputs.add(queryFeeInput);
		});
		BaseOutput<List<QueryFeeOutput>> batchQueryFee = chargeRuleRpc.batchQueryFee(queryFeeInputs);
		return batchQueryFee.getData();
	}
	
	private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, Labor labor,String orderCode,Long amount,BizTypeEnum bizTypeEnum) {
		SettleOrderInfoDto settleOrderInfoDto = 
				new SettleOrderInfoDto(userTicket, bizTypeEnum,SettleTypeEnum.PAY,SettleStateEnum.WAIT_DEAL);
		settleOrderInfoDto.setBusinessCode(labor.getCode());
		settleOrderInfoDto.setOrderCode(orderCode);
		settleOrderInfoDto.setAmount(amount);
		settleOrderInfoDto.setBusinessDepId(labor.getDepartmentId());
		settleOrderInfoDto.setBusinessDepName(labor.getDepartmentName());
		settleOrderInfoDto.setCustomerId(labor.getCustomerId());
		settleOrderInfoDto.setCustomerName(labor.getCustomerName());
		settleOrderInfoDto.setCustomerPhone(labor.getCustomerCellphone());
		if (userTicket.getDepartmentId() != null){
            settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
		return settleOrderInfoDto;
	}
}