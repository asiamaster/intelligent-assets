package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 出库
 * This file was generated on 2020-06-12 11:14:28.
 */
@Table(name = "`stock_out`")
public class StockOut extends BaseDomain {
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
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 出库单号
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
     * 品类
     */
    @Column(name = "`category_id`")
    private Long categoryId;

    @Column(name = "`category_name`")
    private String categoryName;

    /**
     * 客户手机号
     */
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    /**
     * 数量
     */
    @Column(name = "`quantity`")
    private Long quantity;

    /**
     * 出库时间
     */
    @Column(name = "`stock_out_date`")
    private Date stockOutDate;

    /**
     * 部门id
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 部门名称
     */
    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 区域id
     */
    @Column(name = "`district_id`")
    private Long districtId;

    /**
     * 区域名称
     */
    @Column(name = "`district_name`")
    private String districtName;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 冷库ID
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    /**
     * 冷库编码
     */
    @Column(name = "`assets_name`")
    private String assetsName;

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
     * 获取出库单号
     *
     * @return code - 出库单号
     */
    @FieldDef(label="出库单号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置出库单号
     *
     * @param code 出库单号
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
     * 获取品类
     *
     * @return category_id - 品类
     */
    @FieldDef(label="品类")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置品类
     *
     * @param categoryId 品类
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return category_name
     */
    @FieldDef(label="categoryName", maxLength = 40)
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
     * 获取客户手机号
     *
     * @return customer_cellphone - 客户手机号
     */
    @FieldDef(label="客户手机号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
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
     * 获取数量
     *
     * @return quantity - 数量
     */
    @FieldDef(label="数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     *
     * @param quantity 数量
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取出库时间
     *
     * @return stock_out_date - 出库时间
     */
    @FieldDef(label="出库时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    public Date getStockOutDate() {
        return stockOutDate;
    }

    /**
     * 设置出库时间
     *
     * @param stockOutDate 出库时间
     */
    public void setStockOutDate(Date stockOutDate) {
        this.stockOutDate = stockOutDate;
    }

    /**
     * 获取部门id
     *
     * @return department_id - 部门id
     */
    @FieldDef(label="部门id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置部门id
     *
     * @param departmentId 部门id
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取部门名称
     *
     * @return department_name - 部门名称
     */
    @FieldDef(label="部门名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 设置部门名称
     *
     * @param departmentName 部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取区域id
     *
     * @return district_id - 区域id
     */
    @FieldDef(label="区域id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDistrictId() {
        return districtId;
    }

    /**
     * 设置区域id
     *
     * @param districtId 区域id
     */
    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    /**
     * 获取区域名称
     *
     * @return district_name - 区域名称
     */
    @FieldDef(label="区域名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDistrictName() {
        return districtName;
    }

    /**
     * 设置区域名称
     *
     * @param districtName 区域名称
     */
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 50)
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
     * 获取冷库ID
     *
     * @return assets_id - 冷库ID
     */
    @FieldDef(label="冷库ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    /**
     * 设置冷库ID
     *
     * @param assetsId 冷库ID
     */
    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    /**
     * 获取冷库编码
     *
     * @return assets_name - 冷库编码
     */
    @FieldDef(label="冷库编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsName() {
        return assetsName;
    }

    /**
     * 设置冷库编码
     *
     * @param assetsName 冷库编码
     */
    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
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

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
    
    
}