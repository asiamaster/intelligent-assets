package com.dili.ia.service;

import com.dili.ia.domain.RefundOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
public interface RefundOrderService extends BaseService<RefundOrder, Long> {

    /**
     * 退款单 --提交
     * @param refundOrderId 退款单ID
     * @return BaseOutput
     * */
    BaseOutput submitRefundOrder(Long refundOrderId);
    /**
     * 退款单 --撤回
     * @param refundOrderId 退款单ID
     * @return BaseOutput
     * */
    BaseOutput withdrawRefundOrder(Long refundOrderId);

    /**
     * 退款单 --退款成功回调
     * @param refundOrderId 退款单ID
     * @return BaseOutput
     * */
    BaseOutput refundSuccessOrder(Long refundOrderId);

}