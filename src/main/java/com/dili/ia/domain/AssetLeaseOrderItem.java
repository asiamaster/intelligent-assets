package com.dili.ia.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 资产租赁订单项
 * This file was generated on 2020-05-29 14:40:05.
 */
@Table(name = "`asset_lease_order_item`")
public class AssetLeaseOrderItem extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`code`")
    private String code;

    //创建时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    //修改时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;
    /**
     * 租赁单ID
     */
    @Column(name = "`lease_order_id`")
    private Long leaseOrderId;
    /**
     * 租赁单code
     */
    @Column(name = "`lease_order_code`")
    private String leaseOrderCode;

    /**
     *  1:摊位 2：冷库 3：公寓
     */
    @Column(name = "`asset_type`")
    private Integer assetType;

    /**
     * 资产ID
     */
    @Column(name = "`asset_id`")
    private Long assetId;

    /**
     * 资产名称
     */
    @Column(name = "`asset_name`")
    private String assetName;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    @Column(name = "`customer_name`")
    private String customerName;

    /**
     * 数量
     */
    @Column(name = "`number`")
    private BigDecimal number;

    /**
     * 租赁数量
     */
    @Column(name = "`leases_num`")
    private BigDecimal leasesNum;

    @Column(name = "`is_corner`")
    private String isCorner;

    @Column(name = "`unit_code`")
    private String unitCode;

    @Column(name = "`unit_name`")
    private String unitName;

    /**
     * 单价(分)
     */
    @Column(name = "`unit_price`")
    private Long unitPrice;

    /**
     * 应交月数
     */
    @Column(name = "`payment_month`")
    private BigDecimal paymentMonth;

    /**
     * 优惠金额
     */
    @Column(name = "`discount_amount`")
    private Long discountAmount;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`stop_time`")
    private LocalDateTime stopTime;

    /**
     * 停租人ID
     */
    @Column(name = "`stop_operator_id`")
    private Long stopOperatorId;

    @Column(name = "`stop_operator_name`")
    private String stopOperatorName;

    /**
     * 1:立即 2：定时
     */
    @Column(name = "`stop_way`")
    private Integer stopWay;

    @Column(name = "`stop_reason`")
    private String stopReason;

    /**
     * 状态（1：已创建 2：已取消 3：已提交 4：未生效 5：已生效 6：已停租 7：已退款 8：已过期）
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 支付状态（1：未交清 2：已交清）
     */
    @Column(name = "`pay_state`")
    private Integer payState;

    /**
     * 停租状态(1:未发起 2：等待定时执行 3：已停租)
     */
    @Column(name = "`stop_rent_state`")
    private Integer stopRentState;

    /**
     * 区域id
     */
    @Column(name = "`district_id`")
    private Long districtId;

    @Column(name = "`district_name`")
    private String districtName;

    /**
     * 金额
     */
    @Column(name = "`total_amount`")
    private Long totalAmount;

    /**
     * 1:待申请 2：退款中 3：已退款
     */
    @Column(name = "`refund_state`")
    private Integer refundState;

    /**
     * 退款总金额
     */
    @Column(name = "`refund_amount`")
    private Long refundAmount;

    /**
     * 乐观锁，版本号
     */
    @Column(name = "`version`")
    private Byte version;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getLeaseOrderId() {
        return leaseOrderId;
    }

    public void setLeaseOrderId(Long leaseOrderId) {
        this.leaseOrderId = leaseOrderId;
    }

    public String getLeaseOrderCode() {
        return leaseOrderCode;
    }

    public void setLeaseOrderCode(String leaseOrderCode) {
        this.leaseOrderCode = leaseOrderCode;
    }

    public Integer getAssetType() {
        return assetType;
    }

    public void setAssetType(Integer assetType) {
        this.assetType = assetType;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getLeasesNum() {
        return leasesNum;
    }

    public void setLeasesNum(BigDecimal leasesNum) {
        this.leasesNum = leasesNum;
    }

    public String getIsCorner() {
        return isCorner;
    }

    public void setIsCorner(String isCorner) {
        this.isCorner = isCorner;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(BigDecimal paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime) {
        this.stopTime = stopTime;
    }

    public Long getStopOperatorId() {
        return stopOperatorId;
    }

    public void setStopOperatorId(Long stopOperatorId) {
        this.stopOperatorId = stopOperatorId;
    }

    public String getStopOperatorName() {
        return stopOperatorName;
    }

    public void setStopOperatorName(String stopOperatorName) {
        this.stopOperatorName = stopOperatorName;
    }

    public Integer getStopWay() {
        return stopWay;
    }

    public void setStopWay(Integer stopWay) {
        this.stopWay = stopWay;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Integer getStopRentState() {
        return stopRentState;
    }

    public void setStopRentState(Integer stopRentState) {
        this.stopRentState = stopRentState;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getRefundState() {
        return refundState;
    }

    public void setRefundState(Integer refundState) {
        this.refundState = refundState;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }
}