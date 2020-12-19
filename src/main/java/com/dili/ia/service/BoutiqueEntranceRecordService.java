package com.dili.ia.service;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.printDto.BoutiqueEntrancePrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  精品停车
 */
public interface BoutiqueEntranceRecordService extends BaseService<BoutiqueEntranceRecord, Long> {

    /**
     * 列表查询
     *
     * @param  boutiqueEntranceRecordDto
     * @return EasyuiPageOutput
     * @date   2020/8/17
     */
    EasyuiPageOutput listBoutiques(BoutiqueEntranceRecordDto boutiqueEntranceRecordDto, boolean b) throws Exception;

    /**
     * 根据主键获取精品停车数据以及精品停车交费列表
     *
     * @param  id
     * @return BoutiqueEntranceRecordDto
     * @date   2020/7/13
     */
    BoutiqueEntranceRecordDto getBoutiqueEntranceDtoById(Long id);

    /**
     * 确认计费
     *
     * @param  boutiqueEntranceRecord
     * @param  userTicket
     * @return BoutiqueEntranceRecord
     * @date   2020/7/13
     */
    BoutiqueEntranceRecord confirm(BoutiqueEntranceRecord boutiqueEntranceRecord, UserTicket userTicket);

    /**
     * 交费提交
     *
     * @param  id
     * @param  userTicket
     * @return BoutiqueEntranceRecord
     * @date   2020/7/14
     */
    BoutiqueEntranceRecord submit(BoutiqueFeeOrder id, UserTicket userTicket);

    /**
     * 离场
     *
     * @param  id
     * @param  userTicket
     * @return BoutiqueEntranceRecord
     * @date   2020/7/13
     */
    BoutiqueEntranceRecord leave(Long id, UserTicket userTicket);

    /**
     * 强制离场
     *
     * @param  id
     * @param  userTicket
     * @return BoutiqueEntranceRecord
     * @date   2020/7/13
     */
    BoutiqueEntranceRecord forceLeave(Long id, UserTicket userTicket);

    /**
     * 缴费成功回调
     *
     * @param  settleOrder
     * @return BaseOutput
     * @date   2020/7/14
     */
    BoutiqueFeeOrder settlementDealHandler(SettleOrder settleOrder);

    /**
     * 打印票据
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/7/14
     */
    PrintDataDto<BoutiqueEntrancePrintDto> receiptPaymentData(String orderCode, Integer reprint);

    /**
     * 根据code获取交费单相关数据
     *
     * @param  code
     * @return BoutiqueFeeOrderDto
     * @date   2020/7/30
     */
    BoutiqueFeeOrderDto getBoutiqueAndOrderByCode(String code);

    /**
     * 打印退款
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/8/11
     */
    PrintDataDto<BoutiqueEntrancePrintDto> receiptRefundPrintData(String orderCode, String reprint);

    /**
     * 新增计费（提供给其他服务调用者）
     *
     * @param  boutiqueEntranceRecord
     * @return BaseOutput
     * @date   2020/7/13
     */
    BoutiqueEntranceRecord addBoutique(BoutiqueEntranceRecord boutiqueEntranceRecord);

    /**
     * 取消(进门取消，可在待确认和计费中取消)
     *
     * @param  recordDto
     * @return BoutiqueFeeOrder
     * @date   2020/8/5
     */
    BoutiqueEntranceRecord cancel(BoutiqueEntranceRecordDto recordDto);
}