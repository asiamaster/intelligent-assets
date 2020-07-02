package com.dili.ia.service;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
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
     * 根据主键 id 查看详情
     *
     * @param  id
     * @return MeterDetail
     * @date   2020/7/1
     */
    MeterDetail getMeterDetailById(Long id);

    /**
     * 查询水电费单的集合(分页)
     *
     * @param  meterDetailDto
     * @param  useProvider
     * @return MeterDetailDtoList
     * @date   2020/6/28
     */
    EasyuiPageOutput listMeterDetails(MeterDetailDto meterDetailDto, boolean useProvider) throws Exception;

    /**
     * 新增水电费单
     *
     * @param  meterDetailDto
     * @param  userTicket 用户信息
     * @return 是否成功
     * @date   2020/6/28
     */
    BaseOutput addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket);

    /**
     * 修改 水电费单
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/7/1
     */
    BaseOutput updateMeterDetail(MeterDetailDto meterDetailDto);

    /**
     * 根据 meterId、customerId 查询未缴费单的数量
     *
     * @param  meterId
     * @param  customerId
     * @return 未缴费单的数量
     * @date   2020/6/28
     */
    Integer countUnPayByMeterAndCustomer(Long meterId, Long customerId);

    /**
     * 根据 meterId 获取初始值
     *
     * @param  meterId
     * @return 初始值(上期指数)
     * @date   2020/6/29
     */
    BaseOutput getLastAmount(Long meterId);


}