package com.dili.ia.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.assets.sdk.dto.TypeMarketDto;
import com.dili.assets.sdk.rpc.TypeMarketRpc;
import com.dili.ia.domain.Passport;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.PassportDto;
import com.dili.ia.domain.dto.PassportRefundOrderDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.PassportPrintDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PassportStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.PassportMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.PassportService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.ChargeItemEnum;
import com.dili.settlement.enums.EnableEnum;
import com.dili.settlement.enums.LinkTypeEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.rpc.SettleOrderRpc;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/7/27
 * @version:     农批业务系统重构
 * @description: 通行证
 */
@Service
public class PassportServiceImpl extends BaseServiceImpl<Passport, Long> implements PassportService {

    public PassportMapper getActualDao() {
        return (PassportMapper) getDao();
    }

    private final static Logger logger = LoggerFactory.getLogger(PassportServiceImpl.class);

    @Autowired
    private TypeMarketRpc typeMarketRpc;

    @Autowired
    private DepartmentRpc departmentRpc;

    @Autowired
    private SettleOrderRpc settleOrderRpc;

    @Autowired
    private UidRpcResolver uidRpcResolver;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private RefundOrderService refundOrderService;

    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${passport.settlement.handler.url}")
    private String settlerHandlerUrl;
    @Value("${passport.settlement.view.url}")
    private String settleViewUrl;
    @Value("${passport.settlement.print.url}")
    private String settlerPrintUrl;
    @Value("${passport.refundOrder.settlement.print.url}")
    private String refundSettlerPrintUrl;

    /**
     * 根据 code 查询通行证业务单
     *
     * @param  code
     * @return PassportDto
     * @date   2020/7/27
     */
    @Override
    public Passport getPassportByCode(String code) {
        return this.getActualDao().getPassportByCode(code);
    }

    /**
     * 查询列表
     *
     * @param  passportDto
     * @param  useProvider
     * @return EasyuiPageOutput
     * @date   2020/7/29
     */
    @Override
    public EasyuiPageOutput listPassports(PassportDto passportDto, boolean useProvider) throws Exception {
        // 分页
        if (passportDto.getRows() != null && passportDto.getRows() >= 1) {
            PageHelper.startPage(passportDto.getPage(), passportDto.getRows());
        }

        // 查询列表
        List<Passport> passportInfoList = this.getActualDao().listPassports(passportDto);

        // 基础代码
        long total = passportInfoList instanceof Page ? ((Page) passportInfoList).getTotal() : (long) passportInfoList.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(passportDto, passportInfoList) : passportInfoList;

        return new EasyuiPageOutput(total, results);
    }

    /**
     * 新增通行证业务单
     *
     * @param  passportDto
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    @Override
    @GlobalTransactional
    public Passport addPassport(PassportDto passportDto, UserTicket userTicket) {
        Passport passport = new Passport();
        BeanUtils.copyProperties(passportDto, passport);

        // 生成通行证的业务 code
        String passportCode = uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.PASSPORT.getCode());
        passport.setVersion(0);
        passport.setCode(passportCode);
        passport.setCreatorId(userTicket.getId());
        passport.setCreateTime(LocalDateTime.now());
        passport.setModifyTime(LocalDateTime.now());
        passport.setMarketId(userTicket.getFirmId());
        passport.setCreator(userTicket.getRealName());
        passport.setMarketCode(userTicket.getFirmCode());
        passport.setState(PassportStateEnum.CREATED.getCode());
        // 商户ID需要从基础信息中获取
        BaseOutput<List<TypeMarketDto>> baseOutput = typeMarketRpc.queryAll();
        if (baseOutput.isSuccess() && baseOutput.getData() != null) {
            for (TypeMarketDto typeMarketDto : baseOutput.getData()) {
                if (BizNumberTypeEnum.PASSPORT.getCode().equals(typeMarketDto.getType())) {
                    passport.setMchId(typeMarketDto.getMarketId());
                }
            }

            if (passport.getMchId() == null) {
                throw new BusinessException(ResultCode.DATA_ERROR, "新增时没有查询到商户ID，无法新增。");
            }

            this.getActualDao().insertSelective(passport);
        } else {
            throw new BusinessException(ResultCode.DATA_ERROR, "新增时没有查询到商户ID，无法新增。");
        }
        return passport;
    }

    /**
     * 修改通行证业务单
     *
     * @param  passportDto
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    @Override
    @GlobalTransactional
    public Passport updatePassport(PassportDto passportDto, UserTicket userTicket) {
        Passport passportParam = new Passport();

        Passport passportInfo = this.get(passportDto.getId());
        if (passportInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，修改失败！");
        }

        // 已创建状态才能修改
        if (!PassportStateEnum.CREATED.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能修改！");
        }

        BeanUtils.copyProperties(passportDto, passportParam);
        passportParam.setModifyTime(LocalDateTime.now());
        passportParam.setVersion(passportInfo.getVersion() + 1);

        if (this.updateSelective(passportParam) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        return passportInfo;
    }

    /**
     * 提交通行证业务单交费
     *
     * @param  id
     * @param  userTicket
     * @return Passport
     * @date   2020/7/27
     */
    @Override
    @GlobalTransactional
    public Passport submit(Long id, UserTicket userTicket) {
        Passport passportInfo = this.get(id);
        if (passportInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，提交失败！");
        }

        // 已创建状态才能提交
        if (!PassportStateEnum.CREATED.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能提交！");
        }

        // 修改通行证单状态为已提交
        passportInfo.setState(PassportStateEnum.SUBMITTED.getCode());
        passportInfo.setModifyTime(LocalDateTime.now());
        passportInfo.setSubmitterId(userTicket.getId());
        passportInfo.setSubmitTime(LocalDateTime.now());
        passportInfo.setSubmitter(userTicket.getRealName());

        if (this.updateSelective(passportInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 创建缴费单，添加到缴费表中
        PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket, BizTypeEnum.PASSPORT);
        paymentOrder.setAmount(passportInfo.getAmount());
        paymentOrder.setBusinessId(passportInfo.getId());
        paymentOrder.setBusinessCode(passportInfo.getCode());
        paymentOrder.setBizType(BizTypeEnum.PASSPORT.getCode());
        paymentOrder.setCustomerId(passportInfo.getCustomerId());
        paymentOrder.setCustomerName(passportInfo.getCustomerName());
        paymentOrder.setMchId(passportInfo.getMchId());
        paymentOrder.setVersion(0);
        paymentOrderService.insertSelective(paymentOrder);

        // 组装数据，调用结算RPC
        SettleOrderDto settleOrderDto = this.buildSettleOrderDto(userTicket, passportInfo, paymentOrder);
        BaseOutput<SettleOrder> baseOutput = settleOrderRpc.submit(settleOrderDto);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "提交失败，" + baseOutput.getMessage());
        }

        return passportInfo;
    }

    /**
     * 组装数据，调用结算RPC
     */
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, Passport passport, PaymentOrder paymentOrder) {
        SettleOrderDto settleOrderDto = new SettleOrderDto();
        // 必填字段
        settleOrderDto.setOrderCode(paymentOrder.getCode());
        settleOrderDto.setBusinessCode(paymentOrder.getBusinessCode());
        settleOrderDto.setCustomerId(passport.getCustomerId());
        settleOrderDto.setCustomerName(passport.getCustomerName());
        settleOrderDto.setCustomerPhone(passport.getCustomerCellphone());
        settleOrderDto.setCustomerCertificate(passport.getCertificateNumber());
        settleOrderDto.setAmount(passport.getAmount());
        settleOrderDto.setMchId(passport.getMchId());
        settleOrderDto.setBusinessDepId(passport.getDepartmentId());
        settleOrderDto.setBusinessDepName(departmentRpc.get(passport.getDepartmentId()).getData().getName());
        settleOrderDto.setMarketId(passport.getMarketId());
        settleOrderDto.setMarketCode(userTicket.getFirmCode());
        settleOrderDto.setSubmitterId(userTicket.getId());
        settleOrderDto.setSubmitterName(userTicket.getRealName());
        if (userTicket.getDepartmentId() != null){
            settleOrderDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
        settleOrderDto.setSubmitTime(LocalDateTime.now());
        settleOrderDto.setAppId(settlementAppId);
        //结算单业务类型 为 Integer
        settleOrderDto.setBusinessType(BizTypeEnum.PASSPORT.getCode());
        settleOrderDto.setType(SettleTypeEnum.PAY.getCode());
        settleOrderDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrderDto.setDeductEnable(EnableEnum.NO.getCode());

        //组装回调url
        List<SettleOrderLink> settleOrderLinkList = new ArrayList<>();
        // 详情
        SettleOrderLink view = new SettleOrderLink();
        view.setType(LinkTypeEnum.DETAIL.getCode());
        view.setUrl(settleViewUrl + "?id=" + passport.getId());
        // 打印
        SettleOrderLink print = new SettleOrderLink();
        print.setType(LinkTypeEnum.PRINT.getCode());
        print.setUrl(settlerPrintUrl + "?orderCode=" + paymentOrder.getCode());
        // 回调
        SettleOrderLink callBack = new SettleOrderLink();
        callBack.setType(LinkTypeEnum.CALLBACK.getCode());
        callBack.setUrl(settlerHandlerUrl);
        settleOrderLinkList.add(view);
        settleOrderLinkList.add(print);
        settleOrderLinkList.add(callBack);
        settleOrderDto.setSettleOrderLinkList(settleOrderLinkList);

        //组装费用项
        List<SettleFeeItem> settleFeeItemList = new ArrayList<>();
        SettleFeeItem sfItem = new SettleFeeItem();
        sfItem.setChargeItemId(ChargeItemEnum.通行费.getId());
        sfItem.setChargeItemName(ChargeItemEnum.通行费.getName());
        sfItem.setAmount(paymentOrder.getAmount());
        settleFeeItemList.add(sfItem);
        settleOrderDto.setSettleFeeItemList(settleFeeItemList);
        return settleOrderDto;
    }

    /**
     * 取消通行证业务单付款
     *
     * @param  id
     * @param  userTicket
     * @return BaseOutput
     * @date  2020/7/27
     */
    @Override
    @GlobalTransactional
    public Passport cancel(Long id, UserTicket userTicket) throws BusinessException {
        Passport passportInfo = this.get(id);
        if (passportInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，取消失败！");
        }

        // 已创建状态才能取消
        if (!PassportStateEnum.CREATED.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能取消！");
        }

        passportInfo.setCancelerId(userTicket.getId());
        passportInfo.setCancelTime(LocalDateTime.now());
        passportInfo.setCanceler(userTicket.getRealName());
        passportInfo.setState(PassportStateEnum.CANCELLED.getCode());
        if (this.updateSelective(passportInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return passportInfo;
    }

    /**
     * 撤回 通行证业务单缴费
     *
     * @param  id
     * @param  userTicket
     * @return BaseOutput
     * @date   2020/7/27
     */
    @Override
    @GlobalTransactional
    public Passport withdraw(Long id, UserTicket userTicket) throws BusinessException {
        Passport passportInfo = this.get(id);
        if (passportInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，撤回失败！");
        }

        // 已提交状态才能撤回
        if (!PassportStateEnum.SUBMITTED.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能撤回！");
        }

        passportInfo.setWithdrawOperatorId(userTicket.getId());
        passportInfo.setWithdrawOperator(userTicket.getRealName());
        passportInfo.setWithdrawTime(LocalDateTime.now());
        passportInfo.setState(PassportStateEnum.CREATED.getCode());
        if (this.updateSelective(passportInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 查询缴费单（方法抽取）
        PaymentOrder paymentOrder = this.findPaymentOrder(userTicket, passportInfo.getId(), passportInfo.getCode(), PaymentOrderStateEnum.NOT_PAID.getCode());

        // 撤回付款单和修改缴费单的状态（方法抽取）
        this.withdrawPaymentOrder(paymentOrder);

        return passportInfo;
    }

    /**
     * 查询缴费单
     */
    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode, Integer payState) throws BusinessException {
        PaymentOrder pb = new PaymentOrder();

        pb.setBizType(BizTypeEnum.PASSPORT.getCode());
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(userTicket.getFirmId());
        pb.setState(payState);
        PaymentOrder paymentOrder = paymentOrderService.listByExample(pb).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            logger.info("没有查询到付款单PaymentOrder【业务单businessId：{}】 【业务单businessCode:{}】", businessId, businessCode);
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到付款单！");
        }

        return paymentOrder;
    }

    /**
     * 撤回付款单和修改缴费单的状态
     */
    private void withdrawPaymentOrder(PaymentOrder paymentOrder) {
        paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
        if (paymentOrderService.updateSelective(paymentOrder) == 0) {
            logger.info("撤回通行证【删除缴费单】失败.");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 撤回结算单多人操作已判断
        BaseOutput<String> baseOutput = settleOrderRpc.cancel(settlementAppId, paymentOrder.getCode());
        if (!baseOutput.isSuccess()){
            logger.info("撤回，调用结算中心修改状态失败！" + baseOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "撤回，调用结算中心修改状态失败！" + baseOutput.getMessage());
        }
    }

    /**
     * 通行证缴费成功回调
     *
     * @param  settleOrder
     * @return BaseOutput
     * @date   2020/7/27
     */
    @Override
    @GlobalTransactional
    public Passport settlementDealHandler(SettleOrder settleOrder) throws BusinessException {
        // 修改缴费单相关数据
        if (null == settleOrder) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "回调参数为空！");
        }
        PaymentOrder paymentOrder = new PaymentOrder();
        //结算单code唯一
        paymentOrder.setCode(settleOrder.getOrderCode());
        paymentOrder.setBizType(BizTypeEnum.PASSPORT.getCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null);
        Passport passportInfo = this.get(paymentOrderPO.getBusinessId());
        //如果已支付，直接返回
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) {
            return passportInfo;
        }
        if (!paymentOrderPO.getState().equals(PaymentOrderStateEnum.NOT_PAID.getCode())) {
            logger.info("缴费单状态已变更！状态为：" + PaymentOrderStateEnum.getPaymentOrderStateEnum(paymentOrderPO.getState()).getName());
            throw new BusinessException(ResultCode.PARAMS_ERROR, "缴费单状态已变更！");
        }

        //缴费单数据更新
        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setPayedTime(settleOrder.getOperateTime());
        paymentOrderPO.setSettlementCode(settleOrder.getCode());
        paymentOrderPO.setSettlementOperator(settleOrder.getOperatorName());
        paymentOrderPO.setSettlementWay(settleOrder.getWay());
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            logger.info("缴费单成功回调 -- 更新【缴费单】,乐观锁生效！【付款单paymentOrderID:{}】", paymentOrderPO.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 修改通行证,生成证件号
        passportInfo.setModifyTime(LocalDateTime.now());
        String licenseCode = passportInfo.getLicenseCode().toLowerCase();
        String code = uidRpcResolver.bizNumber(passportInfo.getMarketCode() + "_" + BizNumberTypeEnum.PASSPORT_LICENSE_CODE.getCode() + licenseCode);
        passportInfo.setLicenseNumber(code);

        // 判断交费后状态
        if (passportInfo.getStartTime() != null && LocalDateTime.now().isBefore(passportInfo.getStartTime())) {
            passportInfo.setState(PassportStateEnum.NOT_START.getCode());
        } else if (passportInfo.getStartTime() != null && passportInfo.getEndTime() != null && passportInfo.getStartTime().isBefore(LocalDateTime.now())
                && LocalDateTime.now().isBefore(passportInfo.getEndTime())) {
            passportInfo.setState(PassportStateEnum.IN_FORCE.getCode());
        } else if (passportInfo.getEndTime() != null && passportInfo.getEndTime().isBefore(LocalDateTime.now())) {
            passportInfo.setState(PassportStateEnum.EXPIRED.getCode());
        }

        if (this.updateSelective(passportInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return passportInfo;
    }

    /**
     * 通行证打印票据
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/7/27
     */
    @Override
    public PrintDataDto<PassportPrintDto> receiptPaymentData(String orderCode, Integer reprint) throws BusinessException {
        PaymentOrder paymentOrderCondition = new PaymentOrder();

        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.PASSPORT.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new BusinessException(ResultCode.DATA_ERROR, "businessCode无效");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付!");
        }

        // 组装数据
        Passport passportInfo = this.get(paymentOrder.getBusinessId());
        SettleOrder order = settleOrderRpc.get(settlementAppId, paymentOrder.getCode()).getData();
        if (passportInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "通行证单不存在!");
        }
        PassportPrintDto passportPrintDto = new PassportPrintDto();
        passportPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        passportPrintDto.setPrintTime(LocalDateTime.now());
        passportPrintDto.setCode(passportInfo.getCode());
        passportPrintDto.setCustomerName(passportInfo.getCustomerName());
        passportPrintDto.setCustomerCellphone(passportInfo.getCustomerCellphone());
        passportPrintDto.setBusinessType(BizTypeEnum.PASSPORT.getName());
        passportPrintDto.setAmount(MoneyUtils.centToYuan(passportInfo.getAmount()));
        passportPrintDto.setLicenseNumber(passportInfo.getLicenseNumber());
        passportPrintDto.setPlate(passportInfo.getCarNumber());
        // 通行证专有字段 有效期开始时间结束时间
        passportPrintDto.setStartTime(passportInfo.getStartTime());
        passportPrintDto.setEndTime(passportInfo.getEndTime());

        // 支付方式
        String settleDetails = "";
        if (SettleWayEnum.CARD.getCode() == order.getWay()) {
            // 园区卡支付
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【卡号：" + order.getAccountNumber() +
                    "（" + order.getCustomerName() + "）】";
        } else {
            // 现金以及其他方式
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【" + order.getChargeDate() + "  流水号：" + order.getSerialNumber() + "  备注："
                    + order.getNotes() + "】";
        }
        passportPrintDto.setSettleWayDetails(settleDetails);

        passportPrintDto.setNotes(passportInfo.getNotes());
        passportPrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        passportPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        passportPrintDto.setSubmitter(paymentOrder.getCreator());

        PrintDataDto<PassportPrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.PASSPORT_PAY.getCode());
        printDataDto.setItem(passportPrintDto);

        return printDataDto;
    }

    /**
     * 退款票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/8/11
     */
    @Override
    public PrintDataDto<PassportPrintDto> receiptRefundPrintData(String orderCode, String reprint) throws BusinessException {
        RefundOrder condtion = new RefundOrder();
        condtion.setCode(orderCode);
        List<RefundOrder> refundOrders = refundOrderService.list(condtion);
        if (CollectionUtil.isEmpty(refundOrders)) {
            throw new BusinessException(ResultCode.DATA_ERROR, "未查询到退款单!");
        } else {
            RefundOrder refundOrder = refundOrders.get(0);
            Passport passportInfo = this.get(refundOrder.getBusinessId());
            SettleOrder order = settleOrderRpc.get(settlementAppId, refundOrder.getCode()).getData();

            // 组装退款单信息
            PassportPrintDto passportPrintDto = new PassportPrintDto();
            passportPrintDto.setReprint(reprint);
            passportPrintDto.setCode(passportInfo.getCode());
            passportPrintDto.setPrintTime(LocalDateTime.now());
            passportPrintDto.setCustomerName(passportInfo.getCustomerName());
            passportPrintDto.setCustomerCellphone(passportInfo.getCustomerCellphone());
            passportPrintDto.setAmount(String.valueOf(passportInfo.getAmount()));
            passportPrintDto.setBusinessType(BizTypeEnum.PASSPORT.getName());
            passportPrintDto.setPlate(passportInfo.getCarNumber());

            // 退款方式,只要如下三种退款方式
            String settleDetails = "收款人：" + refundOrder.getPayee() + "金额：" + refundOrder.getPayeeAmount();
            if (SettleWayEnum.CARD.getCode() == order.getWay()) {
                // 园区卡支付
                settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     园区卡号：" + order.getAccountNumber();
            } else if (SettleWayEnum.CASH.getCode() == order.getWay()) {
                // 现金
                settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay());
            } else if (SettleWayEnum.BANK.getCode() == order.getWay())  {
                // 银行卡
                settleDetails = "退款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "  开户行：" + order.getBankName() + "  银行卡号：" + order.getBankCardHolder();
            }
            passportPrintDto.setSettleWayDetails(settleDetails);

            passportPrintDto.setRefundReason(refundOrder.getRefundReason());
            passportPrintDto.setSubmitter(passportInfo.getSubmitter());
            passportPrintDto.setRefundReason(refundOrder.getRefundReason());
            passportPrintDto.setSettlementOperator(order.getOperatorName());

            // 打印最外层
            PrintDataDto<PassportPrintDto> printDataDto = new PrintDataDto<>();
            printDataDto.setName(PrintTemplateEnum.PASSPORT_REFUND.getCode());
            printDataDto.setItem(passportPrintDto);

            return printDataDto;
        }
    }

    /**
     * 退款申请
     *
     * @param  refundOrderDto
     * @param  userTicket
     * @return BaseOutput
     * @date   2020/7/27
     */
    @Override
    @GlobalTransactional
    public Passport refund(PassportRefundOrderDto refundOrderDto, UserTicket userTicket) throws BusinessException {
        // 退款申请的条件检查（方法抽取）
        Passport passportInfo = checkRefundCondition(refundOrderDto, userTicket);

        // 修改通行证业务单的状态
        passportInfo.setModifyTime(LocalDateTime.now());
        passportInfo.setState(PassportStateEnum.SUBMITTED_REFUND.getCode());
        if (this.updateSelective(passportInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 查询已缴费的缴费单（方法抽取）
        this.findPaymentOrder(userTicket, passportInfo.getId(), passportInfo.getCode(), PaymentOrderStateEnum.PAID.getCode());

        // 构建退款单以及新增
        refundOrderDto.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.getBizTypeEnum(BizTypeEnum.PASSPORT.getCode()).getEnName() + "_" + BizNumberTypeEnum.REFUND_ORDER.getCode()));
        refundOrderDto.setBizType(BizTypeEnum.PASSPORT.getCode());
        refundOrderDto.setMchId(passportInfo.getMchId());
        BaseOutput output = refundOrderService.doAddHandler(refundOrderDto);
        if (!output.isSuccess()) {
            logger.info("其他收费【编号：{}】退款申请接口异常", refundOrderDto.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常！");
        }

        return passportInfo;
    }

    /**
     *  退款申请的条件检查
     */
    private Passport checkRefundCondition(PassportRefundOrderDto refundOrderDto, UserTicket userTicket) {
        if (null == userTicket){
            throw new BusinessException(ResultCode.DATA_ERROR, "未登录！");
        }

        // 检查客户状态(方法抽取)
        checkCustomerState(refundOrderDto.getPayeeId(), userTicket.getFirmId());

        // 检查通行证费缴费单的状态
        Passport passportInfo = this.get(refundOrderDto.getBusinessId());
        if (passportInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "其他收费缴费单不存在！");
        }
        if (PassportStateEnum.SUBMITTED_REFUND.getCode().equals(passportInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试！");
        }

        return passportInfo;
    }

    /**
     * 检查客户状态
     */
    public void checkCustomerState(Long customerId, Long marketId) {
//        BaseOutput<Customer> output = customerRpc.get(customerId, marketId);
//        if (!output.isSuccess()) {
//            throw new BusinessException(ResultCode.DATA_ERROR, "客户接口调用异常 " + output.getMessage());
//        }
//        Customer customer = output.getData();
//        if (null == customer) {
//            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在，请重新修改后保存");
//        } else if (EnabledStateEnum.DISABLED.getCode().equals(customer.getState())) {
//            throw new BusinessException(ResultCode.DATA_ERROR, "客户【" + customer.getName() + "】已禁用，请重新修改后保存");
//        } else if (YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())) {
//            throw new BusinessException(ResultCode.DATA_ERROR, "客户【" + customer.getName() + "】已删除，请重新修改后保存");
//        }
    }

    /**
     * 定时任务，修改通行证状态（未生效变为已生效/已过期，已生效变为已过期）
     *
     * @date 2020/8/6
     */
    @Override
    public void passportTasking() {
        List<Passport> passportList = new ArrayList<>();

        // 搜索未生效的通行证
        PassportDto passportDtoParam = new PassportDto();
        passportDtoParam.setState(PassportStateEnum.NOT_START.getCode());
        List<Passport> passportInfoList = this.getActualDao().listPassports(passportDtoParam);
        if (passportInfoList != null && passportInfoList.size() > 0) {
            for (Passport passportInfo : passportInfoList) {
                // 当前时间大于开始时间,则为已生效
                if (passportInfo.getStartTime().isBefore(LocalDateTime.now()) && LocalDateTime.now().isBefore(passportInfo.getEndTime())) {
                    passportInfo.setState(PassportStateEnum.IN_FORCE.getCode());
                    passportInfoList.add(passportInfo);
                } else if (passportInfo.getEndTime().isBefore(LocalDateTime.now())) {
                    passportInfo.setState(PassportStateEnum.EXPIRED.getCode());
                    passportList.add(passportInfo);
                }
            }
        }

        // 搜索已生效的通行证
        passportDtoParam.setState(PassportStateEnum.IN_FORCE.getCode());
        List<Passport> passportForceList = this.getActualDao().listPassports(passportDtoParam);
        if (passportForceList != null && passportForceList.size() > 0) {
            for (Passport passportInfo : passportForceList) {
                // 当前时间大于结束时间,则为已到期
                if (passportInfo.getEndTime().isBefore(LocalDateTime.now())) {
                    passportInfo.setState(PassportStateEnum.EXPIRED.getCode());
                    passportList.add(passportInfo);
                }
            }
        }

        // 集合修改
        this.batchUpdateSelective(passportList);
    }

    /**
     * 通行证证件打印
     *
     * @param  orderCode
     * @return BaseOutput
     * @date   2020/7/27
     */
    @Override
    public PrintDataDto<PassportPrintDto> printPaperwork(String orderCode, String reprint) {
        PaymentOrder paymentOrderCondition = new PaymentOrder();

        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.PASSPORT.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new BusinessException(ResultCode.DATA_ERROR, "businessCode无效！");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付！");
        }

        // 组装数据
        Passport passportInfo = this.get(paymentOrder.getBusinessId());
        if (passportInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "通行证单不存在！");
        }
        PassportPrintDto passportPrintDto = new PassportPrintDto();

        passportPrintDto.setCustomerName(passportInfo.getCustomerName());
        passportPrintDto.setPlate(passportInfo.getCarNumber());
        passportPrintDto.setLicenseNumber(passportInfo.getLicenseNumber());
        passportPrintDto.setDepartmentName(passportInfo.getDepartmentName());
        passportPrintDto.setCustomerCellphone(passportInfo.getCustomerCellphone());

        // 打印最外层
        PrintDataDto<PassportPrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.PASSPORT_PRINT.getName());
        printDataDto.setItem(passportPrintDto);

        return printDataDto;
    }
}