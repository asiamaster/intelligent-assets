package com.dili.ia.service;

import com.dili.ia.domain.account.AccountInfo;
import com.dili.ss.domain.BaseOutput;

public interface AccountService {
    /**
     * 客户信息查询
     * @param cardNo
     * 卡面号
     */
    public BaseOutput<AccountInfo> checkCardNo(String cardNo);
}
