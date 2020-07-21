package com.dili.ia.mapper;

import java.util.List;

import com.dili.ia.domain.StockWeighmanRecord;
import com.dili.ss.base.MyMapper;

public interface StockWeighmanRecordMapper extends MyMapper<StockWeighmanRecord> {
	
	/**
	 * 
	 * @Title getNeedWeigh
	 * @Description 根据入库子单判断是否司磅
	 * @param ids
	 * @return
	 * @throws
	 */
	List<String> getNeedWeigh(List<Long> ids);
}