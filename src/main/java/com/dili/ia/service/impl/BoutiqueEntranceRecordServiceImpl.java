package com.dili.ia.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.assets.sdk.dto.TypeMarketDto;
import com.dili.assets.sdk.rpc.TypeMarketRpc;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.printDto.BoutiqueEntrancePrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.BoutiqueEntranceRecordMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.*;
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
    private TypeMarketRpc typeMarketRpc;

    @Autowired
    private DepartmentRpc departmentRpc;

    @Autowired
    private UidRpcResolver uidRpcResolver;

    @Autowired
    private SettleOrderRpc settleOrderRpc;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private BoutiqueFeeOrderService boutiqueFeeOrderService;

    @Autowired
    private BoutiqueFreeSetsService boutiqueFreeSetsService;

    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${boutiqueEntrance.settlement.handler.url}")
    private String settlerHandlerUrl;
    @Value("${boutiqueEntrance.settlement.view.url}")
    private String settleViewUrl;
    @Value("${boutiqueEntrance.settlement.print.url}")
    private String settlerPrintUrl;

    /**
     * 列表查询
     *
     * @param  boutiqueDto
     * @return EasyuiPageOutput
     * @date   2020/8/17
     */
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
     * @date   2020/7/23
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
    public BoutiqueEntranceRecord confirm(BoutiqueEntranceRecord boutiqueEntranceRecord, UserTicket userTicket)  {
        BoutiqueEntranceRecord recordInfo = this.get(boutiqueEntranceRecord.getId());
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，确认计费失败！");
        }

        if (!recordInfo.getState().equals(BoutiqueStateEnum.NOCONFIRM.getCode())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是待确认，不能确认计费！");
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
    public BoutiqueEntranceRecord submit(BoutiqueFeeOrder feeOrder, UserTicket userTicket)  {
        // 先查询精品停车信息
        BoutiqueEntranceRecord recordInfo = this.get(feeOrder.getRecordId());
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，提交失败！");
        }

        // 先根据 recordId 查询是否已存在已提交状态的交费单
        List<BoutiqueFeeOrderDto> orderDtoList = boutiqueFeeOrderService.listByRecordId(feeOrder.getRecordId());
        if (orderDtoList != null && orderDtoList.size() > 0) {
            for (BoutiqueFeeOrderDto feeOrderDto : orderDtoList) {
                if (BoutiqueOrderStateEnum.SUBMITTED_PAY.getCode().equals(feeOrderDto.getState())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "已存在已提交的交费单，请先处理！");
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

        // 创建缴费单，添加到缴费表中
        PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket, BizTypeEnum.BOUTIQUE_ENTRANCE);
        paymentOrder.setAmount(feeOrder.getAmount());
        paymentOrder.setBusinessId(feeOrder.getId());
        paymentOrder.setBusinessCode(feeOrder.getCode());
        paymentOrder.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        paymentOrder.setCustomerId(recordInfo.getCustomerId());
        paymentOrder.setCustomerName(recordInfo.getCustomerName());
        paymentOrder.setMchId(recordInfo.getMchId());
        paymentOrder.setVersion(0);
        paymentOrderService.insertSelective(paymentOrder);

        // 组装数据，调用结算RPC
        SettleOrderDto settleOrderDto = this.buildSettleOrderDto(userTicket, feeOrder, recordInfo, paymentOrder);
        BaseOutput<SettleOrder> baseOutput = settleOrderRpc.submit(settleOrderDto);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "提交失败，" + baseOutput.getMessage());
        }

        return recordInfo;
    }

    /**
     * 组装数据，调用结算RPC
     */
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, BoutiqueFeeOrder feeOrder, BoutiqueEntranceRecord record, PaymentOrder paymentOrder) {
        SettleOrderDto settleOrderDto = new SettleOrderDto();
        // 必填字段
        settleOrderDto.setOrderCode(paymentOrder.getCode());
        settleOrderDto.setBusinessCode(paymentOrder.getBusinessCode());
        settleOrderDto.setCustomerId(record.getCustomerId());
        settleOrderDto.setCustomerName(record.getCustomerName());
        settleOrderDto.setCustomerPhone(record.getCustomerCellphone());
        settleOrderDto.setCustomerCertificate(record.getCertificateNumber());
        settleOrderDto.setAmount(feeOrder.getAmount());
        settleOrderDto.setMchId(record.getMchId());
        settleOrderDto.setBusinessDepId(record.getDepartmentId());
        settleOrderDto.setBusinessDepName(departmentRpc.get(record.getDepartmentId()).getData().getName());
        settleOrderDto.setMarketId(feeOrder.getMarketId());
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
        settleOrderDto.setBusinessType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        settleOrderDto.setType(SettleTypeEnum.PAY.getCode());
        settleOrderDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrderDto.setDeductEnable(EnableEnum.NO.getCode());

        //组装回调url
        List<SettleOrderLink> settleOrderLinkList = new ArrayList<>();
        // 详情
        SettleOrderLink view = new SettleOrderLink();
        view.setType(LinkTypeEnum.DETAIL.getCode());
        view.setUrl(settleViewUrl + "?id=" + record.getId());
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
        sfItem.setChargeItemId(ChargeItemEnum.精品黄楼停车费.getId());
        sfItem.setChargeItemName(ChargeItemEnum.精品黄楼停车费.getName());
        sfItem.setAmount(paymentOrder.getAmount());
        settleFeeItemList.add(sfItem);
        settleOrderDto.setSettleFeeItemList(settleFeeItemList);
        return settleOrderDto;
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
    public BoutiqueEntranceRecord leave(Long id, UserTicket userTicket)  {
        BoutiqueEntranceRecord recordInfo = this.get(id);
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，离场失败！");
        }

        if (!(recordInfo.getState().equals(BoutiqueStateEnum.NOCONFIRM.getCode()) || recordInfo.getState().equals(BoutiqueStateEnum.COUNTING.getCode()))) {
            throw new BusinessException(ResultCode.DATA_ERROR, "所选记录状态已变更，请刷新重试！");
        }

        if (recordInfo.getCountTime() != null && recordInfo.getCountTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ResultCode.DATA_ERROR, recordInfo.getPlate() + " 有停车费未结清，请结清后再离场！");
        }

        List<BoutiqueFeeOrderDto> orderDtoList = boutiqueFeeOrderService.listByRecordId(recordInfo.getId());
        if (orderDtoList != null && orderDtoList.size() > 0) {
            for (BoutiqueFeeOrderDto orderDto : orderDtoList) {
                if (BoutiqueOrderStateEnum.SUBMITTED_PAY.getCode().equals(orderDto.getState())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, recordInfo.getPlate() + " 有停车费未结清，请结清后再离场！");
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
    public BoutiqueEntranceRecord forceLeave(Long id, UserTicket userTicket)  {
        BoutiqueEntranceRecord recordInfo = this.get(id);

        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "所选记录不存在！");
        }
        if (!recordInfo.getState().equals(BoutiqueStateEnum.COUNTING.getCode())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "所选记录状态已变更，请刷新重试！");
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
    public BoutiqueFeeOrder settlementDealHandler(SettleOrder settleOrder)  {
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
    public PrintDataDto<BoutiqueEntrancePrintDto> receiptPaymentData(String orderCode, Integer reprint)  {
        PaymentOrder paymentOrderCondition = new PaymentOrder();
        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new BusinessException(ResultCode.DATA_ERROR,"businessCode无效。");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付！");
        }

        // 组装数据
        BoutiqueFeeOrder feeOrder = boutiqueFeeOrderService.get(paymentOrder.getBusinessId());
        if (feeOrder == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "精品停车交费单不存在!");
        }
        BoutiqueEntranceRecord recordInfo = this.get(feeOrder.getRecordId());
        BoutiqueEntrancePrintDto boutiqueTrancePrintDto = new BoutiqueEntrancePrintDto();
        boutiqueTrancePrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        boutiqueTrancePrintDto.setPrintTime(LocalDateTime.now());
        boutiqueTrancePrintDto.setCode(feeOrder.getCode());
        boutiqueTrancePrintDto.setCustomerName(recordInfo.getCustomerName());
        boutiqueTrancePrintDto.setCustomerCellphone(recordInfo.getCustomerCellphone());
        boutiqueTrancePrintDto.setAmount(MoneyUtils.centToYuan(feeOrder.getAmount()));
        boutiqueTrancePrintDto.setBusinessType(BizTypeEnum.BOUTIQUE_ENTRANCE.getName());
        boutiqueTrancePrintDto.setPlate(recordInfo.getPlate());
        boutiqueTrancePrintDto.setConfirmTime(recordInfo.getConfirmTime());
        boutiqueTrancePrintDto.setEndTime(feeOrder.getEndTime());
        boutiqueTrancePrintDto.setStartTime(feeOrder.getStartTime());
        boutiqueTrancePrintDto.setNotes(feeOrder.getNotes());
        boutiqueTrancePrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        boutiqueTrancePrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        boutiqueTrancePrintDto.setSubmitter(paymentOrder.getCreator());
        PrintDataDto<BoutiqueEntrancePrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.BOUTIQUE_PAY.getCode());
        printDataDto.setItem(boutiqueTrancePrintDto);

        return printDataDto;
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
    public PrintDataDto<BoutiqueEntrancePrintDto> receiptRefundPrintData(String orderCode, String reprint)  {
        RefundOrder condtion = new RefundOrder();
        condtion.setCode(orderCode);
        List<RefundOrder> refundOrders = refundOrderService.list(condtion);
        if (CollectionUtil.isEmpty(refundOrders)) {
            throw new BusinessException(ResultCode.DATA_ERROR, "未查询到退款单！");
        } else {
            RefundOrder refundOrder = refundOrders.get(0);
            BoutiqueFeeOrder orderInfo = boutiqueFeeOrderService.get(refundOrder.getBusinessId());
            BoutiqueEntranceRecord recordInfo = this.get(orderInfo.getRecordId());
            SettleOrder order = settleOrderRpc.get(settlementAppId, refundOrder.getCode()).getData();
            if (order == null) {
                throw new BusinessException(ResultCode.DATA_ERROR, "精品黄楼退款费单不存在！");
            }
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

            // 精品停车特殊字段以及开始时间结束时间
            printDto.setPlate(recordInfo.getPlate());
            printDto.setConfirmTime(recordInfo.getConfirmTime());
            printDto.setEndTime(orderInfo.getEndTime());
            printDto.setStartTime(orderInfo.getStartTime());

            // 打印最外层
            PrintDataDto<BoutiqueEntrancePrintDto> printDataDto = new PrintDataDto<>();
            printDataDto.setName(PrintTemplateEnum.BOUTIQUE_REFUND.getName());
            printDataDto.setItem(printDto);

            return printDataDto;
        }
    }


    /**
     * 取消(进门取消，可在待确认和计费中取消)
     *
     * @param  recordDto
     * @return BoutiqueFeeOrder
     * @date   2020/8/5
     */
    @Override
    public BoutiqueEntranceRecord cancel(BoutiqueEntranceRecordDto recordDto)  {
        // 根据bid 查询数据
        BoutiqueEntranceRecordDto recordInfo = this.getActualDao().getBoutiqueByBid(recordDto.getBid());
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，取消失败！");
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
        // 商户ID需要从基础信息中获取
        BaseOutput<List<TypeMarketDto>> baseOutput = typeMarketRpc.queryAll();
        if (baseOutput.isSuccess() && baseOutput.getData() != null) {
            for (TypeMarketDto typeMarketDto : baseOutput.getData()) {
                if (BizNumberTypeEnum.BOUTIQUE_ENTRANCE.getCode().equals(typeMarketDto.getType())) {
                    boutiqueEntranceRecord.setMchId(typeMarketDto.getMarketId());
                }
            }
            boutiqueEntranceRecord.setCreateTime(LocalDateTime.now());
            boutiqueEntranceRecord.setModifyTime(LocalDateTime.now());
            boutiqueEntranceRecord.setState(BoutiqueStateEnum.NOCONFIRM.getCode());

            this.insertSelective(boutiqueEntranceRecord);
        }

        return boutiqueEntranceRecord;
    }

    /**
     * 修改交费单的状态（作废交费单）
     */
    private void invalidOrders(BoutiqueEntranceRecord record, UserTicket userTicket)  {
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
                settleOrderRpc.cancel(settlementAppId, paymentOrder.getCode());
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
    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode)  {
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