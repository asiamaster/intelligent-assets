package com.dili.ia.service;

import com.dili.ia.domain.DepositBalance;
import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.DepositRefundOrderDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.RefundOrderTempDto;
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
     * @param depositRefundOrderDto 退款单
     * @return BaseOutput
     * */
    BaseOutput<RefundOrder> saveOrUpdateRefundOrder(DepositRefundOrderDto depositRefundOrderDto);
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
    BaseOutput batchAddOrUpdateDepositOrder(List<DepositOrder> depositOrderList);

    /**
     * 批量【提交】保证金单 --- 【摊位租赁同步提交生成使用】
     * @param bizType 业务类型
     * @param businessId 关联订单ID
     * @param map key： assetsId  资产ID ; value: amount 付款金额
     * @return BaseOutput
     */
    BaseOutput batchSubmitDepositOrder(String bizType, Long businessId, Map<Long, Long> map);

    /**
     * 批量【全额提交】保证金单 --- 【摊位租赁审批通过后，默认全额提交关联保证金单和租赁单】
     * @param bizType 业务类型
     * @param businessId 关联订单ID
     * @return BaseOutput
     */
    BaseOutput batchSubmitDepositOrderFull(String bizType, Long businessId);

    /**
     * 批量【撤回】保证金单 --- 【摊位租赁同步撤回成使用】
     * 关联保证金进行了一次单独缴费成功，撤回租赁单需要解除关联操作关系。
     * @param bizType 业务类型
     * @param businessId 关联订单ID
     * @return BaseOutput
     */
    BaseOutput batchWithdrawDepositOrder(String bizType, Long businessId);

    /**
     * 批量【取消】保证金单 --- 【摊位租赁同步取消时使用】
     *
     * @param bizType 业务类型
     * @param businessId 关联订单ID
     * @return BaseOutput
     */
    BaseOutput batchCancelDepositOrder(String bizType, Long businessId);

    /**
     * 【查询】客户摊位保证金余额 --- 【摊位租赁使用】
     * @param bizType 业务类型
     * @param customerId 客户ID
     * @param assetsIds 资产ID的List
     * @return BaseOutput<List<DepositBalance>>
     */
    BaseOutput<List<DepositBalance>> listDepositBalance(String bizType, Long customerId, List<Long> assetsIds);
    /**
     * 批量【取消关联操作】保证金单 --- 【摊位租赁同步撤回成使用】
     * 解除关联操作关系的情况：
     * 1、租赁缴费已交清；
     * 2、关联保证金进行了一次缴费成功，撤回租赁单需要解除关联操作关系。
     * 3、租赁业务单的项交完，或者是未交完退款了，保证金都单飞操作；
     * @param bizType 业务类型
     * @param businessId 关联订单ID
     * @param assetsId 资产Id  此参数可以为空，为空代表解除关联businessId下的所有关联保证金单，assetsId有值，代表解除单个关联的assetsId保证金单
     * @return BaseOutput
     */
    BaseOutput batchReleaseRelated(String bizType, Long businessId, Long assetsId);

    /**
     * 检查当前市场客户状态
     * @param customerId 客户ID
     * @param marketId 当前市场ID
     */
    void checkCustomerState(Long customerId,Long marketId);

    /**
     * 批量【新增】,【已交费】的保证金单 --- 【用于处理老数据开发的接口】，正常流程【禁用！！！】
     * @param depositOrderList 保证金订单列表
     * DepositOrder 对象必要的参数有： customerId 客户Id ; customerName 客户名称; certificateNumber 客户证件号 ; customerCellphone 客户电话
     *                         departmentId 业务所属部门ID ; departmentName 部门名称; typeCode 保证金类型，来源数据字典 ; typeName 保证金类型名称
     *                         assetsType 资产类型; assetsId 资产ID; assetsName 资产名称; amount 保证金金额; isRelated 是否关联订单1，是，0否;
     *                         businessId 关联订单ID; bizType 关联订单业务类型;
     *
     * @return BaseOutput
     */
    BaseOutput oldDataHandler(List<DepositOrder> depositOrderList);
    /**
     *
     * 新增关联退款单
     * 修改/新增保证金余额，
     * 新增退款单的退款结算单
     *
     * 批量【新增】,【已退款】的保证金退款单 --- 【用于处理老数据开发的接口】，正常流程【禁用！！！】
     * @param refundOrderList 租赁订单退款单列表
     * refundOrder 对象必要的参数有： customerId 客户Id ; customerName 客户名称; certificateNumber 客户证件号 ; customerCellphone 客户电话
     *                         departmentId 业务所属部门ID ; departmentName 部门名称; typeCode 保证金类型，来源数据字典 ; typeName 保证金类型名称
     *                         assetsId 资产ID; businessId 关联订单ID; bizType 关联订单业务类型;
     *                         Payee 收款人,  PayeeID 收款人ID, PayeeCertificateNumber 收款人证件号
     *                        RefundType 退款方式 ; Bank 银行 ； BankCardNo 卡号； TotalRefundAmount 总退款金额 ； PayeeAmount付款金额
     * @return BaseOutput
     */
    BaseOutput oldRefundOrderDataHandler(List<RefundOrderTempDto> refundOrderList);
}