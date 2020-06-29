package com.dili.ia.service;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * @author:      xiaosa
 * @date:        2020/6/23
 * @version:     农批业务系统重构
 * @description: 水电费 service 层接口
 */
public interface MeterDetailService extends BaseService<MeterDetail, Long> {

    /**
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param:       meterDetail
     * @return:      EasyuiPageOutput
     * @description：查询缴水电费的列表
     */
    EasyuiPageOutput listMeterDetails(MeterDetailDto meterDetailDto, boolean b) throws Exception;

    /**
     * @author:       xiaosa
     * @date:         2020/6/28
     * @param:        
     * @return:       
     * @description： 新增水电费单
     */
    void addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket);


    /**
     * @author:       xiaosa
     * @date:         2020/6/29
     * @param:        meterId, customerId
     * @return:       Integer
     * @description： 根据 meterId、customerId 查询未缴费单的数量
     */
    Integer countUnPayByMeterAndCustomer(Long meterId, Long customerId);
}