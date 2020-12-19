package com.dili.ia.service.impl;

import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.Customer;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.BoutiqueFeeRefundOrderDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BoutiqueOrderStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.mapper.BoutiqueFeeOrderMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 缴费单
 */
@Service
public class BoutiqueFeeOrderServiceImpl extends BaseServiceImpl<BoutiqueFeeOrder, Long> implements BoutiqueFeeOrderService {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueFeeOrderServiceImpl.class);

    public BoutiqueFeeOrderMapper getActualDao() {
        return (BoutiqueFeeOrderMapper) getDao();
    }

    @Autowired
    private BoutiqueEntranceRecordService boutiqueEntranceRecordService;

    @Autowired
    CustomerRpc customerRpc;

    @Autowired
    private UidRpcResolver uidRpcResolver;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    /**
     * 根据精品停车主键 recordId 查询缴费单列表
     *
     * @param  recordId
     * @return list
     * @date   2020/7/13
     */
    @Override
    public List<BoutiqueFeeOrderDto> listByRecordId(Long recordId) {
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
    public BoutiqueFeeOrderDto getBoutiqueFeeOrderDtoById(Long orderId){
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
     * @param  refundOrderDto
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/23
     */
    @Override
    @GlobalTransactional
    public BoutiqueFeeOrder refund(BoutiqueFeeRefundOrderDto refundOrderDto, UserTicket userTicket) throws BusinessException {
        // 退款申请的条件检查（方法抽取）
        BoutiqueFeeOrder boutiqueFeeOrderInfo = checkRefundCondition(refundOrderDto, userTicket);

        // 修改精品停车费业务单的状态
        boutiqueFeeOrderInfo.setModifyTime(LocalDateTime.now());
        boutiqueFeeOrderInfo.setState(BoutiqueOrderStateEnum.SUBMITTED_REFUND.getCode());
        this.updateSelective(boutiqueFeeOrderInfo);

        // 查询已缴费的缴费单（方法抽取）
        this.findPaymentOrder(userTicket, boutiqueFeeOrderInfo.getId(), boutiqueFeeOrderInfo.getCode(), PaymentOrderStateEnum.PAID.getCode());

        // 构建退款单以及新增
        BoutiqueEntranceRecord recordInfo = boutiqueEntranceRecordService.get(boutiqueFeeOrderInfo.getRecordId());
        refundOrderDto.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.getBizTypeEnum(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode()).getEnName() + "_" + BizNumberTypeEnum.REFUND_ORDER.getCode()));
        refundOrderDto.setBizType(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
        refundOrderDto.setMchId(recordInfo.getMchId());
        BaseOutput output = refundOrderService.doAddHandler(refundOrderDto);
        if (!output.isSuccess()) {
            logger.info("其他收费【编号：{}】退款申请接口异常", refundOrderDto.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常");
        }

        return boutiqueFeeOrderInfo;
    }

    /**
     *  退款申请的条件检查
     */
    private BoutiqueFeeOrder checkRefundCondition(BoutiqueFeeRefundOrderDto refundOrderDto, UserTicket userTicket) {
        if (null == userTicket){
            throw new BusinessException(ResultCode.DATA_ERROR, "未登录！");
        }

        // 检查客户状态
        checkCustomerState(refundOrderDto.getPayeeId(), userTicket.getFirmId());

        // 检查精品停车费缴费单的状态
        BoutiqueFeeOrder boutiqueFeeOrderInfo = this.get(refundOrderDto.getBusinessId());
        if (boutiqueFeeOrderInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "其他收费缴费单不存在！");
        }
        if (!BoutiqueOrderStateEnum.PAID.getCode().equals(boutiqueFeeOrderInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        return boutiqueFeeOrderInfo;
    }

    /**
     * 检查客户状态
     */
    public void checkCustomerState(Long customerId, Long marketId) {
        BaseOutput<Customer> output = customerRpc.get(customerId, marketId);
        if (!output.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户接口调用异常 " + output.getMessage());
        }
        Customer customer = output.getData();
        if (null == customer) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在，请重新修改后保存");
        } else if (EnabledStateEnum.DISABLED.getCode().equals(customer.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户【" + customer.getName() + "】已禁用，请重新修改后保存");
        } else if (YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户【" + customer.getName() + "】已删除，请重新修改后保存");
        }
    }

    /**
     * 查询缴费单
     */
    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode, Integer payState) throws BusinessException {
        PaymentOrder pb = new PaymentOrder();

        pb.setBizType(BizTypeEnum.OTHER_FEE.getCode());
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
     * 取消精品停车的交费
     * 
     * @param  id
     * @param  userTicket
     * @return BoutiqueFeeOrder
     * @date   2020/9/9
     */
    @Override
    public BoutiqueFeeOrder cancel(Long id, UserTicket userTicket) throws BusinessException {
        BoutiqueFeeOrder orderInfo = this.get(id);
        if (orderInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该水电费单号已不存在!");
        }

        // 精品停车缴费，缴费单就是已提交状态
        if (!BoutiqueOrderStateEnum.SUBMITTED_PAY.getCode().equals(orderInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能取消");
        }

        orderInfo.setCancelerId(userTicket.getId());
        orderInfo.setCancelTime(LocalDateTime.now());
        orderInfo.setCanceler(userTicket.getRealName());
        orderInfo.setVersion(orderInfo.getVersion() + 1);
        orderInfo.setState(BoutiqueOrderStateEnum.CANCELLED.getCode());
        if (this.updateSelective(orderInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return orderInfo;
    }
}