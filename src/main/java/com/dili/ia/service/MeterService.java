package com.dili.ia.service;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

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
     * @return 是否成功
     * @date   2020/6/16
     */
    BaseOutput<Meter> addMeter(MeterDto meterDto);

    /**
     * 修改表信息
     *
     * @param  meterDto
     * @return 是否成功
     * @date   2020/6/29
     */
    BaseOutput<Meter> updateMeter(MeterDto meterDto);

    /**
     * 根据表类型,获取未绑定的表编号集合(新增表用户关系页面回显)
     *
     * @param  type 表类型,有枚举 meterTypeEnum
     * @param likeName
     * @return meterList
     * @date   2020/6/16
     */
    List<Meter> listUnbindMetersByType(Integer type, String likeName);

    /**
     * 根据表类型、表编号查询表信息(新增缴水电费时页面回显)
     *
     * @param  type   表类型,有枚举 meterTypeEnum
     * @param  likeName 表编号
     * @return meterList
     * @date   2020/6/28
     */
    List<Meter> listMetersLikeNumber(Integer type, String likeName);
}