package com.dili.ia.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 摊位到期报表
 */
@Data
public class StallExpirationReport extends BaseDomain {

    //从多少天，到多少天，查询使用
    private Integer beginDays;

    //从多少天，到多少天，查询使用
    private Integer endDays;

    //订单项id
    private Long orderItemId;

    //摊位名称
    private String assetsName;

    //第一区id
    private String firstDistrictId;

    //第一区域
    private String firstDistrictName;

    //第二区id
    private String secDistrictId;

    //第二区域
    private String secDistrictName;

    //数量
    private String number;

    //单位
    private String unit;

    //是否转角
    private Integer corner;

    //客户id
    private Long customerId;

    //客户姓名
    private String customerName;

    //客户证件号码
    private String certificateNumber;

    //客户电话号码
    private String customerCellphone;

    //开始时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    //租赁天数
    private Integer days;

    //结束时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    //保证金应交
    private String amount;

    //保证金已交
    private String balance;

    //订单金额
    private Double totalAmount;

    //订单已交金额
    private Double paidAmount;

    //退款状态
    private Integer refundState;

    //停租时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime stopTime;

    //订单项状态
    private Integer state;

    //查询时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date searchDate;
}
