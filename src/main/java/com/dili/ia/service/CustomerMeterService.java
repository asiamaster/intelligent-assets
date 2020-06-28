package com.dili.ia.service;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:35:07.
 */
public interface CustomerMeterService extends BaseService<CustomerMeter, Long> {

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @return:      BaseOutput
     * @description：根据主键 id 查询
     */
    CustomerMeterDto getMeterById(Long id);

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @return:      BaseOutput
     * @description：新增表用户关系
     */
    BaseOutput<CustomerMeter> addCustomerMeter(CustomerMeterDto customerMeterDto);

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @return:      BaseOutput
     * @description：修改表用户关系
     */
    BaseOutput<CustomerMeter> updateCustomerMeter(CustomerMeterDto customerMeterDto);

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @description：删除表用户关系
     */
    BaseOutput<CustomerMeter>  deleteCustomerMeter(Long id);

    /**
     * @author:      xiaosa
     * @date:        2020/6/17
     * @param        customerMeterDto
     * @description：分页根据条件查询列表
     */
    EasyuiPageOutput listCustomerMeters(CustomerMeterDto customerMeterDto, boolean useProvider) throws Exception;

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        type
     * @return       BaseOutput
     * @description：根据表类型获取未绑定的表编号
     */
    BaseOutput<Meter> getUnbindMeterByType(Long type);

    /**
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param        meterId
     * @return       BaseOutput
     * @description：根据表主键id获取表绑定的用户信息
     */
    BaseOutput<CustomerMeter> getBindInfoByMeterId(Long meterId);
}