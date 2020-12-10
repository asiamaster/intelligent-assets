package com.dili.ia.service;

import java.util.List;

import com.dili.ia.domain.MessageFee;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.MessageFeeDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.domain.dto.printDto.MessageFeePayPrintDto;
import com.dili.ia.domain.dto.printDto.MessageFeeRefundPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-08-24 16:16:50.
 */
public interface MessageFeeService extends BaseService<MessageFee, Long> {
	
	/**
	 * @Title create
	 * @Description 创建
	 * @throws
	 */
	void create(MessageFeeDto messageFeeDto);
	
	/**
	 * @Title update
	 * @Description 修改
	 * @throws
	 */
	void update(MessageFeeDto messageFeeDto);
	
	/**
	 * 
	 * @Title getMessageFeeByCode
	 * @Description 获取
	 */
	MessageFee getMessageFeeByCode(String code);
	
	/**
	 * @Title submit
	 * @Description 提交
	 * @param code
	 * @throws
	 */
	void submit(String code);
	
	/**
	 * @Title cancel
	 * @Description 取消
	 * @param code
	 * @throws
	 */
	void cancel(String code);
	
	/**
	 * @Title withdraw
	 * @Description 撤回
	 * @param code
	 * @throws
	 */
	void withdraw(String code);
	
	/**
	 * @Title view
	 * @Description 查看
	 * @param code
	 * @throws
	 */
	MessageFeeDto view(String code);
	
	/**
	 * @Title refund
	 * @Description 退款申请
	 * @param refundInfoDto
	 * @throws
	 */
	void refund(RefundInfoDto refundInfoDto);

	/**
	 * @Title refundSuccessHandler
	 * @Description 退款成功回调
	 * @param settleOrder
	 * @param refundOrder
	 * @throws
	 */
	void refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder);
	
	/**
	 * @Title settlementDealHandler
	 * @Description 结算成功回调
	 * @param settleOrder
	 * @throws
	 */
	void settlementDealHandler(SettleOrder settleOrder);
	
	/**
	 * @Title cancleRefund
	 * @Description 取消退款单
	 * @param refundOrder
	 * @throws
	 */
	void cancleRefund(RefundOrder refundOrder);
	
	/**
	 * @Title getCost
	 * @Description 获取动态收费项
	 * @param messageFeeDto
	 * @throws
	 */
	List<QueryFeeOutput> getCost(MessageFeeDto messageFeeDto);
	
	/**
	 * @Title syncState
	 * @Description 同步消息系统
	 * @param code
	 * @param syncAction 1-添加白名单,2-取消白名单
	 * @throws
	 */
	void syncState(String code,Integer syncAction);
	
	/**
	 * 
	 * @Title scanEffective
	 * @Description 定时扫描过期信息单
	 * @throws
	 */
	void scanEffective();
	
	/**
	 * 
	 * @Title receiptPaymentData
	 * @Description 信息费缴费票据打印
	 * @throws
	 */
	PrintDataDto<MessageFeePayPrintDto> receiptPaymentData(String orderCode, String reprint);
	
	/**
	 * 
	 * @Title receiptRefundPrintData
	 * @Description 信息系费退款票据打印
	 * @param orderCode
	 * @param reprint
	 * @throws
	 */
	PrintDataDto<MessageFeeRefundPrintDto> receiptRefundPrintData(String orderCode, String reprint);
}