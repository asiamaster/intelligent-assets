package com.dili.ia.service;

import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.BoutiqueFeeRefundOrderDto;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  缴费单
 */
public interface BoutiqueFeeOrderService extends BaseService<BoutiqueFeeOrder, Long> {

    /**
     * 根据精品停车主键 recordId 查询缴费单列表
     *
     * @param  recordId 精品停车主键
     * @return list
     * @date   2020/7/13
     */
    List<BoutiqueFeeOrderDto> listByRecordId(Long recordId);

    /**
     * 根据主键查询精品停车单和精品停车相关信息
     * 
     * @param  orderId
     * @return BoutiqueFeeOrderDto
     * @date   2020/7/31
     */
    BoutiqueFeeOrderDto getBoutiqueFeeOrderDtoById(Long orderId);

    /**
     * 退款申请
     *
     * @param refundOrderDto
     * @return BaseOutput
     * @date   2020/7/23
     */
    void refund(BoutiqueFeeRefundOrderDto refundOrderDto);
}