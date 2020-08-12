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
public interface ApproverAssignmentDto extends ApproverAssignment {

    @Column(name = "`id`")
    @Operator(Operator.IN)
    public List<Long> getIds();

    public void setIds(List<Long> ids);

    @Operator(Operator.IN)
    @Column(name = "`district_id`")
    public List<Long> getDistrictIds();

    public void setDistrictIds(List<Long> districtIds);
}