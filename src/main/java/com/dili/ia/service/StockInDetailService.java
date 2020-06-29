package com.dili.ia.service;

import java.util.List;
import java.util.Map;

import com.dili.ia.domain.StockInDetail;
import com.dili.ia.domain.dto.StockInDetailQueryDto;
import com.dili.ss.base.BaseService;
import com.github.pagehelper.Page;

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
	
	/**
	 * 
	 * @Title selectByContion
	 * @Description 查询详情列表
	 * @param stockInDetailQueryDto
	 * @return
	 * @throws
	 */
	Page<Map<String, String>> selectByContion(StockInDetailQueryDto stockInDetailQueryDto);
	
	/**
	 * 
	 * @Title viewStockInDetail
	 * @Description 查看详情获取入库单,入库单详情,缴费等信息
	 * @param code
	 * @return
	 * @throws
	 */
	Map<String, Object> viewStockInDetail(String code);
	
}