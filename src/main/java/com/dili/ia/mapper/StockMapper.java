package com.dili.ia.mapper;

import java.util.List;

import com.dili.ia.domain.Stock;
import com.dili.ia.domain.dto.StockDto;
import com.dili.ia.domain.dto.StockQueryDto;
import com.dili.ss.base.MyMapper;

public interface StockMapper extends MyMapper<Stock> {
	
	List<StockDto> countCustomerStock(StockQueryDto stockQueryDto);
	
}