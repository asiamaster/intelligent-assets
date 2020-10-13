package com.dili.ia.service;

import com.dili.ia.domain.account.AccountInfo;
import com.dili.ss.domain.BaseOutput;

import java.util.List;
/**
 * 卡务调用
 * This file was generated on 2020-07-24 11:15:42.
 */
public interface AccountService {
    /**
     * 客户信息查询
     * @param cardNo
     * 卡面号
     */
    public BaseOutput<AccountInfo> checkCardNo(String cardNo);

    /**
     * 查询客户在该市场下的园区卡列表
     * @param customerId
     * 卡面号
     */
    public BaseOutput<List<AccountInfo>> getAccountListByCustomerId(Long customerId);
}
