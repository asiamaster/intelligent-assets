package com.dili.ia.service;

import com.dili.ia.domain.OtherFee;
import com.dili.ia.domain.dto.OtherFeeDto;
import com.dili.ia.domain.dto.OtherFeeRefundOrderDto;
import com.dili.ia.domain.dto.printDto.OtherFeePrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.exception.BusinessException;
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
     * @return OtherFee
     * @date   2020/8/27
     */
    OtherFee getOtherFeeByCode(String code);

    /**
     * 新增其他收费
     *
     * @param  otherFeeDto
     * @param  userTicket
     * @return OtherFee
     * @date   2020/8/18
     */
    OtherFee addOtherFee(OtherFeeDto otherFeeDto, UserTicket userTicket) throws Exception;

    /**
     * 修改其他收费
     *
     * @param  otherFeeDto
     * @param  userTicket
     * @return OtherFee
     * @date   2020/8/19
     */
    OtherFee updateOtherFee(OtherFeeDto otherFeeDto, UserTicket userTicket) throws BusinessException;

    /**
     * 提交其他收费
     *
     * @param id
     * @return OtherFee
     * @date 2020/8/19
     */
    OtherFee submit(Long id, UserTicket userTicket) throws BusinessException;

    /**
     * 取消其他收费
     *
     * @param id
     * @param userTicket
     * @return OtherFee
     * @date 2020/8/19
     */
    OtherFee cancel(Long id, UserTicket userTicket) throws BusinessException;

    /**
     * 撤回其他收费
     *
     * @param id
     * @param userTicket
     * @return OtherFee
     * @date 2020/8/19
     */
    OtherFee withdraw(Long id, UserTicket userTicket) throws BusinessException;

    /**
     * 其他收费 退款
     *
     * @param refundOrderDto
     * @param userTicket
     * @return OtherFee
     * @date 2020/8/19
     */
    OtherFee refund(OtherFeeRefundOrderDto refundOrderDto, UserTicket userTicket) throws BusinessException;

    /**
     * 缴费成功回调
     *
     * @param settleOrder
     * @return
     * @date 2020/8/19
     */
    OtherFee settlementDealHandler(SettleOrder settleOrder) throws BusinessException;

    /**
     * 缴费成功票据打印
     *
     * @param
     * @return
     * @date 2020/8/19
     */
    PrintDataDto<OtherFeePrintDto> receiptPaymentData(String orderCode, Integer reprint) throws BusinessException ;

    /**
     * 退款票据打印
     *
     * @param
     * @return
     * @date 2020/8/19
     */
    PrintDataDto<OtherFeePrintDto> receiptRefundPrintData(String orderCode, String reprint) throws BusinessException;
}