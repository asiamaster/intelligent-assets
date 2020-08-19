package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 审批流程表，记录每个市场，每种业务下的审批记录
 * This file was generated on 2020-07-15 16:59:23.
 */
@Table(name = "`approval_process`")
public class ApprovalProcess extends BaseDomain {
    /**
     * id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流程实例id
     */
    @Column(name = "`process_instance_id`")
    private String processInstanceId;

    /**
     * 任务名(根据流程来定义)
     */
    @Column(name = "`task_name`")
    private String taskName;

    /**
     * 任务id(用于任务追踪)
     */
    @Column(name = "`task_id`")
    private String taskId;

    /**
     * 办理人id
     */
    @Column(name = "`assignee`")
    private Long assignee;

    /**
     * 办理人名称(冗余UAP用户名)
     */
    @Column(name = "`assignee_name`")
    private String assigneeName;

    /**
     * 创建时间，即办理时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 审批意见
     */
    @Column(name = "`opinion`")
    private String opinion;

    /**
     * 审批结果, 1:同意， 2:拒绝
     */
    @Column(name = "`result`")
    private Integer result;

    /**
     * 业务key
     */
    @Column(name = "`business_key`")
    private String businessKey;

    /**
     * 业务类型(1:租赁交费 2:租赁退款)
     */
    @Column(name = "`business_type`")
    private Byte businessType;

    /**
     * 流程名称
     */
    @Column(name = "`process_name`")
    private String processName;

    /**
     * 任务开始时间，用于计算任务耗时
     */
    @Column(name = "`task_time`")
    private Date taskTime;

    /**
     * 商户id
     */
    @Column(name = "`firm_id`")
    private Long firmId;

    /**
     * 获取id
     *
     * @return id - id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取流程实例id
     *
     * @return process_instance_id - 流程实例id
     */
    @FieldDef(label="流程实例id", maxLength = 64)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * 设置流程实例id
     *
     * @param processInstanceId 流程实例id
     */
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    /**
     * 获取任务名(根据流程来定义)
     *
     * @return task_name - 任务名(根据流程来定义)
     */
    @FieldDef(label="任务名(根据流程来定义)", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置任务名(根据流程来定义)
     *
     * @param taskName 任务名(根据流程来定义)
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 获取任务id(用于任务追踪)
     *
     * @return task_id - 任务id(用于任务追踪)
     */
    @FieldDef(label="任务id(用于任务追踪)", maxLength = 64)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTaskId() {
        return taskId;
    }

    /**
     * 设置任务id(用于任务追踪)
     *
     * @param taskId 任务id(用于任务追踪)
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取办理人id
     *
     * @return assignee - 办理人id
     */
    @FieldDef(label="办理人id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssignee() {
        return assignee;
    }

    /**
     * 设置办理人id
     *
     * @param assignee 办理人id
     */
    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    /**
     * 获取办理人名称(冗余UAP用户名)
     *
     * @return assignee_name - 办理人名称(冗余UAP用户名)
     */
    @FieldDef(label="办理人名称(冗余UAP用户名)", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssigneeName() {
        return assigneeName;
    }

    /**
     * 设置办理人名称(冗余UAP用户名)
     *
     * @param assigneeName 办理人名称(冗余UAP用户名)
     */
    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    /**
     * 获取创建时间，即办理时间
     *
     * @return create_time - 创建时间，即办理时间
     */
    @FieldDef(label="创建时间，即办理时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间，即办理时间
     *
     * @param createTime 创建时间，即办理时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取审批意见
     *
     * @return opinion - 审批意见
     */
    @FieldDef(label="审批意见", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOpinion() {
        return opinion;
    }

    /**
     * 设置审批意见
     *
     * @param opinion 审批意见
     */
    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    /**
     * 获取审批结果, 1:同意， 2:拒绝
     *
     * @return result - 审批结果, 1:同意， 2:拒绝
     */
    @FieldDef(label="审批结果, 1:同意， 2:拒绝")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getResult() {
        return result;
    }

    /**
     * 设置审批结果, 1:同意， 2:拒绝
     *
     * @param result 审批结果, 1:同意， 2:拒绝
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * 获取业务key
     *
     * @return business_key - 业务key
     */
    @FieldDef(label="业务key", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBusinessKey() {
        return businessKey;
    }

    /**
     * 设置业务key
     *
     * @param businessKey 业务key
     */
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    /**
     * 获取业务类型(1:租赁交费 2:租赁退款)
     *
     * @return business_type - 业务类型(1:租赁交费 2:租赁退款)
     */
    @FieldDef(label="业务类型(1:租赁交费 2:租赁退款)")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Byte getBusinessType() {
        return businessType;
    }

    /**
     * 设置业务类型(1:租赁交费 2:租赁退款)
     *
     * @param businessType 业务类型(1:租赁交费 2:租赁退款)
     */
    public void setBusinessType(Byte businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取流程名称
     *
     * @return process_name - 流程名称
     */
    @FieldDef(label="流程名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getProcessName() {
        return processName;
    }

    /**
     * 设置流程名称
     *
     * @param processName 流程名称
     */
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    /**
     * 获取任务开始时间，用于计算任务耗时
     *
     * @return task_time - 任务开始时间，用于计算任务耗时
     */
    @FieldDef(label="任务开始时间，用于计算任务耗时")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getTaskTime() {
        return taskTime;
    }

    /**
     * 设置任务开始时间，用于计算任务耗时
     *
     * @param taskTime 任务开始时间，用于计算任务耗时
     */
    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
    }

    /**
     * 获取商户id
     *
     * @return firm_id - 商户id
     */
    @FieldDef(label="商户id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getFirmId() {
        return firmId;
    }

    /**
     * 设置商户id
     *
     * @param firmId 商户id
     */
    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }
}