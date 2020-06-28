package com.dili.ia.service;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.dto.PayInfoDto;
import com.dili.ia.domain.dto.StockInDto;
import com.dili.ia.domain.dto.StockInQueryDto;
import com.dili.ia.domain.dto.StockInRefundDto;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
public interface StockInService extends BaseService<StockIn, Long> {
	
	/**
	 * 
	 * @Title createStockIn
	 * @Description 入库保存
	 * @param stockInDto
	 * @throws
	 */
	void createStockIn(StockInDto stockInDto);
	
	/**
	 * 
	 * @Title updateStockIn
	 * @Description 修改入库单
	 * @param stockInDto
	 * @throws
	 */
	void updateStockIn(StockInDto stockInDto);
	
	/**
	 * 
	 * @Title submit
	 * @Description 提交入库单
	 * @param code
	 * @throws
	 */
	void submit(String code);
	
	/**
	 * 
	 * @Title pay
	 * @Description 结算入库单
	 * @param payInfoDto 入库单
	 * @throws
	 */
	void pay(PayInfoDto payInfoDto);
	
	/**
	 * 
	 * @Title view
	 * @Description 查看
	 * @param code
	 * @throws
	 */
	StockInDto view(String code);
	
	/**
	 * 
	 * @Title cancel
	 * @Description 取消
	 * @param code
	 * @throws
	 */
	void cancel(String code);
	
	/**
	 * 
	 * @Title remove
	 * @Description 撤回入库单
	 * @param code
	 * @throws
	 */
	void remove(String code);
	
	/**
	 * 
	 * @Title refund
	 * @Description 退款
	 * @param stockInRefundDto
	 * @throws
	 */
	void refund(StockInRefundDto stockInRefundDto);
	
	/**
	 * 
	 * @Title listPageAction
	 * @Description 分页查询
	 * @param stockIn
	 * @return
	 * @throws
	 */
	String listPageAction(StockInQueryDto stockIn);
	
}