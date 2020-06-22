package com.dili.ia.service.impl;

import com.dili.ia.domain.Stock;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.StockRecord;
import com.dili.ia.mapper.StockMapper;
import com.dili.ia.service.StockService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockServiceImpl extends BaseServiceImpl<Stock, Long> implements StockService {

	@Autowired
	private StockRecordServiceImpl stockRecordServiceImpl;
	
    public StockMapper getActualDao() {
        return (StockMapper)getDao();
    }

	@Override
	public void inStock(List<StockInDetail> details,StockIn stockIn) {
		// TODO Auto-generated method stub
		if(CollectionUtils.isEmpty(details)) {
			return;
		}
		for (StockInDetail stockInDetail : details) {
			//根据customerId categoryId assetsId 获取唯一库存信息
			Stock stock = getStock(stockInDetail.getCategoryId(), stockInDetail.getAssetsId(), stockIn.getCustomerId());
			if(stock == null) {
				//初始库存
				buildStock(stockInDetail, stockIn);
				//重试获取库存
				stock = getStock(stockInDetail.getCategoryId(), stockInDetail.getAssetsId(), stockIn.getCustomerId());
			}
			if (stock == null) {
				throw new BusinessException(ResultCode.DATA_ERROR, "数据异常");
			}
			//库存入库
			Stock domain = new Stock();
			domain.setWeight(stock.getWeight()+stockInDetail.getWeight());
			domain.setQuantity(stock.getQuantity()+stockInDetail.getQuantity());
			domain.setVersion(stock.getVersion()+1);
			Stock condition = new Stock();
			condition.setCategoryId(stock.getCategoryId());
			condition.setAssetsId(stock.getAssetsId());
			condition.setCustomerId(stock.getCustomerId());
			condition.setVersion(stock.getVersion());
			int row = updateSelectiveByExample(domain, condition);
			if(row != 1) {
				throw new BusinessException(ResultCode.DATA_ERROR, "入库失败");
			}
			//入库流水记录
			buildStockRecord(stockInDetail.getWeight(), stockInDetail.getQuantity(), stockInDetail.getCode(), stock);
		}
		
	}
	
	private Stock getStock(Long categoryId,Long assetsId,Long customerId) {
		Stock example = new Stock();
		example.setCategoryId(categoryId);
		example.setAssetsId(assetsId);
		example.setCustomerId(customerId);
		List<Stock> stocks = listByExample(example);
		if(CollectionUtils.isEmpty(stocks)) {
			return null;		
		}
		if(stocks.size() != 1) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据异常");	
		}
		return stocks.get(0);
	}
	
	private void buildStock(StockInDetail stockInDetail,StockIn stockIn) {
		Stock domain = new Stock();
		domain.setWeight(0L);
		domain.setQuantity(0L);
		/*BeanUtils.copyProperties(stockInDetail, domain);
		BeanUtils.copyProperties(stockIn, domain);*/
		domain.setCategoryId(stockInDetail.getCategoryId());
		domain.setAssetsId(stockInDetail.getAssetsId());
		domain.setCustomerId(stockIn.getCustomerId());
		domain.setDistrictId(stockInDetail.getDistrictId());
		domain.setAssetsName(stockInDetail.getAssetsCode());
		domain.setCategoryName(stockInDetail.getCategoryName());
		domain.setCustomerName(stockIn.getCustomerName());	
		domain.setDistrictName(stockInDetail.getDistrictName());
		domain.setMarketId(stockIn.getMarketId());
		domain.setMarketCode(stockIn.getMarketCode());
		domain.setVersion(1);
		int row = insertSelective(domain);
		if(row != 1) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据异常");
		}
	}
	
	private void buildStockRecord(Long weight,Long quantity,String businessCode,Stock stock) {
		StockRecord record = new StockRecord();
		record.setWeight(weight);
		record.setQuantity(quantity);
		record.setBusinessCode(businessCode);
		record.setStockId(stock.getId());
		record.setMarketId(stock.getMarketId());
		record.setMarketCode(stock.getMarketCode());
		stockRecordServiceImpl.insertSelective(record);		
	}

	@Override
	public void stockOut(Long stockId, Long weight, Long quantity) {
		// TODO Auto-generated method stub
		// Stock stock = get(key)
	}
	
}