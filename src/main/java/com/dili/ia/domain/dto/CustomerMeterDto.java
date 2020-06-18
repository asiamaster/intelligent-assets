package com.dili.ia.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ia.domain.CustomerMeter;
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
 * @description: 水电费 - 表和用户关系Dto
 */
public class CustomerMeterDto extends CustomerMeter {

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
     * 表
     */
    private Long meterId;

    /**
     * 客户 id
     */
    private Long customerId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户手机号
     */
    private String customerCellphone;

    /**
     * 创建人所属于部门ID
     */
    private Long creatorDepId;

    /**
     * 状态，已绑定，解除绑定
     */
    private Integer state;

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