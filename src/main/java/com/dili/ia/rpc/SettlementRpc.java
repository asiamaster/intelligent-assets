package com.dili.ia.rpc;

import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-02-24 15:49
 */
@FeignClient(name = "settlement-service",url = "http://10.28.10.84:8184")
public interface SettlementRpc {

    /**
     * 【提交】结算单 --- 到结算中心
     * @param settleOrder
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/save", method = RequestMethod.POST)
    BaseOutput<SettleOrder> submit(SettleOrder settleOrder);

    /**
     * 【测回】结算单 ---根据结算单编号取消
     * @param code 结算单编号
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/cancelByCode", method = RequestMethod.POST)
    BaseOutput<String> cancelByCode(String code);
}
