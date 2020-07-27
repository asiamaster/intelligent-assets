package com.dili.ia.service.impl;

import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.dto.StockInDetailQueryDto;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.mapper.StockInDetailMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.StockInDetailService;
import com.dili.ia.service.StockInService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockInDetailServiceImpl extends BaseServiceImpl<StockInDetail, Long> implements StockInDetailService {

	@Value("${settlement.app-id}")
    private Long settlementAppId;
	
	@Autowired
	private StockInService stockInService;
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@Autowired
	private SettlementRpcResolver settlementRpcResolver;
	
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
		Page<Map<String, String>> result = PageHelper.startPage(stockInDetailQueryDto.getPage(), stockInDetailQueryDto.getRows());
		if(stockInDetailQueryDto.getExpireDay() != null) {
			stockInDetailQueryDto.setExpireDate(LocalDate.now().plusDays(stockInDetailQueryDto.getExpireDay()));
		}
		getActualDao().selectByContion(stockInDetailQueryDto);;
		return result;
	}

	@Override
	public Map<String, Object> viewStockInDetail(String code) {
		StockInDetail stockInDetail = getByCode(code);
		StockIn stockIn = stockInService.getStockInByCode(stockInDetail.getStockInCode());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("stockInDetail", stockInDetail);
		result.put("stockIn", stockIn);
		// 结算单信息
		if (stockIn.getState() != StockInStateEnum.CREATED.getCode()
				&& stockIn.getState() != StockInStateEnum.CANCELLED.getCode()) {
			result.put("settleOrder",settlementRpcResolver.get(settlementAppId, stockIn.getPaymentOrderCode()));
		}

		// TODO 缴费单
		return result;
	}
}