package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.AssetLeaseOrderListDto;
import com.dili.ia.domain.dto.LeaseOrderListDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.RefundOrderDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.AssetLeaseOrderMapper;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.*;
import com.dili.ia.util.SpringUtil;
import com.dili.logger.sdk.component.MsgService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Service
public class AssetLeaseOrderServiceImpl extends BaseServiceImpl<AssetLeaseOrder, Long> implements AssetLeaseOrderService {
    private final static Logger LOG = LoggerFactory.getLogger(AssetLeaseOrderServiceImpl.class);
    public AssetLeaseOrderMapper getActualDao() {
        return (AssetLeaseOrderMapper)getDao();
    }

    @Autowired
    private DepartmentRpc departmentRpc;
    @Autowired
    private AssetLeaseOrderItemService assetLeaseOrderItemService;
    @Autowired
    private SettlementRpc settlementRpc;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${settlement.handler.url}")
    private String settlerHandlerUrl;
    @Autowired
    private UidFeignRpc uidFeignRpc;
    @Autowired
    private CustomerAccountService customerAccountService;
    @Autowired
    private AssetsRpc assetsRpc;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private MsgService msgService;
    @Autowired
    private TransactionDetailsService transactionDetailsService;
    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    @Override
    public BaseOutput saveLeaseOrder(AssetLeaseOrderListDto dto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }

        //检查客户状态
        checkCustomerState(dto.getCustomerId(),userTicket.getFirmId());
        dto.getLeaseOrderItems().forEach(o->{
            //检查摊位状态
            checkBoothState(o.getAssetId());
        });

        dto.setMarketId(userTicket.getFirmId());
        dto.setMarketCode(userTicket.getFirmCode());
        dto.setCreatorId(userTicket.getId());
        dto.setCreator(userTicket.getRealName());

        if (null == dto.getId()) {
            //租赁单新增
            checkContractNo(null, dto.getContractNo(), true);//合同编号验证重复
            BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.LEASE_ORDER.getCode());
            if (!bizNumberOutput.isSuccess()) {
                LOG.info("租赁单编号生成异常");
                throw new BusinessException(ResultCode.DATA_ERROR,"编号生成器微服务异常");
            }
            dto.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());
            dto.setState(LeaseOrderStateEnum.CREATED.getCode());
            dto.setPayState(PayStateEnum.NOT_PAID.getCode());
            dto.setWaitAmount(dto.getPayAmount());
            insertSelective(dto);
            insertLeaseOrderItems(dto);
        } else {
            //租赁单修改
            checkContractNo(dto.getId(), dto.getContractNo(), false);//合同编号验证重复
            AssetLeaseOrder oldLeaseOrder = get(dto.getId());
            if(!LeaseOrderStateEnum.CREATED.getCode().equals(oldLeaseOrder.getState())){
                throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + oldLeaseOrder.getCode() + "】 状态已变更，不可以进行修改操作");
            }
            dto.setVersion(oldLeaseOrder.getVersion());
            dto.setWaitAmount(dto.getPayAmount());
            SpringUtil.copyPropertiesIgnoreNull(dto,oldLeaseOrder);
            oldLeaseOrder.setContractNo(dto.getContractNo());
            oldLeaseOrder.setNotes(dto.getNotes());
            if (update(oldLeaseOrder) == 0) {
                LOG.info("摊位租赁单修改异常,乐观锁生效 【租赁单编号:{}】", dto.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }

            deleteLeaseOrderItems(dto.getId());
            insertLeaseOrderItems(dto);
        }
        return BaseOutput.success().setData(dto);
    }

    /**
     * 删除订单项
     * @param leaseOrderId
     */
    private void deleteLeaseOrderItems(Long leaseOrderId){
        AssetLeaseOrderItem condition = new AssetLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrderId);
        List<AssetLeaseOrderItem> assetLeaseOrderItems = assetLeaseOrderItemService.listByExample(condition);
        assetLeaseOrderItemService.deleteByExample(condition);
        assetLeaseOrderItems.forEach(o->{
            BusinessChargeItem bciCondition = new BusinessChargeItem();
            bciCondition.setBusinessId(o.getId());
            bciCondition.setBizType(AssetsTypeEnum.getAssetsTypeEnum(o.getAssetType()).getCode());
            businessChargeItemService.deleteByExample(bciCondition);
        });

    }


    /**
     * 批量插入租赁单项
     *
     * @param dto
     */
    private void insertLeaseOrderItems(AssetLeaseOrderListDto dto) {
        dto.getLeaseOrderItems().forEach(o -> {
            o.setLeaseOrderId(dto.getId());
            o.setLeaseOrderCode(dto.getCode());
            o.setCustomerId(dto.getCustomerId());
            o.setCustomerName(dto.getCustomerName());
            o.setState(LeaseOrderStateEnum.CREATED.getCode());
            o.setPayState(PayStateEnum.NOT_PAID.getCode());
            o.setStopRentState(StopRentStateEnum.NO_APPLY.getCode());
            assetLeaseOrderItemService.insertSelective(o);

            if(CollectionUtils.isNotEmpty(o.getBusinessChargeItems())){
                o.getBusinessChargeItems().forEach(bci->{
                    bci.setBusinessId(o.getId());
                    businessChargeItemService.insertSelective(bci);
                });
            }
        });
    }

    /**
     * 合同编号验重
     * @param leaseOrderId 待修改的租赁单Id
     * @param contractNo
     * @param isAdd
     */
    private void checkContractNo(Long leaseOrderId,String contractNo,Boolean isAdd){
        if(StringUtils.isNotBlank(contractNo)){
            AssetLeaseOrder condition = new AssetLeaseOrder();
            condition.setContractNo(contractNo);
            List<AssetLeaseOrder> leaseOrders = list(condition);
            if(isAdd && CollectionUtils.isNotEmpty(leaseOrders)){
                throw new BusinessException(ResultCode.DATA_ERROR,"合同编号不允许重复使用，请修改");
            }else {
                if(leaseOrders.size() == 1){
                    AssetLeaseOrder leaseOrder = leaseOrders.get(0);
                    if(!leaseOrder.getId().equals(leaseOrderId)){
                        throw new BusinessException(ResultCode.DATA_ERROR,"合同编号不允许重复使用，请修改");
                    }
                }
            }
        }
    }

    /**
     * 检查摊位状态
     * @param boothId
     */
    private void checkBoothState(Long boothId){
        BaseOutput<BoothDTO> output = assetsRpc.getBoothById(boothId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位接口调用异常 "+output.getMessage());
        }
        BoothDTO booth = output.getData();
        if(null == booth){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位不存在，请重新修改后保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(booth.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位"+booth.getName()+"已禁用，请重新修改后保存");
        }else if(YesOrNoEnum.YES.getCode().equals(booth.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位"+booth.getName()+"已删除，请重新修改后保存");
        }
    }

    /**
     * 检查客户状态
     * @param customerId
     * @param marketId
     */
    @Override
    public void checkCustomerState(Long customerId,Long marketId){
        BaseOutput<Customer> output = customerRpc.get(customerId,marketId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户接口调用异常 "+output.getMessage());
        }
        Customer customer = output.getData();
        if(null == customer){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户不存在，请重新修改后保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户【" + customer.getName() + "】已禁用，请重新修改后保存");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户【" + customer.getName() + "】已删除，请重新修改后保存");
        }
    }

    @Override
    public BaseOutput submitPayment(Long id, Long amount, Long waitAmount) {
        return null;
    }

    @Override
    public BaseOutput cancelOrder(Long id) {
        return null;
    }

    @Override
    public BaseOutput withdrawOrder(Long id) {
        return null;
    }

    @Override
    public BaseOutput<Boolean> updateLeaseOrderBySettleInfo(SettleOrder settleOrder) {
        return null;
    }

    @Override
    public void leaseOrderEffectiveHandler(AssetLeaseOrder o) {

    }

    @Override
    public void leaseOrderExpiredHandler(AssetLeaseOrder o) {

    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint) {
        return null;
    }

    @Override
    public BaseOutput createRefundOrder(RefundOrderDto refundOrderDto) {
        return null;
    }

    @Override
    public BaseOutput cancelRefundOrderHandler(Long leaseOrderId, Long leaseOrderItemId) {
        return null;
    }

    @Override
    public BaseOutput settleSuccessRefundOrderHandler(RefundOrder refundOrder) {
        return null;
    }

    @Override
    public BaseOutput supplement(AssetLeaseOrder leaseOrder) {
        return null;
    }
}