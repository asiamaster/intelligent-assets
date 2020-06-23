package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.JSONUtils;
import com.dili.ia.domain.Stock;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.StockRecord;
import com.dili.ia.domain.StockWeighmanRecord;
import com.dili.ia.domain.dto.StockInDetailDto;
import com.dili.ia.domain.dto.StockInDto;
import com.dili.ia.domain.dto.StockWeighmanRecordDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.glossary.StockInTypeEnum;
import com.dili.ia.glossary.StockRecordTypeEnum;
import com.dili.ia.mapper.StockInMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.StockInDetailService;
import com.dili.ia.service.StockInService;
import com.dili.ia.service.StockService;
import com.dili.ia.service.StockWeighmanRecordService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	}
	
	private StockIn buildStockIn(StockInDto stockInDto,UserTicket userTicket) {
		StockIn stockIn = new StockIn();
		BeanUtils.copyProperties(stockInDto, stockIn);
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
			//totalMoney += stockInDetailDto.getReceivable();
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
		// stockIn.set(totalMoney);
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
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "入库单状态已变更不能提交");
		}
		getStockInDetailsByStockCode(code);
		//提交入库单
		updateState(code, stockIn.getVersion(), StockInStateEnum.SUBMITTED);
		//TODO 创建收费单费用收取
		//
	}

	@Override
	@Transactional
	public void pay(String code) {
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.SUBMITTED.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "入库单状态已改变不能结算");
		}
		//缴费
		updateState(code, stockIn.getVersion(), StockInStateEnum.PAID);
		//TODO 调用收费接口
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
	
	private void updateState(String code,Integer version,StockInStateEnum state) {
		StockIn domain = new StockIn();
		//TODO 提交人是否记录到该表
		domain.setState(state.getCode());
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
		// TODO Auto-generated method stub
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
		for (StockInDetailDto stockInDetailDto : detailDtos) {
			if(StringUtils.isEmpty(stockInDetailDto.getCode())) {
				buildStockDetail(stockInDto, stockIn);
			}else {
				StockInDetail detail = stockInDetailService.getByCode(stockInDetailDto.getCode());
				BeanUtils.copyProperties(stockInDetailDto, detail);
				totalWeight += stockInDetailDto.getWeight();
				totalQuantity += stockInDetailDto.getQuantity();
				totalMoney += stockInDetailDto.getReceivable();
				detailList.add(detail);	
			}
		}
		stockIn.setWeight(stockIn.getWeight()+totalWeight);
		stockIn.setQuantity(stockIn.getQuantity()+totalQuantity);
		StockIn condition = new StockIn();
		condition.setCode(stockIn.getCode());
		condition.setVersion(stockIn.getVersion());
		updateByExample(stockIn, condition);
	}

	@Override
	@Transactional
	public void cancel(String code) {
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException("", "");
		}
		updateState(code, stockIn.getVersion(), StockInStateEnum.CANCELLED);
	}

	@Override
	@Transactional
	public void remove(String code) {
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.SUBMITTED.getCode()) {
			throw new BusinessException("", "");
		}
		updateState(code, stockIn.getVersion(), StockInStateEnum.CREATED);
		
	}

	@Override
	@Transactional
	public void refund(String code) {
		StockIn stockIn = getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.PAID.getCode()) {
			throw new BusinessException("", "");
		}
		updateState(code, stockIn.getVersion(), StockInStateEnum.CANCELLED);
		List<StockInDetail> details = getStockInDetailsByStockCode(code);
		details.forEach(detail -> {
			stockService.stockDeduction(detail, stockIn.getCustomerId(), "退款businessCode");
		});
	}
	
	
	
}