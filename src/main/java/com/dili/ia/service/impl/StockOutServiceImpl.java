package com.dili.ia.service.impl;

import com.dili.ia.domain.StockOut;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.StockOutPrintDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.StockOutMapper;
import com.dili.ia.service.StockOutService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;

import cn.hutool.core.bean.BeanUtil;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockOutServiceImpl extends BaseServiceImpl<StockOut, Long> implements StockOutService {

    public StockOutMapper getActualDao() {
        return (StockOutMapper)getDao();
    }

	@Override
	public PrintDataDto<StockOutPrintDto> receiptStockOutData(String orderCode, String reprint) {
		StockOut stockOut = getStockOut(orderCode);
		StockOutPrintDto stockOutPrintDto = new StockOutPrintDto();
		BeanUtil.copyProperties(stockOut, stockOutPrintDto);
		stockOutPrintDto.setStockOutCode(orderCode);
		//stockOutPrintDto.set
		stockOutPrintDto.setDistrictName(stockOut.getDistrictName()+"-"+stockOut.getAssetsName());
		stockOutPrintDto.setReprint(reprint);
		stockOutPrintDto.setPrintTime(LocalDateTime.now());
		stockOutPrintDto.setBusinessType(BizTypeEnum.STOCKOUT.getCode());
		stockOutPrintDto.setSubmitter(stockOut.getCreator());
		stockOutPrintDto.setNotes("备注:"+(StringUtils.isEmpty(stockOut.getNotes())?"":stockOut.getNotes()));
		stockOutPrintDto.setCustomerName(stockOut.getCustomerName());
		PrintDataDto<StockOutPrintDto> printDataDto = new PrintDataDto<>();
		printDataDto.setItem(stockOutPrintDto);
		printDataDto.setName(PrintTemplateEnum.STOCK_OUT_ORDER.getCode());
		return printDataDto;
	}

	@Override
	public StockOut getStockOut(String code) {
		StockOut example = new StockOut();
		example.setCode(code);
		List<StockOut> list = this.listByExample(example);
		if(CollectionUtils.isEmpty(list)) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据异常");	
		}
		return list.get(0);
	}
}