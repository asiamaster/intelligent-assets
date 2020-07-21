package com.dili.ia.service.impl;

import com.dili.ia.domain.StockWeighmanRecord;
import com.dili.ia.mapper.StockWeighmanRecordMapper;
import com.dili.ia.service.StockWeighmanRecordService;
import com.dili.ss.base.BaseServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockWeighmanRecordServiceImpl extends BaseServiceImpl<StockWeighmanRecord, Long> implements StockWeighmanRecordService {

    public StockWeighmanRecordMapper getActualDao() {
        return (StockWeighmanRecordMapper)getDao();
    }
   
	@Override
	public List<String> getNeedWeigh(List<Long> ids) {
		return getActualDao().getNeedWeigh(ids);
	}
}