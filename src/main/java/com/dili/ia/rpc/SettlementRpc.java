package com.dili.ia.rpc;

import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-02-24 15:49
 */
@FeignClient(name = "settlement-service", url="${SettleRpc.url:}")
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
     * 【作废】结算单
     * @param invalidRequestDto  对象里面的参数都是必填
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/invalid", method = RequestMethod.POST)
    BaseOutput<String> invalid(@RequestBody InvalidRequestDto invalidRequestDto);
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
    /**
     * 【查询】结算可用退款方式 ---根据市场ID，marketId查询
     * @param marketId 业务缴费单code
     * @return 退款方式结算列表
     */
    @RequestMapping(value = "/api/settleConfig/listEnableRefundWay", method = RequestMethod.GET)
    BaseOutput<List<SettleConfig>> listEnableRefundWay(@RequestParam("marketId") Long marketId);

    /**
     * 【数据迁移】生成【已交费的结算单】 --- 到结算中心
     * @param itemList 结算单List
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/batchSaveDealt", method = RequestMethod.POST)
    BaseOutput<?> batchSaveDealt(List<SettleOrder> itemList);
    /**
     * 【数据迁移】生成【结算单】和删除【已有的结算单】
     * @param itemList 结算单List
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/batchSaveDealtAndDelete", method = RequestMethod.POST)
    BaseOutput<?> batchSaveDealtAndDelete(List<SettleOrder> itemList);
    /**
     * 【数据迁移】修改结算单金额
     * @param itemList
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/batchUpdateAmount", method = RequestMethod.POST)
    BaseOutput<?> batchUpdateAmount(List<Map<String, Object>> itemList);
}
