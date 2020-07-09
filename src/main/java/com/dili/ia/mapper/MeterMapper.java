package com.dili.ia.mapper;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ss.base.MyMapper;

import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的相关业务 dao 层
 */
public interface MeterMapper extends MyMapper<Meter> {

    /**
     * 根据表类型,获取未绑定的表编号集合(新增表用户关系页面回显)
     *
     * @param meterDto
     * @return meterList
     * @date   2020/6/16
     */
    List<Meter> listUnbindMetersByType(MeterDto meterDto);

    /**
     * 根据表类型 type、表编号 number 查询表信息(新增缴水电费时页面回显)
     *
     * @param  meter
     * @return meterList
     * @date   2020/6/28
     */
    Meter getMeterLikeNumber(Meter meter);
}