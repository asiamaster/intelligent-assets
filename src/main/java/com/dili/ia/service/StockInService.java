package com.dili.ia.service;

import java.util.List;
import java.util.Map;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.StockInDetailDto;
import com.dili.ia.domain.dto.StockInDto;
import com.dili.ia.domain.dto.StockInQueryDto;
import com.dili.ia.domain.dto.StockInRefundDto;
import com.dili.ia.domain.dto.printDto.StockInPrintDto;
import com.dili.ia.domain.dto.printDto.StockOutPrintDto;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.settlement.domain.SettleOrder;
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
	 * @Title withdraw
	 * @Description 撤回入库单
	 * @param code
	 * @throws
	 */
	void withdraw(String code);
	
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

	/**
	 * @Title getStockInByCode
	 * @Description 获取入库单
	 * @param code
	 * @return
	 * @throws
	 */
	StockIn getStockInByCode(String code);
	
	/**
	 * 
	 * @Title settlementDealHandler
	 * @Description 冷库结算成功回调
	 * @param settleOrder
	 * @throws
	 */
	void settlementDealHandler(SettleOrder settleOrder);
	
	/**
	 * 
	 * @Title receiptData
	 * @Description 打印入库收款单据
	 * @param orderCode 业务单号
	 * @param reprint 补打标记
	 * @return
	 * @throws
	 */
	PrintDataDto<StockInPrintDto> receiptPaymentData(String orderCode, Integer reprint);
	
	/**
	 * 
	 * @Title refundSuccessHandler
	 * @Description 退款成功回调
	 * @param settleOrder
	 * @param refundOrder
	 * @throws
	 */
	void refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder);

	/**
	 * 
	 * @Title getCost
	 * @Description 通过计费规则算取费用
	 * @param List<QueryFeeOutput>
	 * @return
	 * @throws
	 */
	List<QueryFeeOutput> getCost(StockInDetailDto stockInDetailDto);
}