package com.dili.ia.service;

import com.dili.ia.domain.RefundOrder;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
public interface RefundOrderDispatcherService {
    /**
     * 退款单 --提交
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput doSubmitDispatcher(RefundOrder refundOrder);
    /**
     * 退款单 --撤回
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput doWithdrawDispatcher(RefundOrder refundOrder);

    /**
     * 退款单 --撤回
     * @param bizType 业务类型
     * @param refundOrderId 退款单Id
     * @return BaseOutput
     * */
    BaseOutput doRefundSuccessHandlerDispatcher(Integer bizType, Long refundOrderId);
}
