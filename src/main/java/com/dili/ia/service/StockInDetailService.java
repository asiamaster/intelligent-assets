package com.dili.ia.service;

import com.dili.ia.domain.StockInDetail;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
public interface StockInDetailService extends BaseService<StockInDetail, Long> {
	
	/**
	 * 
	 * @Title getByCode
	 * @Description 根据code获取详情单
	 * @param code
	 * @return
	 * @throws
	 */
	StockInDetail getByCode(String code);
	
}