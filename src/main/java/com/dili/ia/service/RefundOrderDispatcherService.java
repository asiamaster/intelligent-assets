package com.dili.ia.service;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.TransactionItemTypeEnum;
import com.dili.ia.glossary.TransactionSceneTypeEnum;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.SpringUtil;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2020-03-09 19:34:40.
 */
public interface RefundOrderDispatcherService extends BaseService<RefundOrder, Long> {
	SettlementRpc settlementRpc = SpringUtil.getBean(SettlementRpc.class);
	DepartmentRpc departmentRpc = SpringUtil.getBean(DepartmentRpc.class);
	CustomerAccountService customerAccountService = SpringUtil.getBean(CustomerAccountService.class);
	TransactionDetailsService transactionDetailsService = SpringUtil.getBean(TransactionDetailsService.class);

	default BaseOutput defaultSubmitHandler(RefundOrder refundOrder, BizTypeEnum biz) {
		// 冻结客户资金，写入冻结记录
		customerAccountService.frozenEarnest(refundOrder.getCustomerId(), refundOrder.getMarketId(),
				refundOrder.getPayeeAmount());
		String bizType = biz.getCode();
		Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
		Integer sceneType = TransactionSceneTypeEnum.FROZEN.getCode();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		TransactionDetails transactionDetails = transactionDetailsService.buildByConditions(sceneType, bizType,
				itemType, refundOrder.getPayeeAmount(), refundOrder.getBusinessId(), refundOrder.getBusinessCode(),
				refundOrder.getCustomerId(), refundOrder.getRefundReason(), refundOrder.getMarketId(),
				userTicket.getId(), userTicket.getRealName());
		transactionDetailsService.insertSelective(transactionDetails);
		return BaseOutput.success();
	}

	default BaseOutput defaultWithdrawHandler(RefundOrder refundOrder, BizTypeEnum biz) {
		// 解冻客户资金，写入解冻记录
		customerAccountService.unfrozenEarnest(refundOrder.getCustomerId(), refundOrder.getMarketId(),
				refundOrder.getPayeeAmount());
		String bizType = biz.getCode();
		Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
		Integer sceneType = TransactionSceneTypeEnum.UNFROZEN.getCode();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		TransactionDetails transactionDetails = transactionDetailsService.buildByConditions(sceneType, bizType,
				itemType, refundOrder.getPayeeAmount(), refundOrder.getBusinessId(), refundOrder.getBusinessCode(),
				refundOrder.getCustomerId(), refundOrder.getRefundReason(), refundOrder.getMarketId(),
				userTicket.getId(), userTicket.getRealName());
		transactionDetailsService.insertSelective(transactionDetails);
		return BaseOutput.success();
	}

	default BaseOutput defaultRefundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder, BizTypeEnum biz) {
		// 解冻客户资金，扣除客户余额，写入解冻，扣除记录记录
		customerAccountService.refundSuccessEarnest(refundOrder.getCustomerId(), refundOrder.getMarketId(),
				refundOrder.getPayeeAmount());
		String bizType = biz.getCode();
		Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
		Integer sceneTypeUnfrozen = TransactionSceneTypeEnum.UNFROZEN.getCode();
		Integer sceneTypeRefund = TransactionSceneTypeEnum.REFUND.getCode();
		TransactionDetails unfrozenDetails = transactionDetailsService.buildByConditions(sceneTypeUnfrozen, bizType,
				itemType, refundOrder.getPayeeAmount(), refundOrder.getBusinessId(), refundOrder.getBusinessCode(),
				refundOrder.getCustomerId(), refundOrder.getRefundReason(), refundOrder.getMarketId(),
				settleOrder.getOperatorId(), settleOrder.getOperatorName());
		TransactionDetails refundDetails = transactionDetailsService.buildByConditions(sceneTypeRefund, bizType,
				itemType, refundOrder.getPayeeAmount(), refundOrder.getBusinessId(), refundOrder.getBusinessCode(),
				refundOrder.getCustomerId(), refundOrder.getRefundReason(), refundOrder.getMarketId(),
				settleOrder.getOperatorId(), settleOrder.getOperatorName());
		transactionDetailsService.insertSelective(unfrozenDetails);
		transactionDetailsService.insertSelective(refundDetails);
		return BaseOutput.success();
	}

	/**
	 * 退款单 --修改
	 * 
	 * @param refundOrder 退款单
	 * @return BaseOutput
	 */
	default BaseOutput updateHandler(RefundOrder refundOrder) {
		return BaseOutput.success();
	}

	/**
	 * 退款单 --提交
	 * 
	 * @param refundOrder 退款单
	 * @return BaseOutput
	 */
	BaseOutput submitHandler(RefundOrder refundOrder);

	/**
	 * 退款单 --撤回
	 * 
	 * @param refundOrder 退款单
	 * @return BaseOutput
	 */
	BaseOutput withdrawHandler(RefundOrder refundOrder);

	/**
	 * 退款单 --退款成功回调
	 * 
	 * @param settleOrder 退款结算单
	 * @param refundOrder 退款单
	 * @return BaseOutput
	 */
	BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder);

	/**
	 * 退款单 --取消
	 * 
	 * @param refundOrder 退款单
	 * @return BaseOutput
	 */
	BaseOutput cancelHandler(RefundOrder refundOrder);

	/**
	 * 票据打印 --- 业务数据加载
	 * 
	 * @param refundOrder 退款单
	 * @return BaseOutput<RefundOrderPrintDto>
	 */
	BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder);

	/**
	 * 退款单 --获取业务类型
	 */
	Set<String> getBizType();
}
