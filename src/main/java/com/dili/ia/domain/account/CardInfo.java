package com.dili.ia.domain.account;

/**
 * Created by laikui on 2018/10/26.
 */
public class CardInfo {
    private  AccountInfo accountInfo;
    private  FundInfo accountFund;

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public FundInfo getAccountFund() {
        return accountFund;
    }

    public void setAccountFund(FundInfo accountFund) {
        this.accountFund = accountFund;
    }
}
