package com.dili.ia.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ia.domain.Meter;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的Dto
 */
public class MeterDto extends Meter {

    /**
     * 创建日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 修改日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTime;

    /**
     * 表编号
     */
    private String number;

    /**
     * 表类型
     */
    private Long type;

    /**
     * 部门
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 资产类型code
     */
    private String assetsType;

    /**
     * 对应编号ID
     */
    private Long assetsId;

    /**
     * 对应编号,名称
     */
    private String assetsName;

    /**
     * 表初始值
     */
    private Long initAmount;

    /**
     * 单价
     */
    private Long price;

    /**
     * 创建人所属于部门ID
     */
    private Long creatorDepId;

    /**
     * 水电预存余额
     */
    private Long balance;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建操作员ID
     */
    private Long creatorId;

    /**
     * 创建人名称
     */
    private String creator;

    /**
     * 市场Id
     */
    private Long marketId;

    /**
     * 市场CODE
     */
    private String marketCode;

    /**
     * 版本控制,乐观锁
     */
    private Integer version;
}