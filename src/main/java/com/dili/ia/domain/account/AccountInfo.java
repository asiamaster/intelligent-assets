package com.dili.ia.domain.account;

import com.dili.ss.util.MoneyUtils;

import java.util.List;

/**
 * Created by chenzw on 2017/6/5.
 */
public class AccountInfo {
    /**市场id*/
    private Long firmId;
    /** 父卡账号 */
    private Long parentAccountId;
    /** 卡交易类型: 1-买家 2-卖家*/
    private Integer accountType;
    /**客户id*/
    private Long customerId;

    /** 使用权限(充值、提现、交费等)  */
    private List<String> permissionList;
    /** 卡ID */
    private Long cardId;
    /**账户id*/
    private Long accountId;
    /**
     * 卡面号
     */
    private String cardNo;

    /**卡账户用途 {*/
    private List<String> usageType;
    /** 卡类型-主/副/临时/联营*/
    private Integer cardType;
    /** 卡片状态 */
    private Integer cardState;
    /**账户状态 */
    private Integer accountState;

    /**
     * 支付系统资金账号
     */
    private Long fundAccountId;
    private String name;
    //与name同义，name引用太多了。
    private String customerName;
    private String mobile;
    //卡务
    private String customerContactsPhone;

    /**
     * 客户类型
     */
    private String customerType;
    private String customerTypeView;
    private String customerMarketType;
    /**
     * 身份证号
     */
    private String idCode;
    /**
     * 与idCode同义
     */
    private String customerCertificateNumber;
    /**
     * 证件类型
     */
    private String customerCertificateType;
    /**
     * 客户编号
     */
    private String customerCode;

    private Long balance;
    private Long frozenAmount;
    private String balanceSymbol;
    private Integer gender;
    private String pwd;

    public String getCustomerMarketType() {
        return customerMarketType;
    }

    public void setCustomerMarketType(String customerMarketType) {
        this.customerMarketType = customerMarketType;
    }

    public String getCustomerType() {
        return getCustomerMarketType();
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerTypeView() {
        return customerTypeView;
    }

    public void setCustomerTypeView(String customerTypeView) {
        this.customerTypeView = customerTypeView;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }



    public String getCustomerCertificateNumber() {
        return customerCertificateNumber;
    }

    public void setCustomerCertificateNumber(String customerCertificateNumber) {
        this.customerCertificateNumber = customerCertificateNumber;
    }

    public String getCustomerCertificateType() {
        return customerCertificateType;
    }

    public void setCustomerCertificateType(String customerCertificateType) {
        this.customerCertificateType = customerCertificateType;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBalanceSymbol() {
        balanceSymbol= MoneyUtils.centToYuan(balance);
        return balanceSymbol;
    }

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    public Long getParentAccountId() {
        return parentAccountId;
    }

    public void setParentAccountId(Long parentAccountId) {
        this.parentAccountId = parentAccountId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public List<String> getUsageType() {
        return usageType;
    }

    public void setUsageType(List<String> usageType) {
        this.usageType = usageType;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Integer getCardState() {
        return cardState;
    }

    public void setCardState(Integer cardState) {
        this.cardState = cardState;
    }

    public Integer getAccountState() {
        return accountState;
    }

    public void setAccountState(Integer accountState) {
        this.accountState = accountState;
    }

    public Long getFundAccountId() {
        return fundAccountId;
    }

    public void setFundAccountId(Long fundAccountId) {
        this.fundAccountId = fundAccountId;
    }

    public String getName() {
        return getCustomerName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return getCustomerContactsPhone();
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCode() {
        return getCustomerCertificateNumber();
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCustomerContactsPhone() {
		return customerContactsPhone;
	}

	public void setCustomerContactsPhone(String customerContactsPhone) {
		this.customerContactsPhone = customerContactsPhone;
	}
}
