package com.dili.ia.service;

import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.EarnestTransferOrderDto;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
public interface CustomerAccountService {
    /**
     * 客户账户 -- 定金转移
     * @param etDto 前端传入参数
     * @return
     * */
    BaseOutput<EarnestTransferOrder> earnestTransfer(EarnestTransferOrderDto etDto);
    /**
     * 客户账户定金退款单创建 或者 修改
     * @param order
     * @return
     * */
    BaseOutput saveOrUpdateRefundOrder(RefundOrder order);

}