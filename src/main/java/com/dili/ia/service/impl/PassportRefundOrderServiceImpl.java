package com.dili.ia.service.impl;

import com.dili.ia.domain.Passport;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BoutiqueOrderStateEnum;
import com.dili.ia.glossary.PassportStateEnum;
import com.dili.ia.service.PassportService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
@Service
public class PassportRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    @Autowired
    private PassportService passportService;


    /**
     * 退款单 -- 提交
     * 
     * @param
     * @return 
     * @date   2020/7/21
     */
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {

        Passport passportInfo = passportService.getPassportByCode(refundOrder.getBusinessCode());
        if (!PassportStateEnum.SUBMITTED_REFUND.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        this.updateState(refundOrder.getBusinessCode(), passportInfo.getVersion(), PassportStateEnum.SUBMITTED_REFUND);
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

        Passport passportInfo = passportService.getPassportByCode(refundOrder.getBusinessCode());

        if (!PassportStateEnum.SUBMITTED_REFUND.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        this.updateState(refundOrder.getBusinessCode(), passportInfo.getVersion(), PassportStateEnum.SUBMITTED_REFUND);
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

        Passport passportInfo = passportService.getPassportByCode(refundOrder.getBusinessCode());

        if (!PassportStateEnum.SUBMITTED_REFUND.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        this.updateState(refundOrder.getBusinessCode(), passportInfo.getVersion(), PassportStateEnum.REFUNDED);

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

        Passport passportInfo = passportService.getPassportByCode(refundOrder.getBusinessCode());

        if (!PassportStateEnum.SUBMITTED_REFUND.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        PassportStateEnum state = PassportStateEnum.NOT_START;
        if (passportInfo.getStartTime() != null && passportInfo.getEndTime() != null && passportInfo.getStartTime().isBefore(LocalDateTime.now())
                && LocalDateTime.now().isBefore(passportInfo.getEndTime())){
            // 已生效
            state= PassportStateEnum.IN_FORCE;
        } else if (passportInfo.getEndTime() != null && passportInfo.getEndTime().isBefore(LocalDateTime.now())) {
            // 已过期
            state = PassportStateEnum.EXPIRED;
        }

        this.updateState(refundOrder.getBusinessCode(), passportInfo.getVersion(), state);

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
        return Sets.newHashSet(BizTypeEnum.PASSPORT.getCode());
    }

    private void updateState(String code, Integer version, PassportStateEnum state) {

        Passport domain = new Passport();
        domain.setVersion(version + 1);
        domain.setState(state.getCode());

        Passport condition = new Passport();
        condition.setCode(code);
        condition.setVersion(version);

        // 修改精品停车交费单状态
        int row = passportService.updateSelectiveByExample(domain, condition);
        if (row != 1) {
            throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
        }
    }
}
