package com.dili.ia.service;

import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.PassportDto;
import com.dili.ia.domain.dto.PassportRefundOrderDto;
import com.dili.ia.domain.dto.printDto.PassportPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
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
     * @param  passportDto
     * @param  useProvider
     * @return EasyuiPageOutput
     * @date   2020/7/29
     */
    EasyuiPageOutput listPassports(PassportDto passportDto, boolean useProvider) throws Exception;

    /**
     * 新增通行证业务单
     *
     * @param  passportDto
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    Passport addPassport(PassportDto passportDto, UserTicket userTicket);

    /**
     * 修改通行证业务单
     *
     * @param  passportDto
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    Passport updatePassport(PassportDto passportDto, UserTicket userTicket) ;

    /**
     * 取消通行证业务单
     *
     * @param  id
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    Passport cancel(Long id, UserTicket userTicket) ;

    /**
     * 提交通行证业务单交费
     *
     * @param  id
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    Passport submit(Long id, UserTicket userTicket) ;

    /**
     * 撤回通行证业务单交费
     *
     * @param  id
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    Passport withdraw(Long id, UserTicket userTicket) ;

    /**
     * 通行证业务单交费成功回调
     *
     * @param  settleOrder
     * @return Passport
     * @date   2020/7/27
     */
    Passport settlementDealHandler(SettleOrder settleOrder);

    /**
     * 通行证打业务单印票据
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/7/27
     */
    PrintDataDto<PassportPrintDto> receiptPaymentData(String orderCode, Integer reprint) ;

    /**
     * 根据 code 查询数据
     *
     * @param  code
     * @return Passport
     * @date   2020/7/27
     */
    Passport getPassportByCode(String code);

    /**
     * 退款申请
     *
     * @param  passportRefundOrderDto
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    Passport refund(PassportRefundOrderDto passportRefundOrderDto, UserTicket userTicket);

    /**
     * 退款票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/8/11
     */
    PrintDataDto<PassportPrintDto> receiptRefundPrintData(String orderCode, String reprint);

    /**
     * 定时任务，刷新状态
     *
     * @date 2020/8/6
     */
    void passportTasking();

    /**
     * 通行证证件打印
     *
     * @param  orderCode
     * @return BaseOutput
     * @date   2020/7/27
     */
    PrintDataDto<PassportPrintDto> printPaperwork(String orderCode, String reprint);
}