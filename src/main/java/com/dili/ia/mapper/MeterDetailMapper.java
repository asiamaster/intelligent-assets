package com.dili.ia.mapper;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

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
     * 根据表 meterId、用户 customerId 查询未缴费记录的数量
     *
     * @param  meterDetailDto
     * @return List
     * @date   2020/6/29
     */
    List<Long> countUnPayByMeterAndCustomer(MeterDetailDto meterDetailDto);

    /**
     * 根据 meterId 查询最近的一次已交费的记录的 实际值/本期指数值
     *
     * @param  meterDetailDto
     * @return Long
     * @date   2020/6/30
     */
    Long getLastAmountByMeterId(MeterDetailDto meterDetailDto);

    /**
     * 根据主键 id 查询到水电费单详情以及联表查询表信息
     *
     * @param  id
     * @return MeterDetailDto
     * @date   2020/7/6
     */
    MeterDetailDto getMeterDetailDtoById(Long id);

    /**
     * 根据 code 查询实体
     * 
     * @param  id
     * @return MeterDetailDto
     * @date   2020/7/10
     */
    MeterDetailDto getMeterDetailByCode(Long id);

    /**
     * 根据 code 查询实体
     *
     * @param  meterDetailDto
     * @return List
     * @date   2020/7/10
     */
    List<MeterDetailDto> listMeterDetailByUnPayBusiness(MeterDetailDto meterDetailDto);

    /**
     * 查询所有未提交的水电费单
     *
     * @param  meterDetailQuery
     * @return List
     * @date   2020/7/29
     */
    List<MeterDetailDto> listByStateCreatedAndType(MeterDetailDto meterDetailQuery);

    /**
     * 根据主键 ids 查询到水电费单详情以及联表查询表信息
     *
     * @param  idList
     * @return List
     * @date   2020/12/1
     */
    List<MeterDetailDto> getMeterDetailDtoListByIds(@Param("idList") List<Long> idList);
}