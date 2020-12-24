package com.dili.ia.mapper;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 表管理 dao 层
 */
public interface MeterMapper extends MyMapper<Meter> {

    /**
     * 根据表类型，获取未绑定的表编号集合(新增表用户关系页面回显)
     *
     * @param  meterDto
     * @return meterList
     * @date   2020/6/16
     */
    List<Meter> listUnbindMetersByType(MeterDto meterDto);

    /**
     * 根据表类型type和表编号number 查询表信息(新增缴水电费时页面回显)
     *
     * @param  meterDto
     * @return meterList
     * @date   2020/6/28
     */
    List<Meter> listMetersLikeNumber(MeterDto meterDto);

    /**
     * 根据 number 查询实体
     *
     * @param  number
     * @return meter
     * @date   2020/7/14
     */
    Meter getMeterByNumber(String number);

    /**
     * 根据主键查询表信息以及表用户中的身份证号
     *
     * @param  id
     * @return meterDto
     * @date   2020/12/18
     */
    MeterDto getMeterDtoById(@Param("id") Long id, @Param("state")Integer state);
}