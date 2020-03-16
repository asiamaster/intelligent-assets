package com.dili.ia.service.impl;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.mapper.TransactionDetailsMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.TransactionDetailsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    public TransactionDetails buildByConditions(Integer sceneType, Integer bizType, Integer itemType, Long amount, Long orderId, String orderCode, Long customerId, String notes, Long marketId) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw  new RuntimeException("未登陆");
        }
        TransactionDetails tds = DTOUtils.newDTO(TransactionDetails.class);
        BaseOutput<Customer> out= customerRpc.get(customerId, marketId);
        if(!out.isSuccess()){
            throw new RuntimeException("客户微服务异常！");
        }
        Customer customer = out.getData();
        if (null == customer){
            LOGGER.info("客户不存在！【customerId={}; marketId={}】", customerId, marketId);
            throw new RuntimeException("客户不存在！");
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
            throw new RuntimeException("编号生成器微服务异常");
        }
        tds.setCode(bizNumberOutput.getData());
        tds.setCreateTime(new Date());
        return tds;
    }
}