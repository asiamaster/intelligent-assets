package com.dili.ia.service;

import com.dili.ia.domain.RefundOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
public interface RefundOrderService extends BaseService<RefundOrder, Long> {

    /**
     * 退款单 --提交
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput submitRefundOrder(RefundOrder refundOrder);
    /**
     * 退款单 --撤回
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput withdrawRefundOrder(RefundOrder refundOrder);

    /**
     * 退款单 --退款成功回调
     * @param refundOrderId 退款单Id
     * @return BaseOutput
     * */
    BaseOutput refundSuccessHandler(Long refundOrderId);
    /**
     * 退款单 --获取业务类型
     * */
    Set<Integer> getBizType();

}