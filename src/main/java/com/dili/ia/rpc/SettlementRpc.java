package com.dili.ia.rpc;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-02-24 15:49
 */
@FeignClient(name = "settlement-service")
public interface SettlementRpc {

    /**
     * 【提交】结算单 --- 到结算中心
     * @param settleOrder
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/save", method = RequestMethod.POST)
    BaseOutput<SettleOrder> submit(SettleOrderDto settleOrder);
    /**
     * 【撤回】结算单 ---根据业务缴费单code取消
     * @param appId 应用ID
     * @param orderCode 业务缴费单code
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/cancel", method = RequestMethod.POST)
    BaseOutput<String> cancel(@RequestParam("appId") Long appId, @RequestParam("orderCode") String orderCode);
    /**
     * 【查询】结算单 ---结算单查询
     * @param appId
     * @param orderCode
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/get", method = RequestMethod.POST)
    BaseOutput<SettleOrder> get(@RequestParam("appId") Long appId, @RequestParam("orderCode") String orderCode);

    /**
     * 【查询】结算单结算详情 ---根据结算编号code查询
     * @param settlementCode 业务缴费单code
     * @return
     */
    @RequestMapping(value = "/api/settleWayDetail/listByCode", method = RequestMethod.GET)
    BaseOutput<List<SettleWayDetail>> listSettleWayDetailsByCode(@RequestParam("code") String settlementCode);

    /**
     * 【查询】结算单 ---根据结算编号code查询
     * @param settlementCode 业务缴费单code
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/getByCode", method = RequestMethod.GET)
    BaseOutput<SettleOrder> getByCode(@RequestParam("code") String settlementCode);
}
