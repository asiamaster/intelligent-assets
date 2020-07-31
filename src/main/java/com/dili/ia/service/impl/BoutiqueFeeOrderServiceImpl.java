package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.BoutiqueFeeRefundOrderDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BoutiqueOrderStateEnum;
import com.dili.ia.glossary.PassportStateEnum;
import com.dili.ia.mapper.BoutiqueFeeOrderMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.ia.util.LoggerUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  缴费单
 */
@Service
public class BoutiqueFeeOrderServiceImpl extends BaseServiceImpl<BoutiqueFeeOrder, Long> implements BoutiqueFeeOrderService {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueFeeOrderServiceImpl.class);

    public BoutiqueFeeOrderMapper getActualDao() {
        return (BoutiqueFeeOrderMapper)getDao();
    }

    @Autowired
    private BoutiqueEntranceRecordService boutiqueEntranceRecordService;

    @Autowired
    private UidRpcResolver uidRpcResolver;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private TransferDeductionItemService transferDeductionItemService;

    /**
     * 根据精品停车主键 recordId 查询缴费单列表
     *
     * @param  recordId 精品停车主键
     * @return list
     * @date   2020/7/13
     */
    @Override
    public List<BoutiqueFeeOrderDto> listByRecordId(Long recordId){
        List<BoutiqueFeeOrderDto> boutiqueFeeOrderDtoList = this.getActualDao().listByRecordId(recordId);
        return boutiqueFeeOrderDtoList;
    }

    /**
     * 根据主键查询精品停车单和精品停车相关信息
     *
     * @param  orderId
     * @return BoutiqueFeeOrderDto
     * @date   2020/7/31
     */
    @Override
    public BoutiqueFeeOrderDto getBoutiqueFeeOrderDtoById(Long orderId) throws Exception {
        BoutiqueFeeOrderDto boutiqueFeeOrderDto = new BoutiqueFeeOrderDto();
        BoutiqueFeeOrder orderInfo = this.get(orderId);
        if (orderInfo != null) {
            BeanUtils.copyProperties(orderInfo, boutiqueFeeOrderDto);
            BoutiqueEntranceRecord recordInfo = boutiqueEntranceRecordService.get(orderInfo.getRecordId());
            if (recordInfo != null) {
                boutiqueFeeOrderDto.setCustomerId(recordInfo.getCustomerId());
                boutiqueFeeOrderDto.setCustomerName(recordInfo.getCustomerName());
                boutiqueFeeOrderDto.setCustomerCellphone(recordInfo.getCustomerCellphone());
                boutiqueFeeOrderDto.setCertificateNumber(recordInfo.getCertificateNumber());
                boutiqueFeeOrderDto.setDepartmentId(recordInfo.getDepartmentId());
                boutiqueFeeOrderDto.setDepartmentName(recordInfo.getDepartmentName());

            }
        }
        return boutiqueFeeOrderDto;
    }

    /**
     * 退款申请
     *
     * @param refundDto
     * @return BaseOutput
     * @date   2020/7/23
     */
    @Override
    @GlobalTransactional
    public void refund(BoutiqueFeeRefundOrderDto refundDto) throws Exception {
        BoutiqueFeeOrder orderInfo = new BoutiqueFeeOrder();
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        // 查询相关数据
        BoutiqueFeeOrderDto orderDtoInfo = this.getBoutiqueFeeOrderDtoById(refundDto.getBusinessId());
        if (orderDtoInfo != null && !BoutiqueOrderStateEnum.PAID.getCode().equals(orderDtoInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }
        // 构建退款单,并且新增
        buildRefundOrderDto(userTicket, refundDto, orderDtoInfo);

        LoggerUtil.buildLoggerContext(orderDtoInfo.getId(), orderDtoInfo.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);

        // 修改状态
        orderDtoInfo.setModifyTime(LocalDateTime.now());
        orderDtoInfo.setState(PassportStateEnum.SUBMITTED_REFUND.getCode());
        BeanUtils.copyProperties(orderDtoInfo, orderInfo);
        this.updateSelective(orderInfo);

        // 转抵信息
        if (CollectionUtils.isNotEmpty(refundDto.getTransferDeductionItems())) {
            refundDto.getTransferDeductionItems().forEach(o -> {
                o.setRefundOrderId(refundDto.getBusinessId());
                transferDeductionItemService.insertSelective(o);
            });
        }
    }

    @Override
    public BaseOutput<BoutiqueFeeOrder> cancel(Long id, UserTicket userTicket) throws Exception {
        BoutiqueFeeOrder orderInfo = this.get(id);

        if (orderInfo == null) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "该水电费单号已不存在!");
        }

        // 已创建状态才能取消
        if (!BoutiqueOrderStateEnum.CREATED.getCode().equals(orderInfo.getState())) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "该状态不是已创建，不能取消");
        }

        orderInfo.setCancelerId(userTicket.getId());
        orderInfo.setCanceler(userTicket.getRealName());
        orderInfo.setCancelTime(LocalDateTime.now());
        orderInfo.setState(BoutiqueOrderStateEnum.CANCELLED.getCode());
        if (this.updateSelective(orderInfo) == 0) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return BaseOutput.success().setData(orderInfo);
    }

    /**
     * 构建退款
     */
    private RefundOrder buildRefundOrderDto(UserTicket userTicket, BoutiqueFeeRefundOrderDto refundOrderDto, BoutiqueFeeOrderDto orderDto) {
        //退款单
        RefundOrder refundOrder = new RefundOrder();

        refundOrder.setMarketId(userTicket.getFirmId());
        refundOrder.setMarketCode(userTicket.getFirmCode());

        refundOrder.setBusinessId(orderDto.getId());
        refundOrder.setBusinessCode(orderDto.getCode());
        refundOrder.setCustomerId(orderDto.getCustomerId());
        refundOrder.setCustomerName(orderDto.getCustomerName());
        refundOrder.setCertificateNumber(orderDto.getCertificateNumber());
        refundOrder.setCustomerCellphone(orderDto.getCustomerCellphone());

        refundOrder.setPayee(refundOrderDto.getPayee());
        refundOrder.setPayeeId(refundOrderDto.getPayeeId());
        refundOrder.setPayeeAmount(refundOrderDto.getPayeeAmount());
        refundOrder.setRefundReason(refundOrderDto.getRefundReason());
        refundOrder.setTotalRefundAmount(refundOrderDto.getTotalRefundAmount());

        refundOrder.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        refundOrder.setCode(uidRpcResolver.bizNumber(BizNumberTypeEnum.BOUTIQUE_ORDER_REFUND.getCode()));

        if (!refundOrderService.doAddHandler(refundOrder).isSuccess()) {
            logger.info("通行证【编号：{}】退款申请接口异常", refundOrder.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常");
        }
        return refundOrder;
    }
}