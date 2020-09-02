package com.dili.ia.service.impl;

import com.dili.bpmc.sdk.rpc.TaskRpc;
import com.dili.ia.domain.*;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.service.*;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Service
public class LeaseOrderRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderRefundOrderServiceImpl.class);


    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }
    @Autowired
    AssetsLeaseOrderService assetsLeaseOrderService;
    @Autowired
    AssetsLeaseOrderItemService assetsLeaseOrderItemService;
    @Autowired
    TransferDeductionItemService transferDeductionItemService;
    @Autowired
    private RefundFeeItemService refundFeeItemService;
    @Autowired
    private TaskRpc taskRpc;
    @Autowired
    private CustomerAccountService customerAccountService;
    @Autowired
    CustomerRpc customerRpc;
    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.BOOTH_LEASE.getCode());
    }

    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        TransferDeductionItem condition = new TransferDeductionItem();
        condition.setRefundOrderId(refundOrder.getId());
        List<TransferDeductionItem> transferDeductionItems = transferDeductionItemService.list(condition);
        if(CollectionUtils.isNotEmpty(transferDeductionItems)){
            transferDeductionItems.forEach(o->{
                assetsLeaseOrderService.checkCustomerState(o.getPayeeId(),refundOrder.getMarketId());
                CustomerAccount ca = customerAccountService.getCustomerAccountByCustomerId(o.getPayeeId(), refundOrder.getMarketId());
                //判断转入方客户账户是否存在,不存在先创建客户账户
                if (null == ca){
                    BaseOutput<Customer> output = customerRpc.get(o.getPayeeId(), refundOrder.getMarketId());
                    Customer customer = output.getData();
                    customerAccountService.addCustomerAccountByCustomerInfo(customer.getId(), customer.getName(), customer.getContactsPhone(), customer.getCertificateNumber(), refundOrder.getMarketId());
                }
            });
        }
        return BaseOutput.success();
    }

    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        //发送流程消息通知撤回
        BaseOutput<String> baseOutput = taskRpc.messageEventReceived("withdraw", refundOrder.getProcessInstanceId(), null);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "流程消息发送失败");
        }
        return baseOutput;
    }

    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
        try{
            return assetsLeaseOrderService.settleSuccessRefundOrderHandler(refundOrder);
        }catch (Exception e){
            LOG.info("租赁退款单成功回调异常",e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {
        try{
            return assetsLeaseOrderService.cancelRefundOrderHandler(refundOrder.getBusinessItemId());
        }catch (Exception e){
            LOG.info("租赁退款单取消回调异常",e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @Override
    public BaseOutput<Map<String,Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        Map<String,Object> resultMap = new HashMap<>();
        //已交清退款单打印数据
        resultMap.put("printTemplateCode", PrintTemplateEnum.BOOTH_LEASE_REFUND_PAID.getCode());
        //根据要求拼装订单项
        buildLeaseOrderItem(refundOrder, resultMap);
        buildTransferDeductionItems(refundOrder.getId(), resultMap);
        return BaseOutput.success().setData(resultMap);
    }

    /**
     * 构建退款单转低打印数据
     * @param refundOrderId
     * @return
     */
    private void buildTransferDeductionItems(Long refundOrderId, Map<String, Object> resultMap) {
        TransferDeductionItem transferDeductionItemCondition = new TransferDeductionItem();
        transferDeductionItemCondition.setRefundOrderId(refundOrderId);
        List<TransferDeductionItem> transferDeductionItems = transferDeductionItemService.list(transferDeductionItemCondition);
        List<Map<String,Object>> transferMaps = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if(CollectionUtils.isNotEmpty(transferDeductionItems)){
            for (TransferDeductionItem transferDeductionItem : transferDeductionItems) {
                Map<String,Object> transferMap = new HashMap<>();
                transferMap.put("payee",transferDeductionItem.getPayee());
                transferMap.put("payeeAmount", MoneyUtils.centToYuan(transferDeductionItem.getPayeeAmount()));
                transferMaps.add(transferMap);
                stringBuilder.append(transferDeductionItem.getPayee()).append("  金额: ").append(MoneyUtils.centToYuan(transferDeductionItem.getPayeeAmount()));
                if(transferDeductionItems.size() > 1){
                    stringBuilder.append(";  ");
                }
            }
        }
        resultMap.put("transferDeductionItems", transferMaps);
        resultMap.put("transferDeductionItemsStr", stringBuilder.toString());
    }

    /**
     * 构建打印map外层订单项
     * @param refundOrder
     * @return
     */
    public void buildLeaseOrderItem(RefundOrder refundOrder, Map<String, Object> resultMap) {
        AssetsLeaseOrderItem leaseOrderItem = assetsLeaseOrderItemService.get(refundOrder.getBusinessItemId());
        resultMap.put("assetsName", leaseOrderItem.getAssetsName());
        resultMap.put("districtName", leaseOrderItem.getDistrictName());
        resultMap.put("number", String.valueOf(leaseOrderItem.getNumber()));
        resultMap.put("unitName", leaseOrderItem.getUnitName());
        resultMap.put("unitPrice", MoneyUtils.centToYuan(leaseOrderItem.getUnitPrice()));
        resultMap.put("isCorner", leaseOrderItem.getIsCorner());
        resultMap.put("paymentMonth", String.valueOf(leaseOrderItem.getPaymentMonth()));
        resultMap.put("discountAmount", MoneyUtils.centToYuan(leaseOrderItem.getDiscountAmount()));

        RefundFeeItem condition = new RefundFeeItem();
        condition.setRefundOrderId(refundOrder.getId());
        List<RefundFeeItem> refundFeeItems = refundFeeItemService.list(condition);
        StringBuilder refundFeeItemsStr = new StringBuilder();
        for (int i = 0; i < refundFeeItems.size(); i++) {
            RefundFeeItem rfItem = refundFeeItems.get(i);
            refundFeeItemsStr.append(rfItem.getChargeItemName()).append(":").append(MoneyUtils.centToYuan(rfItem.getAmount()));
            if (i != refundFeeItems.size() - 1) {
                refundFeeItemsStr.append("  ");
            }

        }
        resultMap.put("refundFeeItemsStr", refundFeeItemsStr);
    }
}