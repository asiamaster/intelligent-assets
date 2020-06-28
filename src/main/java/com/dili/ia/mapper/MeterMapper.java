package com.dili.ia.mapper;

import com.dili.ia.domain.Meter;
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
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param        meter
     * @return       String
     * @description：根据表类型、表编号查询表信息
     */
    Meter getMeterLikeNumber(Meter meter);
}