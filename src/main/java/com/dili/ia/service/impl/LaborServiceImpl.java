package com.dili.ia.service.impl;

import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.domain.Labor;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.LaborDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.LaborStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.mapper.LaborMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.LaborService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import io.seata.spring.annotation.GlobalTransactional;

import java.time.LocalDateTime;
import java.util.List;

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
	}
	
	private void updateOldLabor(String code,String actionType) {
		
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
			if(LocalDateTime.now().isBefore(labor.getStartDate())) {
				update(laborDomain, oldCode, labor.getVersion(), LaborStateEnum.IN_EFFECTIVE);
			}else {
				update(laborDomain, oldCode, labor.getVersion(), LaborStateEnum.NOT_STARTED);
			}
		}
	}

	@Override
	@Transactional
	public void submit(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(code);
		if(labor.getState() != LaborStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		
		// 结算单
		PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket);
		paymentOrder.setBusinessCode(code);
		paymentOrder.setAmount(labor.getAmount());
		paymentOrder.setBizType(BizTypeEnum.LABOR_VEST.getCode());
		paymentOrder.setBusinessId(labor.getId());
		paymentOrderService.insertSelective(paymentOrder);
		// 结算服务
		SettleOrderDto settleOrderDto = settlementRpcResolver.buildSettleOrderDto(userTicket,
				labor, paymentOrder.getCode(), paymentOrder.getAmount(), BizTypeEnum.STOCKIN);
		settleOrderDto.setReturnUrl(settlerHandlerUrl);
		settlementRpcResolver.submit(settleOrderDto);
		
		Labor domain = new Labor(userTicket);
		domain.setPaymentOrderCode(paymentOrder.getCode());
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.SUBMITTED_PAY);
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
		Labor odlLabor = new Labor(userTicket);
		update(odlLabor, labor.getCode(), labor.getVersion(), LaborStateEnum.IN_RENAME);
		Labor domain = buildLabor(laborDto, userTicket);
		domain.setRenewCode(labor.getCode());
		this.insertSelective(domain);
		List<BusinessChargeItem> businessChargeItems = buildBusinessCharge(laborDto.getBusinessChargeItems(), domain.getId(), domain.getCode());
		businessChargeItemService.batchInsert(businessChargeItems);
		
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
		
	}

	@Override
	@GlobalTransactional
	public void refund(RefundInfoDto refundInfoDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		Labor labor = getLaborByCode(refundInfoDto.getCode());
		checkRefund(labor);
		Labor domain = new Labor(userTicket);
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
        //LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
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
	@Transactional
	public void refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		String code = refundOrder.getBusinessCode();
		Labor labor = getLaborByCode(code);
		if(labor.getState() != LaborStateEnum.SUBMITTED_REFUND.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		Labor domain = new Labor();
		update(domain, labor.getCode(), labor.getVersion(), LaborStateEnum.REFUNDED);
		
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
		if (LocalDateTime.now().isBefore(labor.getStartDate())) {
			update(domain, code, labor.getVersion(), LaborStateEnum.IN_EFFECTIVE);
		} else {
			update(domain, code, labor.getVersion(), LaborStateEnum.NOT_STARTED);
		}

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
		refundOrder.setCode(uidRpcResolver.bizNumber(BizNumberTypeEnum.LEASE_REFUND_ORDER.getCode()));
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
	
}