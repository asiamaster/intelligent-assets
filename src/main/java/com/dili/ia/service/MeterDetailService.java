package com.dili.ia.service;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.MeterDetailPrintDto;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
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
    MeterDetailDto getMeterDetailById(Long id);

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
    BaseOutput<MeterDetail> addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket) throws Exception;

    /**
     * 修改 水电费单
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/7/1
     */
    BaseOutput<MeterDetail> updateMeterDetail(MeterDetailDto meterDetailDto);

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

    /**
     * 提交水电费单(生缴费单和结算单)
     *
     * @param  id
     * @return 是否成功
     * @date   2020/7/6
     */
    BaseOutput<List<MeterDetail>> submit(List<Long> id, UserTicket userTicket) throws Exception;

    /**
     * 全部提交水电费单(生缴费单和结算单)
     *
     * @param
     * @param metertype
     * @return 是否成功
     * @date   2020/7/29
     */
    BaseOutput<List<MeterDetailDto>> submitAll(UserTicket userTicket, Integer metertype) throws Exception;

    /**
     * 缴费回调
     *
     * @param  settleOrder
     * @return
     * @date   2020/7/6
     */
    BaseOutput<MeterDetail> settlementDealHandler(SettleOrder settleOrder) throws Exception;

    /**
     * 撤回水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @return 是否成功
     * @date   2020/7/6
     */
    BaseOutput<MeterDetail> withdraw(Long id, UserTicket userTicket) throws Exception;

    /**
     * 取消水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @return 是否成功
     * @date   2020/7/6
     */
    BaseOutput<MeterDetail> cancel(Long id, UserTicket userTicket);

    /**
     * 票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return
     * @date   2020/7/10
     */
    PrintDataDto<MeterDetailPrintDto> receiptPaymentData(String orderCode, Integer reprint) throws Exception;

    /**
     * 计费规则
     *
     * @param   meterDetailDto
     * @return  list
     * @date   2020/7/17
     */
    List<QueryFeeOutput> getCost(MeterDetailDto meterDetailDto);
}