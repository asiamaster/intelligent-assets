package com.dili.ia.service;

import java.util.List;

import com.dili.ia.domain.Stock;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.StockDto;
import com.dili.ia.domain.dto.StockQueryDto;
import com.dili.ia.domain.dto.printDto.StockOutPrintDto;
import com.dili.ss.base.BaseService;
import com.github.pagehelper.Page;

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
	 * @param notes 备注
	 * @throws
	 */
	void stockOut(Long stockId,Long weight,Long quantity,String notes);
	
	/**
	 * 
	 * @Title stockDeduction
	 * @Description 非出库扣减(如退款)
	 * @param stockInDetail
	 * @param customerId
	 * @param weight
	 * @throws
	 */
	void stockDeduction(StockInDetail detail,Long customerId,String businessCode);
	
	/**
	 * 
	 * @Title countCustomerStock
	 * @Description 统计客户库存信息
	 * @param stockQueryDto
	 * @return
	 * @throws
	 */
	Page<List<StockDto>> countCustomerStock(StockQueryDto stockQueryDto);
}