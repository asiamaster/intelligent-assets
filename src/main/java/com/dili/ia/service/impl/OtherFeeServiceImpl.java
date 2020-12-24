package com.dili.ia.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.DepartmentChargeItem;
import com.dili.ia.domain.OtherFee;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.OtherFeeDto;
import com.dili.ia.domain.dto.OtherFeeRefundOrderDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.OtherFeePrintDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.OtherFeeStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.OtherFeeMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.DepartmentChargeItemService;
import com.dili.ia.service.MchAndDistrictService;
import com.dili.ia.service.OtherFeeService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EnableEnum;
import com.dili.settlement.enums.LinkTypeEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.rpc.SettleOrderRpc;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
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
import java.util.ArrayList;
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
    private DepartmentRpc departmentRpc;

    @Autowired
    private SettleOrderRpc settleOrderRpc;

    @Autowired
    private UidRpcResolver uidRpcResolver;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private MchAndDistrictService mchAndDistrictService;

    @Autowired
    private DepartmentChargeItemService departmentChargeItemService;

    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${otherFee.settlement.handler.url}")
    private String settlerHandlerUrl;
    @Value("${otherFee.settlement.view.url}")
    private String settleViewUrl;
    @Value("${otherFee.settlement.print.url}")
    private String settlerPrintUrl;

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
    public OtherFee addOtherFee(OtherFeeDto otherFeeDto, UserTicket userTicket) {
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

        // 根据区域ID查询商户ID，如果没有区域，则查询收费项所属的商户ID
        Long mchId = mchAndDistrictService.getMchIdByDistrictId(otherFeeDto.getFirstDistrictId(), otherFeeParam.getSecondDistrictId());
        if (mchId == null) {
            // 沈阳市场的收费性才会绑定组织
            List<DepartmentChargeItem> ChargeItemList = departmentChargeItemService.listChargeByChargeItemId(otherFeeDto.getChargeItemId());
            if (CollectionUtil.isEmpty(ChargeItemList)) {
                throw new BusinessException(ResultCode.DATA_ERROR, "区域未查询到商户，并且收费项已删除！");
            }
            if (ChargeItemList.get(0) != null) {
                mchId = ChargeItemList.get(0).getMchId();
            }
        }
        // 新增时没有区域，也不是沈阳的商户，则添加市场ID为商户ID
        if (mchId == null) {
            mchId = userTicket.getFirmId();
        }
        otherFeeParam.setMchId(mchId);

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
    public OtherFee updateOtherFee(OtherFeeDto otherFeeDto, UserTicket userTicket) {
        OtherFee otherFeeParam = new OtherFee();
        BeanUtils.copyProperties(otherFeeDto, otherFeeParam);

        OtherFee otherFeeInfo = this.get(otherFeeDto.getId());
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，修改失败！");
        }

        // 已创建状态才能修改
        if (!OtherFeeStateEnum.CREATED.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能修改！");
        }

        // 根据区域ID查询商户ID,如果没有区域，则查询收费项所属的商户ID
        Long mchId = mchAndDistrictService.getMchIdByDistrictId(otherFeeDto.getFirstDistrictId(), otherFeeParam.getSecondDistrictId());
        if (mchId == null) {
            List<DepartmentChargeItem> ChargeItemList = departmentChargeItemService.listChargeByChargeItemId(otherFeeDto.getChargeItemId());
            if (CollectionUtil.isEmpty(ChargeItemList)) {
                throw new BusinessException(ResultCode.DATA_ERROR, "区域未查询到商户，并且收费项已删除！");
            }
            mchId = ChargeItemList.get(0).getMchId();
        }
        otherFeeParam.setMchId(mchId);
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
    public OtherFee submit(Long id, UserTicket userTicket) {
        OtherFee otherFeeInfo = this.get(id);
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，提交失败！");
        }

        // 已创建状态才能提交
        if (!OtherFeeStateEnum.CREATED.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能提交！");
        }

        // 修改其他交费状态为已提交
        otherFeeInfo.setState(OtherFeeStateEnum.SUBMITTED.getCode());
        otherFeeInfo.setModifyTime(LocalDateTime.now());
        if (this.updateSelective(otherFeeInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        // 创建缴费单,添加到缴费表中
        PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket, BizTypeEnum.OTHER_FEE);
        paymentOrder.setAmount(otherFeeInfo.getAmount());
        paymentOrder.setBusinessId(otherFeeInfo.getId());
        paymentOrder.setBusinessCode(otherFeeInfo.getCode());
        paymentOrder.setBizType(BizTypeEnum.OTHER_FEE.getCode());
        paymentOrder.setCustomerId(otherFeeInfo.getCustomerId());
        paymentOrder.setCustomerName(otherFeeInfo.getCustomerName());
        paymentOrder.setMchId(otherFeeInfo.getMchId());
        paymentOrder.setDistrictId(otherFeeInfo.getSecondDistrictId() == null? otherFeeInfo.getFirstDistrictId():otherFeeInfo.getSecondDistrictId());
        paymentOrder.setVersion(0);
        paymentOrderService.insertSelective(paymentOrder);

        // 组装数据，调用结算RPC
        SettleOrderDto settleOrderDto = this.buildSettleOrderDto(userTicket, otherFeeInfo, paymentOrder);
        BaseOutput<SettleOrder> baseOutput = settleOrderRpc.submit(settleOrderDto);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "提交失败，" + baseOutput.getMessage());
        }

        return otherFeeInfo;
    }

    /**
     * 组装数据，调用结算RPC
     */
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, OtherFee otherFee, PaymentOrder paymentOrder) {
        SettleOrderDto settleOrderDto = new SettleOrderDto();
        // 必填字段
        settleOrderDto.setOrderCode(paymentOrder.getCode());
        settleOrderDto.setBusinessCode(paymentOrder.getBusinessCode());
        settleOrderDto.setCustomerId(otherFee.getCustomerId());
        settleOrderDto.setCustomerName(otherFee.getCustomerName());
        settleOrderDto.setCustomerPhone(otherFee.getCustomerCellphone());
        settleOrderDto.setCustomerCertificate(otherFee.getCertificateNumber());
        settleOrderDto.setAmount(otherFee.getAmount());
        settleOrderDto.setMchId(otherFee.getMchId());
        settleOrderDto.setBusinessDepId(otherFee.getDepartmentId());
        settleOrderDto.setBusinessDepName(departmentRpc.get(otherFee.getDepartmentId()).getData().getName());
        settleOrderDto.setMarketId(otherFee.getMarketId());
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
        settleOrderDto.setBusinessType(BizTypeEnum.OTHER_FEE.getCode());
        settleOrderDto.setType(SettleTypeEnum.PAY.getCode());
        settleOrderDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrderDto.setDeductEnable(EnableEnum.NO.getCode());

        //组装回调url
        List<SettleOrderLink> settleOrderLinkList = new ArrayList<>();
        // 详情
        SettleOrderLink view = new SettleOrderLink();
        view.setType(LinkTypeEnum.DETAIL.getCode());
        view.setUrl(settleViewUrl + "?id=" + otherFee.getId());
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
        sfItem.setChargeItemId(otherFee.getChargeItemId());
        sfItem.setChargeItemName(otherFee.getChargeItemName());
        sfItem.setAmount(paymentOrder.getAmount());
        settleFeeItemList.add(sfItem);
        settleOrderDto.setSettleFeeItemList(settleFeeItemList);
        return settleOrderDto;
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
    public OtherFee cancel(Long id, UserTicket userTicket) {
        OtherFee otherFeeInfo = this.get(id);
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，取消失败！");
        }

        // 已创建状态才能取消
        if (!OtherFeeStateEnum.CREATED.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能取消！");
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
    public OtherFee withdraw(Long id, UserTicket userTicket)  {
        OtherFee otherFeeInfo = this.get(id);
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，撤回失败！");
        }

        // 已创建状态才能取消
        if (!OtherFeeStateEnum.SUBMITTED.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能撤回！");
        }

        otherFeeInfo.setWithdrawOperatorId(userTicket.getId());
        otherFeeInfo.setWithdrawOperator(userTicket.getRealName());
        otherFeeInfo.setState(OtherFeeStateEnum.CREATED.getCode());
        if (this.updateSelective(otherFeeInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 查询缴费单（方法抽取）
        PaymentOrder paymentOrder = this.findPaymentOrder(userTicket, otherFeeInfo.getId(), otherFeeInfo.getCode(), PaymentOrderStateEnum.NOT_PAID.getCode());

        // 撤回付款单和修改缴费单的状态（方法抽取）
        this.withdrawPaymentOrder(paymentOrder);

        return otherFeeInfo;
    }

    /**
     * 查询缴费单
     */
    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode, Integer payState)  {
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
     * 其他收费 退款申请
     *
     * @param  refundOrderDto
     * @param userTicket
     * @return OtherFee
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public OtherFee refund(OtherFeeRefundOrderDto refundOrderDto, UserTicket userTicket)  {
        // 退款申请的条件检查（方法抽取）
        OtherFee otherFeeInfo = checkRefundCondition(refundOrderDto, userTicket);

        // 查询已缴费的缴费单（方法抽取）
        this.findPaymentOrder(userTicket, otherFeeInfo.getId(), otherFeeInfo.getCode(), PaymentOrderStateEnum.PAID.getCode());

        // 修改其他收费业务单的状态
        otherFeeInfo.setModifyTime(LocalDateTime.now());
        otherFeeInfo.setState(OtherFeeStateEnum.REFUNDING.getCode());
        if (this.updateSelective(otherFeeInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 构建退款单以及新增
        refundOrderDto.setCode(uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.getBizTypeEnum(BizTypeEnum.OTHER_FEE.getCode()).getEnName() + "_" + BizNumberTypeEnum.REFUND_ORDER.getCode()));
        refundOrderDto.setBizType(BizTypeEnum.OTHER_FEE.getCode());
        refundOrderDto.setDistrictId(otherFeeInfo.getSecondDistrictId() == null ? otherFeeInfo.getFirstDistrictId():otherFeeInfo.getSecondDistrictId());
        refundOrderDto.setMchId(otherFeeInfo.getMchId());
        BaseOutput output = refundOrderService.doAddHandler(refundOrderDto);
        if (!output.isSuccess()) {
            logger.info("其他收费【编号：{}】退款申请接口异常", refundOrderDto.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常！");
        }

        //根据退款单业务类型启动流程
//        StartProcessInstanceDto startProcessInstanceDto = DTOUtils.newInstance(StartProcessInstanceDto.class);
//        startProcessInstanceDto.setProcessDefinitionKey(BpmDefKeyConfig.getRefundDefKey(refundOrderDto.getBizType()));
//        startProcessInstanceDto.setBusinessKey(refundOrderDto.getCode());
//        startProcessInstanceDto.setUserId(userTicket.getId().toString());
//        BaseOutput<ProcessInstanceMapping> processOutput = runtimeRpc.startProcessInstanceByKey(startProcessInstanceDto);
//        if(!processOutput.isSuccess()){
//            throw new RemoteException(processOutput.getMessage());
//        }
//        //设置流程实例id和流程定义id到退款单对象
//        refundOrderDto.setBizProcessInstanceId(processOutput.getData().getProcessInstanceId());
//        refundOrderDto.setBizProcessDefinitionId(processOutput.getData().getProcessDefinitionId());
        return otherFeeInfo;
    }

    /**
     *  退款申请的条件检查
     */
    private OtherFee checkRefundCondition(OtherFeeRefundOrderDto refundOrderDto, UserTicket userTicket) {
        if (null == userTicket){
            throw new BusinessException(ResultCode.DATA_ERROR, "未登录！");
        }

        // 检查客户状态
        checkCustomerState(refundOrderDto.getPayeeId(), userTicket.getFirmId());

        // 检查其他收费缴费单的状态
        OtherFee otherFeeInfo = this.get(refundOrderDto.getBusinessId());
        if (otherFeeInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "其他收费缴费单不存在！");
        }
        if (!OtherFeeStateEnum.PAID.getCode().equals(otherFeeInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试！");
        }

        // 检查退款金额和缴费金额的大小
        Long totalRefundAmount = otherFeeInfo.getRefundAmount() + refundOrderDto.getPayeeAmount();
        if (otherFeeInfo.getAmount() < totalRefundAmount){
            throw new BusinessException(ResultCode.DATA_ERROR, "退款总金额不能大于订单已交费金额！");
        }

        return otherFeeInfo;
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
     * 缴费成功回调
     *
     * @param  settleOrder
     * @return OtherFee
     * @date   2020/8/19
     */
    @Override
    @GlobalTransactional
    public OtherFee settlementDealHandler(SettleOrder settleOrder)  {
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
    public PrintDataDto<OtherFeePrintDto> receiptPaymentData(String orderCode, Integer reprint)  {
        PaymentOrder paymentOrderCondition = new PaymentOrder();

        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.OTHER_FEE.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new BusinessException(ResultCode.DATA_ERROR, "businessCode无效！");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付！");
        }

        // 组装数据
        OtherFee otherFeeInfo = this.get(paymentOrder.getBusinessId());
        SettleOrder order = settleOrderRpc.get(settlementAppId, paymentOrder.getCode()).getData();
        if (order == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "其他收费缴费单不存在！");
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
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【卡号：" + order.getAccountNumber() + "（" + order.getCustomerName() + "）】";
        } else {
            // 现金以及其他支付方式
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【" + order.getChargeDate() + "  流水号：" + order.getSerialNumber() + "  备注：" + order.getNotes() + "】";
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
    public PrintDataDto<OtherFeePrintDto> receiptRefundPrintData(String orderCode, String reprint)  {
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setCode(orderCode);
        List<RefundOrder> refundOrders = refundOrderService.list(refundOrder);
        if (CollectionUtil.isEmpty(refundOrders)) {
            throw new BusinessException(ResultCode.DATA_ERROR, "未查询到退款单！");
        } else {
            RefundOrder refundOrderInfo = refundOrders.get(0);
            OtherFee otherFeeInfo = this.get(refundOrderInfo.getBusinessId());
            SettleOrder order = settleOrderRpc.get(settlementAppId, refundOrderInfo.getCode()).getData();
            if (order == null) {
                throw new BusinessException(ResultCode.DATA_ERROR, "其他收费退款单不存在！");
            }

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

            // 退款方式,只要如下三种退款方式
            String settleDetails = "收款人：" + refundOrderInfo.getPayee() + "金额：" + refundOrderInfo.getPayeeAmount();
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
            otherFeePrintDto.setSettleWayDetails(settleDetails);

            otherFeePrintDto.setNotes(otherFeeInfo.getNotes());
            otherFeePrintDto.setRefundReason(refundOrderInfo.getRefundReason());
            otherFeePrintDto.setSettlementOperator(order.getOperatorName());

            // 打印最外层
            PrintDataDto<OtherFeePrintDto> printDataDto = new PrintDataDto<>();
            printDataDto.setName(PrintTemplateEnum.OTHER_FEE_REFUND.getCode());
            printDataDto.setItem(otherFeePrintDto);

            return printDataDto;
        }
    }
}