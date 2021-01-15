package com.dili.ia.service;

import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.settlement.domain.SettleOrder;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2021年1月15日
 */
public interface BusinessLogService {

	/**
     * 记录退款日志
     *
     * @param refundOrder
     * @param leaseOrder
     */
    default BusinessLog recordRefundLog(RefundOrder refundOrder, String bizType, Long businessId ,String businessCode) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.setBusinessId(businessId);
        businessLog.setBusinessCode(businessCode);
        businessLog.setContent(refundOrder.getSettlementCode());
        businessLog.setOperationType("refund");
        businessLog.setMarketId(refundOrder.getMarketId());
        businessLog.setOperatorId(refundOrder.getRefundOperatorId());
        businessLog.setOperatorName(refundOrder.getRefundOperator());
        businessLog.setBusinessType(bizType);
        businessLog.setSystemCode("IA");
        return businessLog;
    }
    
    /**
     * 记录交费日志
     *
     * @param settleOrder
     * @param leaseOrder
     */
    default BusinessLog recordPayLog(SettleOrder settleOrder, String bizType, Long businessId ,String businessCode) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.setBusinessId(businessId);
        businessLog.setBusinessCode(businessCode);
        businessLog.setContent(settleOrder.getCode());
        businessLog.setOperationType("pay");
        businessLog.setMarketId(settleOrder.getMarketId());
        businessLog.setOperatorId(settleOrder.getOperatorId());
        businessLog.setOperatorName(settleOrder.getOperatorName());
        businessLog.setBusinessType(bizType);
        businessLog.setSystemCode("IA");
        return businessLog;
    }
	
}
