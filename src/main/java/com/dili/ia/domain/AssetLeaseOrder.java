package com.dili.ia.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 资产租赁订单
 * This file was generated on 2020-05-29 14:40:05.
 */
@Table(name = "`asset_lease_order`")
public class AssetLeaseOrder extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //编号
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

    //客户ID
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     *  1:摊位 2：冷库 3：公寓
     */
    @Column(name = "`asset_type`")
    private Integer assetType;

    //客户名称
    @Column(name = "`customer_name`")
    private String customerName;

    //证件号
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    //客户电话
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    //开始时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`start_time`")
    private LocalDateTime startTime;

    //截止时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`end_time`")
    private LocalDateTime endTime;

    //天数
    @Column(name = "`days`")
    private Long days;

    //合同编号
    @Column(name = "`contract_no`")
    private String contractNo;

    //租赁形式Code
    @Column(name = "`lease_term_code`")
    private String leaseTermCode;

    //租赁形式
    @Column(name = "`lease_term_name`")
    private String leaseTermName;

    //经营范围code
    @Column(name = "`engage_code`")
    private String engageCode;

    //经营范围名称
    @Column(name = "`engage_name`")
    private String engageName;

    //品类id
    @Column(name = "`category_id`")
    private Long categoryId;

    //品类名称
    @Column(name = "`category_name`")
    private String categoryName;

    //部门ID
    @Column(name = "`department_id`")
    private Long departmentId;

    //部门名称
    @Column(name = "`department_name`")
    private String departmentName;

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
     * 金额
     */
    @Column(name = "`total_amount`")
    private Long totalAmount;

    /**
     * 保证金抵扣金额
     */
    @Column(name = "`deposit_deduction`")
    private Long depositDeduction;

    /**
     * 定金抵扣金额
     */
    @Column(name = "`earnest_deduction`")
    private Long earnestDeduction;

    /**
     * 转低金额
     */
    @Column(name = "`transfer_deduction`")
    private Long transferDeduction;

    /**
     * 实付金额
     */
    @Column(name = "`pay_amount`")
    private Long payAmount;

    /**
     * 已付金额
     */
    @Column(name = "`paid_amount`")
    private Long paidAmount;

    /**
     * 待付金额
     */
    @Column(name = "`wait_amount`")
    private Long waitAmount;

    @Column(name = "`payment_id`")
    private String paymentId;

    /**
     * 创建人ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    @Column(name = "`creator`")
    private String creator;

    /**
     * 撤回人ID
     */
    @Column(name = "`withdraw_operator_id`")
    private Long withdrawOperatorId;

    @Column(name = "`withdraw_operator`")
    private String withdrawOperator;

    /**
     * 取消人ID
     */
    @Column(name = "`canceler_id`")
    private Long cancelerId;

    @Column(name = "`canceler`")
    private String canceler;

    @Column(name = "`notes`")
    private String notes;

    /**
     * 退款金额
     */
    @Column(name = "`refund_amount`")
    private Long refundAmount;

    /**
     * 1:待申请 2：退款中 3：已退款
     */
    @Column(name = "`refund_state`")
    private Integer refundState;

    /**
     * 是否为续租 1:是 2：否
     */
    @Column(name = "`is_renew`")
    private Integer isRenew;

    /**
     * 是否删除
     */
    @Column(name = "`is_delete`")
    private Integer isDelete;

    /**
     * 市场
     */
    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`market_code`")
    private String marketCode;

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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getAssetType() {
        return assetType;
    }

    public void setAssetType(Integer assetType) {
        this.assetType = assetType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCustomerCellphone() {
        return customerCellphone;
    }

    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        this.days = days;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getLeaseTermCode() {
        return leaseTermCode;
    }

    public void setLeaseTermCode(String leaseTermCode) {
        this.leaseTermCode = leaseTermCode;
    }

    public String getLeaseTermName() {
        return leaseTermName;
    }

    public void setLeaseTermName(String leaseTermName) {
        this.leaseTermName = leaseTermName;
    }

    public String getEngageCode() {
        return engageCode;
    }

    public void setEngageCode(String engageCode) {
        this.engageCode = engageCode;
    }

    public String getEngageName() {
        return engageName;
    }

    public void setEngageName(String engageName) {
        this.engageName = engageName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getDepositDeduction() {
        return depositDeduction;
    }

    public void setDepositDeduction(Long depositDeduction) {
        this.depositDeduction = depositDeduction;
    }

    public Long getEarnestDeduction() {
        return earnestDeduction;
    }

    public void setEarnestDeduction(Long earnestDeduction) {
        this.earnestDeduction = earnestDeduction;
    }

    public Long getTransferDeduction() {
        return transferDeduction;
    }

    public void setTransferDeduction(Long transferDeduction) {
        this.transferDeduction = transferDeduction;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Long getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Long paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Long getWaitAmount() {
        return waitAmount;
    }

    public void setWaitAmount(Long waitAmount) {
        this.waitAmount = waitAmount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Integer getRefundState() {
        return refundState;
    }

    public void setRefundState(Integer refundState) {
        this.refundState = refundState;
    }

    public Integer getIsRenew() {
        return isRenew;
    }

    public void setIsRenew(Integer isRenew) {
        this.isRenew = isRenew;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }
}