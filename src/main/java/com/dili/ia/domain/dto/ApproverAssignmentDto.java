package com.dili.ia.domain.dto;

import com.dili.ia.domain.ApproverAssignment;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 任务人分配
 * This file was generated on 2020-07-13 14:42:58.
 */
public class ApproverAssignmentDto extends ApproverAssignment {

    @Column(name = "`id`")
    @Operator(Operator.IN)
    private List<Long> ids;

    @Operator(Operator.IN)
    @Column(name = "`district_id`")
    private List<Long> districtIds;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(List<Long> districtIds) {
        this.districtIds = districtIds;
    }
}