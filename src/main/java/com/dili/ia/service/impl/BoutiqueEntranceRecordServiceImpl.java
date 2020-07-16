package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.PrintDataDto;
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
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-13 10:49:05.
 */
@Service
public class BoutiqueEntranceRecordServiceImpl extends BaseServiceImpl<BoutiqueEntranceRecord, Long> implements BoutiqueEntranceRecordService {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueEntranceRecordServiceImpl.class);

    public BoutiqueEntranceRecordMapper getActualDao() {
        return (BoutiqueEntranceRecordMapper)getDao();
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

    @Value("${settlement.app-id}")
    private Long settlementAppId;

    private String settlerHandlerUrl = "http://ia.diligrp.com:8381/api/boutiqueEntrance/settlementDealHandler";

    /**
     * 根据主键获取精品停车数据以及精品停车交费列表
     *
     * @param  id 主键id
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
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/13
     */
    @Override
    public BaseOutput<BoutiqueEntranceRecord> confirm(BoutiqueEntranceRecord boutiqueEntranceRecord, UserTicket userTicket) throws Exception {
        BoutiqueEntranceRecord recordInfo = this.get(boutiqueEntranceRecord.getId());
        if(recordInfo == null){
            throw new BusinessException("","所选记录不存在");
        }
        if(!recordInfo.getState().equals(BoutiqueStateEnum.NOCONFIRM.getCode())){
            throw new BusinessException("","所选记录状态已变更,请刷新重试");
        }

        // 根据车型查询免费时长
        BoutiqueFreeSets sets=boutiqueFreeSetsService.get(recordInfo.getCarTypeId());
        if(sets != null){
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = boutiqueEntranceRecord.getConfirmTime().atZone(zoneId);
            Date date = Date.from(zdt.toInstant());

            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY,sets.getFreeHours());
            recordInfo.setStartTime(calendar.getTime().toInstant().atZone(zoneId).toLocalDateTime());
            recordInfo.setCountTime(calendar.getTime().toInstant().atZone(zoneId).toLocalDateTime());
        }
        recordInfo.setConfirmTime(boutiqueEntranceRecord.getConfirmTime());
        recordInfo.setState(BoutiqueStateEnum.COUNTING.getCode());
        if(userTicket != null){
            recordInfo.setOperatorId(userTicket.getId());
            recordInfo.setOperatorName(userTicket.getRealName());
        }
        this.updateSelective(recordInfo);

        return BaseOutput.success().setData(recordInfo);
    }

    /**
     * 交费提交
     *
     * @param  userTicket
     * @param  feeOrder
     * @return BaseOutput
     * @date   2020/7/14
     */
    @Override
    public BaseOutput<BoutiqueEntranceRecord> submit(BoutiqueFeeOrder feeOrder, UserTicket userTicket) throws Exception {
        // 先查询精品停车信息
        BoutiqueEntranceRecord recordInfo = this.get(feeOrder.getRecordId());
        if (recordInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "精品停车信息不存在");
        }

        // 先根据 recordId 查询是否已存在已提交状态的交费单
        List<BoutiqueFeeOrderDto> orderDtoList = boutiqueFeeOrderService.listByRecordId(feeOrder.getRecordId());
        if (orderDtoList != null && orderDtoList.size() > 0) {
            orderDtoList.stream().filter(feeOrderDto -> BoutiqueOrderStateEnum.SUBMIT.getCode().equals(feeOrderDto.getState())).forEach(feeOrderDto -> {
                throw new BusinessException(ResultCode.DATA_ERROR, "已存在已提交的交费单，请先处理。");
            });
        }

        String code = uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.METER_DETAIL_CODE.getCode());

        // 新增精品停车交费单
        feeOrder.setCode(code);
        feeOrder.setCreateTime(LocalDateTime.now());
        feeOrder.setModifyTime(LocalDateTime.now());
        feeOrder.setOperatorId(userTicket.getId());
        feeOrder.setMarketId(userTicket.getFirmId());
        feeOrder.setMarketCode(userTicket.getFirmCode());
        feeOrder.setOperatorName(userTicket.getRealName());
        feeOrder.setState(BoutiqueOrderStateEnum.SUBMIT.getCode());
        boutiqueFeeOrderService.insertSelective(feeOrder);

        // 创建缴费单
        PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket);
        paymentOrder.setBusinessCode(code);
        paymentOrder.setBusinessId(feeOrder.getId());
        paymentOrder.setAmount(feeOrder.getAmount());
        paymentOrder.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        paymentOrderService.insertSelective(paymentOrder);

        // 调用结算接口,缴费
        SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, feeOrder, recordInfo, paymentOrder.getCode(), paymentOrder.getAmount());
        settlementRpcResolver.submit(settleOrderDto);

        return null;
    }

    /**
     * 构建结算实体类
     * @param userTicket
     * @param feeOrder
     * @param
     * @param orderCode
     * @param amount
     * @return
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
        settleOrderInfoDto.setBusinessType(Integer.valueOf(BizTypeEnum.UTTLITIES.getCode()));
        settleOrderInfoDto.setType(SettleTypeEnum.PAY.getCode());
        settleOrderInfoDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrderInfoDto.setReturnUrl(settlerHandlerUrl);
        if (userTicket.getDepartmentId() != null){
            settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
        return settleOrderInfoDto;
    }

    /**
     * 离场
     *
     * @param id 主键
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/13
     */
    @Override
    public BaseOutput<BoutiqueEntranceRecord> leave(Long id, UserTicket userTicket) throws Exception {
        BoutiqueEntranceRecord recordInfo = this.get(id);
        if(recordInfo == null){
            throw new BusinessException("","所选记录不存在");
        }
        if(!(recordInfo.getState().equals(BoutiqueStateEnum.NOCONFIRM.getCode()) || recordInfo.getState().equals(BoutiqueStateEnum.COUNTING.getCode()))){
            throw new BusinessException("","所选记录状态已变更,请刷新重试");
        }
        if(recordInfo.getCountTime() != null && recordInfo.getCountTime().isBefore(LocalDateTime.now())){
            throw new BusinessException("",recordInfo.getPlate()+" 有停车费未结清，请结清后再离场");
        }
        List<BoutiqueFeeOrderDto> orderDtoList = boutiqueFeeOrderService.listByRecordId(recordInfo.getId());
        if(orderDtoList != null && orderDtoList.size()>0){
            orderDtoList.stream().filter(order -> order.getState().equals(BoutiqueOrderStateEnum.SUBMIT.getCode())).forEach(order -> {
                throw new BusinessException("", recordInfo.getPlate() + " 有停车费未结清，请结清后再离场");
            });
        }
        recordInfo.setState(BoutiqueStateEnum.LEAVE.getCode());
        if(userTicket != null){
            recordInfo.setOperatorId(userTicket.getId());
            recordInfo.setOperatorName(userTicket.getRealName());
        }
        recordInfo.setLeaveTime(LocalDateTime.now());
        recordInfo.setModifyTime(LocalDateTime.now());

        return BaseOutput.success().setData(recordInfo);
    }

    /**
     * 强制离场
     *
     * @param id 主键
     * @return BaseOutput
     * @date   2020/7/13
     */
    @Override
    public BaseOutput<BoutiqueEntranceRecord> forceLeave(Long id, UserTicket userTicket) {

        BoutiqueEntranceRecord recordInfo = this.get(id);
        if(recordInfo == null){
            throw new BusinessException("","所选记录不存在");
        }
        if(!recordInfo.getState().equals(BoutiqueStateEnum.COUNTING.getCode())){
            throw new BusinessException("","所选记录状态已变更,请刷新重试");
        }
        //修改记录状态为已离场
        recordInfo.setState(BoutiqueStateEnum.LEAVE.getCode());
        recordInfo.setOperatorId(userTicket.getId());
        recordInfo.setOperatorName(userTicket.getRealName());
        recordInfo.setLeaveTime(LocalDateTime.now());
        recordInfo.setModifyTime(LocalDateTime.now());
        this.updateSelective(recordInfo);

        // 修改交费单的状态
        this.invalidOrders(recordInfo, userTicket);

        return BaseOutput.success().setData(recordInfo);
    }

    /**
     * 缴费成功回调
     *
     * @param  settleOrder
     * @return BaseOutput
     * @date   2020/7/14
     */
    @Override
    public BaseOutput<EarnestOrder> settlementDealHandler(SettleOrder settleOrder) {
        // 修改缴费单相关数据
        if (null == settleOrder){
            throw new BusinessException(ResultCode.PARAMS_ERROR, "回调参数为空！");
        }
        PaymentOrder condition = DTOUtils.newInstance(PaymentOrder.class);
        //结算单code唯一
        condition.setCode(settleOrder.getOrderCode());
        condition.setBizType(BizTypeEnum.EARNEST.getCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        BoutiqueFeeOrder feeOrderInfo = boutiqueFeeOrderService.get(paymentOrderPO.getBusinessId());
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) { //如果已支付，直接返回
            return BaseOutput.success().setData(feeOrderInfo);
        }
        if (!paymentOrderPO.getState().equals(PaymentOrderStateEnum.NOT_PAID.getCode())){
            logger.info("缴费单状态已变更！状态为：" + PaymentOrderStateEnum.getPaymentOrderStateEnum(paymentOrderPO.getState()).getName() );
            return BaseOutput.failure("缴费单状态已变更！");
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
        feeOrderInfo.setState(BoutiqueOrderStateEnum.PAY.getCode());
        feeOrderInfo.setModifyTime(LocalDateTime.now());
        if (boutiqueFeeOrderService.updateSelective(feeOrderInfo) == 0) {
            logger.info("缴费单成功回调 -- 更新【水电费单】状态,乐观锁生效！【水电费单Id:{}】", feeOrderInfo.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return BaseOutput.success().setData(feeOrderInfo);
    }

    /**
     * 打印票据
     *
     * @param  orderCode
     * @param  reprint
     * @return
     * @date   2020/7/14
     */
    @Override
    public PrintDataDto<BoutiqueEntrancePrintDto> receiptPaymentData(String orderCode, Integer reprint) {
        PaymentOrder paymentOrderCondition = new PaymentOrder();
        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new RuntimeException("businessCode无效");
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
        boutiqueTrancePrintDto.setStartTime(feeOrder.getStartTime());
        boutiqueTrancePrintDto.setEndTime(feeOrder.getEndTime());
        boutiqueTrancePrintDto.setNotes(feeOrder.getNotes());
        boutiqueTrancePrintDto.setAmount(MoneyUtils.centToYuan(feeOrder.getAmount()));
        boutiqueTrancePrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        boutiqueTrancePrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        boutiqueTrancePrintDto.setSubmitter(paymentOrder.getCreator());
        boutiqueTrancePrintDto.setBusinessType(BizTypeEnum.EARNEST.getName());
        boutiqueTrancePrintDto.setPlate(recordInfo.getPlate());
        // 拼接结算详情
        String settleWayDetails = "计费时间：" + feeOrder.getStartTime() + "至" + feeOrder.getEndTime();
        boutiqueTrancePrintDto.setSettlementOperator(settleWayDetails);

        PrintDataDto<BoutiqueEntrancePrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.BOUTIQUE_ENTRANCE.getCode());
        printDataDto.setItem(boutiqueTrancePrintDto);
        return printDataDto;
    }


    /**
     * 修改交费单的状态（作废交费单）
     */
    private void invalidOrders(BoutiqueEntranceRecord record, UserTicket userTicket){
        List<BoutiqueFeeOrderDto> orderDtoList=boutiqueFeeOrderService.listByRecordId(record.getId());
        if(orderDtoList != null && orderDtoList.size()>0){
            orderDtoList.stream().filter(feeOrderDto -> feeOrderDto.getState().equals(BoutiqueOrderStateEnum.SUBMIT.getCode())).forEach(feeOrderDto -> {
                // 修改精品停车交费单的状态
                BoutiqueFeeOrder feeOrder = new BoutiqueFeeOrder();
                BeanUtils.copyProperties(feeOrderDto, feeOrder);
                feeOrder.setState(BoutiqueOrderStateEnum.CANCEL.getCode());
                feeOrder.setOperatorId(record.getOperatorId());
                feeOrder.setOperatorName(record.getOperatorName());
                feeOrder.setModifyTime(LocalDateTime.now());
                boutiqueFeeOrderService.updateSelective(feeOrder);

                // 撤销缴费单
                PaymentOrder paymentOrder = this.findPaymentOrder(userTicket, feeOrderDto.getId(), feeOrderDto.getCode());
                paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
                if (paymentOrderService.updateSelective(paymentOrder) == 0) {
                    logger.info("撤回定金【删除缴费单】失败.");
                    throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
                }

                // 撤回结算单多人操作已判断
                settlementRpcResolver.cancel(settlementAppId, paymentOrder.getCode());
            });
        }
    }

    /**
     * 根据条件查询缴费单
     * @param userTicket
     * @param businessId
     * @param businessCode
     * @return
     */
    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode){
        PaymentOrder pb = new PaymentOrder();
        pb.setBizType(BizTypeEnum.UTTLITIES.getCode());
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(userTicket.getFirmId());
        pb.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        PaymentOrder order = paymentOrderService.listByExample(pb).stream().findFirst().orElse(null);
        if (null == order) {
            logger.info("没有查询到付款单PaymentOrder【业务单businessId：{}】 【业务单businessCode:{}】", businessId, businessCode);
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到付款单！");
        }

        return order;
    }

}