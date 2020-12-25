package com.dili.ia.domain.dto;

import com.dili.ia.domain.Meter;
import javax.persistence.Transient;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的Dto
 */
public class MeterDto extends Meter {

    /**
     * 关键字(搜索的条件，表编号或表地址)
     */
    @Transient
    private String keyword;

    /**
     * 身份证号码，用于提交水电费
     */
    private String certificateNumber;

    /**
     * 表用户状态
     */
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}