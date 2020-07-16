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
 * This file was generated on 2020-07-13 14:42:58.
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
     * 审批用户id
     */
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 业务类型, 1:租赁审批  2:租赁审批
     */
    @Column(name = "`business_type`")
    private Integer businessType;

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
     * 任务定义key
     */
    @Column(name = "`task_definition_key`")
    private String taskDefinitionKey;

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
     * 获取审批用户id
     *
     * @return user_id - 审批用户id
     */
    @FieldDef(label="审批用户id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置审批用户id
     *
     * @param userId 审批用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取业务类型, 1:租赁审批  2:租赁审批
     *
     * @return business_type - 业务类型, 1:租赁审批  2:租赁审批
     */
    @FieldDef(label="业务类型, 1:租赁审批  2:租赁审批")
    @EditMode(editor = FieldEditor.Text, required = true)
    public Integer getBusinessType() {
        return businessType;
    }

    /**
     * 设置业务类型, 1:租赁审批  2:租赁审批
     *
     * @param businessType 业务类型, 1:租赁审批  2:租赁审批
     */
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
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

    /**
     * 任务定义key
     * @return
     */
    @FieldDef(label="任务定义key")
    @EditMode(editor = FieldEditor.Text, required = true)
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }
}