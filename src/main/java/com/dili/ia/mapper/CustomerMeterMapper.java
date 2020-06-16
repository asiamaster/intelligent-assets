package com.dili.ia.mapper;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ss.base.MyMapper;

public interface CustomerMeterMapper extends MyMapper<CustomerMeter> {

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @return:      BaseOutput
     * @description：根据主键 id 查询
     */
    CustomerMeter getMeterById(Long id);
}