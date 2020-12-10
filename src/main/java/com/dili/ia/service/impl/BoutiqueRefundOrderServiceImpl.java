package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BoutiqueOrderStateEnum;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class BoutiqueRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueRefundOrderServiceImpl.class);


    @Autowired
    private BoutiqueFeeOrderService boutiqueFeeOrderService;

    @Autowired
    private BoutiqueEntranceRecordService boutiqueEntranceRecordService;

    @Autowired
    private CustomerAccountService customerAccountService;

    /**
     * 退款单 -- 提交(退款的提交无需改变通行证缴费单的信息)
     * 
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 撤回(退款的撤回无需改变通行证缴费单的信息)
     *
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 退款成功回调(精品停车的交费单状态由退款中修改为已退款)
     *
     * @param  settleOrder
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {

        BoutiqueFeeOrderDto orderDto = boutiqueEntranceRecordService.getBoutiqueAndOrderByCode(refundOrder.getBusinessCode());
        if (orderDto != null) {
            if (!BoutiqueOrderStateEnum.SUBMITTED_REFUND.getCode().equals(orderDto.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            this.updateState(refundOrder.getBusinessCode(), orderDto.getVersion(), BoutiqueOrderStateEnum.REFUNDED);
        }

        return BaseOutput.success();
    }

    /**
     * 退款单 -- 取消(精品停车的交费单状态由退款中修改为已缴费)
     *
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {

        BoutiqueFeeOrderDto orderDto = boutiqueEntranceRecordService.getBoutiqueAndOrderByCode(refundOrder.getBusinessCode());
        if (orderDto != null) {
            if (!BoutiqueOrderStateEnum.SUBMITTED_REFUND.getCode().equals(orderDto.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            this.updateState(refundOrder.getBusinessCode(), orderDto.getVersion(), BoutiqueOrderStateEnum.PAID);
        }
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 业务数据加载
     *
     * @param  refundOrder
     * @return BaseOutput
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
        return Sets.newHashSet(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
    }

    /**
     * 修改操作
     * 
     * @param  code
     * @param  version
     * @param  state
     * @date   2020/9/9
     */
    private void updateState(String code, Integer version, BoutiqueOrderStateEnum state) {

        BoutiqueFeeOrder domain = new BoutiqueFeeOrder();
        domain.setVersion(version + 1);
        domain.setState(state.getCode());
        domain.setModifyTime(LocalDateTime.now());

        BoutiqueFeeOrder condition = new BoutiqueFeeOrder();
        condition.setCode(code);
        condition.setVersion(version);

        // 修改精品停车交费单状态
        int row = boutiqueFeeOrderService.updateSelectiveByExample(domain, condition);
        if (row != 1) {
            throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
        }
    }
}
