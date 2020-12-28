package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.AssetsLeaseOrderItemListDto;
import com.dili.ia.domain.dto.DepositOrderQuery;
import com.dili.ia.domain.dto.printDto.DepositOrdersPrintDataDto;
import com.dili.ia.domain.dto.printDto.LeaseOrderItemPrintDto;
import com.dili.ia.domain.dto.printDto.LeaseOrderPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.glossary.*;
import com.dili.ia.service.*;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.rpc.SettleOrderRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 打印业务逻辑
 */
@Service
public class PrintServiceImpl implements PrintService {
    private final static Logger LOG = LoggerFactory.getLogger(PrintServiceImpl.class);

    @Autowired
    private AssetsLeaseOrderService assetsLeaseOrderService;
    @Autowired
    private AssetsLeaseOrderItemService assetsLeaseOrderItemService;
    @Autowired
    private SettleOrderRpc settleOrderRpc;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private BusinessChargeItemService businessChargeItemService;
    @Autowired
    DepositOrderService depositOrderService;

    /**
     * 查询打印结算票据数据
     *
     * @param orderCode 缴费单编号
     * @param reprint   是否补打
     * @return
     */
    @Override
    public BaseOutput<PrintDataDto> queryPrintLeaseSettlementBillData(String orderCode, Integer reprint) {
        PaymentOrder paymentOrderCondition = new PaymentOrder();
        paymentOrderCondition.setCode(orderCode);
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            LOG.info("租赁单打印异常 【businessCode:{}】无效", orderCode);
            throw new BusinessException(ResultCode.DATA_ERROR, "businessCode无效");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            LOG.info("租赁单打印异常 【businessCode:{}】此单未支付", orderCode);
            return BaseOutput.failure("此单未支付");
        }

        AssetsLeaseOrder leaseOrder = assetsLeaseOrderService.get(paymentOrder.getBusinessId());
        PrintDataDto<LeaseOrderPrintDto> printDataDto = new PrintDataDto<LeaseOrderPrintDto>();
        LeaseOrderPrintDto leaseOrderPrintDto = new LeaseOrderPrintDto();
        leaseOrderPrintDto.setPrintTime(LocalDate.now());
        leaseOrderPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        leaseOrderPrintDto.setLeaseOrderCode(leaseOrder.getCode());
        leaseOrderPrintDto.setBusinessType(BizTypeEnum.getBizTypeEnum(leaseOrder.getBizType()).getName());
        printDataDto.setName(PrintTemplateEnum.LEASE_SETTLEMENT_PAY_BILL.getCode());

        leaseOrderPrintDto.setCustomerName(leaseOrder.getCustomerName());
        leaseOrderPrintDto.setCustomerCellphone(leaseOrder.getCustomerCellphone());
        leaseOrderPrintDto.setStartTime(leaseOrder.getStartTime().toLocalDate());
        leaseOrderPrintDto.setEndTime(leaseOrder.getEndTime().toLocalDate());
        leaseOrderPrintDto.setIsRenew(YesOrNoEnum.getYesOrNoEnum(leaseOrder.getIsRenew()).getName());
        leaseOrderPrintDto.setCategoryName(leaseOrder.getCategoryName());
        leaseOrderPrintDto.setNotes(leaseOrder.getNotes());
        leaseOrderPrintDto.setTotalAmount(MoneyUtils.centToYuan(leaseOrder.getTotalAmount()));
        leaseOrderPrintDto.setWaitAmount(MoneyUtils.centToYuan(leaseOrder.getWaitAmount()));

        //抵扣金额 结算查询
        BaseOutput<SettleOrder> output = settleOrderRpc.getByCode(paymentOrder.getSettlementCode());
        if (output.isSuccess()) {
            leaseOrderPrintDto.setDeduction(MoneyUtils.centToYuan(output.getData().getDeductAmount()));
        }

        leaseOrderPrintDto.setAmount(MoneyUtils.centToYuan(paymentOrder.getAmount()));
        leaseOrderPrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        leaseOrderPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        leaseOrderPrintDto.setSubmitter(paymentOrder.getCreator());
        leaseOrderPrintDto.setSettleWayDetails(this.buildSettleWayDetails(paymentOrder.getSettlementWay(), paymentOrder.getSettlementCode()));

        AssetsLeaseOrderItem leaseOrderItemCondition = new AssetsLeaseOrderItem();
        leaseOrderItemCondition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(leaseOrderItemCondition);
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemMeta(leaseOrder.getBizType(), leaseOrderItems.stream().map(o -> o.getId()).collect(Collectors.toList()));
        leaseOrderPrintDto.setChargeItems(chargeItemDtos);
        List<AssetsLeaseOrderItemListDto> leaseOrderItemListDtos = assetsLeaseOrderItemService.leaseOrderItemListToDto(leaseOrderItems, leaseOrder.getBizType(), chargeItemDtos);
        List<LeaseOrderItemPrintDto> leaseOrderItemPrintDtos = new ArrayList<>();
        leaseOrderItemListDtos.forEach(o -> {
            leaseOrderItemPrintDtos.add(leaseOrderItem2PrintDto(o));
        });
        leaseOrderPrintDto.setLeaseOrderItems(leaseOrderItemPrintDtos);
        printDataDto.setItem(leaseOrderPrintDto);
        return BaseOutput.success().setData(printDataDto);
    }


    /**
     * 订单项AssetsLeaseOrderItemListDto转PrintDto
     *
     * @param leaseOrderItem
     * @return
     */
    private LeaseOrderItemPrintDto leaseOrderItem2PrintDto(AssetsLeaseOrderItemListDto leaseOrderItem) {
        LeaseOrderItemPrintDto leaseOrderItemPrintDto = new LeaseOrderItemPrintDto();
        leaseOrderItemPrintDto.setAssetsName(leaseOrderItem.getAssetsName());
        leaseOrderItemPrintDto.setNumber(leaseOrderItem.getNumber().toString());
        leaseOrderItemPrintDto.setUnitName(leaseOrderItem.getUnitName());
        if (null != leaseOrderItem.getUnitPrice()) {
            leaseOrderItemPrintDto.setUnitPrice(MoneyUtils.centToYuan(leaseOrderItem.getUnitPrice()));
        }
        if (null != leaseOrderItem.getCorner()) {
            leaseOrderItemPrintDto.setIsCorner(CornerEnum.getCornerEnum(leaseOrderItem.getCorner()).getName());
        }
        if (null != leaseOrderItem.getPaymentMonth()) {
            leaseOrderItemPrintDto.setPaymentMonth(leaseOrderItem.getPaymentMonth().toString());
        }
        if (null != leaseOrderItem.getDiscountAmount()) {
            leaseOrderItemPrintDto.setDiscountAmount(MoneyUtils.centToYuan(leaseOrderItem.getDiscountAmount()));
        }
        leaseOrderItemPrintDto.setBusinessChargeItem(leaseOrderItem.getBusinessChargeItem());
        return leaseOrderItemPrintDto;
    }

    /**
     * 订单项AssetsLeaseOrderItem转PrintDto
     *
     * @param leaseOrderItem
     * @return
     */
    private LeaseOrderItemPrintDto leaseOrderItem2PrintDto(AssetsLeaseOrderItem leaseOrderItem) {
        LeaseOrderItemPrintDto leaseOrderItemPrintDto = new LeaseOrderItemPrintDto();
        leaseOrderItemPrintDto.setAssetsName(leaseOrderItem.getAssetsName());
        leaseOrderItemPrintDto.setNumber(leaseOrderItem.getNumber().toString());
        leaseOrderItemPrintDto.setUnitName(leaseOrderItem.getUnitName());
        if (null != leaseOrderItem.getUnitPrice()) {
            leaseOrderItemPrintDto.setUnitPrice(MoneyUtils.centToYuan(leaseOrderItem.getUnitPrice()));
        }
        if (null != leaseOrderItem.getCorner()) {
            leaseOrderItemPrintDto.setIsCorner(CornerEnum.getCornerEnum(leaseOrderItem.getCorner()).getName());
        }
        if (null != leaseOrderItem.getPaymentMonth()) {
            leaseOrderItemPrintDto.setPaymentMonth(leaseOrderItem.getPaymentMonth().toString());
        }
        if (null != leaseOrderItem.getDiscountAmount()) {
            leaseOrderItemPrintDto.setDiscountAmount(MoneyUtils.centToYuan(leaseOrderItem.getDiscountAmount()));
        }
        return leaseOrderItemPrintDto;
    }

    /**
     * 票据获取结算详情
     * 组合支付，结算详情格式 : 【微信 150.00 2020-08-19 4237458467568870 备注：微信付款150元;银行卡 150.00 2020-08-19 4237458467568870 备注：微信付款150元】
     * 园区卡支付，结算详情格式：【卡号：428838247888（李四）】
     * 除了园区卡 和 组合支付 ，结算详情格式：【2020-08-19 4237458467568870 备注：微信付款150元】
     *
     * @param settlementWay  结算方式
     * @param settlementCode 结算详情
     * @return
     */
    private String buildSettleWayDetails(Integer settlementWay, String settlementCode) {
        //组合支付需要显示结算详情
        StringBuffer settleWayDetails = new StringBuffer();
        settleWayDetails.append("【");
        if (settlementWay.equals(SettleWayEnum.MIXED_PAY.getCode())) {
            //摊位租赁单据的交款时间，也就是结算时填写的时间，显示到结算详情中，显示内容为：支付方式（组合支付的，只显示该类型下的具体支付方式）、金额、收款日期、流水号、结算备注，每个字段间隔一个空格；如没填写的则不显示；
            // 多个支付方式的，均在一行显示，当前行满之后换行，支付方式之间用;隔开；
            BaseOutput<List<SettleWayDetail>> output = settleOrderRpc.listSettleWayDetailsByCode(settlementCode);
            List<SettleWayDetail> swdList = output.getData();
            if (output.isSuccess() && CollectionUtils.isNotEmpty(swdList)) {
                for (SettleWayDetail swd : swdList) {
                    //此循环字符串拼接顺序不可修改，组合支付，结算详情格式 : 【微信 150.00 2020-08-19 4237458467568870 备注：微信付款150元;银行卡 150.00 2020-08-19 4237458467568870 备注：微信付款150元】
                    settleWayDetails.append(SettleWayEnum.getNameByCode(swd.getWay())).append(" ").append(MoneyUtils.centToYuan(swd.getAmount()));
                    if (null != swd.getChargeDate()) {
                        settleWayDetails.append(" ").append(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(swd.getChargeDate()));
                    }
                    if (StringUtils.isNotEmpty(swd.getSerialNumber())) {
                        settleWayDetails.append(" ").append(swd.getSerialNumber());
                    }
                    if (StringUtils.isNotEmpty(swd.getNotes())) {
                        settleWayDetails.append(" ").append("备注：").append(swd.getNotes());
                    }
                    settleWayDetails.append("；");
                }
                //去掉最后一个; 符
                settleWayDetails.replace(settleWayDetails.length() - 1, settleWayDetails.length(), " ");
            } else {
                LOG.info("查询结算微服务组合支付，支付详情失败；原因：{}", output.getMessage());
            }
        } else if (settlementWay.equals(SettleWayEnum.CARD.getCode())) {
            // 园区卡支付，结算详情格式：【卡号：428838247888（李四）】
            BaseOutput<SettleOrder> output = settleOrderRpc.getByCode(settlementCode);
            if (output.isSuccess()) {
                SettleOrder settleOrder = output.getData();
                if (null != settleOrder.getTradeCardNo()) {
                    settleWayDetails.append("卡号:" + settleOrder.getTradeCardNo());
                }
                if (StringUtils.isNotBlank(settleOrder.getTradeCustomerName())) {
                    settleWayDetails.append("（").append(settleOrder.getTradeCustomerName()).append("）");
                }
            } else {
                LOG.info("查询结算微服务非组合支付，支付详情失败；原因：{}", output.getMessage());
            }
        } else {
            // 除了园区卡 和 组合支付 ，结算详情格式：【2020-08-19 4237458467568870 备注：微信付款150元】
            BaseOutput<SettleOrder> output = settleOrderRpc.getByCode(settlementCode);
            if (output.isSuccess()) {
                SettleOrder settleOrder = output.getData();
                if (null != settleOrder.getChargeDate()) {
                    settleWayDetails.append(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(settleOrder.getChargeDate()));
                }
                if (StringUtils.isNotBlank(settleOrder.getSerialNumber())) {
                    settleWayDetails.append(" ").append(settleOrder.getSerialNumber());
                }
                if (StringUtils.isNotBlank(settleOrder.getNotes())) {
                    settleWayDetails.append(" ").append("备注：").append(settleOrder.getNotes());
                }
            } else {
                LOG.info("查询结算微服务非组合支付，支付详情失败；原因：{}", output.getMessage());
            }
        }
        settleWayDetails.append("】");
        if (StringUtils.isNotEmpty(settleWayDetails) && settleWayDetails.length() > 2) { // 长度大于2 是因为，避免内容为空，显示成 【】
            return settleWayDetails.toString();
        }
        return "";
    }

    /**
     * 查询打印合同签订单数据（包含保证金）
     *
     * @param leaseOrderId 租赁单ID
     * @return
     */
    @Override
    public BaseOutput<PrintDataDto> queryPrintLeaseContractSigningBillData(Long leaseOrderId) {
        AssetsLeaseOrder leaseOrder = assetsLeaseOrderService.get(leaseOrderId);
        AssetsLeaseOrderItem leaseOrderItemCondition = new AssetsLeaseOrderItem();
        leaseOrderItemCondition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(leaseOrderItemCondition);
        checkPrintCondition(leaseOrder,leaseOrderItems);

        PaymentOrder paymentOrder = null;
        if (null != leaseOrder.getPaymentId()) {
            paymentOrder = paymentOrderService.get(leaseOrder.getPaymentId());
        }
        PrintDataDto<LeaseOrderPrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.LEASE_CONTRACT_SIGNING_BILL.getCode());
        LeaseOrderPrintDto leaseOrderPrintDto = buildLeaseOrderPrintDto(leaseOrder, leaseOrderItems, paymentOrder, true);
        printDataDto.setItem(leaseOrderPrintDto);
        return BaseOutput.success().setData(printDataDto);
    }

    /**
     * 租赁定金单打印数据查询（包含保证金）
     *
     * @param leaseOrderId 租赁单ID
     * @return
     */
    @Override
    public BaseOutput<PrintDataDto> queryPrintLeasePaymentBillData(Long leaseOrderId) {
        AssetsLeaseOrder leaseOrder = assetsLeaseOrderService.get(leaseOrderId);
        AssetsLeaseOrderItem leaseOrderItemCondition = new AssetsLeaseOrderItem();
        leaseOrderItemCondition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetsLeaseOrderItemService.list(leaseOrderItemCondition);
        checkPrintCondition(leaseOrder,leaseOrderItems);

        PaymentOrder paymentOrder = null;
        if (null != leaseOrder.getPaymentId()) {
            paymentOrder = paymentOrderService.get(leaseOrder.getPaymentId());
        }
        PrintDataDto<LeaseOrderPrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.LEASE_PAYMENT_BILL.getCode());
        LeaseOrderPrintDto leaseOrderPrintDto = buildLeaseOrderPrintDto(leaseOrder, leaseOrderItems, paymentOrder, false);
        printDataDto.setItem(leaseOrderPrintDto);
        return BaseOutput.success().setData(printDataDto);
    }

    /**
     * 业务单打印检查
     * @param leaseOrder
     * @param leaseOrderItems
     */
    private void checkPrintCondition (AssetsLeaseOrder leaseOrder, List<AssetsLeaseOrderItem> leaseOrderItems) {
        if (LeaseOrderStateEnum.INVALIDATED.getCode().equals(leaseOrder.getState()) || LeaseOrderStateEnum.CANCELD.getCode().equals(leaseOrder.getState()) ) {
            throw new BusinessException(ResultCode.DATA_ERROR, "已作废或已取消状态不能打印");
        }
        if (!LeaseRefundStateEnum.WAIT_APPLY.getCode().equals(leaseOrder.getRefundState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "发起过退款不能打印");
        }
        if (leaseOrderItems.stream().filter(o -> !StopRentStateEnum.NO_APPLY.getCode().equals(o.getStopRentState())).collect(Collectors.toList()).size() > 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "发起过停租不能打印");
        }
        if (!depositOrderService.checkDepositOrdersState(leaseOrder.getBizType(), leaseOrder.getId())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "保证金单已退款、已作废或已取消不能打印");
        }
    }

    /**
     * 构造租赁单打印数据对象
     *
     * @param leaseOrder
     * @param leaseOrderItems
     * @param paymentOrder
     * @param isBuildChargeItem 是否构建收费项目
     * @return
     */
    private LeaseOrderPrintDto buildLeaseOrderPrintDto(AssetsLeaseOrder leaseOrder, List<AssetsLeaseOrderItem> leaseOrderItems, PaymentOrder paymentOrder, Boolean isBuildChargeItem) {
        LeaseOrderPrintDto leaseOrderPrintDto = new LeaseOrderPrintDto();
        leaseOrderPrintDto.setBusinessType(BizTypeEnum.EARNEST.getName());
        leaseOrderPrintDto.setPrintTime(LocalDate.now());
        leaseOrderPrintDto.setLeaseOrderCode(leaseOrder.getCode());
        leaseOrderPrintDto.setCustomerName(leaseOrder.getCustomerName());
        leaseOrderPrintDto.setCustomerCellphone(leaseOrder.getCustomerCellphone());
        leaseOrderPrintDto.setStartTime(leaseOrder.getStartTime().toLocalDate());
        leaseOrderPrintDto.setEndTime(leaseOrder.getEndTime().toLocalDate());
        leaseOrderPrintDto.setIsRenew(YesOrNoEnum.getYesOrNoEnum(leaseOrder.getIsRenew()).getName());
        leaseOrderPrintDto.setCategoryName(leaseOrder.getCategoryName());
        leaseOrderPrintDto.setNotes(leaseOrder.getNotes());
        leaseOrderPrintDto.setManager(leaseOrder.getManager());
        leaseOrderPrintDto.setSubmitter(null != paymentOrder ? paymentOrder.getCreator() : null);
        leaseOrderPrintDto.setTransferDeduction(MoneyUtils.centToYuan(0));



        //保证金单查询
        DepositOrderQuery depositOrderQuery = new DepositOrderQuery();
        depositOrderQuery.setBusinessId(leaseOrder.getId());
        depositOrderQuery.setStateNotEquals(DepositOrderStateEnum.CANCELD.getCode());
        List<DepositOrder> depositOrders = depositOrderService.listByExample(depositOrderQuery);
        Map<Long, DepositOrder> depositOrderMap = depositOrders.stream().collect(Collectors.toMap(DepositOrder::getAssetsId, Function.identity()));
        Long depositMakeUpAmountTotal = depositOrders.stream().mapToLong(DepositOrder::getAmount).sum(); //保证金补交金额
        Long depositPaidAmountTotal = depositOrders.stream().mapToLong(DepositOrder::getPaidAmount).sum(); //保证金补交已交金额
        Long depositSnapshotAmountTotal = leaseOrderItems.stream().mapToLong(AssetsLeaseOrderItem::getDepositBalance).sum(); //保证金快照

        //计算本期付款金额
        BaseOutput<DepositOrdersPrintDataDto> depositOrdersPrintDataDtoBaseOutput = depositOrderService.findDepositOrdersPrintData(leaseOrder.getBizType(),leaseOrder.getId());
        if (!depositOrdersPrintDataDtoBaseOutput.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, depositOrdersPrintDataDtoBaseOutput.getMessage());
        }
        DepositOrdersPrintDataDto depositOrdersPrintDataDto = depositOrdersPrintDataDtoBaseOutput.getData();
        if (depositOrdersPrintDataDto.getTotalSubmitAmount().equals(0L) && null == paymentOrder) {
            leaseOrderPrintDto.setAmount(MoneyUtils.centToYuan(leaseOrder.getWaitAmount() + depositOrdersPrintDataDto.getTotalWaitAmount()));
        } else {
            leaseOrderPrintDto.setAmount(MoneyUtils.centToYuan(null == paymentOrder?0L : paymentOrder.getAmount() + depositOrdersPrintDataDto.getTotalSubmitAmount()));
        }

        leaseOrderPrintDto.setDepositDeduction(MoneyUtils.centToYuan(depositSnapshotAmountTotal));
        //定金转抵 = 定金抵扣 + 租赁已交金额 + 保证金补交已交金额
        leaseOrderPrintDto.setEarnestDeduction(MoneyUtils.centToYuan(leaseOrder.getPaidAmount() + depositPaidAmountTotal));
        leaseOrderPrintDto.setTotalAmount(MoneyUtils.centToYuan(leaseOrder.getTotalAmount() + depositSnapshotAmountTotal + depositMakeUpAmountTotal));
        //实付金额 = 租赁实付金额 + 补交保证金金额
        leaseOrderPrintDto.setPayAmount(MoneyUtils.centToYuan(leaseOrder.getTotalAmount() + depositMakeUpAmountTotal));
        if (isBuildChargeItem) {
            List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemMeta(leaseOrder.getBizType(), leaseOrderItems.stream().map(o -> o.getId()).collect(Collectors.toList()));
            leaseOrderPrintDto.setChargeItems(chargeItemDtos);
            List<AssetsLeaseOrderItemListDto> leaseOrderItemListDtos = assetsLeaseOrderItemService.leaseOrderItemListToDto(leaseOrderItems, leaseOrder.getBizType(), chargeItemDtos);
            leaseOrderPrintDto.setLeaseOrderItems(leaseOrderItemList2PrintDtoList(depositOrderMap, leaseOrderItemListDtos));
        } else {
            leaseOrderPrintDto.setLeaseOrderItems(leaseOrderItemList2PrintDtoList(depositOrderMap, leaseOrderItems));
        }
        return leaseOrderPrintDto;
    }

    /**
     * 资产订单项集合转资产打印订单项集合
     *
     * @param depositOrderMap
     * @param assetsLeaseOrderItems
     * @return
     */
    private List<LeaseOrderItemPrintDto> leaseOrderItemList2PrintDtoList(Map<Long, DepositOrder> depositOrderMap, List<? extends AssetsLeaseOrderItem> assetsLeaseOrderItems) {
        List<LeaseOrderItemPrintDto> leaseOrderItemPrintDtos = new ArrayList<>();
        assetsLeaseOrderItems.forEach(o -> {
            LeaseOrderItemPrintDto leaseOrderItemPrintDto = null;
            if (o instanceof AssetsLeaseOrderItemListDto) {
                leaseOrderItemPrintDto = leaseOrderItem2PrintDto((AssetsLeaseOrderItemListDto) o);
            } else {
                leaseOrderItemPrintDto = leaseOrderItem2PrintDto(o);
            }
            if (depositOrderMap.containsKey(o.getAssetsId())) {
                DepositOrder depositOrder = depositOrderMap.get(o.getAssetsId());
                //保证金补交金额
                leaseOrderItemPrintDto.setDepositMakeUpAmount(MoneyUtils.centToYuan(depositOrder.getAmount()));
            } else {
                leaseOrderItemPrintDto.setDepositMakeUpAmount(MoneyUtils.centToYuan(0L));
            }

            //保证金快照
            leaseOrderItemPrintDto.setDepositBalance(MoneyUtils.centToYuan(o.getDepositBalance()));
            leaseOrderItemPrintDtos.add(leaseOrderItemPrintDto);
        });
        return leaseOrderItemPrintDtos;
    }


}
