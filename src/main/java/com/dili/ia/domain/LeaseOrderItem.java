package com.dili.ia.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.Version;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁订单项
 * This file was generated on 2020-02-11 15:54:49.
 */
@Table(name = "`lease_order_item`")
public interface LeaseOrderItem extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`create_time`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getCreateTime();

    void setCreateTime(Date createTime);

    @Column(name = "`modify_time`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModifyTime();

    void setModifyTime(Date modifyTime);

    @Column(name = "`lease_order_id`")
    @FieldDef(label="租赁单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getLeaseOrderId();

    void setLeaseOrderId(Long leaseOrderId);

    @Column(name = "`lease_order_code`")
    @FieldDef(label="租赁单编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLeaseOrderCode();

    void setLeaseOrderCode(String leaseOrderCode);

    @Column(name = "`booth_id`")
    @FieldDef(label="摊位ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getBoothId();

    void setBoothId(Long boothId);

    @Column(name = "`booth_name`")
    @FieldDef(label="摊位名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBoothName();

    void setBoothName(String boothName);

    @Column(name = "`customer_id`")
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`customer_name`")
    @FieldDef(label="客户名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Column(name = "`number`")
    @FieldDef(label="数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    Double getNumber();

    void setNumber(Double number);

    @Column(name = "`is_corner`")
    @FieldDef(label="是否转角")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIsCorner();

    void setIsCorner(String isCorner);

    @Column(name = "`unit_code`")
    @FieldDef(label="单位编码", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getUnitCode();

    void setUnitCode(String unitCode);

    @Column(name = "`unit_name`")
    @FieldDef(label="单位名称", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getUnitName();

    void setUnitName(String unitName);

    @Column(name = "`unit_price`")
    @FieldDef(label="单价")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getUnitPrice();

    void setUnitPrice(Long unitPrice);

    @Column(name = "`payment_month`")
    @FieldDef(label = "应交月数")
    @EditMode(editor = FieldEditor.Number, required = false)
    BigDecimal getPaymentMonth();

    void setPaymentMonth(BigDecimal paymentMonth);

    @Column(name = "`discount_amount`")
    @FieldDef(label="优惠金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDiscountAmount();

    void setDiscountAmount(Long discountAmount);

    @Column(name = "`stop_time`")
    @FieldDef(label="停租时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getStopTime();

    void setStopTime(Date stopTime);

    @Column(name = "`stop_operator_id`")
    @FieldDef(label="停租人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getStopOperatorId();

    void setStopOperatorId(Long stopOperatorId);

    @Column(name = "`stop_operator_name`")
    @FieldDef(label="停租人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getStopOperatorName();

    void setStopOperatorName(String stopOperatorName);

    @Column(name = "`stop_way`")
    @FieldDef(label="停租方式（1：立即 2：定时）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getStopWay();

    void setStopWay(Integer stopWay);

    @Column(name = "`stop_reason`")
    @FieldDef(label="停租原因", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getStopReason();

    void setStopReason(String stopReason);

    @Column(name = "`state`")
    @FieldDef(label="状态（1：已创建 2：已取消 3：已提交 4：未生效 5：已生效 6：已停租 7：已退款 8：已过期）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getState();

    void setState(Integer state);


    @Column(name = "`pay_state`")
    @FieldDef(label="支付状态（1：未交清 2：已交清）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPayState();

    void setPayState(Integer payState);

    @Column(name = "`stop_rent_state`")
    @FieldDef(label="停租状态(1:未发起 2：等待定时执行 3：已停租)")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getStopRentState();

    void setStopRentState(Integer stopRentState);

    @Column(name = "`district_id`")
    @FieldDef(label="区域id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDistrictId();

    void setDistrictId(Long districtId);

    @Column(name = "`district_name`")
    @FieldDef(label="区域全名", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDistrictName();

    void setDistrictName(String districtName);

    @Column(name = "`total_amount`")
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTotalAmount();

    void setTotalAmount(Long totalAmount);

    @Column(name = "`deposit_amount_flag`")
    @FieldDef(label="1:未转抵 2：已转抵")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getDepositAmountFlag();

    void setDepositAmountFlag(Integer depositAmountFlag);

    @Column(name = "`deposit_amount_source_id`")
    @FieldDef(label="保证金源单ID", maxLength = 20)
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepositAmountSourceId();

    void setDepositAmountSourceId(Long depositAmountSourceId);

    @Column(name = "`deposit_amount`")
    @FieldDef(label="保证金")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepositAmount();

    void setDepositAmount(Long depositAmount);

    @Column(name = "`manage_amount`")
    @FieldDef(label="物管费")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getManageAmount();

    void setManageAmount(Long manageAmount);

    @Column(name = "`rent_amount`")
    @FieldDef(label="租金")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRentAmount();

    void setRentAmount(Long rentAmount);

    @Column(name = "`refund_state`")
    @FieldDef(label="退款状态状态（1：未发起申请 2：退款中 3：已退款）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getRefundState();

    void setRefundState(Integer refundState);

    @Column(name = "`refund_amount`")
    @FieldDef(label="退款总金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRefundAmount();

    void setRefundAmount(Long refundAmount);

    @Column(name = "`deposit_refund_amount`")
    @FieldDef(label="保证金额退款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepositRefundAmount();

    void setDepositRefundAmount(Long depositRefundAmount);

    @Column(name = "`manage_refund_amount`")
    @FieldDef(label="物管费退款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getManageRefundAmount();

    void setManageRefundAmount(Long manageRefundAmount);

    @Column(name = "`rent_refund_amount`")
    @FieldDef(label="租金退款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRentRefundAmount();

    void setRentRefundAmount(Long rentRefundAmount);

    @Column(name = "`version`")
    @FieldDef(label="版本号")
    @EditMode(editor = FieldEditor.Number, required = false)
    @Version
    Integer getVersion();

    void setVersion(Integer version);
}