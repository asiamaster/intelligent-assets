package com.dili.ia.service;

import com.dili.ia.domain.OtherFee;
import com.dili.ia.domain.Passport;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.OtherFeeDto;
import com.dili.ia.domain.dto.OtherFeeRefundOrderDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.OtherFeePrintDto;
import com.dili.ia.domain.dto.printDto.PassportPrintDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-21 18:08:01.
 */
public interface OtherFeeService extends BaseService<OtherFee, Long> {

    /**
     * 根据code查询数据实例
     *
     * @param  code
     * @return
     * @date   2020/8/27
     */
    OtherFee getOtherFeeByCode(String code);

    /**
     * 新增其他收费
     *
     * @param  otherFeeDto
     * @return BaseOutput
     * @date   2020/8/18
     */
    BaseOutput<OtherFee> addOtherFee(OtherFeeDto otherFeeDto, UserTicket userTicket) throws Exception;

    /**
     * 修改其他收费
     * 
     * @param  otherFeeDto
     * @return BaseOutput
     * @date   2020/8/19
     */
    BaseOutput<OtherFee> updateOtherFee(OtherFeeDto otherFeeDto, UserTicket userTicket) throws Exception;

    /**
     * 提交其他收费
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/8/19
     */
    BaseOutput<OtherFee> submit(Long id, UserTicket userTicket) throws Exception;

    /**
     * 取消其他收费
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/8/19
     */
    BaseOutput<OtherFee> cancel(Long id, UserTicket userTicket);

    /**
     * 撤回其他收费
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/8/19
     */
    BaseOutput<OtherFee> withdraw(Long id, UserTicket userTicket) throws Exception;

    /**
     * 其他收费 退款
     *
     * @param  refundOrderDto
     * @return BaseOutput
     * @date   2020/8/19
     */
    BaseOutput<OtherFee> refund(OtherFeeRefundOrderDto refundOrderDto) throws Exception;

    /**
     * 缴费成功回调
     * 
     * @param  settleOrder
     * @return 
     * @date   2020/8/19
     */
    BaseOutput<OtherFee> settlementDealHandler(SettleOrder settleOrder);

    /**
     * 缴费成功票据打印
     *
     * @param
     * @return
     * @date   2020/8/19
     */
    PrintDataDto<OtherFeePrintDto> receiptPaymentData(String orderCode, Integer reprint);

    /**
     * 退款票据打印
     *
     * @param
     * @return
     * @date   2020/8/19
     */
    PrintDataDto<OtherFeePrintDto> receiptRefundPrintData(String orderCode, String reprint);
}