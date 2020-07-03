package com.dili.ia.service.impl;

import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.domain.dto.AssetsLeaseOrderItemListDto;
import com.dili.ia.domain.dto.LeaseOrderItemPrintDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.service.*;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
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
    LeaseOrderService leaseOrderService;
    @Autowired
    LeaseOrderItemService leaseOrderItemService;
    @Autowired
    TransferDeductionItemService transferDeductionItemService;

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
                leaseOrderService.checkCustomerState(o.getPayeeId(),refundOrder.getMarketId());
            });
        }
        return BaseOutput.success();
    }

    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
        try{
            return leaseOrderService.settleSuccessRefundOrderHandler(refundOrder);
        }catch (Exception e){
            LOG.info("租赁退款单成功回调异常",e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {
        try{
            return leaseOrderService.cancelRefundOrderHandler(refundOrder.getBusinessId(),refundOrder.getBusinessItemId());
        }catch (Exception e){
            LOG.info("租赁退款单取消回调异常",e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @Override
    public BaseOutput<Map<String,Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        Map<String,Object> resultMap = new HashMap<>();
        if(null == refundOrder.getBusinessItemId()){
            //未交清退款单打印数据
            resultMap.put("printTemplateCode", PrintTemplateEnum.BOOTH_LEASE_REFUND_NOT_PAID.getCode());
        }else{
            //已交清退款单打印数据
            resultMap.put("printTemplateCode", PrintTemplateEnum.BOOTH_LEASE_REFUND_PAID.getCode());
            //resultMap.put("leaseOrderItem", leaseOrderItem2PrintDto(leaseOrderItemService.get(refundOrder.getOrderItemId())));
            //根据要求拼装订单项
            buildLeaseOrderItem(leaseOrderItemService.get(refundOrder.getBusinessItemId()), resultMap);
        }
        //resultMap.put("transferDeductionItems", buildTransferDeductionItemsPrintDto(refundOrder.getId()));
        buildTransferDeductionItems(refundOrder.getId(), resultMap);
        return BaseOutput.success().setData(resultMap);
    }

    /**
     * 构建退款单转低打印dto
     * @param refundOrderId
     * @return
     */
    private List<Map<String,Object>> buildTransferDeductionItemsPrintDto(Long refundOrderId) {
        TransferDeductionItem transferDeductionItemCondition = new TransferDeductionItem();
        transferDeductionItemCondition.setRefundOrderId(refundOrderId);
        List<TransferDeductionItem> transferDeductionItems = transferDeductionItemService.list(transferDeductionItemCondition);
        List<Map<String,Object>> transferMaps = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(transferDeductionItems)){
            for (TransferDeductionItem transferDeductionItem : transferDeductionItems) {
                Map<String,Object> transferMap = new HashMap<>();
                transferMap.put("payee",transferDeductionItem.getPayee());
                transferMap.put("payeeAmount", MoneyUtils.centToYuan(transferDeductionItem.getPayeeAmount()));
                transferMaps.add(transferMap);
            }
        }
        return transferMaps;
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

    @Autowired private BusinessChargeItemService businessChargeItemService;

    /**
     * 订单项Bean转PrintDto
     * @param leaseOrderItem
     * @return
     */
    static LeaseOrderItemPrintDto leaseOrderItem2PrintDto(AssetsLeaseOrderItemListDto leaseOrderItem) {
        LeaseOrderItemPrintDto leaseOrderItemPrintDto = new LeaseOrderItemPrintDto();
        leaseOrderItemPrintDto.setBoothName(leaseOrderItem.getAssetsName());
        leaseOrderItemPrintDto.setDistrictName(leaseOrderItem.getDistrictName());
        leaseOrderItemPrintDto.setNumber(leaseOrderItem.getNumber().toString());
        leaseOrderItemPrintDto.setUnitName(leaseOrderItem.getUnitName());
        leaseOrderItemPrintDto.setUnitPrice(MoneyUtils.centToYuan(leaseOrderItem.getUnitPrice()));
        leaseOrderItemPrintDto.setIsCorner(leaseOrderItem.getIsCorner());
        leaseOrderItemPrintDto.setPaymentMonth(leaseOrderItem.getPaymentMonth().toString());
        leaseOrderItemPrintDto.setDiscountAmount(MoneyUtils.centToYuan(leaseOrderItem.getDiscountAmount()));
        leaseOrderItemPrintDto.setBusinessChargeItem(leaseOrderItem.getBusinessChargeItem());
//        leaseOrderItemPrintDto.setRentAmount(MoneyUtils.centToYuan(leaseOrderItem.getRentAmount()));
//        leaseOrderItemPrintDto.setManageAmount(MoneyUtils.centToYuan(leaseOrderItem.getManageAmount()));
//        leaseOrderItemPrintDto.setDepositAmount(MoneyUtils.centToYuan(leaseOrderItem.getDepositAmount()));
//        leaseOrderItemPrintDto.setRentRefundAmount(MoneyUtils.centToYuan(leaseOrderItem.getRentRefundAmount()));
//        leaseOrderItemPrintDto.setManageRefundAmount(MoneyUtils.centToYuan(leaseOrderItem.getManageRefundAmount()));
//        leaseOrderItemPrintDto.setDepositRefundAmount(MoneyUtils.centToYuan(leaseOrderItem.getDepositRefundAmount()));
        return leaseOrderItemPrintDto;
    }

    /**
     * 构建打印map外层订单项
     * @param leaseOrderItem
     * @return
     */
    static void buildLeaseOrderItem(LeaseOrderItem leaseOrderItem, Map<String, Object> resultMap) {
        resultMap.put("boothName", leaseOrderItem.getBoothName());
        resultMap.put("districtName", leaseOrderItem.getDistrictName());
        resultMap.put("number", String.valueOf(leaseOrderItem.getNumber()));
        resultMap.put("unitName", leaseOrderItem.getUnitName());
        resultMap.put("unitPrice", MoneyUtils.centToYuan(leaseOrderItem.getUnitPrice()));
        resultMap.put("isCorner", leaseOrderItem.getIsCorner());
        resultMap.put("paymentMonth", String.valueOf(leaseOrderItem.getPaymentMonth()));
        resultMap.put("discountAmount", MoneyUtils.centToYuan(leaseOrderItem.getDiscountAmount()));
        resultMap.put("rentRefundAmount", MoneyUtils.centToYuan(leaseOrderItem.getRentRefundAmount()));
        resultMap.put("manageRefundAmount", MoneyUtils.centToYuan(leaseOrderItem.getManageRefundAmount()));
        resultMap.put("depositRefundAmount", MoneyUtils.centToYuan(leaseOrderItem.getDepositRefundAmount()));
    }
}