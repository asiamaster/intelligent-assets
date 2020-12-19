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
    MeterDetailDto getMeterDetailDtoById(Long id);

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
     * 新增水电费业务单
     *
     * @param  meterDetailDto
     * @param  userTicket
     * @return MeterDetailDto
     * @date   2020/6/28
     */
    MeterDetailDto addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket);

    /**
     * 修改水电费业务单
     *
     * @param  meterDetailDto
     * @return MeterDetailDto
     * @date   2020/7/1
     */
    MeterDetailDto updateMeterDetail(MeterDetailDto meterDetailDto);

    /**
     * 提交水电费业务单(生成缴费单和结算单)
     *
     * @param  idList
     * @return List
     * @date   2020/7/6
     */
    List<MeterDetailDto> submit(List<Long> idList, UserTicket userTicket);

    /**
     * 全部提交水电费业务单(生成缴费单和结算单)
     *
     * @param  userTicket
     * @param  metertype
     * @return List
     * @date   2020/7/29
     */
    List<MeterDetailDto> submitAll(UserTicket userTicket, Integer metertype);

    /**
     * 撤回水电费业务单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @param  userTicket
     * @return MeterDetailDto
     * @date   2020/7/6
     */
    MeterDetailDto withdraw(Long id, UserTicket userTicket);

    /**
     * 取消水电费业务单
     *
     * @param  id
     * @param  userTicket
     * @return MeterDetailDto
     * @date   2020/7/6
     */
    MeterDetailDto cancel(Long id, UserTicket userTicket);

    /**
     * 根据 meterId 获取初始值
     *
     * @param  meterId
     * @return 初始值(上期指数)
     * @date   2020/6/29
     */
    BaseOutput getLastAmount(Long meterId);

    /**
     * 根据 meterId、customerId 查询未缴费单的数量
     *
     * @param  meterId
     * @param  customerId
     * @return count
     * @date   2020/6/28
     */
    Integer countUnPayByMeterAndCustomer(Long meterId, Long customerId);

    /**
     * 缴费回调
     *
     * @param  settleOrder
     * @return MeterDetail
     * @date   2020/7/6
     */
    MeterDetailDto settlementDealHandler(SettleOrder settleOrder);

    /**
     * 票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/7/10
     */
    PrintDataDto<MeterDetailPrintDto> receiptPaymentData(String orderCode, Integer reprint);

    /**
     * 计费规则
     *
     * @param  meterDetailDto
     * @return list
     * @date   2020/7/17
     */
    List<QueryFeeOutput> getCost(MeterDetailDto meterDetailDto);
}