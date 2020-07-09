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
    private String likeName;

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }
}