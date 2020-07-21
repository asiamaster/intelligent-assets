package com.dili.ia.service;

import java.util.List;

import com.dili.ia.domain.StockWeighmanRecord;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
public interface StockWeighmanRecordService extends BaseService<StockWeighmanRecord, Long> {
	
	/**
	 * 
	 * @Title getNeedWeigh
	 * @Description 判断司磅是否完成皮重,毛重
	 * @return
	 * @throws
	 */
	 List<String> getNeedWeigh(List<Long> ids);
}