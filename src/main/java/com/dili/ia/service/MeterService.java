package com.dili.ia.service;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的相关业务 service 层
 */
public interface MeterService extends BaseService<Meter, Long> {


    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:
     * @return:      
     * @description：
     */
    void downMeterList(MeterDto meterDto);

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       meterDto
     * @return:      BaseOutput
     * @description：新增表信息
     */
    BaseOutput<Meter> addMeter(MeterDto meterDto);

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       meterDto
     * @return:      BaseOutput
     * @description：修改表信息
     */
    BaseOutput<Meter> updateMeter(MeterDto meterDto);

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        type
     * @return       BaseOutput
     * @description：根据表类型获取未绑定的表编号
     */
    BaseOutput<Meter> listUnbindMetersByType(Integer type);

    /**
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param        type, name
     * @return       String
     * @description：根据表类型、表编号查询表信息
     */
    BaseOutput getMeterLikeNumber(Integer type, String name);
}