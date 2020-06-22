package com.dili.ia.service;

import java.util.List;

import com.dili.ia.domain.Stock;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
public interface StockService extends BaseService<Stock, Long> {
	
	/**
	 * 
	 * @Title inStock
	 * @Description 入库
	 * @param details
	 * @param stockIn
	 * @throws
	 */
	void inStock(List<StockInDetail> details,StockIn stockIn);
	
	/**
	 * 
	 * @Title stockOut
	 * @Description 出库
	 * @param stockId 库存id
	 * @param weight 出库重量
	 * @param quantity 出库数量
	 * @throws
	 */
	void stockOut(Long stockId,Long weight,Long quantity);
	
}