package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.StockWeighmanRecord;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.domain.dto.StockInDetailDto;
import com.dili.ia.domain.dto.StockInDto;
import com.dili.ia.domain.dto.StockInQueryDto;
import com.dili.ia.domain.dto.StockInRefundDto;
import com.dili.ia.domain.dto.StockWeighmanRecordDto;
import com.dili.ia.domain.dto.printDto.StockInPrintDto;
import com.dili.ia.domain.dto.printDto.StockInPrintDto.StockInPrintItemDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PayStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.glossary.StockInTypeEnum;
import com.dili.ia.mapper.StockInMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.StockInDetailService;
import com.dili.ia.service.StockInService;
import com.dili.ia.service.StockService;
import com.dili.ia.service.StockWeighmanRecordService;
import com.dili.ia.util.LoggerUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockInServiceImpl extends BaseServiceImpl<StockIn, Long> implements StockInService {
	 private final static Logger LOG = LoggerFactory.getLogger(StockInServiceImpl.class);
	@Autowired
	private UidRpcResolver UidRpcResolver;
	
	@Autowired
	private SettlementRpcResolver settlementRpcResolver;
	
	@Autowired 
	private StockInDetailService stockInDetailService;
	
	@Autowired
	private StockWeighmanRecordService stockWeighmanRecordService;
	
	@Autowired
	private StockService stockService;
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@Autowired
	private RefundOrderService refundOrderService;
	
	@Value("${settlement.app-id}")
    private Long settlementAppId;
    
    private String settlerHandlerUrl = "http://ia.diligrp.com:8381/api/stock/stockIn/settlementDealHandler";
	
    public StockInMapper getActualDao() {
        return (StockInMapper)getDao();
    }

	@Override
	@Transactional
	public void createStockIn(StockInDto stockInDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = buildStockIn(stockInDto, userTicket);
		buildStockDetail(stockInDto.getStockInDetailDtos(), stockIn);
		insertSelective(stockIn);
		LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
	}
	
	private StockIn buildStockIn(StockInDto stockInDto,UserTicket userTicket) {
		StockIn stockIn = new StockIn(userTicket);
		BeanUtils.copyProperties(stockInDto, stockIn, "operatorId", "operatorName");
		stockIn.setCode(UidRpcResolver.bizNumber(userTicket.getFirmCode()+"_"+BizNumberTypeEnum.STOCK_IN_CODE.getCode()));
		stockIn.setCreateTime(new Date());
		stockIn.setState(StockInStateEnum.CREATED.getCode());
		stockIn.setCreator(userTicket.getRealName());
		stockIn.setCreatorId(userTicket.getId());
		stockIn.setMarketId(userTicket.getFirmId());
		stockIn.setMarketCode(userTicket.getFirmCode());
		stockIn.setVersion(1);
		return stockIn;
	}
	private List<StockInDetail> buildStockDetail(List<StockInDetailDto> detailDtos,StockIn stockIn) {
		//List<StockInDetailDto> detailDtos = stockInDto.getAddStockInDetailDtos();
		List<StockInDetail> detailList = new ArrayList<>();
		// 总金额 件数 重量计算
		// 总量 克 计算
		// 金额 分 计算
		Long totalWeight = 0L;
		Long totalQuantity = 0L;
		Long totalMoney = 0L;
		for (StockInDetailDto stockInDetailDto : detailDtos) {
			StockInDetail detail = new StockInDetail();
			BeanUtils.copyProperties(stockInDetailDto, detail);
			detail.setCode(UidRpcResolver.bizNumber(stockIn.getMarketCode()+"_"+BizNumberTypeEnum.STOCK_IN_DETAIL_CODE.getCode()));
			detail.setStockInCode(stockIn.getCode());
			detail.setCreateTime(new Date());
			detail.setMarketId(stockIn.getMarketId());
			detail.setMarketCode(stockIn.getMarketCode());
			detail.setModifyTime(new Date());
			detail.setVersion(1);
			totalWeight += stockInDetailDto.getWeight();
			totalQuantity += stockInDetailDto.getQuantity();
			totalMoney += stockInDetailDto.getAmount();
			// 司磅入库,保存司磅记录信息
			if (stockIn.getType() == StockInTypeEnum.WEIGHT.getCode()) {
				StockWeighmanRecord stockWeighmanRecord = bulidWeighmanRecord(stockInDetailDto.getStockWeighmanRecordDto(),detail);
				// stockWeighmanRecord.setVersion(1);
				stockWeighmanRecordService.insertSelective(stockWeighmanRecord);
				detail.setWeightmanId(stockWeighmanRecord.getId());
			}
			detailList.add(detail);
		}
		stockIn.setWeight(totalWeight);
		stockIn.setQuantity(totalQuantity);
		stockIn.setAmount(totalMoney);
		if(CollectionUtils.isNotEmpty(detailList)) {
			stockInDetailService.batchInsert(detailList);
		}
		
		return detailList;
	}
	
	private StockWeighmanRecord bulidWeighmanRecord (StockWeighmanRecordDto stockWeighmanRecordDto,StockInDetail detail) {
		StockWeighmanRecord stockWeighmanRecord = new StockWeighmanRecord();
		BeanUtils.copyProperties(stockWeighmanRecordDto, stockWeighmanRecord);
		stockWeighmanRecord.setNewWeight(stockWeighmanRecordDto.getGrossWeight()-stockWeighmanRecordDto.getTareWeight());
		stockWeighmanRecord.setMarketId(detail.getMarketId());
		stockWeighmanRecord.setMarketCode(detail.getMarketCode());
		return stockWeighmanRecord;
	}

	@Override
	@Transactional
	public void updateStockIn(StockInDto stockInDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(stockInDto.getCode());
		
		// 总金额 件数 重量计算
		// 总量 克 计算
		// 金额 分 计算
		// 人法地、地法天、天法道、道法自然
		Long totalWeight = 0L;
		Long totalQuantity = 0L;
		Long totalMoney = 0L;
		stockIn.setWeight(totalWeight);
		stockIn.setQuantity(totalQuantity);
		stockIn.setAmount(totalQuantity);
		List<StockInDetailDto> addDetailDtos = new ArrayList<StockInDetailDto>();
		List<StockInDetailDto> detailDtos = stockInDto.getStockInDetailDtos();
		for (StockInDetailDto stockInDetailDto : detailDtos) {
			if(StringUtils.isEmpty(stockInDetailDto.getCode())) {
				addDetailDtos.add(stockInDetailDto);
				continue;
			}
			// 修改子单
			StockInDetail detail = stockInDetailService.getByCode(stockInDetailDto.getCode());
			if(stockInDetailDto.getDelete()) {
				stockInDetailService.delete(detail.getId());
				continue;
			}
			
			totalWeight += stockInDetailDto.getWeight();
			totalQuantity += stockInDetailDto.getQuantity();
			totalMoney += stockInDetailDto.getAmount();
			// 修改入库单信息
			StockInDetail domain = new StockInDetail();
			BeanUtil.copyProperties(stockInDetailDto, domain,
					CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			StockInDetail condition = new StockInDetail();
			condition.setCode(detail.getCode());
			condition.setVersion(detail.getVersion());
			// update
			stockInDetailService.updateSelectiveByExample(domain, condition);
		}
		// 新增子单
		buildStockDetail(addDetailDtos, stockIn);
		// 修改入库单信息
		StockIn domain = new StockIn(userTicket);
		BeanUtil.copyProperties(stockInDto, domain,CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
		domain.setWeight(stockIn.getWeight()+totalWeight);
		domain.setQuantity(stockIn.getQuantity()+totalQuantity);
		domain.setAmount(stockIn.getAmount()+totalMoney);
		StockIn condition = new StockIn();
		condition.setCode(stockIn.getCode());
		condition.setVersion(stockIn.getVersion());
		updateSelectiveByExample(domain, condition);
	}
	
	@Override
	@Transactional
	//@GlobalTransactional
	public void submit(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if (stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		getStockInDetailsByStockCode(code);

		// 创建收费单费用收取
		PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket);
		paymentOrder.setBusinessCode(code);
		paymentOrder.setAmount(stockIn.getAmount());
		paymentOrderService.insertSelective(paymentOrder);
		// 提交入库单
		StockIn domain = new StockIn(userTicket);
		domain.setSubmitterId(userTicket.getId());
		domain.setSubmitter(userTicket.getRealName());
		domain.setPaymentOrderCode(paymentOrder.getCode());
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.SUBMITTED_PAY);
		//
		// 调用结算接口,缴费
		SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, stockIn, paymentOrder.getCode(), paymentOrder.getAmount());
		settlementRpcResolver.submit(settleOrderDto);
	}
	
	@Override
	public StockIn getStockInByCode(String code) {
		StockIn example = new StockIn();
		example.setCode(code);
		//根据code获取入库单
		List<StockIn> stockInList = listByExample(example);
		if(CollectionUtils.isEmpty(stockInList) || stockInList.size() != 1) {
			throw new BusinessException(ResultCode.DATA_ERROR, "入库单不存在");
		}
		return stockInList.get(0);
	}
	
	private List<StockInDetail> getStockInDetailsByStockCode(String code){
		StockInDetail example = new StockInDetail();
		example.setStockInCode(code);
		//根据code获取入库单
		List<StockInDetail> details = stockInDetailService.listByExample(example);
		if(CollectionUtils.isEmpty(details)) {
			throw new BusinessException(ResultCode.DATA_ERROR, "子单记录不存在");
		}
		return details;
	}
	
	private void updateState(StockIn domain,String code,Integer version,StockInStateEnum state) {
		domain.setVersion(version+1);
		domain.setState(state.getCode());
		StockIn condition = new StockIn();
		condition.setCode(code);
		condition.setVersion(version);
		//修改入库单状态提交入库单
		int row = updateSelectiveByExample(domain, condition);
		if(row != 1) {
			throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
		}
	}

	@Override
	public StockInDto view(String code) {
		StockIn stockIn = getStockInByCode(code);
		StockInDto stockInDto = new StockInDto();
		BeanUtils.copyProperties(stockIn, stockInDto);
		List<StockInDetail> stockInDetails = getStockInDetailsByStockCode(code);
		stockInDto.setStockInDetails(stockInDetails);
		stockInDto.setJsonStockInDetailDtos(JSON.toJSONString(stockInDetails));
		return stockInDto;
	}

	

	@Override
	@Transactional
	public void cancel(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn domain = new StockIn(userTicket);
		domain.setCancelerId(userTicket.getId());
		domain.setCanceler(userTicket.getRealName());
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.CANCELLED);
	}

	@Override
	@Transactional
	public void withdraw(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.SUBMITTED_PAY.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn domain = new StockIn(userTicket);
		domain.setWithdrawOperator(userTicket.getRealName());
		domain.setWithdrawOperatorId(userTicket.getId());
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.CREATED);
		// 撤销缴费单/结算单
		PaymentOrder paymentOrder = paymentOrderService.getByCode(stockIn.getPaymentOrderCode());
		paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
		paymentOrderService.updateSelective(paymentOrder);
		settlementRpcResolver.cancel(settlementAppId, paymentOrder.getCode());
	}

	@Override
	@Transactional
	public void refund(StockInRefundDto stockInRefundDto) {
		String code = stockInRefundDto.getCode();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.PAID.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		RefundOrder refundOrder = buildRefundOrderDto(userTicket, stockInRefundDto, stockIn);
		//BaseOutput out = refundOrderService.doSubmitDispatcher(refundOrder);
		//SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, stockIn, refundOrder.getCode(), refundOrder.getPayeeAmount());
		//settlementRpcResolver.submit(settleOrderDto);
		refundOrderService.doSubmitDispatcher(refundOrder);
	}
	
	@Override
	public void refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		String code = refundOrder.getBusinessCode();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.PAID.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn domain = new StockIn(userTicket);
		//domain.setWithdrawOperator(userTicket.getRealName());
		//domain.setWithdrawOperatorId(userTicket.getId());
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.CANCELLED);
		List<StockInDetail> details = getStockInDetailsByStockCode(code);
		details.forEach(detail -> {
			stockService.stockDeduction(detail, stockIn.getCustomerId(), "退款businessCode");
		});
	}
	
	@Override
	public String listPageAction(StockInQueryDto stockIn) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		//TODO 数据隔离
		stockIn.setMarketId(userTicket.getFirmId());
		try {
			return this.listEasyuiPageByExample(stockIn, true).toString();
		} catch (Exception e) {
			e.printStackTrace();
		};
		return null;
	}

	
	private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, StockIn stockIn,String orderCode,Long amount) {
		SettleOrderInfoDto settleOrderInfoDto = 
				new SettleOrderInfoDto(userTicket, BizTypeEnum.STOCKIN,SettleTypeEnum.PAY,SettleStateEnum.WAIT_DEAL);
		settleOrderInfoDto.setBusinessCode(stockIn.getCode());
		settleOrderInfoDto.setOrderCode(orderCode);
		settleOrderInfoDto.setAmount(amount);
		settleOrderInfoDto.setAppId(settlementAppId);
		settleOrderInfoDto.setBusinessDepId(stockIn.getDepartmentId());
		settleOrderInfoDto.setBusinessDepName(stockIn.getDepartmentName());
		settleOrderInfoDto.setCustomerId(stockIn.getCustomerId());
		settleOrderInfoDto.setCustomerName(stockIn.getCustomerName());
		settleOrderInfoDto.setCustomerPhone(stockIn.getCustomerCellphone());
		settleOrderInfoDto.setReturnUrl(settlerHandlerUrl);
		return settleOrderInfoDto;
	}
	
	private RefundOrder buildRefundOrderDto(UserTicket userTicket, StockInRefundDto stockInRefundDto, StockIn stockIn) {
		//退款单
		RefundOrder refundOrder = DTOUtils.newInstance(RefundOrder.class);
		refundOrder.setBusinessCode(stockIn.getCode());
		refundOrder.setBusinessId(stockIn.getId());
		refundOrder.setCustomerId(stockIn.getCustomerId());
		refundOrder.setCustomerName(stockIn.getCustomerName());
		refundOrder.setCustomerCellphone(stockIn.getCustomerCellphone());
		refundOrder.setCertificateNumber("0000");
		refundOrder.setTotalRefundAmount(stockInRefundDto.getAmount());
		refundOrder.setPayeeAmount(stockInRefundDto.getAmount());
		refundOrder.setRefundReason(stockInRefundDto.getNotes());
		refundOrder.setBizType(BizTypeEnum.STOCKIN.getCode());
		refundOrder.setCode(UidRpcResolver.bizNumber(BizNumberTypeEnum.LEASE_REFUND_ORDER.getCode()));
		if (!refundOrderService.doAddHandler(refundOrder).isSuccess()) {
			LOG.info("入库单【编号：{}】退款申请接口异常", refundOrder.getBusinessCode());
			throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常");
		}
		return refundOrder;
	}

	@Override
	public void settlementDealHandler(SettleOrder settleOrder) {
		String code = settleOrder.getBusinessCode();
		StockIn stockIn = getStockInByCode(code);
		if (stockIn.getState() != StockInStateEnum.SUBMITTED_PAY.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		// 获取缴费单
		PaymentOrder paymentOrder = paymentOrderService.getByCode(stockIn.getPaymentOrderCode());
		paymentOrder.setState(PaymentOrderStateEnum.PAID.getCode());
		// 填充缴费单的结算单信息
		paymentOrder.setSettlementCode(settleOrder.getCode());
		paymentOrder.setSettlementOperator(settleOrder.getOperatorName());
		paymentOrder.setSettlementWay(settleOrder.getWay());
		// 变更缴费单状态
		paymentOrderService.updateSelective(paymentOrder);
		// paymentOrder.setIsSettle();
		StockIn domain = new StockIn();
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.PAID);
		// 入库 库存
		List<StockInDetail> stockInDetails = getStockInDetailsByStockCode(code);
		stockService.inStock(stockInDetails, stockIn);

	}

	@Override
	public PrintDataDto<Map<String, Object>> receiptData(String orderCode, Integer reprint) {
		PaymentOrder paymentOrder = paymentOrderService.getByCode(orderCode);
		if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
			throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付!");
		}
		StockIn stockIn = getStockInByCode(paymentOrder.getBusinessCode());
		StockInPrintDto stockInPrintDto = new StockInPrintDto();
		//stockInPrintDto.set
		stockInPrintDto.setBusinessType("businessType");
		stockInPrintDto.setCardNo("");
		stockInPrintDto.setCategoryName(stockIn.getCategoryName());
		stockInPrintDto.setCustomerCellphone(stockIn.getCustomerCellphone());
		stockInPrintDto.setCustomerName(stockIn.getCustomerName());
		stockInPrintDto.setDepartmentName(stockIn.getDepartmentName());
		stockInPrintDto.setPrintTime(LocalDate.now());
		stockInPrintDto.setReprint("1");
		stockInPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
		stockInPrintDto.setSubmitter("");
		stockInPrintDto.setReviewer("");
		stockInPrintDto.setTotalAmount(String.valueOf(paymentOrder.getAmount()));
		
		
		//StockInPrintItemDto stockInPrintItemDto = stockInPrintDto.getItemDto();
		
		return null;
	}

	
	
}