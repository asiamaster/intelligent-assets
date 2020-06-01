package com.dili.ia.service;

import com.dili.ia.domain.RefundOrder;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.Map;
import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
public interface RefundOrderDispatcherService extends BaseService<RefundOrder, Long> {

    /**
     * 退款单 --提交
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput submitHandler(RefundOrder refundOrder);
    /**
     * 退款单 --撤回
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput withdrawHandler(RefundOrder refundOrder);

    /**
     * 退款单 --退款成功回调
     * @param settleOrder 退款结算单
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder);
    /**
     * 退款单 --取消
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput cancelHandler(RefundOrder refundOrder);

    /**
     * 票据打印  --- 业务数据加载
     * @param refundOrder 退款单
     * @return BaseOutput<RefundOrderPrintDto>
     */
   BaseOutput<Map<String,Object>> buildBusinessPrintData(RefundOrder refundOrder);

    /**
     * 退款单 --获取业务类型
     * */
    Set<Integer> getBizType();
}
