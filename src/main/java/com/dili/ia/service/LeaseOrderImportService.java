package com.dili.ia.service;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.Customer;
import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.glossary.*;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * leaseOrder excel import
 */
@Service
public class LeaseOrderImportService{
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderImportService.class);
    @Autowired
    private AssetsRpc assetsRpc;
    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private LeaseOrderService leaseOrderService;
    @Autowired
    private LeaseOrderItemService leaseOrderItemService;
    @Autowired
    private UidFeignRpc uidFeignRpc;

    /**
     * 导入租赁单
     * @param map
     * @return
     */
    @Transactional
    public BaseOutput importLeaseOrder(Map<String, Object> map) {
        String customerName = String.valueOf(map.get("客户名称"));
        if(StringUtils.isBlank(customerName)){
            throw new BusinessException(ResultCode.PARAMS_ERROR,"客户名称为空");
        }
        String certificateNumber = String.valueOf(map.get("证件号"));
        if(StringUtils.isBlank(certificateNumber)){
            throw new BusinessException(ResultCode.PARAMS_ERROR,"证件号为空");
        }
        String boothName = String.valueOf(map.get("摊位编号"));
        if(StringUtils.isBlank(boothName)){
            throw new BusinessException(ResultCode.PARAMS_ERROR,"摊位编号为空");
        }
        Long depositAmount = MoneyUtils.yuanToCent(String.valueOf(map.get("保证金")));
        if(null == depositAmount){
            throw new BusinessException(ResultCode.PARAMS_ERROR,"保证金为空");
        }

        BaseOutput<Customer> customerOutput = customerRpc.getByCertificateNumber(certificateNumber,11L);
        if (!customerOutput.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户接口调用异常" + customerOutput.getMessage());
        }
        Customer customer = customerOutput.getData();
        if (null == customer) {
            throw new BusinessException(ResultCode.DATA_ERROR, "客户信息未查到");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",boothName);
        BaseOutput<List<BoothDTO>> boothOutput = assetsRpc.searchBooth(jsonObject);
        if (!boothOutput.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位接口调用异常" + boothOutput.getMessage());
        }
        BoothDTO boothDTO = boothOutput.getData().stream().findFirst().orElse(null);
        if (null == boothDTO) {
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位信息未查到");
        }
        LeaseOrder leaseOrder = buildLeaseOrder(customer, depositAmount);
        leaseOrderService.insertSelective(leaseOrder);

        LeaseOrderItem leaseOrderItem = buildLeaseOrderItem(customer, boothDTO, leaseOrder);
        leaseOrderItemService.insertSelective(leaseOrderItem);
        return BaseOutput.success();
    }

    /**
     * 构建租赁订单项
     * @param customer
     * @param boothDTO
     * @param leaseOrder
     * @return
     */
    private LeaseOrderItem buildLeaseOrderItem(Customer customer, BoothDTO boothDTO, LeaseOrder leaseOrder) {
        LeaseOrderItem leaseOrderItem = DTOUtils.newInstance(LeaseOrderItem.class);
        leaseOrderItem.setLeaseOrderId(leaseOrder.getId());
        leaseOrderItem.setLeaseOrderCode(leaseOrder.getCode());
        leaseOrderItem.setCustomerId(customer.getId());
        leaseOrderItem.setCustomerName(customer.getName());
        leaseOrderItem.setBoothId(boothDTO.getId());
        leaseOrderItem.setBoothName(boothDTO.getName());
        leaseOrderItem.setDepositAmount(leaseOrder.getDepositAmount());
        leaseOrderItem.setRentAmount(0L);
        leaseOrderItem.setManageAmount(0L);
        leaseOrderItem.setDistrictId(boothDTO.getSecondArea() != null ? boothDTO.getSecondArea().longValue() : boothDTO.getArea().longValue());
        leaseOrderItem.setDistrictName(boothDTO.getSecondAreaName() != null ? boothDTO.getSecondAreaName() : boothDTO.getAreaName());
        leaseOrderItem.setIsCorner(boothDTO.getCornerName());
        leaseOrderItem.setNumber(boothDTO.getNumber());
        leaseOrderItem.setUnitCode(boothDTO.getUnit());
        leaseOrderItem.setUnitName(boothDTO.getUnitName());
        leaseOrderItem.setUnitPrice(0L);
        leaseOrderItem.setPaymentMonth(new BigDecimal(0));
        leaseOrderItem.setDiscountAmount(0L);
        leaseOrderItem.setState(LeaseOrderStateEnum.EXPIRED.getCode());
        leaseOrderItem.setDepositAmountFlag(DepositAmountFlagEnum.TRANSFERRED.getCode());
        leaseOrderItem.setPayState(PayStateEnum.PAID.getCode());
        leaseOrderItem.setStopRentState(StopRentStateEnum.NO_APPLY.getCode());
        return leaseOrderItem;
    }

    /**
     * 构建租赁单
     * @param customer
     * @param depositAmount
     * @return
     */
    private LeaseOrder buildLeaseOrder(Customer customer,Long depositAmount) {
        LeaseOrder leaseOrder = DTOUtils.newInstance(LeaseOrder.class);
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.LEASE_ORDER.getCode());
        if (!bizNumberOutput.isSuccess()) {
            LOG.info("租赁单编号生成异常");
            throw new BusinessException(ResultCode.DATA_ERROR,"编号生成器微服务异常");
        }
        leaseOrder.setCode("hzsc" + bizNumberOutput.getData());
        leaseOrder.setRefundState(RefundStateEnum.WAIT_APPLY.getCode());
        leaseOrder.setState(LeaseOrderStateEnum.EXPIRED.getCode());
        leaseOrder.setPayState(PayStateEnum.PAID.getCode());
        leaseOrder.setPaidAmount(depositAmount);
        leaseOrder.setPayAmount(depositAmount);
        leaseOrder.setTotalAmount(depositAmount);
        leaseOrder.setDepositAmount(depositAmount);
        leaseOrder.setCategoryId(0L);
        leaseOrder.setCategoryName("水产品");
        leaseOrder.setMarketId(11L);
        leaseOrder.setMarketCode("hzsc");
        leaseOrder.setDepartmentId(74L);
        leaseOrder.setDepartmentName("市场经营管理部");
        leaseOrder.setCreator("hzsc_admin");
        leaseOrder.setCreatorId(195L);
        leaseOrder.setCustomerId(customer.getId());
        leaseOrder.setCustomerName(customer.getName());
        leaseOrder.setCertificateNumber(customer.getCertificateNumber());
        leaseOrder.setCustomerCellphone(customer.getContactsPhone());
        leaseOrder.setStartTime(new Date("1990/01/01 00:00:00"));
        leaseOrder.setEndTime(new Date("1990/01/02 23:59:59"));
        leaseOrder.setDays(2);
        leaseOrder.setEngageCode("one_year");
        leaseOrder.setEngageName("一年");
        leaseOrder.setLeaseTermCode("stall");
        leaseOrder.setLeaseTermName("摊位");
        leaseOrder.setIsDelete(YesOrNoEnum.NO.getCode());
        leaseOrder.setIsRenew(YesOrNoEnum.NO.getCode());
        return leaseOrder;
    }
}
