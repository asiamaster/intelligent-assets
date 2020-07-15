package com.dili.ia.service;

import com.dili.ia.domain.StockOut;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.StockOutPrintDto;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
public interface StockOutService extends BaseService<StockOut, Long> {
	
	/**
	 * 
	 * @Title receiptStockOutData
	 * @Description 出库票据打印
	 * @param orderCode
	 * @param reprint
	 * @return
	 * @throws
	 */
	PrintDataDto<StockOutPrintDto> receiptStockOutData(String orderCode, String reprint);
	/**
	 * 
	 * @Title getStockOut
	 * @Description 更具code获取
	 * @param code 业务号
	 * @return
	 * @throws
	 */
	StockOut getStockOut(String code);
}