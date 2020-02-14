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
@Getter
@Setter
public class Customer extends BaseDomain {

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

}