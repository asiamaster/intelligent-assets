package com.dili.ia.mapper;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ss.base.MyMapper;

import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/23
 * @version:     农批业务系统重构
 * @description: 水电费 dao 层
 */
public interface MeterDetailMapper extends MyMapper<MeterDetail> {

    /**
     * meter、meterDetail 两表查询水电费单集合(分页)
     *
     * @param  meterDetailDto
     * @return MeterDetailDtoList
     * @date   2020/6/28
     */
    List<MeterDetailDto> listMeterDetails(MeterDetailDto meterDetailDto);

    /**
     * 根据表 meterId、用户 customerId 查询未缴费单的数量
     *
     * @param  meterDetailDto
     * @return 未缴费单的数量
     * @date   2020/6/29
     */
    List<Long> countUnPayByMeterAndCustomer(MeterDetailDto meterDetailDto);
}