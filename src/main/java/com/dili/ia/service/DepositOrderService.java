package com.dili.ia.service;

import com.dili.ia.domain.DepositBalance;
import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-22 17:54:56.
 */
public interface DepositOrderService extends BaseService<DepositOrder, Long> {
    /**
     * 新增保证金，需要先检查客户账户是否存在
     * */
    BaseOutput<DepositOrder> addDepositOrder(DepositOrder depositOrder);

    /**
     * 保证金 --修改
     * @param depositOrder 修改对象
     * @return BaseOutput
     * */
    BaseOutput<DepositOrder> updateDepositOrder(DepositOrder depositOrder);

    /**
     * 保证金 --提交
     * @param id 保证金单ID
     * @param amount  本次提交付款金额
     * @param waitAmount  提交付款时候的待付金额
     * @return BaseOutput
     * */
    BaseOutput<DepositOrder> submitDepositOrder(Long id, Long amount, Long waitAmount);
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
     * 保证金 --创建退款单
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput<RefundOrder> addRefundOrder(RefundOrder refundOrder);
    /**
     * 保证金 --结算退款成功回调
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput refundSuccessHandler(RefundOrder refundOrder);

    /**
     * 保证金票据打印数据加载
     * @param orderCode 订单号
     * @param reprint 是否补打标记
     * @return BaseOutput<PrintDataDto>
     */
    BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint);
    /**
     * 批量【新增】保证金单 --- 【摊位租赁同步生成使用】
     * @param depositOrderList 保证金订单列表
     * DepositOrder 对象必要的参数有： customerId 客户Id ; customerName 客户名称; certificateNumber 客户证件号 ; customerCellphone 客户电话
     *                         departmentId 业务所属部门ID ; typeCode 保证金类型，来源数据字典 ; typeName 保证金类型名称
     *                         assetsType 资产类型; assetsId 资产ID; assetsName 资产名称; amount 保证金金额; isRelated 是否关联订单1，是，0否;
     *                         businessId 关联订单ID; bizType 关联订单业务类型;
     * @return BaseOutput
     */
    BaseOutput batchAddDepositOrder(List<DepositOrder> depositOrderList);

    /**
     * 批量【提交】保证金单 --- 【摊位租赁同步提交生成使用】
     * @param bizType 业务类型
     * @param map key： businessId  订单项ID ; value: amount 付款金额
     * @return BaseOutput
     */
    BaseOutput batchSubmitDepositOrder(String bizType, Map<Long, Long> map);

    /**
     * 批量【撤回】保证金单 --- 【摊位租赁同步撤回成使用】
     * @param bizType 业务类型
     * @param businessIds 业务单ids
     * @return BaseOutput
     */
    BaseOutput batchWithdrawDepositOrder(String bizType, List<Long> businessIds);

    /**
     * 【查询】客户摊位保证金余额 --- 【摊位租赁使用】
     * @param bizType 业务类型
     * @param customerId 客户ID
     * @param assetsIds 资产ID的List
     * @return BaseOutput<List<DepositBalance>>
     */
    BaseOutput<List<DepositBalance>> listDepositBalance(String bizType, Long customerId, List<Long> assetsIds);
}