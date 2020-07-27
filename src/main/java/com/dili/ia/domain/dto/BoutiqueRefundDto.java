package com.dili.ia.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * @author:       xiaosa
 * @date:         2020/7/23
 * @version:      农批业务系统重构
 * @description:  精品停车退款
 */
public class BoutiqueRefundDto {
    /**
     * 入库单
     */
    @NotNull(message = "code is null")
    private String code;

    /**
     * 退款原因
     */
    private String notes;

    /**
     * 退款金额
     */
    private Long amount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
