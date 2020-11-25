package com.dili.ia.domain.account;

/**
 * Created by laikui on 2018/10/26.
 */
public class FundInfo {
    private Long balance;
    private Long frozenAmount;

    public Long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
