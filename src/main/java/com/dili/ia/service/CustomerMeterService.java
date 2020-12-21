package com.dili.ia.service;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.sdk.domain.UserTicket;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/6/12
 * @version:      农批业务系统重构
 * @description:  表用户关系 service 层
 */
public interface CustomerMeterService extends BaseService<CustomerMeter, Long> {

    /**
     * 根据主键 id 查询表用户关系
     *
     * @param  id
     * @return CustomerMeterDto
     * @date   2020/6/29
     */
    CustomerMeterDto getMeterById(Long id);

    /**
     * 新增表用户关系
     *
     * @param  customerMeterDto
     * @param  userTicket
     * @return CustomerMeter
     * @date   2020/6/16
     */
    CustomerMeter addCustomerMeter(CustomerMeterDto customerMeterDto, UserTicket userTicket);

    /**
     * 修改表用户关系(解绑)
     *
     * @param  id
     * @return CustomerMeter
     * @date   2020/6/16
     */
    CustomerMeter updateCustomerMeter(Long id);

    /**
     * 查询表用户关系的集合(分页)
     *
     * @param  customerMeterDto
     * @param  useProvider
     * @return EasyuiPageOutput
     * @date   2020/6/17
     */
    EasyuiPageOutput listCustomerMeters(CustomerMeterDto customerMeterDto, boolean useProvider) throws Exception;

    /**
     * 根据表主键 meterId 获取表绑定的用户信息
     * 
     * @param  meterId
     * @return CustomerMeterDto
     * @date   2020/6/28
     */
    CustomerMeterDto getBindInfoByMeterId(Long meterId);

    /**
     * 根据表编号模糊查询表客户信息列表
     *
     *
     * @param  type
     * @param  keyword
     * @return List
     * @date   2020/7/10
     */
    List<CustomerMeterDto> listCustomerMetersByLikeName(Integer type, String keyword);

    /**
     * 根据表地址查询是否处于租期状态和相应的用户
     *
     * @param  assetsId
     * @param  assetsType
     * @return CustomerMeterDto
     * @date   2020/12/10
     */
    CustomerMeterDto getCustomerByAssetsIdAndAssetsType(UserTicket code, Long assetsId, Integer assetsType);
}