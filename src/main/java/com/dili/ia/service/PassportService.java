package com.dili.ia.service;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.PassportDto;
import com.dili.ia.domain.dto.PassportRefundOrderDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.PassportPrintDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * @author:      xiaosa
 * @date:        2020/7/27
 * @version:     农批业务系统重构
 * @description: 通行证
 */
public interface PassportService extends BaseService<Passport, Long> {

    /**
     * 查询列表
     *
     * @param  passport
     * @param  useProvider
     * @return EasyuiPageOutput
     * @date   2020/7/29
     */
    EasyuiPageOutput listPassports(PassportDto passportDto, boolean useProvider) throws Exception;
    
    /**
     * 新增通行证
     * 
     * @param  passportDto
     * @return BaseOutput
     * @date   2020/7/27
     */
    BaseOutput<Passport> addPassport(PassportDto passportDto, UserTicket userTicket);

    /**
     * 修改通行证
     *
     * @param  passportDto
     * @return BaseOutput
     * @date   2020/7/27
     */
    BaseOutput<Passport> updatePassport(PassportDto passportDto, UserTicket userTicket) throws Exception;

    /**
     * 取消通行证付款
     *
     * @param  id
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/27
     */
    BaseOutput<Passport> cancle(Long id, UserTicket userTicket) throws Exception;

    /**
     * 提交通行证缴费
     *
     * @param  id
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/27
     */
    BaseOutput<Passport> submit(Long id, UserTicket userTicket) throws Exception;

    /**
     * 撤回通行证缴费
     *
     * @param  id
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/27
     */
    BaseOutput<Passport> withdraw(Long id, UserTicket userTicket) throws Exception;

    /**
     * 通行证交费成功回调
     * 
     * @param  settleOrder
     * @return BaseOutput
     * @date   2020/7/27
     */
    BaseOutput<Passport> settlementDealHandler(SettleOrder settleOrder);

    /**
     * 通行证打印票据
     *
     * @param  orderCode
     * @return BaseOutput
     * @date   2020/7/27
     */
    PrintDataDto<PassportPrintDto> receiptPaymentData(String orderCode, Integer reprint);

    /**
     * 退款单 查询
     *
     * @param  code
     * @return PassportDto
     * @date   2020/7/27
     */
    Passport getPassportByCode(String code);

    /**
     * 退款申请
     *
     * @param passportRefundOrderDto
     * @return BaseOutput
     * @date   2020/7/27
     */
    BaseOutput<Passport> refund(PassportRefundOrderDto passportRefundOrderDto);

}