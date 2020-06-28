package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.domain.UserTicket;

import java.util.Date;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;

/**
 * 由MyBatis Generator工具自动生成
 * 入库
 * This file was generated on 2020-06-12 11:14:28.
 */
@Table(name = "`stock_in`")
public class StockIn extends BaseDomain {

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    @Column(name = "`code`")
    private String code;

    /**
     * 入库时间
     */
    @Column(name = "`stock_in_date`")
    private Date stockInDate;

    /**
     * 客户id
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    @Column(name = "`customer_name`")
    private String customerName;

    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    /**
     * 操作员id
     */
    @Column(name = "`operator_id`")
    private Long operatorId;

    @Column(name = "`operator_name`")
    private String operatorName;

    /**
     * 总重量
     */
    @Column(name = "`weight`")
    private Long weight;

    /**
     * 总数量
     */
    @Column(name = "`quantity`")
    private Long quantity;

    /**
     * 状态
     */
    @Column(name = "`state`")
    private Integer state;
    
    /**
     * 总金额
     */
    @Column(name = "amount")
    private Long amount;

    /**
     * 入库类型
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 品类id
     */
    @Column(name = "`category_id`")
    private Long categoryId;

    @Column(name = "`category_name`")
    private String categoryName;

    /**
     * 产地
     */
    @Column(name = "`origin`")
    private String origin;

    /**
     * 过期时间
     */
    @Column(name = "`expire_date`")
    private Date expireDate;

    /**
     * 支付方式 1 现金，2 POS，3 刷卡
     */
    @Column(name = "`pay_type`")
    private Integer payType;

    /**
     * 部门
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 计价单位
     */
    @Column(name = "`uom`")
    private Integer uom;

    @Column(name = "`version`")
    private Integer version;

    /**
     * 市场id
     */
    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`market_code`")
    private String marketCode;

    /**
     * 创建人
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    @Column(name = "`creator`")
    private String creator;
    
    @Column(name= "`submitter_id`")
    private Long submitterId;
    
    @Column(name= "`submitter`")
    private String submitter;
    
    @Column(name= "`sub_date`")
    private Date subDate; 
    
    @Column(name= "`withdraw_operator_id`")
    private Long withdrawOperatorId;
    
    @Column(name= "`withdraw_operator`")
    private String withdrawOperator;
    
    @Column(name= "`canceler_id`")
    private Long cancelerId;
    
    @Column(name= "`canceler`")
    private String canceler;

    
    /**
     * 空参构造
     */
    public StockIn() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    /**
     * 记录操作人
     * @param userTicket
     */
    public StockIn(UserTicket userTicket) {
		super();
		this.operatorId = userTicket.getId();
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
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
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
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
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
     * @return code
     */
    @FieldDef(label="code", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
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
     * 获取入库时间
     *
     * @return stock_in_date - 入库时间
     */
    @FieldDef(label="入库时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getStockInDate() {
        return stockInDate;
    }

    /**
     * 设置入库时间
     *
     * @param stockInDate 入库时间
     */
    public void setStockInDate(Date stockInDate) {
        this.stockInDate = stockInDate;
    }

    /**
     * 获取客户id
     *
     * @return customer_id - 客户id
     */
    @FieldDef(label="客户id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置客户id
     *
     * @param customerId 客户id
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return customer_name
     */
    @FieldDef(label="customerName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return customer_cellphone
     */
    @FieldDef(label="customerCellphone", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerCellphone() {
        return customerCellphone;
    }

    /**
     * @param customerCellphone
     */
    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    /**
     * 获取操作员id
     *
     * @return operator_id - 操作员id
     */
    @FieldDef(label="操作员id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * 设置操作员id
     *
     * @param operatorId 操作员id
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return operator_name
     */
    @FieldDef(label="operatorName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 获取总重量
     *
     * @return weight - 总重量
     */
    @FieldDef(label="总重量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWeight() {
        return weight;
    }

    /**
     * 设置总重量
     *
     * @param weight 总重量
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    /**
     * 获取总数量
     *
     * @return quantity - 总数量
     */
    @FieldDef(label="总数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getQuantity() {
        return quantity;
    }

    /**
     * 设置总数量
     *
     * @param quantity 总数量
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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
     * 获取入库类型
     *
     * @return type - 入库类型
     */
    @FieldDef(label="入库类型")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getType() {
        return type;
    }

    /**
     * 设置入库类型
     *
     * @param type 入库类型
     */
    public void setType(Integer type) {
        this.type = type;
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
     * @return category_name
     */
    @FieldDef(label="categoryName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取产地
     *
     * @return origin - 产地
     */
    @FieldDef(label="产地", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOrigin() {
        return origin;
    }

    /**
     * 设置产地
     *
     * @param origin 产地
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * 获取过期时间
     *
     * @return expire_date - 过期时间
     */
    @FieldDef(label="过期时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * 设置过期时间
     *
     * @param expireDate 过期时间
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * 获取支付方式 1 现金，2 POS，3 刷卡
     *
     * @return pay_type - 支付方式 1 现金，2 POS，3 刷卡
     */
    @FieldDef(label="支付方式 1 现金，2 POS，3 刷卡")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getPayType() {
        return payType;
    }

    /**
     * 设置支付方式 1 现金，2 POS，3 刷卡
     *
     * @param payType 支付方式 1 现金，2 POS，3 刷卡
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * 获取部门
     *
     * @return department_id - 部门
     */
    @FieldDef(label="部门")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置部门
     *
     * @param departmentId 部门
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
     * 获取计价单位
     *
     * @return uom - 计价单位
     */
    @FieldDef(label="计价单位")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getUom() {
        return uom;
    }

    /**
     * 设置计价单位
     *
     * @param uom 计价单位
     */
    public void setUom(Integer uom) {
        this.uom = uom;
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
     * 获取市场id
     *
     * @return market_id - 市场id
     */
    @FieldDef(label="市场id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场id
     *
     * @param marketId 市场id
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
     * 获取创建人
     *
     * @return creator_id - 创建人
     */
    @FieldDef(label="创建人")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建人
     *
     * @param creatorId 创建人
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return creator
     */
    @FieldDef(label="creator", maxLength = 20)
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

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
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

	public Date getSubDate() {
		return subDate;
	}

	public void setSubDate(Date subDate) {
		this.subDate = subDate;
	}

	public Long getWithdrawOperatorId() {
		return withdrawOperatorId;
	}

	public void setWithdrawOperatorId(Long withdrawOperatorId) {
		this.withdrawOperatorId = withdrawOperatorId;
	}

	public String getWithdrawOperator() {
		return withdrawOperator;
	}

	public void setWithdrawOperator(String withdrawOperator) {
		this.withdrawOperator = withdrawOperator;
	}

	public Long getCancelerId() {
		return cancelerId;
	}

	public void setCancelerId(Long cancelerId) {
		this.cancelerId = cancelerId;
	}

	public String getCanceler() {
		return canceler;
	}

	public void setCanceler(String canceler) {
		this.canceler = canceler;
	}
	
	
}