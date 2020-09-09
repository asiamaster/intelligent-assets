package com.dili.ia.service;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.domain.dto.printDto.MeterDetailPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;

import java.util.List;

/**
 * @author: xiaosa
 * @date: 2020/6/23
 * @version: 农批业务系统重构
 * @description: 水电费 service 层接口
 */
public interface MeterDetailService extends BaseService<MeterDetail, Long> {

    /**
     * 根据主键 id 查看详情
     *
     * @param id
     * @return MeterDetail
     * @date 2020/7/1
     */
    MeterDetailDto getMeterDetailById(Long id);

    /**
     * 查询水电费单的集合(分页)
     *
     * @param meterDetailDto
     * @param useProvider
     * @return MeterDetailDtoList
     * @date 2020/6/28
     */
    EasyuiPageOutput listMeterDetails(MeterDetailDto meterDetailDto, boolean useProvider) throws Exception;

    /**
     * 新增水电费单
     *
     * @param  meterDetailDto
     * @param  userTicket
     * @return MeterDetail
     * @date   2020/6/28
     */
    MeterDetail addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket) throws BusinessException;

    /**
     * 修改 水电费单
     *
     * @param  meterDetailDto
     * @return MeterDetail
     * @date   2020/7/1
     */
    MeterDetail updateMeterDetail(MeterDetailDto meterDetailDto) throws BusinessException;

    /**
     * 根据 meterId、customerId 查询未缴费单的数量
     *
     * @param meterId
     * @param customerId
     * @return 未缴费单的数量
     * @date 2020/6/28
     */
    Integer countUnPayByMeterAndCustomer(Long meterId, Long customerId);

    /**
     * 根据 meterId 获取初始值
     *
     * @param  meterId
     * @return 初始值(上期指数)
     * @date  2020/6/29
     */
    BaseOutput getLastAmount(Long meterId);

    /**
     * 提交水电费单(生缴费单和结算单)
     *
     * @param  idList
     * @return List
     * @date   2020/7/6
     */
    List<MeterDetail> submit(List<Long> idList, UserTicket userTicket) throws BusinessException;

    /**
     * 全部提交水电费单(生缴费单和结算单)
     *
     * @param  userTicket
     * @param  metertype
     * @return List
     * @date   2020/7/29
     */
    List<MeterDetailDto> submitAll(UserTicket userTicket, Integer metertype) throws BusinessException;

    /**
     * 撤回水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @param  userTicket
     * @return MeterDetail
     * @date   2020/7/6
     */
    MeterDetail withdraw(Long id, UserTicket userTicket) throws BusinessException;

    /**
     * 缴费回调
     *
     * @param  settleOrder
     * @return MeterDetail
     * @date   2020/7/6
     */
    MeterDetail settlementDealHandler(SettleOrder settleOrder) throws BusinessException;

    /**
     * 取消水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @param  userTicket
     * @return MeterDetail
     * @date   2020/7/6
     */
    MeterDetail cancel(Long id, UserTicket userTicket) throws BusinessException;

    /**
     * 票据打印
     *
     * @param orderCode
     * @param reprint
     * @return
     * @date 2020/7/10
     */
    PrintDataDto<MeterDetailPrintDto> receiptPaymentData(String orderCode, Integer reprint) throws BusinessException;

    /**
     * 计费规则
     *
     * @param meterDetailDto
     * @return list
     * @date 2020/7/17
     */
    List<QueryFeeOutput> getCost(MeterDetailDto meterDetailDto);
}