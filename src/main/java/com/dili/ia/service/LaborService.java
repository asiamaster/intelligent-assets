package com.dili.ia.service;

import java.util.List;

import com.dili.ia.domain.Labor;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.LaborDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.domain.dto.printDto.LaborPayPrintDto;
import com.dili.ia.domain.dto.printDto.LaborRefundPrintDto;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-27 14:50:45.
 */
public interface LaborService extends BaseService<Labor, Long> {
	
	/**
	 * 
	 * @Title create
	 * @Description 创建劳务马甲单
	 * @param laborDto
	 * @throws
	 */
	void create(LaborDto laborDto); 
	
	/**
	 * 
	 * @Title getLabor
	 * @Description 获取劳务马甲单信息
	 * @param code
	 * @throws
	 */
	LaborDto getLabor(String code);
	
	/**
	 * 
	 * @Title update
	 * @Description 修改劳务马甲单
	 * @param laborDto
	 * @throws
	 */
	void update(LaborDto laborDto);
	
	/**
	 * 
	 * @Title cancel
	 * @Description 取消劳务马甲单
	 * @param code
	 * @throws
	 */
	void cancel(String code);
	
	/**
	 * 
	 * @Title submit
	 * @Description 提交付款
	 * @param code
	 * @throws
	 */
	void submit(String code);
	
	/**
	 * 
	 * @Title withdraw
	 * @Description 撤回劳务马甲单
	 * @param code
	 * @throws
	 */
	void withdraw(String code);
	
	/**
	 * 
	 * @Title Renew
	 * @Description 续费
	 * @throws
	 */
	void renew(LaborDto laborDto);
	
	/**
	 * 
	 * @Title rename
	 * @Description 更名
	 * @param laborDto
	 * @throws
	 */
	void rename(LaborDto laborDto);
	
	/**
	 * 
	 * @Title remodel
	 * @Description 修改型号
	 * @param laborDto
	 * @throws
	 */
	void remodel(LaborDto laborDto);
	
	/**
	 * 
	 * @Title refund
	 * @Description 退款申请
	 * @throws
	 */
	void refund(RefundInfoDto refundInfoDto);
	
	/**
	 * 
	 * @Title cancleRefund
	 * @Description 取消退款申请
	 * @throws
	 */
	void cancleRefund(RefundOrder refundOrder);
	
	/**
	 * 
	 * @Title refundSubmitHandler
	 * @Description 退款申请回调
	 * @param refundOrder
	 * @throws
	 */
	void refundSubmitHandler(RefundOrder refundOrder);
	
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
	 * @Title settlementDealHandler
	 * @Description 结算成功回调
	 * @param settleOrder
	 * @throws
	 */
	void settlementDealHandler(SettleOrder settleOrder);
	
	/**
	 * 
	 * @Title scanLaborVest
	 * @Description 扫描过期,待生效单
	 * @throws
	 */
	void scanLaborVest();
	
	/**
	 * 
	 * @Title receiptPaymentData
	 * @Description 马甲办理收款票据打印
	 * @param orderCode
	 * @param reprint
	 * @return
	 * @throws
	 */
	PrintDataDto<LaborPayPrintDto> receiptPaymentData(String orderCode, String reprint);
	
	/**
	 * 
	 * @Title receiptRefundPrintData
	 * @Description 退款单打印
	 * @param orderCode
	 * @param reprint
	 * @return
	 * @throws
	 */
	PrintDataDto<LaborRefundPrintDto> receiptRefundPrintData(String orderCode, String reprint);
	
	/**
	 * 
	 * @Title getCost
	 * @Description 获取动态收费项
	 * @param laborDto
	 * @return
	 * @throws
	 */
	List<QueryFeeOutput> getCost(LaborDto laborDto);
}