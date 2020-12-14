package com.dili.ia.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.ia.domain.OtherFee;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.OtherFeeDto;
import com.dili.ia.domain.dto.OtherFeeRefundOrderDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.domain.dto.printDto.OtherFeePrintDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.OtherFeeStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.OtherFeeMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.OtherFeeService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/8/18
 * @version:      农批业务系统重构
 * @description:  其他收费实现类
 */
@Service
public class OtherFeeServiceImpl extends BaseServiceImpl<OtherFee, Long> implements OtherFeeService {

    private final static Logger logger = LoggerFactory.getLogger(OtherFeeServiceImpl.class);

    public OtherFeeMapper getActualDao() {
        return (OtherFeeMapper) getDao();
    }

    @Autowired
    private UidRpcResolver uidRpcResolver;

    @Autowired
    private DepartmentRpc departmentRpc;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private SettlementRpcResolver settlementRpcResolver;

    @Value("${settlement.app-id}")
    private Long settlementAppId;

    private String settlerHandlerUrl = "http://ia.diligrp.com:8381/api/otherFee/settlementDealHandler";

    /**
     * 根据code查询数据实例
     *
     * @param  code
     * @return OtherFee
     * @date   2020/8/27
     */
    @Override
    public OtherFee getOtherFeeByCode(String code) {
        return this.getActualDao().getOtherFeeByCode(code);
    }

    /**
     * 新增 其他收费
     *
     * @param  otherFeeDto
     * @param  userTicket
     * @return OtherFee
     * @date   2020/8/18
     */
    @Override
    @GlobalTransactional
    public OtherFee addOtherFee(OtherFeeDto otherFeeDto, UserTicket userTicket){
        OtherFee otherFeeParam = new OtherFee();
        BeanUtils.copyProperties(otherFeeDto, otherFeeParam);

        // 生成 其他收费 code
        String otherFeeCode = uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.OTHER_FEE.getCode());
        otherFeeParam.setVersion(0);
        otherFeeParam.setCode(otherFeeCode);
        otherFeeParam.setCreatorId(userTicket.getId());
        otherFeeParam.setCreateTime(LocalDateTime.now());
        otherFeeParam.setModifyTime(LocalDateTime.now());
        otherFeeParam.setMarketId(userTicket.getFirmId());
        otherFeeParam.setCreator(userTicket.getRealName());
        otherFeeParam.setMarketCode(userTicket.getFirmCode());
        otherFeeParam.setState(OtherFeeStateEnum.CREATED.getCode());

        this.getActualDao().insertSelective(otherFeeParam);

        return otherFeeParam;
    }

    /**
     * 修改 其他收费
     *
     * @param  otherFeeDto
     * @param  userTicket
     * @return OtherFee
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public OtherFee updateOtherFee(OtherFeeDto otherFeeDto, UserTicket userTicket) throws BusinessException {
        OtherFee otherFeeParam = new OtherFee();
        BeanUtils.copyProperties(otherFeeDto, otherFeeParam);

        OtherFee otherFeeInfo = this.get(otherFeeDto.getId());
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，修改失败。");
        }

        // 已创建状态才能修改
        if (!OtherFeeStateEnum.CREATED.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能修改。");
        }

        // 修改操作
        otherFeeParam.setModifyTime(LocalDateTime.now());

        if (this.updateSelective(otherFeeParam) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        return otherFeeInfo;
    }

    /**
     * 提交 其他收费
     *
     * @param  id
     * @param  userTicket
     * @return OtherFee
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public OtherFee submit(Long id, UserTicket userTicket) throws BusinessException {
        OtherFee otherFeeInfo = this.get(id);
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，提交失败。");
        }

        // 已创建状态才能提交
        if (!OtherFeeStateEnum.CREATED.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能提交。");
        }

        // 修改其他交费状态为已提交
        otherFeeInfo.setState(OtherFeeStateEnum.SUBMITTED.getCode());
        otherFeeInfo.setModifyTime(LocalDateTime.now());
        if (this.updateSelective(otherFeeInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        // 创建缴费单
        PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket, BizTypeEnum.OTHER_FEE);
        paymentOrder.setBusinessId(otherFeeInfo.getId());
        paymentOrder.setAmount(otherFeeInfo.getAmount());
        paymentOrder.setBusinessCode(otherFeeInfo.getCode());
        paymentOrder.setBizType(BizTypeEnum.OTHER_FEE.getCode());
        paymentOrderService.insertSelective(paymentOrder);

        // 调用结算接口,缴费
        SettleOrderDto settleOrderDto = this.buildSettleOrderDto(userTicket, otherFeeInfo, paymentOrder.getCode(), paymentOrder.getAmount());
        settlementRpcResolver.submit(settleOrderDto);

        return otherFeeInfo;
    }

    /**
     * 取消 其他收费
     *
     * @param  id
     * @param  userTicket
     * @return OtherFee
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public OtherFee cancel(Long id, UserTicket userTicket) throws BusinessException{
        OtherFee otherFeeInfo = this.get(id);
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，取消失败。");
        }

        // 已创建状态才能取消
        if (!OtherFeeStateEnum.CREATED.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能取消。");
        }

        otherFeeInfo.setCancelerId(userTicket.getId());
        otherFeeInfo.setCanceler(userTicket.getRealName());
        otherFeeInfo.setModifyTime(LocalDateTime.now());
        otherFeeInfo.setState(OtherFeeStateEnum.CANCELD.getCode());
        if (this.updateSelective(otherFeeInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        return otherFeeInfo;
    }

    /**
     * 撤回 其他收费
     *
     * @param  id
     * @param  userTicket
     * @return OtherFee
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public OtherFee withdraw(Long id, UserTicket userTicket) throws BusinessException {
        OtherFee otherFeeInfo = this.get(id);
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，撤回失败。");
        }

        // 已创建状态才能取消
        if (!OtherFeeStateEnum.SUBMITTED.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能撤回。");
        }

        otherFeeInfo.setWithdrawOperatorId(userTicket.getId());
        otherFeeInfo.setWithdrawOperator(userTicket.getRealName());
        otherFeeInfo.setState(OtherFeeStateEnum.CREATED.getCode());
        if (this.updateSelective(otherFeeInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 撤销缴费单
        PaymentOrder paymentOrder = this.findPaymentOrder(userTicket, otherFeeInfo.getId(), otherFeeInfo.getCode());
        paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
        if (paymentOrderService.updateSelective(paymentOrder) == 0) {
            logger.info("撤回通行证【删除缴费单】失败.");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 撤回结算单多人操作已判断
        settlementRpcResolver.cancel(settlementAppId, paymentOrder.getCode());

        return otherFeeInfo;
    }

    /**
     * 其他收费 退款申请
     *
     * @param  refundOrderDto
     * @param userTicket
     * @return OtherFee
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public OtherFee refund(OtherFeeRefundOrderDto refundOrderDto, UserTicket userTicket) throws BusinessException {
        // 根据业务主键查询相关数据
        OtherFee otherFeeInfo = this.get(refundOrderDto.getBusinessId());
        if (otherFeeInfo != null && !OtherFeeStateEnum.PAID.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        // 构建退款单以及新增
        this.buildRefundOrderDto(userTicket, otherFeeInfo, refundOrderDto);

        // 修改状态
        otherFeeInfo.setModifyTime(LocalDateTime.now());
        otherFeeInfo.setState(OtherFeeStateEnum.REFUNDING.getCode());
        if (this.updateSelective(otherFeeInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return otherFeeInfo;
    }

    /**
     * 缴费成功回调
     *
     * @param  settleOrder
     * @return OtherFee
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public OtherFee settlementDealHandler(SettleOrder settleOrder) throws BusinessException {
        // 修改缴费单相关数据
        if (null == settleOrder) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "回调参数为空！");
        }
        PaymentOrder paymentOrder = new PaymentOrder();
        // 结算单code唯一
        paymentOrder.setCode(settleOrder.getOrderCode());
        paymentOrder.setBizType(BizTypeEnum.OTHER_FEE.getCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null);
        OtherFee otherFeeInfo = this.get(paymentOrderPO.getBusinessId());
        // 如果已支付，直接返回
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) {
            return otherFeeInfo;
        }
        if (!paymentOrderPO.getState().equals(PaymentOrderStateEnum.NOT_PAID.getCode())) {
            logger.info("缴费单状态已变更！状态为：" + PaymentOrderStateEnum.getPaymentOrderStateEnum(paymentOrderPO.getState()).getName());
            throw new BusinessException(ResultCode.DATA_ERROR, "缴费单状态已变更！");
        }

        // 缴费单数据更新
        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setPayedTime(settleOrder.getOperateTime());
        paymentOrderPO.setSettlementCode(settleOrder.getCode());
        paymentOrderPO.setSettlementOperator(settleOrder.getOperatorName());
        paymentOrderPO.setSettlementWay(settleOrder.getWay());
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            logger.info("缴费单成功回调 -- 更新【缴费单】,乐观锁生效！【付款单paymentOrderID:{}】", paymentOrderPO.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 修改其他收费状态
        otherFeeInfo.setModifyTime(LocalDateTime.now());
        otherFeeInfo.setState(OtherFeeStateEnum.PAID.getCode());
        if (this.updateSelective(otherFeeInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return otherFeeInfo;
    }

    /**
     * 缴费成功票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public PrintDataDto<OtherFeePrintDto> receiptPaymentData(String orderCode, Integer reprint) throws BusinessException {
        PaymentOrder paymentOrderCondition = new PaymentOrder();

        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.OTHER_FEE.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new BusinessException(ResultCode.DATA_ERROR, "businessCode无效");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付!");
        }

        // 组装数据
        OtherFee otherFeeInfo = this.get(paymentOrder.getBusinessId());
        SettleOrder order = settlementRpcResolver.get(settlementAppId, otherFeeInfo.getCode());
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "水电费单不存在!");
        }
        OtherFeePrintDto otherFeePrintDto = new OtherFeePrintDto();
        otherFeePrintDto.setPrintTime(LocalDateTime.now());
        otherFeePrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        otherFeePrintDto.setCode(otherFeeInfo.getCode());
        otherFeePrintDto.setCustomerName(otherFeeInfo.getCustomerName());
        otherFeePrintDto.setCustomerCellphone(otherFeeInfo.getCustomerCellphone());
        otherFeePrintDto.setAmount(MoneyUtils.centToYuan(otherFeeInfo.getAmount()));
        otherFeePrintDto.setBusinessType(BizTypeEnum.OTHER_FEE.getName());
        otherFeePrintDto.setChargeItemName(otherFeeInfo.getChargeItemName());

        // 支付方式
        String settleDetails = "";
        if (SettleWayEnum.CARD.getCode() == order.getWay()) {
            // 园区卡支付
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【卡号：" + order.getAccountNumber() +
                    "（" + order.getCustomerName() + "）】";
        } else if (SettleWayEnum.CASH.getCode() == order.getWay()) {
            // 现金
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【" + order.getChargeDate() + "  流水号：" + order.getSerialNumber() + "  备注："
                    + order.getNotes() + "】";
        }
        otherFeePrintDto.setSettleWayDetails(settleDetails);

        otherFeePrintDto.setNotes(otherFeeInfo.getNotes());
        otherFeePrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        otherFeePrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        otherFeePrintDto.setSubmitter(paymentOrder.getCreator());

        PrintDataDto<OtherFeePrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.OTHER_FEE_PAY.getCode());
        printDataDto.setItem(otherFeePrintDto);

        return printDataDto;
    }

    /**
     * 退款成功票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/8/28
     */
    @Override
    @GlobalTransactional
    public PrintDataDto<OtherFeePrintDto> receiptRefundPrintData(String orderCode, String reprint) throws BusinessException {
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setCode(orderCode);
        List<RefundOrder> refundOrders = refundOrderService.list(refundOrder);
        if (CollectionUtil.isEmpty(refundOrders)) {
            throw new BusinessException(ResultCode.DATA_ERROR, "未查询到退款单!");
        } else {
            RefundOrder refundOrderInfo = refundOrders.get(0);
            OtherFee otherFeeInfo = this.get(refundOrderInfo.getBusinessId());
            SettleOrder order = settlementRpcResolver.get(settlementAppId, otherFeeInfo.getCode());

            // 组装退款单信息
            OtherFeePrintDto otherFeePrintDto = new OtherFeePrintDto();
            otherFeePrintDto.setReprint(reprint);
            otherFeePrintDto.setCode(otherFeeInfo.getCode());
            otherFeePrintDto.setPrintTime(LocalDateTime.now());
            otherFeePrintDto.setCustomerName(otherFeeInfo.getCustomerName());
            otherFeePrintDto.setCustomerCellphone(otherFeeInfo.getCustomerCellphone());
            otherFeePrintDto.setAmount(String.valueOf(otherFeeInfo.getAmount()));
            otherFeePrintDto.setBusinessType(BizTypeEnum.OTHER_FEE.getName());
            otherFeePrintDto.setChargeItemName(otherFeeInfo.getChargeItemName());

            // 退款方式
            String settleDetails = "收款人：" + refundOrderInfo.getPayee() + "金额：" + refundOrderInfo.getPayeeAmount();
            if (SettleWayEnum.CARD.getCode() == order.getWay()) {
                // 园区卡支付
                settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     园区卡号：" + order.getAccountNumber();
            } else if (SettleWayEnum.CASH.getCode() == order.getWay()) {
                // 现金
                settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay());
            } else if (SettleWayEnum.CASH.getCode() == order.getWay())  {
                // 银行卡
                settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "  开户行：" + order.getBankName() + "  银行卡号：" + order.getBankCardHolder();
            }
            otherFeePrintDto.setSettleWayDetails(settleDetails);

            otherFeePrintDto.setNotes(otherFeeInfo.getNotes());
            otherFeePrintDto.setRefundReason(refundOrderInfo.getRefundReason());
            otherFeePrintDto.setSettlementOperator(order.getOperatorName());

            // 打印最外层
            PrintDataDto<OtherFeePrintDto> printDataDto = new PrintDataDto<>();
            printDataDto.setName(PrintTemplateEnum.OTHER_FEE_REFUND.getName());
            printDataDto.setItem(otherFeePrintDto);

            return printDataDto;
        }
    }

    /**
     * 构建退款项
     */
    private RefundOrder buildRefundOrderDto(UserTicket userTicket, OtherFee otherFeeInfo, OtherFeeRefundOrderDto refundOrderDto) throws BusinessException {
        //退款单
        RefundOrder refundOrder = new RefundOrder();

        refundOrder.setMarketId(userTicket.getFirmId());
        refundOrder.setMarketCode(userTicket.getFirmCode());

        refundOrder.setBusinessId(otherFeeInfo.getId());
        refundOrder.setBusinessCode(otherFeeInfo.getCode());
        refundOrder.setCustomerId(otherFeeInfo.getCustomerId());
        refundOrder.setCustomerName(otherFeeInfo.getCustomerName());
        refundOrder.setCustomerCellphone(otherFeeInfo.getCustomerCellphone());
        refundOrder.setCertificateNumber(otherFeeInfo.getCertificateNumber());

        refundOrder.setPayee(refundOrderDto.getPayee());
        refundOrder.setPayeeId(refundOrderDto.getPayeeId());
        refundOrder.setRefundType(refundOrderDto.getRefundType());
        refundOrder.setPayeeAmount(refundOrderDto.getPayeeAmount());
        refundOrder.setRefundReason(refundOrderDto.getRefundReason());
        refundOrder.setTotalRefundAmount(refundOrderDto.getTotalRefundAmount());

        refundOrder.setBizType(BizTypeEnum.OTHER_FEE.getCode());
        refundOrder.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.getBizTypeEnum(BizTypeEnum.OTHER_FEE.getCode()).getEnName() + "_" + BizNumberTypeEnum.REFUND_ORDER.getCode()));

        if (!refundOrderService.doAddHandler(refundOrder).isSuccess()) {
            logger.info("其他收费【编号：{}】退款申请接口异常", refundOrder.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常");
        }
        return refundOrder;
    }

    /**
     * 构建缴费单，提交
     */
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, OtherFee otherFeeInfo, String orderCode, Long amount) {

        SettleOrderInfoDto settleOrderInfoDto = new SettleOrderInfoDto(userTicket, BizTypeEnum.OTHER_FEE, SettleTypeEnum.PAY, SettleStateEnum.WAIT_DEAL);
        settleOrderInfoDto.setMarketId(otherFeeInfo.getMarketId());
        settleOrderInfoDto.setMarketCode(userTicket.getFirmCode());
        settleOrderInfoDto.setBusinessCode(otherFeeInfo.getCode());

        settleOrderInfoDto.setAmount(amount);
        settleOrderInfoDto.setOrderCode(orderCode);
        settleOrderInfoDto.setAppId(settlementAppId);

        settleOrderInfoDto.setBusinessDepId(otherFeeInfo.getDepartmentId());
        settleOrderInfoDto.setBusinessDepName(otherFeeInfo.getDepartmentName());

        settleOrderInfoDto.setCustomerId(otherFeeInfo.getCustomerId());
        settleOrderInfoDto.setCustomerName(otherFeeInfo.getCustomerName());
        settleOrderInfoDto.setCustomerPhone(otherFeeInfo.getCustomerCellphone());

        settleOrderInfoDto.setSubmitterId(userTicket.getId());
        settleOrderInfoDto.setSubmitterName(userTicket.getRealName());
//        settleOrderInfoDto.setBusinessType(Integer.valueOf(BizTypeEnum.OTHER_FEE.getCode()));
        settleOrderInfoDto.setType(SettleTypeEnum.PAY.getCode());
        settleOrderInfoDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
//        settleOrderInfoDto.setReturnUrl(settlerHandlerUrl);
        if (userTicket.getDepartmentId() != null) {
            settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }

        return settleOrderInfoDto;
    }

    /**
     * 构建撤销缴费单
     */
    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode) throws BusinessException {
        PaymentOrder pb = new PaymentOrder();

        pb.setBizType(BizTypeEnum.OTHER_FEE.getCode());
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(userTicket.getFirmId());
        pb.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        PaymentOrder paymentOrder = paymentOrderService.listByExample(pb).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            logger.info("没有查询到付款单PaymentOrder【业务单businessId：{}】 【业务单businessCode:{}】", businessId, businessCode);
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到付款单！");
        }

        return paymentOrder;
    }
}