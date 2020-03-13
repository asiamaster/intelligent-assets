package com.dili.ia.service.impl;

import com.dili.ia.api.RefundOrderApi;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.swing.text.Utilities;
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

    private final static Logger LOG = LoggerFactory.getLogger(RefundOrderDispatcherServiceImpl.class);

    @Autowired
    RefundOrderService refundOrderService;

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
        try {
            RefundOrderService service = refundBiz.get(refundOrder.getBizType());
            if(service!=null){
                BaseOutput refundResult = service.submitRefundOrder(refundOrder);
                return  refundResult;
            }
        } catch (RuntimeException e) {
            LOG.info("提交异常！{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("提交异常！{}", e1.getMessage());
            return BaseOutput.failure("提交异常");
        }
        return BaseOutput.failure("提交异常");
    }

    @Override
    public BaseOutput doWithdrawDispatcher(RefundOrder refundOrder) {
        try {
            RefundOrderService service=refundBiz.get(refundOrder.getBizType());
            if(service!=null){
                BaseOutput refundResult = service.withdrawRefundOrder(refundOrder);
                return refundResult;
            }
        } catch (RuntimeException e) {
            LOG.info("测回异常！{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("测回异常！{}", e1.getMessage());
            return BaseOutput.failure("测回异常");
        }
        return BaseOutput.failure("测回异常");
    }

    @Override
    public BaseOutput doRefundSuccessHandlerDispatcher(Integer bizType, Long refundOrderId) {
        try {
            RefundOrderService service=refundBiz.get(bizType);
            if(service!=null){
                BaseOutput refundResult = service.refundSuccessHandler(refundOrderId);
                return refundResult;
            }
        } catch (RuntimeException e) {
            LOG.info("退款成功回调异常！{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("退款成功回调异常！{}", e1.getMessage());
            return BaseOutput.failure("退款成功回调异常");
        }
        return BaseOutput.failure("退款成功回调异常");
    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String businessCode, Integer reprint) {
        try {
            RefundOrder refundOrder = DTOUtils.newDTO(RefundOrder.class);
            refundOrder.setCode(businessCode);
            List<RefundOrder> reList = refundOrderService.list(refundOrder);
            if (CollectionUtils.isEmpty(reList)){
                return BaseOutput.failure("没有获取到结算单【" + businessCode + "】");
            }

            RefundOrderService service=refundBiz.get(refundOrder.getBizType());
            if(service!=null){
                BaseOutput refundResult = service.queryPrintData(refundOrder, reprint);
                return refundResult;
            }
        } catch (RuntimeException e) {
            LOG.info("获取打印数据异常！{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("获取打印数据异常！{}", e1.getMessage());
            return BaseOutput.failure("获取打印数据异常");
        }
        return BaseOutput.failure("获取打印数据异常");
    }

    public Map<Integer, RefundOrderService> getRefundBiz() {
        return refundBiz;
    }

    public void setRefundBiz(Map<Integer, RefundOrderService> refundBiz) {
        this.refundBiz = refundBiz;
    }
}
