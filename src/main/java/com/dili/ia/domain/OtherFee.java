package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-07-21 18:08:01.
 */
@Table(name = "`other_fee`")
public class OtherFee extends BaseDomain {
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
     * 业务编号
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 客户名称
     */
    @Column(name = "`customer_name`")
    private String customerName;

    /**
     * 客户证件号
     */
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    /**
     * 客户电话
     */
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    /**
     * 业务所属部门ID
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 业务所属部门名称
     */
    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 收费项ID
     */
    @Column(name = "`charge_item_id`")
    private String chargeItemId;

    /**
     * 收费项名称
     */
    @Column(name = "`charge_item_name`")
    private String chargeItemName;

    /**
     * 资产类型，费用类型
     */
    @Column(name = "`assets_type`")
    private Integer assetsType;

    /**
     * 资产ID
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    /**
     * 资产名称
     */
    @Column(name = "`assets_name`")
    private String assetsName;

    /**
     * 开始时间
     */
    @Column(name = "`start_time`")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "`end_time`")
    private LocalDateTime endTime;

    /**
     * 品类id
     */
    @Column(name = "`category_id`")
    private Long categoryId;

    /**
     * 品类名称
     */
    @Column(name = "`category_name`")
    private String categoryName;

    /**
     * 金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 退款金额
     */
    @Column(name = "`refund_amount`")
    private Long refundAmount;

    /**
     * 备注信息
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * （1：已创建 2：已取消 3：已提交 4：已交费 5：退款中 6：已退款）
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 创建操作员ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 创建人名称
     */
    @Column(name = "`creator`")
    private String creator;

    /**
     * 撤回人ID
     */
    @Column(name = "`withdraw_operator_id`")
    private Long withdrawOperatorId;

    /**
     * 撤回人名称
     */
    @Column(name = "`withdraw_operator`")
    private String withdrawOperator;

    /**
     * 取消人ID
     */
    @Column(name = "`canceler_id`")
    private Long cancelerId;

    /**
     * 取消人名称
     */
    @Column(name = "`canceler`")
    private String canceler;

    /**
     * 市场Id
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 市场CODE
     */
    @Column(name = "`market_code`")
    private String marketCode;


    /**
     * 商户Id
     */
    @Column(name = "`mch_id`")
    private Long mchId;

    /**
     * 商户名称
     */
    @Column(name = "`mch_name`")
    private String mchName;

    /**
     * 一级区域Id
     */
    @Column(name = "`first_district_id`")
    private Long firstDistrictId;

    /**
     * 一级区域名称
     */
    @Column(name = "`first_district_name`")
    private String firstDistrictName;

    /**
     * 二级区域Id
     */
    @Column(name = "`second_district_id`")
    private Long secondDistrictId;

    /**
     * 二级区域名称
     */
    @Column(name = "`second_district_name`")
    private String secondDistrictName;

    /**
     * 版本控制,乐观锁
     */
    @Version
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
     * 获取业务编号
     *
     * @return code - 业务编号
     */
    @FieldDef(label="业务编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置业务编号
     *
     * @param code 业务编号
     */
    public void setCode(String code) {
        this.code = code;
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
     * 获取客户名称
     *
     * @return customer_name - 客户名称
     */
    @FieldDef(label="客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户名称
     *
     * @param customerName 客户名称
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
     * 获取客户电话
     *
     * @return customer_cellphone - 客户电话
     */
    @FieldDef(label="客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerCellphone() {
        return customerCellphone;
    }

    /**
     * 设置客户电话
     *
     * @param customerCellphone 客户电话
     */
    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    /**
     * 获取业务所属部门ID
     *
     * @return department_id - 业务所属部门ID
     */
    @FieldDef(label="业务所属部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置业务所属部门ID
     *
     * @param departmentId 业务所属部门ID
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取业务所属部门名称
     *
     * @return department_name - 业务所属部门名称
     */
    @FieldDef(label="业务所属部门名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 设置业务所属部门名称
     *
     * @param departmentName 业务所属部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取收费项ID
     *
     * @return charge_item_id - 收费项ID
     */
    @FieldDef(label="收费项ID")
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getChargeItemId() {
        return chargeItemId;
    }

    /**
     * 设置收费项ID
     *
     * @param chargeItemId 收费项ID
     */
    public void setChargeItemId(String chargeItemId) {
        this.chargeItemId = chargeItemId;
    }

    /**
     * 获取收费项名称
     *
     * @return charge_item_name - 收费项名称
     */
    @FieldDef(label="收费项名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getChargeItemName() {
        return chargeItemName;
    }

    /**
     * 设置收费项名称
     *
     * @param chargeItemName 收费项名称
     */
    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }

    /**
     * 获取资产类型，费用类型
     *
     * @return assets_type - 资产类型，费用类型
     */
    @FieldDef(label="资产类型，费用类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getAssetsType() {
        return assetsType;
    }

    /**
     * 设置资产类型，费用类型
     *
     * @param assetsType 资产类型，费用类型
     */
    public void setAssetsType(Integer assetsType) {
        this.assetsType = assetsType;
    }

    /**
     * 获取资产ID
     *
     * @return assets_id - 资产ID
     */
    @FieldDef(label="资产ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    /**
     * 设置资产ID
     *
     * @param assetsId 资产ID
     */
    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    /**
     * 获取资产名称
     *
     * @return assets_name - 资产名称
     */
    @FieldDef(label="资产名称", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsName() {
        return assetsName;
    }

    /**
     * 设置资产名称
     *
     * @param assetsName 资产名称
     */
    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
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
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    @FieldDef(label="结束时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取品类id
     *
     * @return category_id - 品类id
     */
    @FieldDef(label="品类id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置品类id
     *
     * @param categoryId 品类id
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取品类名称
     *
     * @return category_name - 品类名称
     */
    @FieldDef(label="品类名称", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置品类名称
     *
     * @param categoryName 品类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取金额
     *
     * @return amount - 金额
     */
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 获取退款金额
     *
     * @return refund_amount - 退款金额
     */
    @FieldDef(label="退款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getRefundAmount() {
        return refundAmount;
    }

    /**
     * 设置退款金额
     *
     * @param refundAmount 退款金额
     */
    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * 获取备注信息
     *
     * @return notes - 备注信息
     */
    @FieldDef(label="备注信息", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注信息
     *
     * @param notes 备注信息
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 获取（1：已创建 2：已取消 3：已提交 4：已交费 5：退款中 6：已退款）
     *
     * @return state - （1：已创建 2：已取消 3：已提交 4：已交费 5：退款中 6：已退款）
     */
    @FieldDef(label="（1：已创建 2：已取消 3：已提交 4：已交费 5：退款中 6：已退款）")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置（1：已创建 2：已取消 3：已提交 4：已交费 5：退款中 6：已退款）
     *
     * @param state （1：已创建 2：已取消 3：已提交 4：已交费 5：退款中 6：已退款）
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建操作员ID
     *
     * @return creator_id - 创建操作员ID
     */
    @FieldDef(label="创建操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建操作员ID
     *
     * @param creatorId 创建操作员ID
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取创建人名称
     *
     * @return creator - 创建人名称
     */
    @FieldDef(label="创建人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人名称
     *
     * @param creator 创建人名称
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取撤回人ID
     *
     * @return withdraw_operator_id - 撤回人ID
     */
    @FieldDef(label="撤回人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWithdrawOperatorId() {
        return withdrawOperatorId;
    }

    /**
     * 设置撤回人ID
     *
     * @param withdrawOperatorId 撤回人ID
     */
    public void setWithdrawOperatorId(Long withdrawOperatorId) {
        this.withdrawOperatorId = withdrawOperatorId;
    }

    /**
     * 获取撤回人名称
     *
     * @return withdraw_operator - 撤回人名称
     */
    @FieldDef(label="撤回人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getWithdrawOperator() {
        return withdrawOperator;
    }

    /**
     * 设置撤回人名称
     *
     * @param withdrawOperator 撤回人名称
     */
    public void setWithdrawOperator(String withdrawOperator) {
        this.withdrawOperator = withdrawOperator;
    }

    /**
     * 获取取消人ID
     *
     * @return canceler_id - 取消人ID
     */
    @FieldDef(label="取消人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCancelerId() {
        return cancelerId;
    }

    /**
     * 设置取消人ID
     *
     * @param cancelerId 取消人ID
     */
    public void setCancelerId(Long cancelerId) {
        this.cancelerId = cancelerId;
    }

    /**
     * 获取取消人名称
     *
     * @return canceler - 取消人名称
     */
    @FieldDef(label="取消人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCanceler() {
        return canceler;
    }

    /**
     * 设置取消人名称
     *
     * @param canceler 取消人名称
     */
    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }

    /**
     * 获取市场Id
     *
     * @return market_id - 市场Id
     */
    @FieldDef(label="市场Id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场Id
     *
     * @param marketId 市场Id
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * 获取市场CODE
     *
     * @return market_code - 市场CODE
     */
    @FieldDef(label="市场CODE", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * 设置市场CODE
     *
     * @param marketCode 市场CODE
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public Long getFirstDistrictId() {
        return firstDistrictId;
    }

    public void setFirstDistrictId(Long firstDistrictId) {
        this.firstDistrictId = firstDistrictId;
    }

    public String getFirstDistrictName() {
        return firstDistrictName;
    }

    public void setFirstDistrictName(String firstDistrictName) {
        this.firstDistrictName = firstDistrictName;
    }

    public Long getSecondDistrictId() {
        return secondDistrictId;
    }

    public void setSecondDistrictId(Long secondDistrictId) {
        this.secondDistrictId = secondDistrictId;
    }

    public String getSecondDistrictName() {
        return secondDistrictName;
    }

    public void setSecondDistrictName(String secondDistrictName) {
        this.secondDistrictName = secondDistrictName;
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