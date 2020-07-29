package com.dili.ia.domain.dto;

import com.dili.ia.domain.Passport;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @author:       xiaosa
 * @date:         2020/7/27
 * @version:      农批业务系统重构
 * @description:  通行证
 */
public class PassportDto extends Passport {

    /**
     * 创建时间查询的开始时间
     */
    private LocalDateTime startTimeQuery;

    /**
     * 创建时间查询的结束时间
     */
    private LocalDateTime endTimeQuery;

    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public LocalDateTime getStartTimeQuery() {
        return startTimeQuery;
    }

    public void setStartTimeQuery(LocalDateTime startTimeQuery) {
        this.startTimeQuery = startTimeQuery;
    }

    public LocalDateTime getEndTimeQuery() {
        return endTimeQuery;
    }

    public void setEndTimeQuery(LocalDateTime endTimeQuery) {
        this.endTimeQuery = endTimeQuery;
    }
}
