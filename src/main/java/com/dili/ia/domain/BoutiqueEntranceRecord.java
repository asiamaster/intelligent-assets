package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  精品停车记录表实体类
 */
@Table(name = "`boutique_entrance_record`")
public class BoutiqueEntranceRecord extends BaseDomain {
    /**
     * id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 客户姓名
     */
    @Column(name = "`customer_name`")
    private String customerName;

    /**
     * 手机号
     */
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    /**
     * 客户证件号
     */
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    /**
     * 挂号
     */
    @Column(name = "`plate`")
    private String plate;

    /**
     * 车型id
     */
    @Column(name = "`car_type_id`")
    private Long carTypeId;

    /**
     * 接车部门
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 部门名称
     */
    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 进场时间
     */
    @Column(name = "`enter_time`")
    private LocalDateTime enterTime;

    /**
     * 确认时间
     */
    @Column(name = "`confirm_time`")
    private LocalDateTime confirmTime;

    /**
     * 开始时间
     */
    @Column(name = "`start_time`")
    private LocalDateTime startTime;

    /**
     * 计费时间
     */
    @Column(name = "`count_time`")
    private LocalDateTime countTime;

    /**
     * 离场时间
     */
    @Column(name = "`leave_time`")
    private LocalDateTime leaveTime;

    /**
     * 交费总额
     */
    @Column(name = "`total_amount`")
    private Long totalAmount;

    /**
     * 状态 1 待确认 2 计费中 3 已离场 4 已取消
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 操作员ID
     */
    @Column(name = "`operator_id`")
    private Long operatorId;

    /**
     * 操作员名称
     */
    @Column(name = "`operator_name`")
    private String operatorName;

    /**
     * 取消时间
     */
    @Column(name = "`cancel_time`")
    private LocalDateTime cancelTime;

    /**
     * 进门记录id
     */
    @Column(name = "`bid`")
    private Long bid;

    /**
     * 版本控制,乐观锁
     */
    @Column(name = "`version`")
    private Integer version;

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取客户ID
     *
     * @return customer_id - 客户ID
     */
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置客户ID
     *
     * @param customerId 客户ID
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取客户姓名
     *
     * @return customer_name - 客户姓名
     */
    @FieldDef(label="客户姓名", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户姓名
     *
     * @param customerName 客户姓名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取手机号
     *
     * @return customer_cellphone - 手机号
     */
    @FieldDef(label="手机号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerCellphone() {
        return customerCellphone;
    }

    /**
     * 设置手机号
     *
     * @param customerCellphone 手机号
     */
    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    /**
     * 获取客户证件号
     *
     * @return certificate_number - 客户证件号
     */
    @FieldDef(label="客户证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCertificateNumber() {
        return certificateNumber;
    }

    /**
     * 设置客户证件号
     *
     * @param certificateNumber 客户证件号
     */
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    /**
     * 获取挂号
     *
     * @return plate - 挂号
     */
    @FieldDef(label="挂号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPlate() {
        return plate;
    }

    /**
     * 设置挂号
     *
     * @param plate 挂号
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }

    /**
     * 获取车型id
     *
     * @return car_type_id - 车型id
     */
    @FieldDef(label="车型id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCarTypeId() {
        return carTypeId;
    }

    /**
     * 设置车型id
     *
     * @param carTypeId 车型id
     */
    public void setCarTypeId(Long carTypeId) {
        this.carTypeId = carTypeId;
    }

    /**
     * 获取业务部门
     *
     * @return department_id - 业务部门
     */
    @FieldDef(label="业务部门")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置业务部门
     *
     * @param departmentId 业务部门
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @FieldDef(label="部门名称", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取进场时间
     *
     * @return enter_time - 进场时间
     */
    @FieldDef(label="进场时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getEnterTime() {
        return enterTime;
    }

    /**
     * 设置进场时间
     *
     * @param enterTime 进场时间
     */
    public void setEnterTime(LocalDateTime enterTime) {
        this.enterTime = enterTime;
    }

    /**
     * 获取确认时间
     *
     * @return confirm_time - 确认时间
     */
    @FieldDef(label="确认时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getConfirmTime() {
        return confirmTime;
    }

    /**
     * 设置确认时间
     *
     * @param confirmTime 确认时间
     */
    public void setConfirmTime(LocalDateTime confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    @FieldDef(label="开始时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取计费时间
     *
     * @return count_time - 计费时间
     */
    @FieldDef(label="计费时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getCountTime() {
        return countTime;
    }

    /**
     * 设置计费时间
     *
     * @param countTime 计费时间
     */
    public void setCountTime(LocalDateTime countTime) {
        this.countTime = countTime;
    }

    /**
     * 获取离场时间
     *
     * @return leave_time - 离场时间
     */
    @FieldDef(label="离场时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    /**
     * 设置离场时间
     *
     * @param leaveTime 离场时间
     */
    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    /**
     * 获取交费总额
     *
     * @return total_amount - 交费总额
     */
    @FieldDef(label="交费总额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置交费总额
     *
     * @param totalAmount 交费总额
     */
    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 获取状态 1 待确认 2 计费中 3 已离场 4 已取消
     *
     * @return state - 状态 1 待确认 2 计费中 3 已离场 4 已取消
     */
    @FieldDef(label="状态 1 待确认 2 计费中 3 已离场 4 已取消")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态 1 待确认 2 计费中 3 已离场 4 已取消
     *
     * @param state 状态 1 待确认 2 计费中 3 已离场 4 已取消
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取操作员ID
     *
     * @return operator_id - 操作员ID
     */
    @FieldDef(label="操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * 设置操作员ID
     *
     * @param operatorId 操作员ID
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * 获取操作员名称
     *
     * @return operator_name - 操作员名称
     */
    @FieldDef(label="操作员名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * 设置操作员名称
     *
     * @param operatorName 操作员名称
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 获取取消时间
     *
     * @return cancel_time - 取消时间
     */
    @FieldDef(label="取消时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getCancelTime() {
        return cancelTime;
    }

    /**
     * 设置取消时间
     *
     * @param cancelTime 取消时间
     */
    public void setCancelTime(LocalDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     * 获取进门记录id
     *
     * @return bid - 进门记录id
     */
    @FieldDef(label="进门记录id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getBid() {
        return bid;
    }

    /**
     * 设置进门记录id
     *
     * @param bid 进门记录id
     */
    public void setBid(Long bid) {
        this.bid = bid;
    }

    /**
     * 获取版本控制,乐观锁
     *
     * @return version - 版本控制,乐观锁
     */
    @FieldDef(label="版本控制,乐观锁")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本控制,乐观锁
     *
     * @param version 版本控制,乐观锁
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}