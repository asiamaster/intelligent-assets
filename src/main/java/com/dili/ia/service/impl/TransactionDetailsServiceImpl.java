package com.dili.ia.service.impl;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.TransactionItemTypeEnum;
import com.dili.ia.mapper.TransactionDetailsMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.TransactionDetailsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private void addTransactionDetails(Integer sceneType, Integer bizType, Integer itemType, Long orderId, String orderCode, Long customerId, Long amount, Long marketId){
        //写入 定金，转抵，保证金对应 sceneType 的流水 --- 抵扣项为 null 或者 0 元 不写入流水记录
        if (null != amount && amount > 0){ //定金流水
            TransactionDetails earnestUnfrozenDetail = this.buildByConditions(sceneType, bizType, itemType, amount, orderId, orderCode, customerId, orderCode, marketId);
            int count = this.insertSelective(earnestUnfrozenDetail);
            if (count != 1){
                throw new BusinessException(ResultCode.DATA_ERROR, "写入流水记录失败");
            }
        }
    }

    @Override
    public TransactionDetails buildByConditions(Integer sceneType, Integer bizType, Integer itemType, Long amount, Long orderId, String orderCode, Long customerId, String notes, Long marketId) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.NOT_AUTH_ERROR, "未登陆");
        }
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
        tds.setCreator(userTicket.getRealName());
        tds.setCreatorId(userTicket.getId());
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.TRANSACTION_CODE.getCode());
        if(!bizNumberOutput.isSuccess()){
            LOGGER.info("编号生成器微服务异常！{}",bizNumberOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
        }
        tds.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());
        tds.setCreateTime(new Date());
        return tds;
    }
}