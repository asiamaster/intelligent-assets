package com.dili.ia.service;

import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.dto.DepositOrderQuery;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-22 17:54:56.
 */
public interface DepositOrderService extends BaseService<DepositOrder, Long> {
    /**
     * 新增保证金，需要先检查客户账户是否存在
     * */
    BaseOutput<DepositOrder> addDepositOrder(DepositOrderQuery depositOrderQuery);

    /**
     * 保证金 --修改
     * @param depositOrder 修改对象
     * @return BaseOutput
     * */
    BaseOutput<DepositOrder> updateDepositOrder(DepositOrder depositOrder);

    /**
     * 保证金 --提交
     * @param depositOrderId 保证金单ID
     * @return BaseOutput
     * */
    BaseOutput<DepositOrder> submitDepositOrder(Long depositOrderId);
    /**
     * 保证金 --撤回
     * @param depositOrderId 保证金单ID
     * @return BaseOutput
     * */
    BaseOutput<DepositOrder> withdrawDepositOrder(Long depositOrderId);

    /**
     * 保证金 --缴费成功回调
     * @param settleOrder 结算单
     * @return BaseOutput
     * */
    BaseOutput<DepositOrder> paySuccessHandler(SettleOrder settleOrder);

    /**
     * 保证金票据打印数据加载
     * @param orderCode 订单号
     * @param reprint 是否补打标记
     * @return BaseOutput<PrintDataDto>
     */
    BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint);
}