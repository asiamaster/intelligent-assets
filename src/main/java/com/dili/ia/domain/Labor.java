package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.domain.UserTicket;

import java.time.LocalDateTime;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 劳务管理
 * This file was generated on 2020-07-27 14:50:45.
 */
@Table(name = "`labor`")
public class Labor extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`mch_id`")
   	private Long mchId;
    
    @Column(name = "`code`")
    private String code;

    /**
     * 运营车型
     */
    @Column(name = "`models`")
    private String models;

    /**
     * 发票编号
     */
    @Column(name = "`invoice_number`")
    private String invoiceNumber;

    /**
     * 马甲号
     */
    @Column(name = "`work_card`")
    private String workCard;

    /**
     * 车牌号
     */
    @Column(name = "`license_plate`")
    private String licensePlate;

    /**
     * 办理周期
     */
    @Column(name = "`interval`")
    private Long interval;

    /**
     * 开始日期
     */
    @Column(name = "`start_date`")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @Column(name = "`end_date`")
    private LocalDateTime endDate;

    /**
     * 状态
     */
    @Column(name = "`state`")
    private Integer state;
    
    /**
     * 上一步状态
     */
    @Column(name = "`pre_state`")
    private Integer preState;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 创建日期
     */
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    /**
     * 操作员
     */
    @Column(name = "`operator`")
    private Long operator;

    /**
     * 操作员姓名
     */
    @Column(name = "`operator_name`")
    private String operatorName;

    /**
     * 金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 缴费单
     */
    @Column(name = "`payment_order_code`")
    private String paymentOrderCode;

    /**
     * 加收类型
     */
    @Column(name = "`extra_charge_type`")
    private Long extraChargeType;

    /**
     * 加收天数
     */
    @Column(name = "`extra_charge_days`")
    private Long extraChargeDays;

    /**
     * 加收费用
     */
    @Column(name = "`extra_charge`")
    private Long extraCharge;

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
     * 客户手机号
     */
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;
    
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    /**
     * 客户性别
     */
    @Column(name = "`customer_gender`")
    private Integer customerGender;

    /**
     * 上下半年
     */
    @Column(name = "`half_year`")
    private Long halfYear;

    /**
     * 劳务类型(1:马甲证,2:自用证)
     */
    @Column(name = "`labor_type`")
    private String laborType;

    /**
     * 取消时间
     */
    @Column(name = "`cancel_time`")
    private LocalDateTime cancelTime;

    /**
     * 取消人
     */
    @Column(name = "`canceler_id`")
    private Long cancelerId;

    /**
     * 取消人id
     */
    @Column(name = "`canceler`")
    private String canceler;

    /**
     * 更名费用
     */
    @Column(name = "`rename_fee`")
    private Long renameFee;

    /**
     * 更名主单id
     */
    @Column(name = "`rename_code`")
    private String renameCode;

    /**
     * 更型费用
     */
    @Column(name = "`remodel_fee`")
    private Long remodelFee;

    /**
     * 更型主单id
     */
    @Column(name = "`remodel_code`")
    private String remodelCode;

    /**
     * 续费父单ID
     */
    @Column(name = "`renew_code`")
    private String renewCode;

    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`market_code`")
    private String marketCode;

    @Column(name = "`department_id`")
    private Long departmentId;

    @Column(name = "`department_name`")
    private String departmentName;

    @Column(name = "`version`")
    private Integer version;

    @Column(name = "`creator`")
    private String creator;

    @Column(name = "`creator_id`")
    private Long creatorId;
    
    @Column(name = "`submitter_id`")
    private Long submitterId;
    
    @Column(name = "`submitter`")
    private String submitter;

    public Labor() {
    	super();
    }
    
    public Labor(UserTicket userTicket) {
    	super();
    	this.operator = userTicket.getId();
    	this.operatorName = userTicket.getRealName();	
    }
    
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
     * @return code
     */
    @FieldDef(label="code", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取运营车型
     *
     * @return models - 运营车型
     */
    @FieldDef(label="运营车型")
    @EditMode(editor = FieldEditor.Number, required = false)
    public String getModels() {
        return models;
    }

    /**
     * 设置运营车型
     *
     * @param models 运营车型
     */
    public void setModels(String models) {
        this.models = models;
    }

    /**
     * 获取发票编号
     *
     * @return invoice_number - 发票编号
     */
    @FieldDef(label="发票编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * 设置发票编号
     *
     * @param invoiceNumber 发票编号
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * 获取马甲号
     *
     * @return work_card - 马甲号
     */
    @FieldDef(label="马甲号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getWorkCard() {
        return workCard;
    }

    /**
     * 设置马甲号
     *
     * @param workCard 马甲号
     */
    public void setWorkCard(String workCard) {
        this.workCard = workCard;
    }

    /**
     * 获取车牌号
     *
     * @return license_plate - 车牌号
     */
    @FieldDef(label="车牌号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * 设置车牌号
     *
     * @param licensePlate 车牌号
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * 获取办理周期
     *
     * @return interval - 办理周期
     */
    @FieldDef(label="办理周期")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getInterval() {
        return interval;
    }

    /**
     * 设置办理周期
     *
     * @param interval 办理周期
     */
    public void setInterval(Long interval) {
        this.interval = interval;
    }

    

    public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	/**
     * 获取状态
     *
     * @return state - 状态
     */
    @FieldDef(label="状态")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注
     *
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    @FieldDef(label="创建日期")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
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
     * 获取操作员
     *
     * @return operator - 操作员
     */
    @FieldDef(label="操作员")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getOperator() {
        return operator;
    }

    /**
     * 设置操作员
     *
     * @param operator 操作员
     */
    public void setOperator(Long operator) {
        this.operator = operator;
    }

    /**
     * 获取操作员姓名
     *
     * @return operator_name - 操作员姓名
     */
    @FieldDef(label="操作员姓名", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * 设置操作员姓名
     *
     * @param operatorName 操作员姓名
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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
     * 获取缴费单
     *
     * @return payment_order_code - 缴费单
     */
    @FieldDef(label="缴费单", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPaymentOrderCode() {
        return paymentOrderCode;
    }

    /**
     * 设置缴费单
     *
     * @param paymentOrderCode 缴费单
     */
    public void setPaymentOrderCode(String paymentOrderCode) {
        this.paymentOrderCode = paymentOrderCode;
    }

    /**
     * 获取加收类型
     *
     * @return extra_charge_type - 加收类型
     */
    @FieldDef(label="加收类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getExtraChargeType() {
        return extraChargeType;
    }

    /**
     * 设置加收类型
     *
     * @param extraChargeType 加收类型
     */
    public void setExtraChargeType(Long extraChargeType) {
        this.extraChargeType = extraChargeType;
    }

    /**
     * 获取加收天数
     *
     * @return extra_charge_days - 加收天数
     */
    @FieldDef(label="加收天数")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getExtraChargeDays() {
        return extraChargeDays;
    }

    /**
     * 设置加收天数
     *
     * @param extraChargeDays 加收天数
     */
    public void setExtraChargeDays(Long extraChargeDays) {
        this.extraChargeDays = extraChargeDays;
    }

    /**
     * 获取加收费用
     *
     * @return extra_charge - 加收费用
     */
    @FieldDef(label="加收费用")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getExtraCharge() {
        return extraCharge;
    }

    /**
     * 设置加收费用
     *
     * @param extraCharge 加收费用
     */
    public void setExtraCharge(Long extraCharge) {
        this.extraCharge = extraCharge;
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
     * 获取客户手机号
     *
     * @return customer_cellphone - 客户手机号
     */
    @FieldDef(label="客户手机号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerCellphone() {
        return customerCellphone;
    }

    /**
     * 设置客户手机号
     *
     * @param customerCellphone 客户手机号
     */
    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    /**
     * 获取客户性别
     *
     * @return customer_sex - 客户性别
     */
    @FieldDef(label="客户性别")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getCustomerGender() {
        return customerGender;
    }

    /**
     * 设置客户性别
     *
     * @param customerGender 客户性别
     */
    public void setCustomerGender(Integer customerGender) {
        this.customerGender = customerGender;
    }

    /**
     * 获取上下半年
     *
     * @return half_year - 上下半年
     */
    @FieldDef(label="上下半年")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getHalfYear() {
        return halfYear;
    }

    /**
     * 设置上下半年
     *
     * @param halfYear 上下半年
     */
    public void setHalfYear(Long halfYear) {
        this.halfYear = halfYear;
    }

    /**
     * 获取劳务类型(1:马甲证,2:自用证)
     *
     * @return labor_type - 劳务类型(1:马甲证,2:自用证)
     */
    @FieldDef(label="劳务类型(1:马甲证,2:自用证)")
    @EditMode(editor = FieldEditor.Number, required = false)
    public String getLaborType() {
        return laborType;
    }

    /**
     * 设置劳务类型(1:马甲证,2:自用证)
     *
     * @param laborType 劳务类型(1:马甲证,2:自用证)
     */
    public void setLaborType(String laborType) {
        this.laborType = laborType;
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
     * 获取取消人
     *
     * @return canceler_id - 取消人
     */
    @FieldDef(label="取消人")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCancelerId() {
        return cancelerId;
    }

    /**
     * 设置取消人
     *
     * @param cancelerId 取消人
     */
    public void setCancelerId(Long cancelerId) {
        this.cancelerId = cancelerId;
    }

    /**
     * 获取取消人id
     *
     * @return canceler - 取消人id
     */
    @FieldDef(label="取消人id", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCanceler() {
        return canceler;
    }

    /**
     * 设置取消人id
     *
     * @param canceler 取消人id
     */
    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }

    /**
     * 获取更名费用
     *
     * @return rename_fee - 更名费用
     */
    @FieldDef(label="更名费用")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getRenameFee() {
        return renameFee;
    }

    /**
     * 设置更名费用
     *
     * @param renameFee 更名费用
     */
    public void setRenameFee(Long renameFee) {
        this.renameFee = renameFee;
    }

    /**
     * 获取更名主单id
     *
     * @return rename_id - 更名主单id
     */
    @FieldDef(label="更名主单id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public String getRenameCode() {
        return renameCode;
    }

    /**
     * 设置更名主单id
     *
     * @param renameId 更名主单id
     */
    public void setRenameCode(String renameCode) {
        this.renameCode = renameCode;
    }

    /**
     * 获取更型费用
     *
     * @return remodel_fee - 更型费用
     */
    @FieldDef(label="更型费用")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getRemodelFee() {
        return remodelFee;
    }

    /**
     * 设置更型费用
     *
     * @param remodelFee 更型费用
     */
    public void setRemodelFee(Long remodelFee) {
        this.remodelFee = remodelFee;
    }

    /**
     * 获取更型主单id
     *
     * @return remodel_id - 更型主单id
     */
    @FieldDef(label="更型主单id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public String getRemodelCode() {
        return remodelCode;
    }

    /**
     * 设置更型主单id
     *
     * @param remodelId 更型主单id
     */
    public void setRemodelCode(String remodelCode) {
        this.remodelCode = remodelCode;
    }

    /**
     * 获取续费父单ID
     *
     * @return renew_id - 续费父单ID
     */
    @FieldDef(label="续费父单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public String getRenewCode() {
        return renewCode;
    }

    /**
     * 设置续费父单ID
     *
     * @param renewId 续费父单ID
     */
    public void setRenewCode(String renewCode) {
        this.renewCode = renewCode;
    }

    /**
     * @return market_id
     */
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * @param marketId
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * @return market_code
     */
    @FieldDef(label="marketCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * @param marketCode
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    /**
     * @return department_id
     */
    @FieldDef(label="departmentId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return department_name
     */
    @FieldDef(label="departmentName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return version
     */
    @FieldDef(label="version")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return creator
     */
    @FieldDef(label="creator", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return creator_id
     */
    @FieldDef(label="creatorId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public Long getSubmitterId() {
		return submitterId;
	}

	public void setSubmitterId(Long submitterId) {
		this.submitterId = submitterId;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public Integer getPreState() {
		return preState;
	}

	public void setPreState(Integer preState) {
		this.preState = preState;
	}

	public Long getMchId() {
		return mchId;
	}

	public void setMchId(Long mchId) {
		this.mchId = mchId;
	}
    
    
}