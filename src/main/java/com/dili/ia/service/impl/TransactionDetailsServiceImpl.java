package com.dili.ia.service.impl;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.mapper.TransactionDetailsMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.TransactionDetailsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class TransactionDetailsServiceImpl extends BaseServiceImpl<TransactionDetails, Long> implements TransactionDetailsService {

    public TransactionDetailsMapper getActualDao() {
        return (TransactionDetailsMapper)getDao();
    }

    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    UidFeignRpc uidFeignRpc;
    @Autowired
    FirmRpc firmRpc;

    @Override
    public TransactionDetails buildByConditions(Integer sceneType, Integer bizType, Integer itemType, Long amount, Long orderId, String orderCode, Long customerId, String notes, Long marketId, Long operaterId, String operatorName) {
        TransactionDetails tds = DTOUtils.newDTO(TransactionDetails.class);
        BaseOutput<Customer> out= customerRpc.get(customerId, marketId);
        if(!out.isSuccess()){
            LOGGER.info("客户微服务异常！【customerId={}; marketId={}】{}", customerId, marketId, out.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, out.getMessage());
        }
        Customer customer = out.getData();
        if (null == customer){
            LOGGER.info("客户不存在！【customerId={}; marketId={}】", customerId, marketId);
            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在！");
        }
        tds.setCustomerId(customer.getId());
        tds.setCustomerName(customer.getName());
        tds.setCertificateNumber(customer.getCertificateNumber());
        tds.setCustomerCellphone(customer.getContactsPhone());
        //参数传入
        tds.setAmount(amount);
        tds.setNotes(notes);
        tds.setOrderCode(orderCode);
        tds.setOrderId(orderId);
        tds.setBizType(bizType);
        tds.setSceneType(sceneType);
        tds.setItemType(itemType);
        tds.setMarketId(marketId);
        //固定构建参数
        tds.setCreator(operatorName);
        tds.setCreatorId(operaterId);
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.TRANSACTION_CODE.getCode());
        if(!bizNumberOutput.isSuccess()){
            LOGGER.info("编号生成器微服务异常！{}",bizNumberOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
        }
        BaseOutput<Firm> marketOut = firmRpc.getById(marketId);
        if (!marketOut.isSuccess() || marketOut.getData() == null){
            LOGGER.info("获取市场code失败！市场ID{}",marketId);
            throw new BusinessException(ResultCode.DATA_ERROR ,"获取市场code失败！");
        }
        tds.setCode(marketOut.getData().getCode().toUpperCase() + bizNumberOutput.getData());
        return tds;
    }
}