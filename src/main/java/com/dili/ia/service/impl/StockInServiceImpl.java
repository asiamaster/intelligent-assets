package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.StockWeighmanRecord;
import com.dili.ia.domain.dto.PayInfoDto;
import com.dili.ia.domain.dto.StockInDetailDto;
import com.dili.ia.domain.dto.StockInDto;
import com.dili.ia.domain.dto.StockWeighmanRecordDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.glossary.StockInTypeEnum;
import com.dili.ia.mapper.StockInMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.StockInDetailService;
import com.dili.ia.service.StockInService;
import com.dili.ia.service.StockService;
import com.dili.ia.service.StockWeighmanRecordService;
import com.dili.ia.util.LoggerUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockInServiceImpl extends BaseServiceImpl<StockIn, Long> implements StockInService {

	@Autowired
	private UidRpcResolver UidRpcResolver;
	
	@Autowired 
	private StockInDetailService stockInDetailService;
	
	@Autowired
	private StockWeighmanRecordService stockWeighmanRecordService;
	
	@Autowired
	private StockService stockService;
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	
    public StockInMapper getActualDao() {
        return (StockInMapper)getDao();
    }

	@Override
	@Transactional
	public void createStockIn(StockInDto stockInDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = buildStockIn(stockInDto, userTicket);
		List<StockInDetail> detailList = buildStockDetail(stockInDto, stockIn);
		insertSelective(stockIn);
		stockInDetailService.batchInsert(detailList);
		LoggerUtil.buildLoggerContext(stockIn.getId(), stockIn.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
	}
	
	private StockIn buildStockIn(StockInDto stockInDto,UserTicket userTicket) {
		StockIn stockIn = new StockIn(userTicket);
		BeanUtils.copyProperties(stockInDto, stockIn);
		stockIn.setCode(UidRpcResolver.bizNumber(userTicket.getFirmCode()+"_"+BizNumberTypeEnum.STOCK_IN_CODE.getCode()));
		stockIn.setCreateTime(new Date());
		stockIn.setState(StockInStateEnum.CREATED.getCode());
		stockIn.setCreator(userTicket.getUserName());
		stockIn.setCreatorId(userTicket.getId());
		stockIn.setMarketId(userTicket.getFirmId());
		stockIn.setMarketCode(userTicket.getFirmCode());
		stockIn.setVersion(1);
		return stockIn;
	}
	private List<StockInDetail> buildStockDetail(StockInDto stockInDto,StockIn stockIn) {
		List<StockInDetailDto> detailDtos = stockInDto.getStockInDetailDtos();
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
			detail.setVersion(1);
			totalWeight += stockInDetailDto.getWeight();
			totalQuantity += stockInDetailDto.getQuantity();
			totalMoney += stockInDetailDto.getAmount();
			// 司磅入库,保存司磅记录信息
			if (stockInDto.getType() == StockInTypeEnum.WEIGHT.getCode()) {
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
	public void submit(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		getStockInDetailsByStockCode(code);
		//提交入库单
		StockIn domain = new StockIn(userTicket);
		domain.setSubmitterId(userTicket.getId());
		domain.setSubmitter(userTicket.getUserName());
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.SUBMITTED);
		//创建收费单费用收取
		//TODO 创建收费单(支付生成or提交生成??)
		PayInfoDto payInfoDto = new PayInfoDto();
		payInfoDto.setBusinessCode(code);
		payInfoDto.setPayMoney(stockIn.getAmount());
		PaymentOrder paymentOrder = paymentOrderService.savePaymentOrder(userTicket, payInfoDto);
		//
	}

	@Override
	@Transactional
	public void pay(PayInfoDto payInfoDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		String code = payInfoDto.getBusinessCode();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.SUBMITTED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		
		//TODO 调用收费接口,缴费
		
		StockIn domain = new StockIn(userTicket);
		
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.PAID);
		//入库 库存
		List<StockInDetail> stockInDetails = getStockInDetailsByStockCode(code);
		stockService.inStock(stockInDetails, stockIn);
		
	}
	
	private StockIn getStockInByCode(String code) {
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
	public void updateStockIn(StockInDto stockInDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(stockInDto.getCode());
		BeanUtils.copyProperties(stockInDto, stockIn);
		List<StockInDetailDto> detailDtos = stockInDto.getStockInDetailDtos();
		List<StockInDetail> detailList = new ArrayList<>();
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
		for (StockInDetailDto stockInDetailDto : detailDtos) {
			if(StringUtils.isEmpty(stockInDetailDto.getCode())) {
				buildStockDetail(stockInDto, stockIn);
			}else {
				StockInDetail detail = stockInDetailService.getByCode(stockInDetailDto.getCode());
				BeanUtils.copyProperties(stockInDetailDto, detail);
				totalWeight += stockInDetailDto.getWeight();
				totalQuantity += stockInDetailDto.getQuantity();
				totalMoney += stockInDetailDto.getAmount();
				detailList.add(detail);	
			}
		}
		stockIn.setWeight(stockIn.getWeight()+totalWeight);
		stockIn.setQuantity(stockIn.getQuantity()+totalQuantity);
		stockIn.setAmount(totalMoney);
		StockIn condition = new StockIn(userTicket);
		condition.setCode(stockIn.getCode());
		condition.setVersion(stockIn.getVersion());
		updateByExample(stockIn, condition);
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
		domain.setCanceler(userTicket.getUserName());
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.CANCELLED);
	}

	@Override
	@Transactional
	public void remove(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.SUBMITTED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn domain = new StockIn(userTicket);
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.CREATED);
		
	}

	@Override
	@Transactional
	public void refund(String code) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.PAID.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn domain = new StockIn(userTicket);
		domain.setWithdrawOperator(userTicket.getUserName());
		domain.setWithdrawOperatorId(userTicket.getId());
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.CANCELLED);
		List<StockInDetail> details = getStockInDetailsByStockCode(code);
		details.forEach(detail -> {
			stockService.stockDeduction(detail, stockIn.getCustomerId(), "退款businessCode");
		});
	}

	
}