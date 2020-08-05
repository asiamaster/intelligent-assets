package com.dili.ia.service.impl;

import com.dili.ia.domain.ApportionRecord;
import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PayStateEnum;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.*;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    SettlementRpc settlementRpc;

    @Override
    @Transactional
    public BaseOutput leaseOrderDataHandler() {
        LOG.info("****************跑订单数据开始。。。******************");
        AssetsLeaseOrder condition = new AssetsLeaseOrder();
        condition.setPayState(PayStateEnum.PAID.getCode());
        List<AssetsLeaseOrder> assetsLeaseOrders = assetsLeaseOrderService.list(condition).stream().filter(o -> o.getDepositDeduction() > 0 || o.getDepositAmount() > 0).collect(Collectors.toList());
        assetsLeaseOrders.stream().forEach(o -> {
            o.setPayAmount(o.getPayAmount() + o.getDepositDeduction() - o.getDepositAmount());
            o.setTotalAmount(o.getPayAmount() + o.getTransferDeduction() + o.getEarnestDeduction());
            o.setPaidAmount(o.getPayAmount());
        });
        if (assetsLeaseOrderService.batchUpdateSelective(assetsLeaseOrders) != assetsLeaseOrders.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        LOG.info("****************跑订单数据结束 success。。。******************");
        return BaseOutput.success();
    }

    @Override
    public BaseOutput moveDepositAmount() {
        return null;
    }

    @Override
    @Transactional
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
        LOG.info("****************跑收费项分摊明细数据结束 success。。。******************");
        LOG.info("****************跑订单项及收费项数据结束 success。。。******************");
        return BaseOutput.success();
    }
}
