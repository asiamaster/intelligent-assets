package com.dili.ia.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.domain.dto.printDto.BoutiqueEntrancePrintDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BoutiqueOrderStateEnum;
import com.dili.ia.glossary.BoutiqueStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.BoutiqueEntranceRecordMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ia.service.BoutiqueFreeSetsService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/7/23
 * @version:     农批业务系统重构
 * @description: 精品停车
 */
@Service
public class BoutiqueEntranceRecordServiceImpl extends BaseServiceImpl<BoutiqueEntranceRecord, Long> implements BoutiqueEntranceRecordService {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueEntranceRecordServiceImpl.class);

    public BoutiqueEntranceRecordMapper getActualDao() {
        return (BoutiqueEntranceRecordMapper) getDao();
    }

    @Autowired
    private DepartmentRpc departmentRpc;

    @Autowired
    private BoutiqueFeeOrderService boutiqueFeeOrderService;

    @Autowired
    private BoutiqueFreeSetsService boutiqueFreeSetsService;

    @Autowired
    private UidRpcResolver uidRpcResolver;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private SettlementRpcResolver settlementRpcResolver;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private TransferDeductionItemService transferDeductionItemService;

    @Value("${settlement.app-id}")
    private Long settlementAppId;

    private String settlerHandlerUrl = "http://ia.diligrp.com:8381/api/boutiqueEntrance/settlementDealHandler";

    @Override
    public EasyuiPageOutput listBoutiques(BoutiqueEntranceRecordDto boutiqueDto, boolean useProvider) throws Exception {
        // 分页
        if (boutiqueDto.getRows() != null && boutiqueDto.getRows() >= 1) {
            PageHelper.startPage(boutiqueDto.getPage(), boutiqueDto.getRows());
        }

        // 查询列表
        List<BoutiqueEntranceRecordDto> boutiqueList = this.getActualDao().listBoutiques(boutiqueDto);

        // 基础代码
        long total = boutiqueList instanceof Page ? ((Page) boutiqueList).getTotal() : (long) boutiqueList.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(boutiqueDto, boutiqueList) : boutiqueList;

        return new EasyuiPageOutput(total, results);
    }

    /**
     * 根据 code 获取退款单相关信息
     *
     * @param  code
     * @return BoutiqueFeeOrderDto
     * @date  2020/7/23
     */
    @Override
    public BoutiqueFeeOrderDto getBoutiqueAndOrderByCode(String code) {
        return this.getActualDao().getBoutiqueAndOrderByCode(code);
    }

    /**
     * 根据主键获取精品停车数据以及精品停车交费列表
     *
     * @param  id
     * @return BoutiqueEntranceRecordDto
     * @date   2020/7/13
     */
    @Override
    public BoutiqueEntranceRecordDto getBoutiqueEntranceDtoById(Long id) {
        BoutiqueEntranceRecordDto boutiqueEntranceRecordDto = new BoutiqueEntranceRecordDto();

        // 不联表查询，先查询精品停车数据
        BoutiqueEntranceRecord boutiqueEntranceRecord = this.get(id);
        BeanUtils.copyProperties(boutiqueEntranceRecord, boutiqueEntranceRecordDto);

        // 查询关联的缴费单
        if (boutiqueEntranceRecord != null) {
            List<BoutiqueFeeOrderDto> boutiqueFeeOrderDtoList = boutiqueFeeOrderService.listByRecordId(boutiqueEntranceRecord.getId());
            boutiqueEntranceRecordDto.setOrderDtoList(boutiqueFeeOrderDtoList);
        }

        return boutiqueEntranceRecordDto;
    }

    /**
     * 确认计费
     *
     * @param  boutiqueEntranceRecord
     * @param  userTicket
     * @return BoutiqueEntranceRecord
     * @date   2020/7/13
     */
    @Override
    @GlobalTransactional
    public BoutiqueEntranceRecord confirm(BoutiqueEntranceRecord boutiqueEntranceRecord, UserTicket userTicket) throws BusinessException {
        BoutiqueEntranceRecord recordInfo = this.get(boutiqueEntranceRecord.getId());
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，确认计费失败。");
        }

        if (!recordInfo.getState().equals(BoutiqueStateEnum.NOCONFIRM.getCode())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是待确认，不能确认计费。");
        }

        // 根据车型查询免费时长
        BoutiqueFreeSets sets = boutiqueFreeSetsService.get(recordInfo.getCarTypeId());
        if (sets != null) {
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = boutiqueEntranceRecord.getConfirmTime().atZone(zoneId);
            Date date = Date.from(zdt.toInstant());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, sets.getFreeHours());
            recordInfo.setStartTime(calendar.getTime().toInstant().atZone(zoneId).toLocalDateTime());
            recordInfo.setCountTime(calendar.getTime().toInstant().atZone(zoneId).toLocalDateTime());
        }
        recordInfo.setConfirmTime(boutiqueEntranceRecord.getConfirmTime());
        recordInfo.setState(BoutiqueStateEnum.COUNTING.getCode());
        recordInfo.setModifyTime(LocalDateTime.now());
        recordInfo.setVersion(recordInfo.getVersion() + 1);

        if (userTicket != null) {
            recordInfo.setOperatorId(userTicket.getId());
            recordInfo.setOperatorName(userTicket.getRealName());
        }

        if (this.updateSelective(recordInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return recordInfo;
    }

    /**
     * 交费提交
     *
     * @param  feeOrder
     * @param  userTicket
     * @return BoutiqueEntranceRecord
     * @date   2020/7/14
     */
    @Override
    @GlobalTransactional
    public BoutiqueEntranceRecord submit(BoutiqueFeeOrder feeOrder, UserTicket userTicket) throws BusinessException {
        // 先查询精品停车信息
        BoutiqueEntranceRecord recordInfo = this.get(feeOrder.getRecordId());
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，提交失败。");
        }

        // 先根据 recordId 查询是否已存在已提交状态的交费单
        List<BoutiqueFeeOrderDto> orderDtoList = boutiqueFeeOrderService.listByRecordId(feeOrder.getRecordId());
        if (orderDtoList != null && orderDtoList.size() > 0) {
            for (BoutiqueFeeOrderDto feeOrderDto : orderDtoList) {
                if (BoutiqueOrderStateEnum.SUBMITTED_PAY.getCode().equals(feeOrderDto.getState())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "已存在已提交的交费单，请先处理。");
                }
            }
        }

        String code = uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.BOUTIQUE_ENTRANCE.getCode());

        // 新增精品停车交费单
        feeOrder.setCode(code);
        feeOrder.setVersion(0);
        feeOrder.setCreateTime(LocalDateTime.now());
        feeOrder.setModifyTime(LocalDateTime.now());
        feeOrder.setSubmitterId(userTicket.getId());
        feeOrder.setMarketId(userTicket.getFirmId());
        feeOrder.setSubmitter(userTicket.getRealName());
        feeOrder.setStartTime(recordInfo.getCountTime());
        feeOrder.setMarketCode(userTicket.getFirmCode());
        feeOrder.setState(BoutiqueOrderStateEnum.SUBMITTED_PAY.getCode());

        boutiqueFeeOrderService.insertSelective(feeOrder);

        // 创建缴费单
        PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket, BizTypeEnum.BOUTIQUE_ENTRANCE);
        paymentOrder.setBusinessCode(code);
        paymentOrder.setBusinessId(feeOrder.getId());
        paymentOrder.setAmount(feeOrder.getAmount());
        paymentOrder.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        paymentOrderService.insertSelective(paymentOrder);

        // 调用结算接口,缴费
        SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, feeOrder, recordInfo, paymentOrder.getCode(), paymentOrder.getAmount());
        settlementRpcResolver.submit(settleOrderDto);

        return recordInfo;
    }

    /**
     * 离场
     *
     * @param  id
     * @param  userTicket
     * @return BoutiqueEntranceRecord
     * @date   2020/7/13
     */
    @Override
    public BoutiqueEntranceRecord leave(Long id, UserTicket userTicket) throws BusinessException {
        BoutiqueEntranceRecord recordInfo = this.get(id);
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，离场失败。");
        }

        if (!(recordInfo.getState().equals(BoutiqueStateEnum.NOCONFIRM.getCode()) || recordInfo.getState().equals(BoutiqueStateEnum.COUNTING.getCode()))) {
            throw new BusinessException(ResultCode.DATA_ERROR, "所选记录状态已变更,请刷新重试。");
        }

        if (recordInfo.getCountTime() != null && recordInfo.getCountTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ResultCode.DATA_ERROR, recordInfo.getPlate() + " 有停车费未结清，请结清后再离场。");
        }

        List<BoutiqueFeeOrderDto> orderDtoList = boutiqueFeeOrderService.listByRecordId(recordInfo.getId());
        if (orderDtoList != null && orderDtoList.size() > 0) {
            for (BoutiqueFeeOrderDto orderDto : orderDtoList) {
                if (BoutiqueOrderStateEnum.SUBMITTED_PAY.getCode().equals(orderDto.getState())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, recordInfo.getPlate() + " 有停车费未结清，请结清后再离场。");
                }
            }
        }

        recordInfo.setState(BoutiqueStateEnum.LEAVE.getCode());
        if (userTicket != null) {
            recordInfo.setOperatorId(userTicket.getId());
            recordInfo.setOperatorName(userTicket.getRealName());
        }
        recordInfo.setLeaveTime(LocalDateTime.now());
        recordInfo.setModifyTime(LocalDateTime.now());
        recordInfo.setVersion(recordInfo.getVersion() + 1);
        if (this.updateSelective(recordInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return recordInfo;
    }

    /**
     * 强制离场
     *
     * @param  id
     * @return BoutiqueEntranceRecord
     * @date   2020/7/13
     */
    @Override
    public BoutiqueEntranceRecord forceLeave(Long id, UserTicket userTicket) throws BusinessException {
        BoutiqueEntranceRecord recordInfo = this.get(id);

        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "所选记录不存在");
        }
        if (!recordInfo.getState().equals(BoutiqueStateEnum.COUNTING.getCode())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "所选记录状态已变更,请刷新重试");
        }
        //修改记录状态为已离场
        recordInfo.setState(BoutiqueStateEnum.LEAVE.getCode());
        recordInfo.setOperatorId(userTicket.getId());
        recordInfo.setOperatorName(userTicket.getRealName());
        recordInfo.setLeaveTime(LocalDateTime.now());
        recordInfo.setModifyTime(LocalDateTime.now());
        recordInfo.setVersion(recordInfo.getVersion() + 1);
        if (this.updateSelective(recordInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 修改交费单的状态
        this.invalidOrders(recordInfo, userTicket);

        return recordInfo;
    }

    /**
     * 缴费成功回调
     *
     * @param  settleOrder
     * @return BoutiqueFeeOrder
     * @date   2020/7/14
     */
    @Override
    public BoutiqueFeeOrder settlementDealHandler(SettleOrder settleOrder) throws BusinessException {
        // 修改缴费单相关数据
        if (null == settleOrder) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "回调参数为空！");
        }
        PaymentOrder condition = new PaymentOrder();
        //结算单code唯一
        condition.setCode(settleOrder.getOrderCode());
        condition.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        BoutiqueFeeOrder feeOrderInfo = boutiqueFeeOrderService.get(paymentOrderPO.getBusinessId());
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) { //如果已支付，直接返回
            return feeOrderInfo;
        }
        if (!paymentOrderPO.getState().equals(PaymentOrderStateEnum.NOT_PAID.getCode())) {
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

        // 修改精品停车交费单的状态
        feeOrderInfo.setState(BoutiqueOrderStateEnum.PAID.getCode());
        feeOrderInfo.setModifyTime(LocalDateTime.now());
        if (boutiqueFeeOrderService.updateSelective(feeOrderInfo) == 0) {
            logger.info("缴费单成功回调 -- 更新【精品停车交费单】状态,乐观锁生效！【交费单Id:{}】", feeOrderInfo.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 修改下次计费时间，叠加总计费
        BoutiqueEntranceRecord recordInfo = this.get(feeOrderInfo.getRecordId());
        recordInfo.setModifyTime(LocalDateTime.now());
        recordInfo.setCountTime(feeOrderInfo.getEndTime());
        recordInfo.setTotalAmount(recordInfo.getTotalAmount() + feeOrderInfo.getAmount());
        recordInfo.setVersion(recordInfo.getVersion() + 1);
        if (this.updateSelective(recordInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return feeOrderInfo;
    }

    /**
     * 打印票据
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/7/14
     */
    @Override
    public PrintDataDto<BoutiqueEntrancePrintDto> receiptPaymentData(String orderCode, Integer reprint) throws BusinessException {
        PaymentOrder paymentOrderCondition = new PaymentOrder();
        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new BusinessException(ResultCode.DATA_ERROR,"businessCode无效。");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付!");
        }

        // 组装数据
        BoutiqueFeeOrder feeOrder = boutiqueFeeOrderService.get(paymentOrder.getBusinessId());
        if (feeOrder == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "精品停车交费单不存在!");
        }
        BoutiqueEntranceRecord recordInfo = this.get(feeOrder.getRecordId());
        BoutiqueEntrancePrintDto boutiqueTrancePrintDto = new BoutiqueEntrancePrintDto();
        boutiqueTrancePrintDto.setPrintTime(LocalDateTime.now());
        boutiqueTrancePrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        boutiqueTrancePrintDto.setCode(feeOrder.getCode());
        boutiqueTrancePrintDto.setCustomerName(recordInfo.getCustomerName());
        boutiqueTrancePrintDto.setCustomerCellphone(recordInfo.getCustomerCellphone());

        boutiqueTrancePrintDto.setNotes(feeOrder.getNotes());
        boutiqueTrancePrintDto.setAmount(MoneyUtils.centToYuan(feeOrder.getAmount()));
        boutiqueTrancePrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        boutiqueTrancePrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        boutiqueTrancePrintDto.setSubmitter(paymentOrder.getCreator());
        boutiqueTrancePrintDto.setBusinessType(BizTypeEnum.BOUTIQUE_ENTRANCE.getName());

        // 精品停车特殊字段以及开始时间结束时间
        boutiqueTrancePrintDto.setPlate(recordInfo.getPlate());
        boutiqueTrancePrintDto.setConfirmTime(recordInfo.getConfirmTime());
        boutiqueTrancePrintDto.setEndTime(feeOrder.getEndTime());
        boutiqueTrancePrintDto.setStartTime(feeOrder.getStartTime());

        PrintDataDto<BoutiqueEntrancePrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.BOUTIQUE_ENTRANCE.getCode());
        printDataDto.setItem(boutiqueTrancePrintDto);

        return printDataDto;
    }

    /**
     * 取消(进门取消，可在待确认和计费中取消)
     *
     * @param  recordDto
     * @return BoutiqueFeeOrder
     * @date   2020/8/5
     */
    @Override
    public BoutiqueEntranceRecord cancel(BoutiqueEntranceRecordDto recordDto) throws BusinessException {
        BoutiqueEntranceRecord recordInfo = this.get(recordDto.getId());
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，取消失败。");
        }

        if (!(BoutiqueStateEnum.NOCONFIRM.getCode().equals(recordInfo.getState()) || BoutiqueStateEnum.COUNTING.getCode().equals(recordInfo.getState()))) {
            throw new BusinessException(ResultCode.DATA_ERROR, "精品停车不是待确认和计费中状态，不能取消！");
        }

        // 修改状态
        recordInfo.setModifyTime(LocalDateTime.now());
        recordInfo.setCancelTime(LocalDateTime.now());
        recordInfo.setState(BoutiqueStateEnum.REVOKE.getCode());

        // 先查询是否有缴费单, 再撤销
        List<BoutiqueFeeOrderDto> orderDtoList = boutiqueFeeOrderService.listByRecordId(recordDto.getId());
        if (orderDtoList != null && orderDtoList.size() > 0) {
            List<BoutiqueFeeOrder> orderAddList = new ArrayList<>();
            for (BoutiqueFeeOrderDto orderDto : orderDtoList) {
                BoutiqueFeeOrder orderAdd = new BoutiqueFeeOrder();
                BeanUtils.copyProperties(orderDto, orderAdd);
                orderAdd.setCancelTime(LocalDateTime.now());
                orderAdd.setVersion(orderAdd.getVersion() + 1);
                orderAdd.setCancelerId(recordDto.getOperatorId());
                orderAdd.setCanceler(recordDto.getOperatorName());
                orderAdd.setCancelReason(recordDto.getCancelReason());
                orderAdd.setState(BoutiqueOrderStateEnum.REVOKE.getCode());
                orderAddList.add(orderAdd);
            }
            if (boutiqueFeeOrderService.batchUpdateSelective(orderAddList) == 0) {
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
            }
        }

        if (this.updateSelective(recordInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return recordInfo;
    }

    /**
     * 新增计费（提供给其他服务调用者）
     *
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     * @date 2020/7/13
     */
    @Override
    public BoutiqueEntranceRecord addBoutique(BoutiqueEntranceRecord boutiqueEntranceRecord) {
        boutiqueEntranceRecord.setCreateTime(LocalDateTime.now());
        boutiqueEntranceRecord.setModifyTime(LocalDateTime.now());
        boutiqueEntranceRecord.setState(BoutiqueStateEnum.NOCONFIRM.getCode());

        this.insertSelective(boutiqueEntranceRecord);

        return boutiqueEntranceRecord;
    }

    /**
     * 打印退款
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/8/11
     */
    @Override
    public PrintDataDto<BoutiqueEntrancePrintDto> receiptRefundPrintData(String orderCode, String reprint) throws BusinessException {
        RefundOrder condtion = new RefundOrder();
        condtion.setCode(orderCode);
        List<RefundOrder> refundOrders = refundOrderService.list(condtion);
        if (CollectionUtil.isEmpty(refundOrders)) {
            throw new BusinessException(ResultCode.DATA_ERROR, "未查询到退款单!");
        } else {
            RefundOrder refundOrder = refundOrders.get(0);
            BoutiqueFeeOrder orderInfo = boutiqueFeeOrderService.get(refundOrder.getBusinessId());
            BoutiqueEntranceRecord recordInfo = this.get(orderInfo.getRecordId());
            SettleOrder order = settlementRpcResolver.get(settlementAppId, orderInfo.getCode());

            // 组装退款单信息
            BoutiqueEntrancePrintDto printDto = new BoutiqueEntrancePrintDto();
            printDto.setReprint(reprint);
            printDto.setNotes(orderInfo.getNotes());
            printDto.setPrintTime(LocalDateTime.now());
            printDto.setSubmitter(orderInfo.getSubmitter());
            printDto.setPayeeAmount(refundOrder.getPayeeAmount());
            printDto.setCustomerName(recordInfo.getCustomerName());
            printDto.setRefundReason(refundOrder.getRefundReason());
            printDto.setSettlementOperator(order.getOperatorName());
            printDto.setBusinessType(BizTypeEnum.BOUTIQUE_ENTRANCE.getName());
            printDto.setAmount(String.valueOf(orderInfo.getAmount()));
            printDto.setCustomerCellphone(recordInfo.getCustomerCellphone());
            printDto.setBusinessCode(refundOrder.getBusinessId());

            //TODO 判断支付方式
            //园区卡号
            printDto.setAccountCardNo(order.getAccountNumber());
            //银行卡号
            printDto.setBankName(refundOrder.getBank());
            printDto.setBankNo(refundOrder.getBankCardNo());

            // 获取转抵信息
            TransferDeductionItem transferDeductionItemQuery = new TransferDeductionItem();
            transferDeductionItemQuery.setRefundOrderId(refundOrder.getId());
            printDto.setTransferDeductionItems(transferDeductionItemService.list(transferDeductionItemQuery));

            // 打印最外层
            PrintDataDto<BoutiqueEntrancePrintDto> printDataDto = new PrintDataDto<>();
            printDataDto.setName(PrintTemplateEnum.BOUTIQUE_ENTRANCE.getName());
            printDataDto.setItem(printDto);

            return printDataDto;
        }
    }

    /**
     * 构建结算实体类
     *
     * @param  userTicket
     * @param  feeOrder
     * @param  recordInfo
     * @param  orderCode
     * @param  amount
     * @return SettleOrderDto
     */
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, BoutiqueFeeOrder feeOrder, BoutiqueEntranceRecord recordInfo, String orderCode, Long amount) {
        SettleOrderInfoDto settleOrderInfoDto = new SettleOrderInfoDto(userTicket, BizTypeEnum.BOUTIQUE_ENTRANCE, SettleTypeEnum.PAY, SettleStateEnum.WAIT_DEAL);
        settleOrderInfoDto.setMarketId(feeOrder.getMarketId());
        settleOrderInfoDto.setMarketCode(userTicket.getFirmCode());
        settleOrderInfoDto.setBusinessCode(feeOrder.getCode());
        settleOrderInfoDto.setOrderCode(orderCode);
        settleOrderInfoDto.setAmount(amount);
        settleOrderInfoDto.setAppId(settlementAppId);
        settleOrderInfoDto.setBusinessDepId(recordInfo.getDepartmentId());
        settleOrderInfoDto.setBusinessDepName(recordInfo.getDepartmentName());
        settleOrderInfoDto.setCustomerId(recordInfo.getCustomerId());
        settleOrderInfoDto.setCustomerName(recordInfo.getCustomerName());
        settleOrderInfoDto.setCustomerPhone(recordInfo.getCustomerCellphone());
        settleOrderInfoDto.setSubmitterId(userTicket.getId());
        settleOrderInfoDto.setSubmitterName(userTicket.getRealName());
        settleOrderInfoDto.setBusinessType(Integer.valueOf(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode()));
        settleOrderInfoDto.setType(SettleTypeEnum.PAY.getCode());
        settleOrderInfoDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrderInfoDto.setReturnUrl(settlerHandlerUrl);
        if (userTicket.getDepartmentId() != null) {
            settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
        return settleOrderInfoDto;
    }

    /**
     * 修改交费单的状态（作废交费单）
     */
    private void invalidOrders(BoutiqueEntranceRecord record, UserTicket userTicket) throws BusinessException {
        List<BoutiqueFeeOrderDto> orderDtoList = boutiqueFeeOrderService.listByRecordId(record.getId());
        if (orderDtoList != null && orderDtoList.size() > 0) {
            orderDtoList.stream().filter(feeOrderDto -> feeOrderDto.getState().equals(BoutiqueOrderStateEnum.SUBMITTED_PAY.getCode())).forEach(feeOrderDto -> {
                // 修改精品停车交费单的状态
                BoutiqueFeeOrder feeOrder = new BoutiqueFeeOrder();
                BeanUtils.copyProperties(feeOrderDto, feeOrder);
                feeOrder.setState(BoutiqueOrderStateEnum.CANCELLED.getCode());
                feeOrder.setCancelerId(record.getOperatorId());
                feeOrder.setCanceler(record.getOperatorName());
                feeOrder.setCancelTime(LocalDateTime.now());
                boutiqueFeeOrderService.updateSelective(feeOrder);

                // 撤销缴费单
                PaymentOrder paymentOrder = this.findPaymentOrder(userTicket, feeOrderDto.getId(), feeOrderDto.getCode());
                paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
                if (paymentOrderService.updateSelective(paymentOrder) == 0) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
                }

                // 撤回结算单多人操作已判断
                settlementRpcResolver.cancel(settlementAppId, paymentOrder.getCode());
            });
        }
    }

    /**
     * 根据条件查询缴费单
     *
     * @param  userTicket
     * @param  businessId
     * @param  businessCode
     * @return PaymentOrder
     */
    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode) throws BusinessException {
        PaymentOrder pb = new PaymentOrder();
        pb.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
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