package com.dili.ia.service.impl;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

public class BoutiqueRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    @Autowired
    private BoutiqueFeeOrderService boutiqueFeeOrderService;

    /**
     * 退款单 -- 提交
     * 
     * @param
     * @return 
     * @date   2020/7/21
     */
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 撤回
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {

        return BaseOutput.success();
    }

    /**
     * 退款单 -- 退款成功回调
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 取消
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 业务数据加载
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 获取业务类型
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public Set<String> getBizType() {
        return null;
    }
}
