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
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param:       meterDetailDto.type,
     * @return:
     * @description：根据条件查询缴费信息、表信息
     */
    List<MeterDetailDto> listMeterDetails(MeterDetailDto meterDetailDto);
}