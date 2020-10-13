package com.dili.ia.service.impl;

import com.dili.ia.domain.account.AccountInfo;
import com.dili.ia.domain.account.CardInfo;
import com.dili.ia.domain.account.CardQuery;
import com.dili.ia.rpc.AccountRpc;
import com.dili.ia.service.AccountService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AccountServiceImpl implements AccountService {
    private final static Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Autowired
    AccountRpc accountRpc;
    @Autowired
    DataDictionaryRpc dataDictionaryRpc;

    @Override
    public BaseOutput<AccountInfo> checkCardNo(String cardNo) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        if (StringUtils.isBlank(cardNo)) {
            return BaseOutput.failure("查询卡号没传入");
        }
        try {
            BaseOutput<CardInfo> output = accountRpc.cardInfo(cardNo, userTicket.getFirmId());
            if (output.isSuccess()) {
                AccountInfo accountInfo = output.getData().getAccountInfo();
                accountInfo.setBalance(output.getData().getAccountFund().getBalance());

                DataDictionaryValue dataDictionaryValueQuery = DTOUtils.newInstance(DataDictionaryValue.class);
                dataDictionaryValueQuery.setDdCode("cus_customer_type");
                dataDictionaryValueQuery.setFirmId(userTicket.getFirmId());
                BaseOutput<List<DataDictionaryValue>> dataDictionaryValueResult = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValueQuery);
                if (!dataDictionaryValueResult.isSuccess()) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "数据字典调用错误:" + dataDictionaryValueResult.getMessage());
                }

                List<DataDictionaryValue> customerTypes = dataDictionaryValueResult.getData();
                Map<String, String> typeMap = customerTypes.stream().collect(Collectors.toMap(DataDictionaryValue::getCode, DataDictionaryValue::getName));
                accountInfo.setCustomerTypeView(typeMap.get(accountInfo.getCustomerType()));
                return BaseOutput.success().setData(accountInfo);
            } else {
                LOG.error("----cardNo:" + cardNo + "查询失败：" + output.getMessage());
                return BaseOutput.failure(output.getMessage());
            }

        } catch (Exception e) {
            LOG.error(cardNo + ",获取卡务信息失败" + cardNo + e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @Override
    public BaseOutput<List<AccountInfo>> getAccountListByCustomerId(Long customerId) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        LOG.info("---query customerId:" + customerId);
        if (null == customerId) {
            return BaseOutput.failure("客户Id未传入");
        }
        try {
            ArrayList<Long> customerIds = new ArrayList<>();
            customerIds.add(customerId);
            CardQuery cardQuery = new CardQuery();
            cardQuery.setFirmId(userTicket.getFirmId());
            cardQuery.setCustomerIds(customerIds);
            BaseOutput<List<AccountInfo>> output = accountRpc.getList(cardQuery);
            if (!output.isSuccess()) {
                LOG.error("----customerId:{}, 市场：{}，查询失败：{}", customerId, userTicket.getFirmName(), output.getMessage());
            }
            return output;
        } catch (Exception e) {
            LOG.error("customerId={},获取卡务信息失败,{}", customerId, e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }
    }
}
