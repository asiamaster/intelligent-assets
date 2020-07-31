package com.dili.ia.service;

import com.dili.ia.domain.Labor;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.LaborDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.glossary.LaborStateEnum;
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
}