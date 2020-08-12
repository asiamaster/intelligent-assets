package com.dili.ia.service.impl;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.AssetsLeaseOrderItemListDto;
import com.dili.ia.domain.dto.AssetsLeaseOrderListDto;
import com.dili.ia.glossary.*;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.*;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AssetsLeaseDataHandlerServiceImpl implements AssetsLeaseDataHandlerService {
    private final static Logger LOG = LoggerFactory.getLogger(AssetsLeaseDataHandlerServiceImpl.class);


    @Autowired
    AssetsLeaseOrderService assetsLeaseOrderService;
    @Autowired
    AssetsLeaseOrderItemService assetsLeaseOrderItemService;
    @Autowired
    BusinessChargeItemService businessChargeItemService;
    @Autowired
    ApportionRecordService apportionRecordService;
    @Autowired
    DepositOrderService depositOrderService;
    @Autowired
    RefundOrderService refundOrderService;
    @Autowired
    RefundFeeItemService refundFeeItemService;
    @Autowired
    TransferDeductionItemService transferDeductionItemService;
    @Autowired
    PaymentOrderService paymentOrderService;
    @Autowired
    SettlementRpc settlementRpc;
    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${settlement.handler.url}")
    private String settlerHandlerUrl;
    @Autowired
    UidFeignRpc uidFeignRpc;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    TransactionDetailsService transactionDetailsService;

    @Override
    @Transactional
    public BaseOutput leaseOrderDataHandler() {
        LOG.info("****************跑订单数据开始。。。******************");
        //动态收费项迁移
        leaseOrderChargeItemDataHandler();
        AssetsLeaseOrder condition = new AssetsLeaseOrder();
        List<AssetsLeaseOrder> assetsLeaseOrders = assetsLeaseOrderService.list(condition).stream().filter(o -> o.getDepositDeduction() > 0 || o.getDepositAmount() > 0).collect(Collectors.toList());
        assetsLeaseOrders.stream().forEach(o -> {
            o.setPayAmount(o.getPayAmount() + o.getDepositDeduction() - o.getDepositAmount());
            o.setTotalAmount(o.getPayAmount() + o.getTransferDeduction() + o.getEarnestDeduction());
            if (PayStateEnum.PAID.getCode().equals(o.getPayState())) {
                o.setPaidAmount(o.getPayAmount());
            }
        });
        if (assetsLeaseOrderService.batchUpdateSelective(assetsLeaseOrders) != assetsLeaseOrders.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        paymentDataHandler(assetsLeaseOrders);
        LOG.info("****************跑订单数据结束 success。。。******************");
        return BaseOutput.success();
    }

    /**
     * 动态收费项迁移
     *
     * @return
     */
    public BaseOutput leaseOrderChargeItemDataHandler() {
        LOG.info("****************跑订单项及收费项数据开始。。。******************");
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        List<AssetsLeaseOrderItem> assetsLeaseOrderItems = assetsLeaseOrderItemService.list(condition);
        List<BusinessChargeItem> businessChargeItems = new ArrayList<>();
        List<ApportionRecord> apportionRecords = new ArrayList<>();
        assetsLeaseOrderItems.forEach(o -> {
            BusinessChargeItem rentChargeItem = new BusinessChargeItem();
            rentChargeItem.setBusinessId(o.getId());
            rentChargeItem.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
            rentChargeItem.setChargeItemId(1L);
            rentChargeItem.setChargeItemName("租金");
            rentChargeItem.setAmount(o.getRentAmount());
            if (PayStateEnum.PAID.getCode().equals(o.getPayState())) {
                rentChargeItem.setPaidAmount(o.getRentAmount());
                rentChargeItem.setWaitAmount(0L);
            } else {
                rentChargeItem.setPaidAmount(0L);
                rentChargeItem.setWaitAmount(o.getRentAmount());
            }
            rentChargeItem.setPaymentAmount(0L);
            rentChargeItem.setCreateTime(LocalDateTime.now());
            rentChargeItem.setModifyTime(LocalDateTime.now());
            businessChargeItems.add(rentChargeItem);

            ApportionRecord rentApportionRecord = new ApportionRecord();
            rentApportionRecord.setLeaseOrderId(o.getLeaseOrderId());
            rentApportionRecord.setLeaseOrderItemId(o.getId());
            rentApportionRecord.setAmount(o.getRentAmount());
            rentApportionRecord.setChargeItemId(1L);
            rentApportionRecord.setChargeItemName("租金");
            rentApportionRecord.setCreateTime(LocalDateTime.now());
            apportionRecords.add(rentApportionRecord);

            BusinessChargeItem manageChargeItem = new BusinessChargeItem();
            manageChargeItem.setBusinessId(o.getId());
            manageChargeItem.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
            manageChargeItem.setChargeItemId(2L);
            manageChargeItem.setChargeItemName("物业管理费");
            manageChargeItem.setAmount(o.getManageAmount());

            if (PayStateEnum.PAID.getCode().equals(o.getPayState())) {
                manageChargeItem.setPaidAmount(o.getManageAmount());
                manageChargeItem.setWaitAmount(0L);
            } else {
                manageChargeItem.setPaidAmount(0L);
                manageChargeItem.setWaitAmount(o.getManageAmount());
            }
            manageChargeItem.setPaymentAmount(0L);
            manageChargeItem.setCreateTime(LocalDateTime.now());
            manageChargeItem.setModifyTime(LocalDateTime.now());
            businessChargeItems.add(manageChargeItem);

            if (PayStateEnum.PAID.getCode().equals(o.getPayState())) {
                ApportionRecord manageApportionRecord = new ApportionRecord();
                manageApportionRecord.setLeaseOrderId(o.getLeaseOrderId());
                manageApportionRecord.setLeaseOrderItemId(o.getId());
                manageApportionRecord.setAmount(o.getRentAmount());
                manageApportionRecord.setChargeItemId(2L);
                manageApportionRecord.setChargeItemName("物业管理费");
                manageApportionRecord.setCreateTime(LocalDateTime.now());
                apportionRecords.add(manageApportionRecord);

                o.setPaidAmount(o.getTotalAmount());
                o.setWaitAmount(0L);
            } else {
                o.setPaidAmount(0L);
                o.setWaitAmount(o.getTotalAmount());
            }
        });

        if (assetsLeaseOrderItemService.batchUpdateSelective(assetsLeaseOrderItems) != assetsLeaseOrderItems.size()) {
            LOG.error("跑订单项数据失败 fail。。。");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        LOG.info("****************跑订单项数据结束 success。。。******************");

        if (businessChargeItemService.batchInsert(businessChargeItems) != businessChargeItems.size()) {
            LOG.error("收费项金额分摊失败");
            throw new BusinessException(ResultCode.DATA_ERROR, "收费项金额分摊失败");
        }
        LOG.info("****************跑动态收费项金额数据结束 success。。。******************");

        if (apportionRecordService.batchInsert(apportionRecords) != apportionRecords.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "分摊明细写入失败！");
        }
        LOG.info("****************跑订单项及收费项数据结束 success。。。******************");
        return BaseOutput.success();
    }

    /**
     * 缴费数据处理
     *
     * @param assetsLeaseOrders
     * @return
     */
    private void paymentDataHandler(List<AssetsLeaseOrder> assetsLeaseOrders) {
        List<PaymentOrder> waitAddPaymentOrders = new ArrayList<>();
        List<SettleOrder> waitSettleOrders = new ArrayList<>();
        assetsLeaseOrders.stream().filter(o -> PayStateEnum.PAID.getCode().equals(o.getPayState())).forEach(o -> {
            PaymentOrder paymentOrderCondition = new PaymentOrder();
            paymentOrderCondition.setBusinessId(o.getId());
            paymentOrderCondition.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
            List<PaymentOrder> paymentOrders = paymentOrderService.listByExample(paymentOrderCondition);
            PaymentOrder newPaymentOrder = paymentOrders.stream().filter(po -> YesOrNoEnum.YES.getCode().equals(po.getIsSettle())).findFirst().orElse(null);
            newPaymentOrder.setAmount(o.getPayAmount());
            newPaymentOrder.setCreateTime(LocalDateTime.now());
            newPaymentOrder.setModifyTime(LocalDateTime.now());
            newPaymentOrder.setId(null);
            newPaymentOrder.setVersion(0);
            BaseOutput<String> paymentBizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.PAYMENT_ORDER.getCode());
            if (!paymentBizNumberOutput.isSuccess()) {
                LOG.info("租赁单【编号：{}】,缴费单编号生成异常", o.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
            }
            newPaymentOrder.setCode(o.getMarketCode().toUpperCase() + paymentBizNumberOutput.getData());
            waitAddPaymentOrders.add(newPaymentOrder);

            SettleOrder settleOrder = settlementRpc.getByCode(newPaymentOrder.getSettlementCode()).getData();
            settleOrder.setId(null);
            BaseOutput<String> settlementBizNumberOutput = uidFeignRpc.bizNumber("settleOrder");
            if (!settlementBizNumberOutput.isSuccess()) {
                LOG.info("编号生成失败!" + settlementBizNumberOutput.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "编号生成失败!");
            }
            settleOrder.setCode(o.getMarketCode().toUpperCase() + settlementBizNumberOutput.getData());
            settleOrder.setVersion(0);
            settleOrder.setOrderCode(newPaymentOrder.getCode());
            settleOrder.setAmount(o.getPayAmount());
            settleOrder.setOperatorId(243L);
            settleOrder.setOperatorName("数据迁移");
            settleOrder.setWay(SettleWayEnum.CASH.getCode());
            settleOrder.setOperateTime(LocalDateTime.now());
            waitSettleOrders.add(settleOrder);
        });

        BaseOutput settlemenOutput = settlementRpc.batchSaveDealtAndDelete(waitSettleOrders);
        if (!settlemenOutput.isSuccess()) {
            LOG.info("批量租赁结算单数据重新生产失败{}", settlemenOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, settlemenOutput.getMessage());
        }

        if (paymentOrderService.batchInsert(waitAddPaymentOrders) != waitAddPaymentOrders.size()) {
            LOG.info("批量租赁缴费单数据重新生产失败{}");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        LOG.info("******************租赁单缴费数据处理结束 success。。。****************************");
    }

    @Override
    @Transactional
    public BaseOutput moveDepositAmount() {
        LOG.info("******************保证金数据迁移开始。。。****************************");
        AssetsLeaseOrderItemListDto itemCondition = new AssetsLeaseOrderItemListDto();
        itemCondition.setPayState(PayStateEnum.PAID.getCode());
        itemCondition.setDepositAmountFlags(List.of(DepositAmountFlagEnum.TRANSFERRED.getCode(),DepositAmountFlagEnum.REFUNDED.getCode()));
        List<AssetsLeaseOrderItem> assetsLeaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition).stream().filter(o -> o.getDepositAmount() > 0).collect(Collectors.toList());
        AssetsLeaseOrderListDto orderCondition = new AssetsLeaseOrderListDto();
        orderCondition.setIds(assetsLeaseOrderItems.stream().map(o -> o.getLeaseOrderId()).collect(Collectors.toList()));
        Map<Long, AssetsLeaseOrder> assetsLeaseOrderMap = assetsLeaseOrderService.listByExample(orderCondition).stream().collect(Collectors.toMap(AssetsLeaseOrder::getId, Function.identity()));
        List<DepositOrder> depositOrders = new ArrayList<>();
        assetsLeaseOrderItems.stream().forEach(o -> {
            AssetsLeaseOrder assetsLeaseOrder = assetsLeaseOrderMap.get(o.getLeaseOrderId());
            DepositOrder depositOrder = new DepositOrder();
            depositOrder.setCustomerId(o.getCustomerId());
            depositOrder.setCustomerName(o.getCustomerName());
            depositOrder.setCertificateNumber(assetsLeaseOrder.getCertificateNumber());
            depositOrder.setCustomerCellphone(assetsLeaseOrder.getCustomerCellphone());
            depositOrder.setDepartmentId(assetsLeaseOrder.getDepartmentId());
            DepositTypeEnum dtEnum = DepositTypeEnum.getAssetsTypeEnum(AssetsTypeEnum.getAssetsTypeEnum(o.getAssetsType()).getTypeCode());
            depositOrder.setTypeCode(dtEnum.getTypeCode());
            depositOrder.setTypeName(dtEnum.getTypeName());
            depositOrder.setAssetsType(o.getAssetsType());
            depositOrder.setAssetsId(o.getAssetsId());
            depositOrder.setAssetsName(o.getAssetsName());
            depositOrder.setAmount(o.getDepositAmount());
            depositOrder.setIsRelated(YesOrNoEnum.NO.getCode());
            depositOrder.setBusinessId(assetsLeaseOrder.getId());
            depositOrder.setBizType(AssetsTypeEnum.getAssetsTypeEnum(o.getAssetsType()).getBizType());
            depositOrder.setCreatorId(243L);
            depositOrder.setCreator("数据迁移");
            depositOrder.setMarketId(11L);
            depositOrder.setMarketCode("hzsc");
            depositOrders.add(depositOrder);
        });
        depositOrderService.oldDataHandler(depositOrders);

        //删除之前导入的保证金数据
        AssetsLeaseOrder assetsLeaseOrderDelCondition = new AssetsLeaseOrder();
        assetsLeaseOrderDelCondition.setIsShow(YesOrNoEnum.NO.getCode());
        List<Long> waitDelLeaseOrderIds = assetsLeaseOrderService.listByExample(assetsLeaseOrderDelCondition).stream().map(o -> o.getId()).collect(Collectors.toList());
        assetsLeaseOrderService.delete(waitDelLeaseOrderIds);

        AssetsLeaseOrderItemListDto assetsLeaseOrderItemDelCondition = new AssetsLeaseOrderItemListDto();
        assetsLeaseOrderItemDelCondition.setLeaseOrderIds(waitDelLeaseOrderIds);
        assetsLeaseOrderItemService.deleteByExample(assetsLeaseOrderItemDelCondition);
        LOG.info("******************保证金导入数据已删除。。。****************************");
        LOG.info("******************保证金数据迁移结束 success。。。****************************");
        return BaseOutput.success();
    }

    @Override
    @Transactional
    public BaseOutput refundOrderDataHandler() {
        LOG.info("****************退款单金额及退款收费项数据写入开始。。。******************");
        RefundOrder condition = new RefundOrder();
        condition.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
        List<RefundOrder> refundOrders = refundOrderService.list(condition).stream().filter(o -> null != o.getBusinessItemId()).collect(Collectors.toList());
        AssetsLeaseOrderItemListDto orderItemCondition = new AssetsLeaseOrderItemListDto();
        orderItemCondition.setIds(refundOrders.stream().map(o -> o.getBusinessItemId()).collect(Collectors.toList()));
        Map<Long, AssetsLeaseOrderItem> assetsLeaseOrderItemMap = assetsLeaseOrderItemService.listByExample(orderItemCondition).stream().collect(Collectors.toMap(AssetsLeaseOrderItem::getId, Function.identity()));
        List<RefundFeeItem> refundFeeItems = new ArrayList<>();
        List<RefundOrder> waitModifyRefundOrders = new ArrayList<>();
        refundOrders.forEach(o -> {
            AssetsLeaseOrderItem assetsLeaseOrderItem = assetsLeaseOrderItemMap.get(o.getBusinessItemId());
            RefundFeeItem rentRefundFeeItem = new RefundFeeItem();
            rentRefundFeeItem.setRefundOrderId(o.getId());
            rentRefundFeeItem.setRefundOrderCode(o.getCode());
            rentRefundFeeItem.setChargeItemId(1L);
            rentRefundFeeItem.setChargeItemName("租金");
            rentRefundFeeItem.setAmount(assetsLeaseOrderItem.getRentRefundAmount());
            rentRefundFeeItem.setCreateTime(LocalDateTime.now());
            rentRefundFeeItem.setModifyTime(LocalDateTime.now());
            refundFeeItems.add(rentRefundFeeItem);

            RefundFeeItem manageRefundFeeItem = new RefundFeeItem();
            manageRefundFeeItem.setRefundOrderId(o.getId());
            manageRefundFeeItem.setRefundOrderCode(o.getCode());
            manageRefundFeeItem.setChargeItemId(2L);
            manageRefundFeeItem.setChargeItemName("物业管理费");
            manageRefundFeeItem.setAmount(assetsLeaseOrderItem.getManageRefundAmount());
            manageRefundFeeItem.setCreateTime(LocalDateTime.now());
            manageRefundFeeItem.setModifyTime(LocalDateTime.now());
            refundFeeItems.add(manageRefundFeeItem);

            if (assetsLeaseOrderItem.getDepositRefundAmount() > 0L) {
                o.setTotalRefundAmount(o.getTotalRefundAmount() - assetsLeaseOrderItem.getDepositRefundAmount());
                TransferDeductionItem transferDeductionItemCondition = new TransferDeductionItem();
                transferDeductionItemCondition.setRefundOrderId(o.getId());
                List<TransferDeductionItem> transferDeductionItems = transferDeductionItemService.list(transferDeductionItemCondition);
                Long leaseRefundAmount = o.getPayeeAmount() - assetsLeaseOrderItem.getDepositRefundAmount();
                if (CollectionUtils.isEmpty(transferDeductionItems)) {
                    o.setPayeeAmount(leaseRefundAmount);
                } else {
                    TransferDeductionItem transferDeductionItem = transferDeductionItems.get(0);
                    TransactionDetails transactionDetailsCondition = new TransactionDetails();
                    transactionDetailsCondition.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
                    transactionDetailsCondition.setOrderId(o.getId());
                    TransactionDetails transactionDetails = transactionDetailsService.listByExample(transactionDetailsCondition).stream().findFirst().orElse(null);
                    if (leaseRefundAmount > 0L) {
                        transferDeductionItem.setPayeeAmount(leaseRefundAmount);
                        transferDeductionItemService.updateSelective(transferDeductionItem);
                        transactionDetails.setAmount(leaseRefundAmount);
                        transactionDetailsService.updateSelective(transactionDetails);
                    } else {
                        transferDeductionItemService.delete(transferDeductionItem.getId());
                        transactionDetailsService.delete(transactionDetails.getId());
                    }
                }
                depositAmountRefundDataHandler(o, assetsLeaseOrderItem, transferDeductionItems);
                waitModifyRefundOrders.add(o);
            }
        });

        if (refundFeeItemService.batchInsert(refundFeeItems) != refundFeeItems.size()) {
            LOG.error("退款收费项数据写入失败");
            throw new BusinessException(ResultCode.DATA_ERROR, "退款收费项数据写入失败");
        }

        if (refundOrderService.batchUpdateSelective(waitModifyRefundOrders) != waitModifyRefundOrders.size()) {
            LOG.error("退款单金额数据修改失败");
            throw new BusinessException(ResultCode.DATA_ERROR, "退款单金额数据修改失败失败");
        }

        List<Map<String, Object>> refundAmountMaps = new ArrayList<>();
        waitModifyRefundOrders.forEach(o -> {
            Map<String, Object> refundAmountMap = new HashMap<>();
            refundAmountMap.put(o.getSettlementCode(), o.getPayeeAmount());
            refundAmountMaps.add(refundAmountMap);
        });

        BaseOutput settlementOutput = settlementRpc.batchUpdateAmount(refundAmountMaps);
        if (!settlementOutput.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "结算退款单金额数据修改失败失败");
        }

        LOG.info("****************退款单金额及退款收费项数据写入结束 success。。。******************");
        return BaseOutput.success();
    }

    /**
     * 保证金退款数据处理
     *
     * @param refundOrder
     * @param assetsLeaseOrderItem
     * @param transferDeductionItems
     */
    private void depositAmountRefundDataHandler(RefundOrder refundOrder, AssetsLeaseOrderItem assetsLeaseOrderItem, List<TransferDeductionItem> transferDeductionItems) {
        refundOrder.setId(null);
        refundOrder.setBizType(BizTypeEnum.DEPOSIT_ORDER.getCode());
        TransferDeductionItem transferDeductionItem = null;
        if (CollectionUtils.isEmpty(transferDeductionItems)) {
            refundOrder.setPayeeAmount(assetsLeaseOrderItem.getDepositRefundAmount());
        } else {
            refundOrder.setPayeeAmount(0L);
            transferDeductionItem = transferDeductionItems.get(0);
            transferDeductionItem.setPayeeAmount(assetsLeaseOrderItem.getDepositRefundAmount());
            transferDeductionItem.setId(null);
        }
        refundOrder.setTotalRefundAmount(assetsLeaseOrderItem.getDepositRefundAmount());
        depositOrderService.oldRefundOrderDataHandler(refundOrder, assetsLeaseOrderItem.getAssetsId(), transferDeductionItem);
        LOG.info("****************保证金退款数据处理结束 success。。。******************");
    }

    @Override
    @Transactional
    public BaseOutput deleteLeaseOrder(List<Long> leaseOrderIds) {
        AssetsLeaseOrderListDto orderCondition = new AssetsLeaseOrderListDto();
        orderCondition.setIds(leaseOrderIds);
        List<AssetsLeaseOrder> assetsLeaseOrders = assetsLeaseOrderService.listByExample(orderCondition);
        assetsLeaseOrders.stream().filter( o -> PayStateEnum.PAID.getCode().equals(o.getPayState())).forEach(o -> {
            if (o.getEarnestDeduction() > 0L || o.getTransferDeduction() > 0L) {
                customerAccountService.oldDataHandler(o.getId(), o.getCustomerId(), o.getMarketId(), o.getEarnestDeduction(), o.getTransferDeduction());
            }
            AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
            itemCondition.setLeaseOrderId(o.getId());
            List<AssetsLeaseOrderItem> assetsLeaseOrderItems = assetsLeaseOrderItemService.listByExample(itemCondition);
            assetsLeaseOrderItems.stream()
                .filter(item -> DepositAmountFlagEnum.TRANSFERRED.getCode().equals(item.getDepositAmountFlag())
                        || DepositAmountFlagEnum.REFUNDED.getCode().equals(item.getDepositAmountFlag()))
                .forEach(item -> {
                    AssetsLeaseOrderItem depositAmountSourceItem = assetsLeaseOrderItemService.get(item.getDepositAmountSourceId());
                    if (LeaseRefundStateEnum.REFUNDED.getCode().equals(depositAmountSourceItem.getRefundState())) {
                        depositAmountSourceItem.setDepositAmountFlag(DepositAmountFlagEnum.REFUNDED.getCode());
                    } else {
                        depositAmountSourceItem.setDepositAmountFlag(DepositAmountFlagEnum.TRANSFERRED.getCode());
                    }
                    assetsLeaseOrderItemService.updateSelective(depositAmountSourceItem);
                });

        });
        return null;
    }
}
