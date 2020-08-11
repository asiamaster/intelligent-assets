package com.dili.ia.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.Stock;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.StockWeighmanRecord;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.domain.dto.StockInDetailDto;
import com.dili.ia.domain.dto.StockInDto;
import com.dili.ia.domain.dto.StockInQueryDto;
import com.dili.ia.domain.dto.StockInRefundDto;
import com.dili.ia.domain.dto.StockWeighmanRecordDto;
import com.dili.ia.domain.dto.printDto.StockInPrintDto;
import com.dili.ia.domain.dto.printDto.StockInPrintItemDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.glossary.StockInTypeEnum;
import com.dili.ia.mapper.StockInMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.StockInDetailService;
import com.dili.ia.service.StockInService;
import com.dili.ia.service.StockService;
import com.dili.ia.service.StockWeighmanRecordService;
import com.dili.ia.util.LoggerUtil;
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
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockInServiceImpl extends BaseServiceImpl<StockIn, Long> implements StockInService {
	private final static Logger LOG = LoggerFactory.getLogger(StockInServiceImpl.class);
	@Autowired
	private UidRpcResolver uidRpcResolver;
	
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
	
	@Autowired
	private BusinessChargeItemService businessChargeItemService;
	
	@Autowired
	private DepartmentRpc departmentRpc;
	
	@Value("${settlement.app-id}")
    private Long settlementAppId;
    
    private String settlerHandlerUrl = "http://10.28.1.187:8381/api/stockIn/settlementDealHandler";
	
    public StockInMapper getActualDao() {
        return (StockInMapper)getDao();
    }

    @Override
	@Transactional
	public void createStockIn(StockInDto stockInDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		//构建入库单
		StockIn stockIn = buildStockIn(stockInDto, userTicket);
		//构建入库单详情(子单)
		buildStockDetail(stockInDto.getStockInDetailDtos(), stockIn);
		insertSelective(stockIn);
		//构建动态收费项
		businessChargeItemService.batchInsert(buildBusinessCharge(stockInDto.getBusinessChargeItems(), stockIn.getId(),stockIn.getCode()));
		//businessChargeItemService.save(BusinessChargeItem);
		LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
	}
	
	private StockIn buildStockIn(StockInDto stockInDto,UserTicket userTicket) {
		StockIn stockIn = new StockIn(userTicket);
		BeanUtils.copyProperties(stockInDto, stockIn, "operatorId", "operatorName");
		stockIn.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode()+"_"+BizNumberTypeEnum.STOCK_IN_CODE.getCode()));
		stockIn.setCreateTime(LocalDateTime.now());
		stockIn.setState(StockInStateEnum.CREATED.getCode());
		stockIn.setCreator(userTicket.getRealName());
		stockIn.setCreatorId(userTicket.getId());
		stockIn.setMarketId(userTicket.getFirmId());
		stockIn.setMarketCode(userTicket.getFirmCode());
		stockIn.setVersion(1);
		return stockIn;
	}
	
	private List<BusinessChargeItem> buildBusinessCharge(List<BusinessChargeItem> businessChargeItems,Long businessId,String businessCode){
		businessChargeItems.stream().forEach(item -> {
			item.setBizType(BizTypeEnum.STOCKIN.getCode());
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
	private List<StockInDetail> buildStockDetail(List<StockInDetailDto> detailDtos,StockIn stockIn) {
		List<StockInDetail> detailList = new ArrayList<>();
		List<BusinessChargeItem> businessChargeItems = new ArrayList<>();
		// 总金额 件数 重量计算 总量 克 计算 金额 分 计算
		Long totalWeight = 0L;
		Long totalQuantity = 0L;
		Long totalMoney = 0L;
		for (StockInDetailDto stockInDetailDto : detailDtos) {
			StockInDetail detail = new StockInDetail();
			BeanUtils.copyProperties(stockInDetailDto, detail);
			detail.setCode(uidRpcResolver.bizNumber(stockIn.getMarketCode()+"_"+BizNumberTypeEnum.STOCK_IN_DETAIL_CODE.getCode()));
			detail.setStockInCode(stockIn.getCode());
			detail.setCreateTime(LocalDateTime.now());
			detail.setMarketId(stockIn.getMarketId());
			detail.setMarketCode(stockIn.getMarketCode());
			detail.setModifyTime(LocalDateTime.now());
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
			stockInDetailService.insertSelective(detail);
			//detailList.add(detail);
			businessChargeItems.addAll(buildBusinessCharge(stockInDetailDto.getBusinessChargeItems(), detail.getId(),detail.getCode()));
			
		}
		
		stockIn.setWeight(totalWeight);
		stockIn.setQuantity(totalQuantity);
		stockIn.setAmount(totalMoney);
		/*if(CollectionUtils.isNotEmpty(detailList)) {
			stockInDetailService.batchInsert(detailList);
		}*/
		if(CollectionUtils.isNotEmpty(businessChargeItems)) {
			businessChargeItemService.batchInsert(businessChargeItems);
		}
		
		return detailList;
	}
	
	private StockWeighmanRecord bulidWeighmanRecord (StockWeighmanRecordDto stockWeighmanRecordDto,StockInDetail detail) {
		StockWeighmanRecord stockWeighmanRecord = new StockWeighmanRecord();
		BeanUtils.copyProperties(stockWeighmanRecordDto, stockWeighmanRecord);
		if(stockWeighmanRecordDto.getGrossWeight() !=null && stockWeighmanRecordDto.getTareWeight() != null) {
			stockWeighmanRecord.setNewWeight(stockWeighmanRecordDto.getGrossWeight()-stockWeighmanRecordDto.getTareWeight());
		}
		stockWeighmanRecord.setMarketId(detail.getMarketId());
		stockWeighmanRecord.setMarketCode(detail.getMarketCode());
		return stockWeighmanRecord;
	}

	@Override
	@Transactional
	public void updateStockIn(StockInDto stockInDto) {
		
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(stockInDto.getCode());
		if (stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		// 总金额 件数 重量计算 总量 克 计算金额 分 计算
		Long totalWeight = 0L;
		Long totalQuantity = 0L;
		Long totalMoney = 0L;
		stockIn.setWeight(totalWeight);
		stockIn.setQuantity(totalQuantity);
		stockIn.setAmount(totalQuantity);
		List<StockInDetailDto> addDetailDtos = new ArrayList<StockInDetailDto>();
		List<StockInDetailDto> detailDtos = stockInDto.getStockInDetailDtos();
		for (StockInDetailDto stockInDetailDto : detailDtos) {
			if (StringUtils.isEmpty(stockInDetailDto.getCode())) {
				addDetailDtos.add(stockInDetailDto);
				continue;
			}
			// 修改子单
			StockInDetail detail = stockInDetailService.getByCode(stockInDetailDto.getCode());
			if (stockInDetailDto.getDelete()) {
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
			// 司磅入库,修改司磅记录信息,司磅记录未更改前端不在回传参数
			if (stockIn.getType() == StockInTypeEnum.WEIGHT.getCode() && stockInDetailDto.getStockWeighmanRecordDto() != null) {
				StockWeighmanRecord stockWeighmanRecord = bulidWeighmanRecord(
						stockInDetailDto.getStockWeighmanRecordDto(), detail);
				stockWeighmanRecordService.updateSelective(stockWeighmanRecord);
				detail.setWeightmanId(stockWeighmanRecord.getId());
			}
			//修改动态费用项
			List<BusinessChargeItem> businessChargeItems = stockInDetailDto.getBusinessChargeItems();
			businessChargeItemService.batchUpdateSelective(businessChargeItems);
			/*businessChargeItems.stream().forEach(item -> {
				businessChargeItemService.updateSelective(item);
			});*/

		}
		// 新增子单
		buildStockDetail(addDetailDtos, stockIn);
		// 修改入库单信息
		StockIn domain = new StockIn(userTicket);
		BeanUtil.copyProperties(stockInDto, domain, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
		domain.setWeight(stockIn.getWeight() + totalWeight);
		domain.setQuantity(stockIn.getQuantity() + totalQuantity);
		domain.setAmount(stockIn.getAmount() + totalMoney);
		domain.setVersion(stockIn.getVersion());
		StockIn condition = new StockIn();
		condition.setCode(stockIn.getCode());
		condition.setVersion(stockIn.getVersion());
		updateStockIn(domain, stockIn.getCode(), stockIn.getVersion(), StockInStateEnum.CREATED);
		//动态收费项
		businessChargeItemService.batchUpdateSelective(stockInDto.getBusinessChargeItems());
		//updateSelectiveByExample(domain, condition);
        LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
	}
	
	@Override
	@GlobalTransactional
	public void submit(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if (stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		List<StockInDetail> details = getStockInDetailsByStockCode(code);
		//TODO 司磅入库判断是否已回皮
		if(stockIn.getType() == StockInTypeEnum.WEIGHT.getCode()) {
			List<String> codeList = stockWeighmanRecordService.getNeedWeigh(details.stream().map(StockInDetail::getWeightmanId).collect(Collectors.toList()));
			if(CollectionUtils.isNotEmpty(codeList)) {
				throw new BusinessException(ResultCode.DATA_ERROR, String.format("子单%s待司磅", codeList.toString()));
			}
		}
		// 创建收费单费用收取
		PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket);
		paymentOrder.setBusinessCode(code);
		paymentOrder.setAmount(stockIn.getAmount());
		paymentOrder.setBizType(BizTypeEnum.STOCKIN.getCode());
		paymentOrder.setBusinessId(stockIn.getId());
		paymentOrderService.insertSelective(paymentOrder);
		// 提交入库单
		StockIn domain = new StockIn(userTicket);
		domain.setSubmitterId(userTicket.getId());
		domain.setSubmitter(userTicket.getRealName());
		domain.setPaymentOrderCode(paymentOrder.getCode());
		updateStockIn(domain, code, stockIn.getVersion(), StockInStateEnum.SUBMITTED_PAY);
		// 调用结算接口,缴费
		SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, stockIn, paymentOrder.getCode(), paymentOrder.getAmount());
		settlementRpcResolver.submit(settleOrderDto);
        LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
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
	
	private void updateStockIn(StockIn domain,String code,Integer version,StockInStateEnum state) {
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
		JSONArray details = new JSONArray();
		// 获取父级品类信息
		stockInDetails.forEach(item -> {
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(item);
			// 组装司磅入库信息
			if (StockInTypeEnum.WEIGHT.getCode() == stockIn.getType()) {
				jsonObject.put("stockWeighmanRecord", stockWeighmanRecordService.get(item.getWeightmanId()));
			}
			// 组装动态收费项
			BusinessChargeItem condtion = new BusinessChargeItem();
			condtion.setBusinessCode(item.getCode());
			//jsonObject.put("amount",MoneyUtils.centToYuan(item.getAmount()));
			jsonObject.put("businessChargeItem", businessChargeItemService.list(condtion));
			//
			details.add(jsonObject);
		});
		
		// 结算单信息
		if (stockIn.getState() != StockInStateEnum.CREATED.getCode()
				&& stockIn.getState() != StockInStateEnum.CANCELLED.getCode()) {
			stockInDto.setSettleOrder(settlementRpcResolver.get(settlementAppId, stockIn.getCode()));
		}
		BusinessChargeItem condtion = new BusinessChargeItem();
		condtion.setBusinessCode(stockInDto.getCode());
		stockInDto.setBusinessChargeItems(businessChargeItemService.list(condtion));
		stockInDto.setStockInDetails(stockInDetails);
		stockInDto.setJsonStockInDetailDtos(details);
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
		updateStockIn(domain, code, stockIn.getVersion(), StockInStateEnum.CANCELLED);
        LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);

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
		updateStockIn(domain, code, stockIn.getVersion(), StockInStateEnum.CREATED);
		// 撤销缴费单/结算单
		PaymentOrder paymentOrder = paymentOrderService.getByCode(stockIn.getPaymentOrderCode());
		paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
		paymentOrderService.updateSelective(paymentOrder);
		settlementRpcResolver.cancel(settlementAppId, paymentOrder.getCode());
        LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);

	}

	@Override
	@Transactional
	public void refund(RefundInfoDto refundInfoDto) {
		String code = refundInfoDto.getCode();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.PAID.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		//TODO 判断库存
		List<StockInDetail> details = getStockInDetailsByStockCode(code);
		for (StockInDetail stockInDetail : details) {
			Stock stock = stockService.getStock(stockInDetail.getCategoryId(), stockInDetail.getAssetsId(), stockIn.getCustomerId());
			if(stock.getQuantity() - stockInDetail.getQuantity() < 0) {
				throw new BusinessException(ResultCode.DATA_ERROR, "库存件数小于作废件数,退款失败");
			}
		}
		StockIn domain = new StockIn(userTicket);
		updateStockIn(domain, stockIn.getCode(), stockIn.getVersion(), StockInStateEnum.SUBMITTED_REFUND);
		// 获取结算单
		SettleOrder order = settlementRpcResolver.get(settlementAppId, stockIn.getCode());
		RefundOrder refundOrder = buildRefundOrderDto(userTicket, refundInfoDto, stockIn,order);
		// refundOrderService.doSubmitDispatcher(refundOrder);
        LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);

	}
	
	@Override
	@GlobalTransactional
	public void refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		String code = refundOrder.getBusinessCode();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.SUBMITTED_REFUND.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn domain = new StockIn(settleOrder.getOperatorId(), settleOrder.getOperatorName());
		updateStockIn(domain, code, stockIn.getVersion(), StockInStateEnum.REFUNDED);
		List<StockInDetail> details = getStockInDetailsByStockCode(code);
		details.forEach(detail -> {
			stockService.stockDeduction(detail, stockIn.getCustomerId(), refundOrder.getCode());
		});
        LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), settleOrder.getOperatorId(), settleOrder.getOperatorName(), settleOrder.getMarketId(), null);

	}
	
	@Override
	public String listPageAction(StockInQueryDto stockIn) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		List<Map> name = SessionContext.getSessionContext().dataAuth("department");
		System.err.println(JSON.toJSON(name));
        List<Map> name2 = SessionContext.getSessionContext().dataAuth("market");
        System.err.println(JSON.toJSON(name2));
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
		if (userTicket.getDepartmentId() != null){
            settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
		return settleOrderInfoDto;
	}
	
	private RefundOrder buildRefundOrderDto(UserTicket userTicket, RefundInfoDto refundInfoDto, StockIn stockIn,SettleOrder order) {
		//退款单
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setBusinessCode(stockIn.getCode());
		refundOrder.setBusinessId(stockIn.getId());
		refundOrder.setCustomerId(stockIn.getCustomerId());
		refundOrder.setCustomerName(stockIn.getCustomerName());
		refundOrder.setCustomerCellphone(stockIn.getCustomerCellphone());
		refundOrder.setCertificateNumber("0000");
		refundOrder.setTotalRefundAmount(refundInfoDto.getTotalRefundAmount());
		refundOrder.setPayeeAmount(refundInfoDto.getPayeeAmount());
		refundOrder.setRefundReason(refundInfoDto.getNotes());
		refundOrder.setBizType(BizTypeEnum.STOCKIN.getCode());
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

	@Override
	@GlobalTransactional
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
		domain.setPayDate(LocalDateTime.now());
		domain.setTollman(settleOrder.getOperatorName());
		domain.setTollmanId(settleOrder.getOperatorId());
		updateStockIn(domain, code, stockIn.getVersion(), StockInStateEnum.PAID);
		// 入库 库存
		List<StockInDetail> stockInDetails = getStockInDetailsByStockCode(code);
		stockService.inStock(stockInDetails, stockIn);
        LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), settleOrder.getOperatorId(), settleOrder.getOperatorName(), settleOrder.getMarketId(), null);
	}

	@Override
	public PrintDataDto<StockInPrintDto> receiptPaymentData(String orderCode, String reprint) {
		PaymentOrder paymentOrder = paymentOrderService.getByCode(orderCode);
		if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
			throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付!");
		}
		StockIn stockIn = getStockInByCode(paymentOrder.getBusinessCode());
		StockInPrintDto stockInPrintDto = new StockInPrintDto();
		//stockInPrintDto.set
		stockInPrintDto.setBusinessType(BizTypeEnum.STOCKIN.getCode());
		stockInPrintDto.setCardNo("");
		stockInPrintDto.setCategoryName(stockIn.getCategoryName());
		stockInPrintDto.setCustomerCellphone(stockIn.getCustomerCellphone());
		stockInPrintDto.setCustomerName(stockIn.getCustomerName());
		stockInPrintDto.setDepartmentName(stockIn.getDepartmentName());
		stockInPrintDto.setPrintTime(LocalDateTime.now());
		stockInPrintDto.setReprint(reprint);
		stockInPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
		stockInPrintDto.setSubmitter(stockIn.getSubmitter());
		stockInPrintDto.setReviewer("");
		stockInPrintDto.setTotalAmount(String.valueOf(paymentOrder.getAmount()));
		//详情
		List<StockInPrintItemDto> stockInItems =  new ArrayList<>();;
		List<StockInDetail> stockInDetails = getStockInDetailsByStockCode(stockIn.getCode());
		stockInDetails.forEach(detail -> {
			StockInPrintItemDto stockInPrintItemDto = new StockInPrintItemDto();
			BeanUtil.copyProperties(detail, stockInPrintItemDto);
			stockInPrintItemDto.setExpireDate(stockIn.getExpireDate());
			stockInPrintItemDto.setPayWay("paymentOrder.get");
			stockInPrintItemDto.setProxyPayer("proxyPayer");
			stockInPrintItemDto.setStockInDate(stockIn.getStockInDate());
			stockInPrintItemDto.setStockInType(String.valueOf(stockIn.getType()));
			stockInPrintItemDto.setUnitPrice("unitPrice");
			stockInItems.add(stockInPrintItemDto);
		});
		stockInPrintDto.setStockInItems(stockInItems);
		PrintDataDto<StockInPrintDto> printDataDto = new PrintDataDto<>();
		printDataDto.setName(PrintTemplateEnum.STOCKIN_ORDER.getCode());
		printDataDto.setItem(stockInPrintDto);
		return printDataDto;	
	}
	@Autowired
	private ChargeRuleRpc chargeRuleRpc;
	
	@Override
	public List<QueryFeeOutput> getCost(StockInDto stockInDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		List<QueryFeeInput> queryFeeInputs = new ArrayList<>();
		stockInDto.getBusinessChargeItems().forEach(itme -> {
			QueryFeeInput queryFeeInput =new QueryFeeInput();
			queryFeeInput.setMarketId(userTicket.getFirmId());
			queryFeeInput.setBusinessType(itme.getBizType());
			queryFeeInput.setChargeItem(itme.getChargeItemId());
			Map<String, Object> calcParams = new HashMap<String, Object>();
			calcParams.put("quantity", stockInDto.getQuantity());
			calcParams.put("weight", stockInDto.getWeight());
			// calcParams.put("assetsId", stockInDto.getAssetsId());
			calcParams.put("categoryId", stockInDto.getCategoryId());
			Map<String, Object> conditionParams = new HashMap<String, Object>();
			conditionParams.put("uom", stockInDto.getUom());
			queryFeeInput.setConditionParams(conditionParams);
			queryFeeInput.setCalcParams(calcParams);
			queryFeeInputs.add(queryFeeInput);
		});
		BaseOutput<List<QueryFeeOutput>> batchQueryFee = chargeRuleRpc.batchQueryFee(queryFeeInputs);
		return batchQueryFee.getData();
	}

	@Override
	public void scanStockIn() {
		StockIn condition = new StockIn();
		condition.setState(StockInStateEnum.PAID.getCode());
		condition.setExpireDate(LocalDate.now().atStartOfDay());
		StockIn domain = new StockIn();
		domain.setState(StockInStateEnum.EXPIRE.getCode());
		updateSelectiveByExample(domain, condition);
	}
	
}