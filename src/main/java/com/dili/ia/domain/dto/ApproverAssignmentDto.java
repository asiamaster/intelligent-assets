package com.dili.ia.domain.dto;

import com.dili.ia.domain.ApproverAssignment;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 任务人分配
 * This file was generated on 2020-07-13 14:42:58.
 */
public class ApproverAssignmentDto extends ApproverAssignment {

    private List<Long> ids;


    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}