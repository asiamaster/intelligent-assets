package com.dili.ia.service;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.dto.EarnestOrderListDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
public interface EarnestOrderService extends BaseService<EarnestOrder, Long> {
    /**
     * 新增定金单，需要先检查客户账户是否存在
     * */
    BaseOutput<EarnestOrder> addEarnestOrder(EarnestOrderListDto earnestOrder);

    /**
     * 定金单 --修改
     * @param earnestOrder 修改对象
     * @return BaseOutput
     * */
    BaseOutput<EarnestOrder> updateEarnestOrder(EarnestOrderListDto earnestOrder);

    /**
     * 定金单 --提交
     * @param earnestOrderId 定金单ID
     * @return BaseOutput
     * */
    BaseOutput<EarnestOrder> submitEarnestOrder(Long earnestOrderId);
    /**
     * 定金单 --撤回
     * @param earnestOrderId 定金单ID
     * @return BaseOutput
     * */
    BaseOutput<EarnestOrder> withdrawEarnestOrder(Long earnestOrderId);

    /**
     * 定金单 --缴费成功回调
     * @param settleOrder 结算单
     * @return BaseOutput
     * */
    BaseOutput<EarnestOrder> paySuccessHandler(SettleOrder settleOrder);

    /**
     * 定金票据打印数据加载
     * @param orderCode 订单号
     * @param reprint 是否补打标记
     * @return BaseOutput<PrintDataDto>
     */
    BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint);


}