package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.MeterDetailStateEnum;
import com.dili.ia.glossary.MeterTypeEnum;
import com.dili.ia.glossary.PayStateEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.mapper.MeterDetailMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.service.MeterService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.BeanConver;
import com.dili.uap.sdk.domain.UserTicket;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:      xiaosa
 * @date:        2020/6/23
 * @version:     农批业务系统重构
 * @description: 水电费 service 实现层
 */
@Service
public class MeterDetailServiceImpl extends BaseServiceImpl<MeterDetail, Long> implements MeterDetailService {

    private final static Logger logger = LoggerFactory.getLogger(MeterDetailServiceImpl.class);

    public MeterDetailMapper getActualDao() {
        return (MeterDetailMapper)getDao();
    }

    @Autowired
    private MeterService meterService;

    @Autowired
    private UidRpcResolver UidRpcResolver;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private CustomerMeterService customerMeterService;

    /**
     * 根据主键 id 查看详情
     *
     * @param  id
     * @return MeterDetail
     * @date   2020/7/1
     */
    @Override
    public MeterDetail getMeterDetailById(Long id) {







        return null;
    }

    /**
     * meter、meterDetail 两表查询水电费单集合(分页)
     *
     * @param  meterDetailDto
     * @param  useProvider
     * @return MeterDetailDtoList
     * @date   2020/6/28
     */
    @Override
    public EasyuiPageOutput listMeterDetails(MeterDetailDto meterDetailDto, boolean useProvider) throws Exception {
        // 查询列表时必须表类型不能为空
        Integer type = meterDetailDto.getType();
        if (type == null) {
            logger.info("查询缴费信息表类型为空，报错！");
        }
        // 分页
        if (meterDetailDto.getRows() != null && meterDetailDto.getRows() >= 1) {
            PageHelper.startPage(meterDetailDto.getPage(), meterDetailDto.getRows());
        }

        // 联表查询表编号、部门等
        List<MeterDetailDto> meterDetailDtooList= this.getActualDao().listMeterDetails(meterDetailDto);

        // 基础代码
        long total = meterDetailDtooList instanceof Page ? ((Page)meterDetailDtooList).getTotal() : (long)meterDetailDtooList.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(meterDetailDto, meterDetailDtooList) : meterDetailDtooList;
        return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);
    }

    /**
     * 新增水电费单
     *
     * @param  meterDetailDto
     * @param  userTicket 用户信息
     * @return 是否成功
     * @date   2020/6/28
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket) {
        MeterDetail meterDetail = new MeterDetail();

        // 根据 meterId 查询是否有未提交、已提交的缴费记录(某月份)
        List<MeterDetailDto> meterDetailDtoList = this.listUnPayUnSubmitByMeter(meterDetailDto.getMeterId(), meterDetailDto.getUsageTime());
        if (CollectionUtils.isNotEmpty(meterDetailDtoList)) {
            return BaseOutput.failure("该表存在未交费单据，无法保存！");
        }

        //在更新状态之前查询指数信息，不然可能会查询出当前数据(脏读)
        BaseOutput lastAmountReturn = this.getLastAmount(meterDetailDto.getMeterId());
        if (!lastAmountReturn.isSuccess()){
            return BaseOutput.failure("该表初始指数获取失败,保存失败!");
        }
        Long lastAmount = (Long) lastAmountReturn.getData();

        // 生成水电费单号的 code
        String meterDetailCode = UidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.METER_DETAIL_CODE.getCode());
        // 新增 缴水电费单 数据
        meterDetail.setCode(meterDetailCode);
        meterDetail.setCreatorId(userTicket.getId());
        meterDetail.setCreator(userTicket.getRealName());
        meterDetail.setCreatorDepId(userTicket.getDepartmentId());
        meterDetail.setMarketId(userTicket.getFirmId());
        meterDetail.setMarketCode(userTicket.getFirmCode());
        meterDetail.setState(MeterDetailStateEnum.UNSUBMITED.getCode());

        this.getActualDao().insertSelective(meterDetail);

        // 新增 缴费单 数据
        PaymentOrder meterPaymentOrder = DTOUtils.newInstance(PaymentOrder.class);
        // 生成 缴费单 的 code
        String paymentOrderCode = UidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.PAYMENT_ORDER.getCode());
        meterPaymentOrder.setCode(paymentOrderCode);
        meterPaymentOrder.setCreateTime(new Date());
        meterPaymentOrder.setModifyTime(new Date());
        // TODO
        meterPaymentOrder.setBusinessId(1L);
        meterPaymentOrder.setBusinessCode(meterDetailCode);
        meterPaymentOrder.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        meterPaymentOrder.setIsSettle(PayStateEnum.NOT_PAID.getCode());
        // TODO
        meterPaymentOrder.setAmount(1L);
        meterPaymentOrder.setCreatorId(userTicket.getId());
        meterPaymentOrder.setCreator(userTicket.getRealName());
        meterPaymentOrder.setMarketId(userTicket.getFirmId());
        meterPaymentOrder.setMarketCode(userTicket.getFirmCode());
        meterPaymentOrder.setPayedTime(new Date());
        // 水表
        meterPaymentOrder.setBizType(BizTypeEnum.WATER_METER.getCode());
        if(MeterTypeEnum.ELECTRIC_METER.getCode().equals(meterDetailDto.getType())) {
            // 电表
            meterPaymentOrder.setBizType(BizTypeEnum.ELECTRIC_METER.getCode());
        }

        // 有公摊费, 则有多个缴费单
        List<PaymentOrder> saveList = Lists.newArrayList();
        // 如果有公摊费,设置公摊费信息
        Long sharedAmount = meterDetailDto.getSharedAmount();
        if (sharedAmount != null) {
            // TODO
            //如果有公摊费，水电费的应收金额则需减去公摊费(前端页面)
            //设置公摊费的相关信息
            PaymentOrder sharedPaymentOrder = BeanConver.copyBean(meterPaymentOrder, PaymentOrder.class);
            sharedPaymentOrder.setAmount(sharedAmount);
            saveList.add(sharedPaymentOrder);
        }
        saveList.add(meterPaymentOrder);

        paymentOrderService.batchInsert(saveList);

        CustomerMeter customerMeter = customerMeterService.getBindInfoByMeterId(meterDetail.getMeterId());
        if (null == customerMeter || !customerMeter.getCustomerId().equals(meterDetail.getCustomerId())) {
            //已被解绑或删除
            BaseOutput.failure("表已被解绑或删除，请刷新数据后重试!");
        }
        Boolean isEquals = meterDetailDto.getLastAmount().equals(lastAmount);
        if (!isEquals){
            BaseOutput.failure("上期指数已发生变化,请修改后重新提交!");
        }

        return BaseOutput.success("新增成功");
    }

    /**
     * 修改 水电费单
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/7/1
     */
    @Override
    public BaseOutput updateMeterDetail(MeterDetailDto meterDetailDto) {
        BaseOutput baseOutput = new BaseOutput();
        
        // 先查询
        MeterDetail meterDetailInfo = this.get(meterDetailDto.getId());
        if (meterDetailInfo == null) {
            return BaseOutput.failure("该记录已删除，修改失败。");
        }

        //在更新状态之前查询指数信息，不然可能会查询出当前数据(脏读)
        BaseOutput lastAmountReturn = this.getLastAmount(meterDetailDto.getMeterId());
        if (!lastAmountReturn.isSuccess()){
            return BaseOutput.failure("该表初始指数获取失败,保存失败!");
        }
        Long lastAmount = (Long) lastAmountReturn.getData();




        return null;
    }

    /**
     * 根据 meterId 查询是否有未提交、已提交的缴费记录(某月份)
     * 
     * @param  meterId
     * @param usageTime
     * @return 缴费集合
     * @date   2020/6/30
     */
    private List<MeterDetailDto> listUnPayUnSubmitByMeter(Long meterId, Date usageTime) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();

        // 获取使用月份的第一天和最后一天时间,用于数据库查询
        Map dateTimeMap = this.getStartTimeAndEndTime(usageTime);

        //未提交,已提交
        StringBuilder statusBuff = new StringBuilder("");
        statusBuff.append(PaymentOrderStateEnum.NOT_PAID.getCode()).append(",");
        statusBuff.append(PaymentOrderStateEnum.PAID.getCode());

        // 业务类型(水表、电表)
        List<Integer> bizTypeList = Lists.newArrayList(BizTypeEnum.WATER_METER.getCode(), BizTypeEnum.ELECTRIC_METER.getCode());
        String bizTypes = bizTypeList.toString().replace("[", "").replace("]", "");

        // 设置查询参数
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setBizTypes(bizTypes);
        meterDetailDto.setStatus(statusBuff.toString());
        meterDetailDto.setStartTime((Date) dateTimeMap.get("startTime"));
        meterDetailDto.setEndTime((Date) dateTimeMap.get("endTime"));
        List<MeterDetailDto> meterDetailDtoList = this.getActualDao().listUnPayUnSubmitByMeter(meterDetailDto);

        return meterDetailDtoList;
    }

    /**
     * 根据传入月份,获取到该月份的第一天0时0分0秒和最后一天的23时59分59秒,用于数据库查询
     *
     * @param  usageTime
     * @return map
     * @date   2020/6/30
     */
    private Map getStartTimeAndEndTime(Date usageTime) {
        Map dateTimeMap = new HashMap();
        if (StringUtils.isBlank(usageTime.toString())){
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(usageTime);
        // 将开始时间设置为第一天的0时0分0秒
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        Date startTime = c.getTime();

        // 将结束时间设置为最后一天的23时59分59秒
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        Date endTime = c.getTime();

        dateTimeMap.put("startTime", startTime);
        dateTimeMap.put("endTime", endTime);

        return dateTimeMap;
    }

    /**
     * 根据表 meterId、用户 customerId 查询未缴费的记录数量
     *
     *
     * @param  meterId
     * @param  customerId
     * @return 未缴费记录的数量
     * @date   2020/6/29
     */
    @Override
    public Integer countUnPayByMeterAndCustomer(Long meterId, Long customerId) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();

        // 业务类型(水表、电表)
        List<Integer> bizTypeList = Lists.newArrayList(BizTypeEnum.WATER_METER.getCode(), BizTypeEnum.ELECTRIC_METER.getCode());
        String bizTypes = bizTypeList.toString().replace("[", "").replace("]", "");
        meterDetailDto.setBizTypes(bizTypes);
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setCustomerId(customerId);
        List<Long> list = this.getActualDao().countUnPayByMeterAndCustomer(meterDetailDto);
        if (CollectionUtils.isEmpty(list)){
            return 0;
        }

        return list.size();
    }

    /**
     * 根据 meterId 获取初始值
     *
     * @param  meterId
     * @return 初始值(上期指数)
     * @date   2020/6/29
     */
    @Override
    public BaseOutput getLastAmount(Long meterId) {
        BaseOutput baseOutput = new BaseOutput();

        // 先查询表信息是否存在
        Meter meterInfo = meterService.get(meterId);
        if (meterInfo == null) {
            BaseOutput.failure("表信息不存在。");
        }

        // 查询缴费历史中,已缴费的最新数据
        Long lastAmount = this.getLastAmountByMeterId(meterId);
        if (lastAmount == null) {
            lastAmount = meterInfo.getThisAmount();
        }
        baseOutput.setData(lastAmount);

        return baseOutput;
    }

    /**
     * 根据表 meterId 查询最近的一次已交费的记录的实际值/本期指数值
     *
     * @param  meterId
     * @return 实际值/本期指数值
     * @date   2020/6/30
     */
    private Long getLastAmountByMeterId(Long meterId) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();

        // 业务类型(水表、电表)
        List<Integer> bizTypeList = Lists.newArrayList(BizTypeEnum.WATER_METER.getCode(), BizTypeEnum.ELECTRIC_METER.getCode());
        String bizTypes = bizTypeList.toString().replace("[", "").replace("]", "");
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setState(PaymentOrderStateEnum.PAID.getCode());
        meterDetailDto.setBizTypes(bizTypes);

        return this.getActualDao().getLastAmountByMeterId(meterDetailDto);
    }
}