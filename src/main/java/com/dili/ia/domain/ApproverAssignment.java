package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 任务人分配
 * This file was generated on 2020-07-22 11:16:04.
 */
@Table(name = "`approver_assignment`")
public class ApproverAssignment extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 区域id，可能是一级或二级
     */
    @Column(name = "`district_id`")
    private Long districtId;

    /**
     * 办理用户id
     */
    @Column(name = "`assignee`")
    private Long assignee;

    /**
     * 任务定义key
     */
    @Column(name = "`task_definition_key`")
    private String taskDefinitionKey;

    /**
     * 流程定义key
     */
    @Column(name = "`process_definition_key`")
    private String processDefinitionKey;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * @return id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取区域id，可能是一级或二级
     *
     * @return district_id - 区域id，可能是一级或二级
     */
    @FieldDef(label="区域id，可能是一级或二级")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getDistrictId() {
        return districtId;
    }

    /**
     * 设置区域id，可能是一级或二级
     *
     * @param districtId 区域id，可能是一级或二级
     */
    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    /**
     * 获取办理用户id
     *
     * @return assignee - 办理用户id
     */
    @FieldDef(label="办理用户id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getAssignee() {
        return assignee;
    }

    /**
     * 设置办理用户id
     *
     * @param assignee 办理用户id
     */
    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    /**
     * 获取任务定义key
     *
     * @return task_definition_key - 任务定义key
     */
    @FieldDef(label="任务定义key", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    /**
     * 设置任务定义key
     *
     * @param taskDefinitionKey 任务定义key
     */
    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    /**
     * 获取流程定义key
     *
     * @return process_definition_key - 流程定义key
     */
    @FieldDef(label="流程定义key", maxLength = 64)
    @EditMode(editor = FieldEditor.Text, required = true)
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    /**
     * 设置流程定义key
     *
     * @param processDefinitionKey 流程定义key
     */
    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}