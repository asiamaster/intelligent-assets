package com.dili.ia.service.impl;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-12 18:45
 */
@Service
public class RefundOrderDispatcherServiceImpl implements RefundOrderDispatcherService {

    @Autowired
    @Lazy
    private List<RefundOrderService> refundBizTypes;
    private Map<Integer,RefundOrderService> refundBiz = new HashMap<>();
    @PostConstruct
    public void init() {
        for(RefundOrderService service : refundBizTypes) {
            for(Integer bizType : service.getBizType()) {
                this.refundBiz.put(bizType, service);
            }

        }
    }

    @Override
    public BaseOutput doSubmitDispatcher(RefundOrder refundOrder) {
        BaseOutput result = null;
        try {
            RefundOrderService service = refundBiz.get(refundOrder.getBizType());
            if(service!=null){
                BaseOutput refundResult = service.submitRefundOrder(refundOrder);
                result = refundResult;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseOutput doWithdrawDispatcher(RefundOrder refundOrder) {
        BaseOutput result = null;
        try {
            RefundOrderService service=refundBiz.get(refundOrder.getBizType());
            if(service!=null){
                BaseOutput refundResult = service.withdrawRefundOrder(refundOrder);
                result = refundResult;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseOutput doRefundSuccessHandlerDispatcher(Integer bizType, Long refundOrderId) {
        BaseOutput result = null;
        try {
            RefundOrderService service=refundBiz.get(bizType);
            if(service!=null){
                BaseOutput refundResult = service.refundSuccessHandler(refundOrderId);
                result = refundResult;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Map<Integer, RefundOrderService> getRefundBiz() {
        return refundBiz;
    }

    public void setRefundBiz(Map<Integer, RefundOrderService> refundBiz) {
        this.refundBiz = refundBiz;
    }
}
