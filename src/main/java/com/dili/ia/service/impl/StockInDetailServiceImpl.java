package com.dili.ia.service.impl;

import com.dili.ia.domain.StockInDetail;
import com.dili.ia.mapper.StockInDetailMapper;
import com.dili.ia.service.StockInDetailService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.exception.BusinessException;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockInDetailServiceImpl extends BaseServiceImpl<StockInDetail, Long> implements StockInDetailService {

    public StockInDetailMapper getActualDao() {
        return (StockInDetailMapper)getDao();
    }

	@Override
	public StockInDetail getByCode(String code) {
		StockInDetail condtion = new StockInDetail();
		List<StockInDetail> stockInDetails = listByExample(condtion);
		if (CollectionUtils.isEmpty(stockInDetails) || stockInDetails.size() != 1) {
			throw new BusinessException("", "");
		}
		return stockInDetails.get(0);
	}
}