package com.dili.ia.mapper;

import java.util.List;
import java.util.Map;

import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.dto.StockInDetailQueryDto;
import com.dili.ss.base.MyMapper;

public interface StockInDetailMapper extends MyMapper<StockInDetail> {
	
	/**
	 * @Title selectByContion
	 * @Description TODO
	 * @throws
	 */
	List<Map<String, String>> selectByContion(StockInDetailQueryDto stockInDetailQueryDto);
}