package com.dili.ia.service;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ss.base.BaseService;
import com.dili.uap.sdk.domain.UserTicket;

import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的相关业务 service 层
 */
public interface MeterService extends BaseService<Meter, Long> {

    /**
     * 新增表信息
     *
     * @param  meterDto
     * @param userTicket
     * @return Meter
     * @date   2020/6/16
     */
    Meter addMeter(MeterDto meterDto, UserTicket userTicket);

    /**
     * 修改表信息
     *
     * @param  meterDto
     * @param userTicket
     * @return Meter
     * @date   2020/6/29
     */
    Meter updateMeter(MeterDto meterDto, UserTicket userTicket);

    /**
     * 根据表类型,获取未绑定的表编号集合(新增表用户关系页面回显)
     *
     * @param  type 表类型,有枚举 meterTypeEnum
     * @param  likeName
     * @return meterList
     * @date   2020/6/16
     */
    List<Meter> listUnbindMetersByType(Integer type, String likeName);

    /**
     * 根据 number 查询实体
     * 
     * @param  number
     * @return Meter
     * @date   2020/7/14
     */
    Meter getMeterByNumber(String number);

    /**
     * 根据主键查询表信息以及表用户中的身份证号
     *
     * @param
     * @return
     * @date   2020/12/18
     */
    MeterDto getMeterDtoById(Long meterId);
}