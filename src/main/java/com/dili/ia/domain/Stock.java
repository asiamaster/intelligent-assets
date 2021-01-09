package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 库存
 * This file was generated on 2020-06-12 11:14:28.
 */
@Table(name = "`stock`")
public class Stock extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 一级区域
     */
    @Column(name = "`first_district_id`")
   	private Long firstDistrictId;

    /**
     * 一级区域
     */
    @Column(name = "`first_district_name`")
   	private Long firstDistrictName;
    
    /**
     * 区域id
     */
    @Column(name = "`district_id`")
    private Long districtId;

    /**
     * 区域
     */
    @Column(name = "`district_name`")
    private String districtName;

    /**
     * 资产 冷库id
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    /**
     * 资产 冷库名称
     */
    @Column(name = "`assets_name`")
    private String assetsName;

    @Column(name = "`category_id`")
    private Long categoryId;

    @Column(name = "`category_name`")
    private String categoryName;

    @Column(name = "`customer_id`")
    private Long customerId;

    @Column(name = "`customer_name`")
    private String customerName;
    
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;
    
    /**
     * 部门
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    @Column(name = "`department_name`")
    private String departmentName;

    @Column(name = "`quantity`")
    private Long quantity;

    @Column(name = "`weight`")
    private Long weight;

    /**
     * 版本号
     */
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
     * 获取区域
     *
     * @return district_name - 区域
     */
    @FieldDef(label="区域", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDistrictName() {
        return districtName;
    }

    /**
     * 设置区域
     *
     * @param districtName 区域
     */
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    /**
     * 获取资产 冷库id
     *
     * @return assets_id - 资产 冷库id
     */
    @FieldDef(label="资产 冷库id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    /**
     * 设置资产 冷库id
     *
     * @param assetsId 资产 冷库id
     */
    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    /**
     * 获取资产 冷库名称
     *
     * @return assets_name - 资产 冷库名称
     */
    @FieldDef(label="资产 冷库名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsName() {
        return assetsName;
    }

    /**
     * 设置资产 冷库名称
     *
     * @param assetsName 资产 冷库名称
     */
    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    /**
     * @return category_id
     */
    @FieldDef(label="categoryId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId
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
     * @return customer_id
     */
    @FieldDef(label="customerId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
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
     * @return quantity
     */
    @FieldDef(label="quantity")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return weight
     */
    @FieldDef(label="weight")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    /**
     * 获取版本号
     *
     * @return version - 版本号
     */
    @FieldDef(label="版本号")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param version 版本号
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

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCustomerCellphone() {
		return customerCellphone;
	}

	public void setCustomerCellphone(String customerCellphone) {
		this.customerCellphone = customerCellphone;
	}

	public Long getFirstDistrictId() {
		return firstDistrictId;
	}

	public void setFirstDistrictId(Long firstDistrictId) {
		this.firstDistrictId = firstDistrictId;
	}

	public Long getFirstDistrictName() {
		return firstDistrictName;
	}

	public void setFirstDistrictName(Long firstDistrictName) {
		this.firstDistrictName = firstDistrictName;
	}
	
	
    
}