package com.dili.ia.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 客户基础数据
企业客户没有性别和民族和certificate_time，但有certificate_rang
 * This file was generated on 2020-01-09 17:36:22.
 */
public class Customer extends BaseDomain implements Cloneable {
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private Long id;

    /**
     * 客户编号
     */
    private String code;

    /**
     * 证件号
     */
    private String certificateNumber;

    /**
     * 证件类型
     */
    private String certificateType;

    /**
     * 证件日期##企业时为营业执照日期,如:2011-09-01 至 长期
     */
    private String certificateRange;

    /**
     * 证件地址
     */
    private String certificateAddr;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 出生日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate birthdate;

    /**
     * 性别:男,女
     */
    private Integer gender;

    /**
     * 照片
     */
    private String photo;

    /**
     * 手机号
     */
    private String cellphone;

    /**
     * 联系电话
     */
    private String contactsPhone;

    /**
     * 组织类型,个人/企业
     */
    private String organizationType;

    /**
     * 来源系统##外部系统来源标识
     */
    private String sourceSystem;

    /**
     * 客户行业##水果批发/蔬菜批发/超市
     */
    private String profession;

    /**
     * 经营地区##经营地区城市id
     */
    private String operatingArea;

    /**
     * 经营地区经度
     */
    private String operatingLng;

    /**
     * 经营地区纬度
     */
    private String operatingLat;

    /**
     * 其它头衔
     */
    private String otherTitle;

    /**
     * 主营品类
     */

    private String mainCategory;

    /**
     * 注册资金##企业客户属性
     */
    private Long registeredCapital;

    /**
     * 企业员工数
     */
    private String employeeNumber;

    /**
     * 证件类型
     */
    private String corporationCertificateType;

    /**
     * 法人证件号
     */
    private String corporationCertificateNumber;

    /**
     * 法人真实姓名
     */
    private String corporationName;

    /**
     * 手机号是否验证
     */
    private Integer isCellphoneValid;

    /**
     * 创建人
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 是否可用
     */
    private Integer isDelete;

    /**
     * 客户状态 0注销，1生效，2禁用，
     */
    private Integer state;

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

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateRange() {
        return certificateRange;
    }

    public void setCertificateRange(String certificateRange) {
        this.certificateRange = certificateRange;
    }

    public String getCertificateAddr() {
        return certificateAddr;
    }

    public void setCertificateAddr(String certificateAddr) {
        this.certificateAddr = certificateAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOperatingArea() {
        return operatingArea;
    }

    public void setOperatingArea(String operatingArea) {
        this.operatingArea = operatingArea;
    }

    public String getOperatingLng() {
        return operatingLng;
    }

    public void setOperatingLng(String operatingLng) {
        this.operatingLng = operatingLng;
    }

    public String getOperatingLat() {
        return operatingLat;
    }

    public void setOperatingLat(String operatingLat) {
        this.operatingLat = operatingLat;
    }

    public String getOtherTitle() {
        return otherTitle;
    }

    public void setOtherTitle(String otherTitle) {
        this.otherTitle = otherTitle;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public Long getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(Long registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getCorporationCertificateType() {
        return corporationCertificateType;
    }

    public void setCorporationCertificateType(String corporationCertificateType) {
        this.corporationCertificateType = corporationCertificateType;
    }

    public String getCorporationCertificateNumber() {
        return corporationCertificateNumber;
    }

    public void setCorporationCertificateNumber(String corporationCertificateNumber) {
        this.corporationCertificateNumber = corporationCertificateNumber;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public Integer getIsCellphoneValid() {
        return isCellphoneValid;
    }

    public void setIsCellphoneValid(Integer isCellphoneValid) {
        this.isCellphoneValid = isCellphoneValid;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}