package com.dili.ia.service.impl;

import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.dto.StockInDetailQueryDto;
import com.dili.ia.mapper.StockInDetailMapper;
import com.dili.ia.service.StockInDetailService;
import com.dili.ia.service.StockInService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockInDetailServiceImpl extends BaseServiceImpl<StockInDetail, Long> implements StockInDetailService {

	@Autowired
	private StockInService stockInService;
	
    public StockInDetailMapper getActualDao() {
        return (StockInDetailMapper)getDao();
    }

	@Override
	public StockInDetail getByCode(String code) {
		StockInDetail condtion = new StockInDetail();
		condtion.setCode(code);
		List<StockInDetail> stockInDetails = listByExample(condtion);
		if (CollectionUtils.isEmpty(stockInDetails) || stockInDetails.size() != 1) {
			throw new BusinessException("", "");
		}
		return stockInDetails.get(0);
	}

	@Override
	public Page<Map<String, String>> selectByContion(StockInDetailQueryDto stockInDetailQueryDto) {
		Page<Map<String, String>> result = PageHelper.startPage(1, 5);
		getActualDao().selectByContion(stockInDetailQueryDto);
		return result;
	}

	@Override
	public Map<String, Object> viewStockInDetail(String code) {
		StockInDetail stockInDetail = getByCode(code);
		StockIn stockIn = stockInService.getStockInByCode(stockInDetail.getStockInCode());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("stockInDetail", stockInDetail);
		result.put("stockIn", stockIn);
		// TODO 缴费单
		return result;
	}
}