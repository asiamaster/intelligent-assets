package com.dili.ia.service;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.BoutiqueEntrancePrintDto;
import com.dili.ia.domain.dto.printDto.LaborRefundPrintDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
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
     * @return
     * @date   2020/8/17
     */
    EasyuiPageOutput listBoutiques(BoutiqueEntranceRecordDto boutiqueEntranceRecordDto, boolean b) throws Exception;
    
    /**
     * 根据主键获取精品停车数据以及精品停车交费列表
     * 
     * @param
     * @return 
     * @date   2020/7/13
     */
    BoutiqueEntranceRecordDto getBoutiqueEntranceDtoById(Long id);

    /**
     * 确认计费
     * 
     * @param  boutiqueEntranceRecord
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/13
     */
    BaseOutput<BoutiqueEntranceRecord> confirm(BoutiqueEntranceRecord boutiqueEntranceRecord, UserTicket userTicket) throws Exception;

    /**
     * 交费提交
     *
     * @param
     * @param id
     * @return
     * @date   2020/7/14
     */
    BaseOutput<BoutiqueEntranceRecord> submit(BoutiqueFeeOrder id, UserTicket userTicket) throws Exception;

    /**
     * 离场
     *
     * @param id 主键
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/13
     */
    BaseOutput<BoutiqueEntranceRecord> leave(Long id, UserTicket userTicket) throws Exception;

    /**
     * 强制离场
     *
     * @param id 主键
     * @return BaseOutput
     * @date   2020/7/13
     */
    BaseOutput<BoutiqueEntranceRecord> forceLeave(Long id, UserTicket userTicket) throws Exception;

    /**
     * 缴费成功回调
     *
     * @param  settleOrder
     * @return BaseOutput
     * @date   2020/7/14
     */
    BaseOutput<BoutiqueFeeOrder> settlementDealHandler(SettleOrder settleOrder);

    /**
     * 打印票据
     *
     * @param  orderCode
     * @param  reprint
     * @return
     * @date   2020/7/14
     */
    PrintDataDto<BoutiqueEntrancePrintDto> receiptPaymentData(String orderCode, Integer reprint);

    /**
     * 根据code获取相关数据
     *
     * @param  code
     * @return
     * @date   2020/7/30
     */
    BoutiqueFeeOrderDto getBoutiqueAndOrderByCode(String code);

    /**
     * 取消(进门取消，可在待确认和计费中取消)
     *
     * @param
     * @return BaseOutput
     * @date   2020/8/5
     */
    BaseOutput<BoutiqueFeeOrder> cancel(BoutiqueEntranceRecordDto recordDto );

    /**
     * 新增计费（提供给其他服务调用者）
     *
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     * @date   2020/7/13
     */
    BaseOutput<BoutiqueEntranceRecord> addBoutique(BoutiqueEntranceRecord boutiqueEntranceRecord);

    /**
     * 打印退款
     *
     * @param  orderCode
     * @param  reprint
     * @return
     * @date   2020/8/11
     */
    PrintDataDto<BoutiqueEntrancePrintDto> receiptRefundPrintData(String orderCode, String reprint);

}