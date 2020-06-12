package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 入库详情(子单)
 * This file was generated on 2020-06-12 11:14:28.
 */
@Table(name = "`stock_in_detail`")
public class StockInDetail extends BaseDomain {
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
     * 入库单号
     */
    @Column(name = "`stock_in_code`")
    private String stockInCode;

    /**
     * 入库详情编号
     */
    @Column(name = "`code`")
    private String code;

    @Column(name = "`state`")
    private Byte state;

    /**
     * 单件重量
     */
    @Column(name = "`unit_weight`")
    private Long unitWeight;

    /**
     * 查件管理员
     */
    @Column(name = "`check_operator_id`")
    private Long checkOperatorId;

    @Column(name = "`check_operator`")
    private String checkOperator;

    /**
     * 接车单号
     */
    @Column(name = "`pickup_number`")
    private String pickupNumber;

    /**
     * 司磅记录
     */
    @Column(name = "`weightman_id`")
    private Long weightmanId;

    /**
     * 库位ID
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    @Column(name = "`assets_code`")
    private String assetsCode;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 区域id
     */
    @Column(name = "`district_id`")
    private Long districtId;

    @Column(name = "`district_name`")
    private String districtName;

    /**
     * 应收款
     */
    @Column(name = "`receivable`")
    private Long receivable;

    /**
     * 实收款
     */
    @Column(name = "`cope`")
    private Long cope;

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
     * 入库数量
     */
    @Column(name = "`quantity`")
    private Long quantity;

    /**
     * 入库总量
     */
    @Column(name = "`weight`")
    private Long weight;

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
     * 车牌号
     */
    @Column(name = "`car_plate`")
    private String carPlate;

    /**
     * 汽车编号,类型
     */
    @Column(name = "`car_type_public_code`")
    private String carTypePublicCode;

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
     * 获取入库单号
     *
     * @return stock_in_code - 入库单号
     */
    @FieldDef(label="入库单号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getStockInCode() {
        return stockInCode;
    }

    /**
     * 设置入库单号
     *
     * @param stockInCode 入库单号
     */
    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    /**
     * 获取入库详情编号
     *
     * @return code - 入库详情编号
     */
    @FieldDef(label="入库详情编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置入库详情编号
     *
     * @param code 入库详情编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return state
     */
    @FieldDef(label="state")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Byte getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * 获取单件重量
     *
     * @return unit_weight - 单件重量
     */
    @FieldDef(label="单件重量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getUnitWeight() {
        return unitWeight;
    }

    /**
     * 设置单件重量
     *
     * @param unitWeight 单件重量
     */
    public void setUnitWeight(Long unitWeight) {
        this.unitWeight = unitWeight;
    }

    /**
     * 获取查件管理员
     *
     * @return check_operator_id - 查件管理员
     */
    @FieldDef(label="查件管理员")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCheckOperatorId() {
        return checkOperatorId;
    }

    /**
     * 设置查件管理员
     *
     * @param checkOperatorId 查件管理员
     */
    public void setCheckOperatorId(Long checkOperatorId) {
        this.checkOperatorId = checkOperatorId;
    }

    /**
     * @return check_operator
     */
    @FieldDef(label="checkOperator", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCheckOperator() {
        return checkOperator;
    }

    /**
     * @param checkOperator
     */
    public void setCheckOperator(String checkOperator) {
        this.checkOperator = checkOperator;
    }

    /**
     * 获取接车单号
     *
     * @return pickup_number - 接车单号
     */
    @FieldDef(label="接车单号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPickupNumber() {
        return pickupNumber;
    }

    /**
     * 设置接车单号
     *
     * @param pickupNumber 接车单号
     */
    public void setPickupNumber(String pickupNumber) {
        this.pickupNumber = pickupNumber;
    }

    /**
     * 获取司磅记录
     *
     * @return weightman_id - 司磅记录
     */
    @FieldDef(label="司磅记录")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWeightmanId() {
        return weightmanId;
    }

    /**
     * 设置司磅记录
     *
     * @param weightmanId 司磅记录
     */
    public void setWeightmanId(Long weightmanId) {
        this.weightmanId = weightmanId;
    }

    /**
     * 获取库位ID
     *
     * @return assets_id - 库位ID
     */
    @FieldDef(label="库位ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    /**
     * 设置库位ID
     *
     * @param assetsId 库位ID
     */
    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    /**
     * @return assets_code
     */
    @FieldDef(label="assetsCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsCode() {
        return assetsCode;
    }

    /**
     * @param assetsCode
     */
    public void setAssetsCode(String assetsCode) {
        this.assetsCode = assetsCode;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 100)
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
     * @return district_name
     */
    @FieldDef(label="districtName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDistrictName() {
        return districtName;
    }

    /**
     * @param districtName
     */
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    /**
     * 获取应收款
     *
     * @return receivable - 应收款
     */
    @FieldDef(label="应收款")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getReceivable() {
        return receivable;
    }

    /**
     * 设置应收款
     *
     * @param receivable 应收款
     */
    public void setReceivable(Long receivable) {
        this.receivable = receivable;
    }

    /**
     * 获取实收款
     *
     * @return cope - 实收款
     */
    @FieldDef(label="实收款")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCope() {
        return cope;
    }

    /**
     * 设置实收款
     *
     * @param cope 实收款
     */
    public void setCope(Long cope) {
        this.cope = cope;
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
     * 获取入库数量
     *
     * @return quantity - 入库数量
     */
    @FieldDef(label="入库数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getQuantity() {
        return quantity;
    }

    /**
     * 设置入库数量
     *
     * @param quantity 入库数量
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取入库总量
     *
     * @return weight - 入库总量
     */
    @FieldDef(label="入库总量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWeight() {
        return weight;
    }

    /**
     * 设置入库总量
     *
     * @param weight 入库总量
     */
    public void setWeight(Long weight) {
        this.weight = weight;
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
     * 获取车牌号
     *
     * @return car_plate - 车牌号
     */
    @FieldDef(label="车牌号", maxLength = 10)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCarPlate() {
        return carPlate;
    }

    /**
     * 设置车牌号
     *
     * @param carPlate 车牌号
     */
    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    /**
     * 获取汽车编号,类型
     *
     * @return car_type_public_code - 汽车编号,类型
     */
    @FieldDef(label="汽车编号,类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCarTypePublicCode() {
        return carTypePublicCode;
    }

    /**
     * 设置汽车编号,类型
     *
     * @param carTypePublicCode 汽车编号,类型
     */
    public void setCarTypePublicCode(String carTypePublicCode) {
        this.carTypePublicCode = carTypePublicCode;
    }
}