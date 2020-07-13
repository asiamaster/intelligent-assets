package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BoutiqueOrderStateEnum;
import com.dili.ia.glossary.BoutiqueStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.mapper.BoutiqueEntranceRecordMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ia.service.BoutiqueFreeSetsService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private BoutiqueFeeOrderService boutiqueFeeOrderService;

    @Autowired
    private BoutiqueFreeSetsService boutiqueFreeSetsService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private SettlementRpcResolver settlementRpcResolver;

    @Value("${settlement.app-id}")
    private Long settlementAppId;

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
        if(sets!=null){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(boutiqueEntranceRecord.getConfirmTime());
            calendar.add(Calendar.HOUR_OF_DAY,sets.getFreeHours());
            recordInfo.setStartTime(calendar.getTime());
            recordInfo.setCountTime(calendar.getTime());
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
        if(recordInfo.getCountTime() != null && recordInfo.getCountTime().getTime() < System.currentTimeMillis()){
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
        recordInfo.setLeaveTime(new Date());
        recordInfo.setModifyTime(new Date());

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
        recordInfo.setLeaveTime(new Date());
        recordInfo.setModifyTime(new Date());
        this.updateSelective(recordInfo);

        // 修改交费单的状态
        this.invalidOrders(recordInfo, userTicket);

        return BaseOutput.success().setData(recordInfo);
    }

    /**
     * 修改交费单的状态
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
                feeOrder.setModifyTime(new Date());
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
        PaymentOrder pb = DTOUtils.newDTO(PaymentOrder.class);
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