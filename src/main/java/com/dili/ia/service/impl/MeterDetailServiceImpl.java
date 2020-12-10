package com.dili.ia.service.impl;

import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.domain.dto.printDto.MeterDetailPrintDto;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.MeterDetailStateEnum;
import com.dili.ia.glossary.MeterTypeEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.MeterDetailMapper;
import com.dili.ia.rpc.SettlementRpcResolver;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.service.MeterService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.rule.sdk.domain.input.QueryFeeInput;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xiaosa
 * @date: 2020/6/23
 * @version: 农批业务系统重构
 * @description: 水电费 service 实现层
 */
@Service
public class MeterDetailServiceImpl extends BaseServiceImpl<MeterDetail, Long> implements MeterDetailService {

    private final static Logger logger = LoggerFactory.getLogger(MeterDetailServiceImpl.class);

    public MeterDetailMapper getActualDao() {
        return (MeterDetailMapper) getDao();
    }

    @Autowired
    private DepartmentRpc departmentRpc;

    @Autowired
    private MeterService meterService;

    @Autowired
    private ChargeRuleRpc chargeRuleRpc;

    @Autowired
    private UidRpcResolver uidRpcResolver;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private CustomerMeterService customerMeterService;

    @Autowired
    private SettlementRpcResolver settlementRpcResolver;

    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    @Value("${settlement.app-id}")
    private Long settlementAppId;

    private String settlerHandlerUrl = "http://ia.diligrp.com:8381/api/meterDetail/settlementDealHandler";


    /**
     * 根据主键 id 查看详情
     *
     * @param id
     * @return MeterDetailDto
     * @date 2020/7/1
     */
    @Override
    public MeterDetailDto getMeterDetailDtoById(Long id) {
        // 根据主键 id 查询到水电费单详情以及联表查询表信息
        MeterDetailDto meterDetailDtoInfo = this.getActualDao().getMeterDetailDtoById(id);

        // 计算费用
        long receivable = 0L;
        if (meterDetailDtoInfo.getUsageAmount() != null && meterDetailDtoInfo.getPrice() != null) {
            receivable = meterDetailDtoInfo.getUsageAmount() * meterDetailDtoInfo.getPrice();
        }
        meterDetailDtoInfo.setReceivable(receivable);

        // 组装动态收费项
        BusinessChargeItem condition = new BusinessChargeItem();
        condition.setBusinessCode(meterDetailDtoInfo.getCode());
        condition.setBizType(BizTypeEnum.ELECTRICITY.getCode());
        if (MeterTypeEnum.WATER_METER.getCode().equals(meterDetailDtoInfo.getType())) {
            // 默认电费，判断后是水费则水费
            condition.setBizType(BizTypeEnum.WATER.getCode());
        }
        List<BusinessChargeItem> list = businessChargeItemService.list(condition);
        meterDetailDtoInfo.setBusinessChargeItems(list);

        return meterDetailDtoInfo;
    }

    /**
     * meter、meterDetail 两表查询水电费单集合(分页)
     *
     * @param meterDetailDto
     * @param useProvider
     * @return MeterDetailDtoList
     * @date 2020/6/28
     */
    @Override
    public EasyuiPageOutput listMeterDetails(MeterDetailDto meterDetailDto, boolean useProvider) throws Exception {
        // 查询列表时必须表类型不能为空
        Integer type = meterDetailDto.getType();
        if (type == null) {
            logger.info("水电表类型为空！");
            throw new BusinessException(ResultCode.PARAMS_ERROR, "水电表类型为空，请刷新页面");
        }

        // 分页
        if (meterDetailDto.getRows() != null && meterDetailDto.getRows() >= 1) {
            PageHelper.startPage(meterDetailDto.getPage(), meterDetailDto.getRows());
        }
        // 截止月份不为空
        if (meterDetailDto.getUsageMonth() != null) {
            // 获取使用月份的第一天和最后一天时间,用于数据库查询
            Map dateTimeMap = this.getStartTimeAndEndTime(meterDetailDto.getUsageMonth());
            meterDetailDto.setStartTime((LocalDateTime) dateTimeMap.get("startTime"));
            meterDetailDto.setEndTime((LocalDateTime) dateTimeMap.get("endTime"));
        }
        List<MeterDetailDto> meterDetailDtooList = this.getActualDao().listMeterDetails(meterDetailDto);

        // 基础代码
        long total = meterDetailDtooList instanceof Page ? ((Page) meterDetailDtooList).getTotal() : (long) meterDetailDtooList.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(meterDetailDto, meterDetailDtooList) : meterDetailDtooList;

        return new EasyuiPageOutput(total, results);
    }

    /**
     * 新增水电费单
     *
     * @param  meterDetailDto
     * @param  userTicket
     * @return MeterDetailDto
     * @date   2020/6/28
     */
    @Override
    @GlobalTransactional
    public MeterDetailDto addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket) throws BusinessException {
        CustomerMeter customerMeter = customerMeterService.getBindInfoByMeterId(meterDetailDto.getMeterId());
        if (null == customerMeter || !customerMeter.getCustomerId().equals(meterDetailDto.getCustomerId())) {
            //已被解绑或删除
            throw new BusinessException(ResultCode.DATA_ERROR, "表已被解绑或删除，请刷新数据后重试!");
        }

        // 根据 meterId 查询是否有代缴费的业务单（已创建已提交）
        List<MeterDetailDto> meterDetailDtoList = this.listMeterDetailByUnPayBusiness(meterDetailDto.getMeterId());
        if (CollectionUtils.isNotEmpty(meterDetailDtoList)) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该表存在未交费单据，无法保存！");
        }

        //在更新状态之前查询指数信息，不然可能会查询出当前数据(脏读)
        BaseOutput lastAmountReturn = this.getLastAmount(meterDetailDto.getMeterId());
        if (!lastAmountReturn.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该表初始指数获取失败，保存失败!");
        }

        Meter meter = meterService.get(meterDetailDto.getMeterId());
        String meterTypeCode = BizTypeEnum.WATER.getEnName();
        if (MeterTypeEnum.ELECTRIC_METER.getCode().equals(meter.getType())) {
            meterTypeCode = BizTypeEnum.ELECTRICITY.getEnName();
        }

        // 生成水或者电费单号的 code
        String meterDetailCode = uidRpcResolver.bizNumber(userTicket.getFirmCode() + "_" + meterTypeCode);
        meterDetailDto.setVersion(0);
        meterDetailDto.setCode(meterDetailCode);
        meterDetailDto.setCreatorId(userTicket.getId());
        meterDetailDto.setCreateTime(LocalDateTime.now());
        meterDetailDto.setModifyTime(LocalDateTime.now());
        meterDetailDto.setMarketId(userTicket.getFirmId());
        meterDetailDto.setCreator(userTicket.getRealName());
        meterDetailDto.setMarketCode(userTicket.getFirmCode());
        meterDetailDto.setCreatorDepId(userTicket.getDepartmentId());
        meterDetailDto.setState(MeterDetailStateEnum.CREATED.getCode());
        this.getActualDao().insertSelective(meterDetailDto);

        //构建动态收费项
        if (meterDetailDto.getBusinessChargeItems() != null) {
            List<BusinessChargeItem> businessChargeItems = buildAddBusinessCharge(meterDetailDto.getBusinessChargeItems(), meterDetailDto.getId(),
                    meterDetailDto.getCode(), meter.getType());
            if (businessChargeItems != null && businessChargeItems.size() > 0) {
                businessChargeItemService.batchInsert(businessChargeItems);
            }
        }

        return meterDetailDto;
    }

    /**
     * 修改水电费单
     *
     * @param  meterDetailDto
     * @return MeterDetailDto
     * @date   2020/7/1
     */
    @Override
    @GlobalTransactional
    public MeterDetailDto updateMeterDetail(MeterDetailDto meterDetailDto) throws BusinessException {
        // 先查询
        MeterDetailDto meterDetailDtoInfo = this.getActualDao().getMeterDetailDtoById(meterDetailDto.getId());
        if (meterDetailDtoInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，修改失败。");
        }

        // 已创建状态才能修改
        if (!MeterDetailStateEnum.CREATED.getCode().equals(meterDetailDtoInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能修改。");
        }

        //在更新状态之前查询指数信息，不然可能会查询出当前数据(脏读)
        BaseOutput lastAmountReturn = this.getLastAmount(meterDetailDtoInfo.getMeterId());
        if (!lastAmountReturn.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该表初始指数获取失败,保存失败!");
        }

        //构建动态收费项
        if (meterDetailDto.getBusinessChargeItems() != null) {
            List<BusinessChargeItem> businessChargeItems = buildUpdateBusinessCharge(meterDetailDto.getBusinessChargeItems(), meterDetailDtoInfo.getId(), meterDetailDtoInfo.getCode(), meterDetailDtoInfo.getType());
            if (businessChargeItems != null && businessChargeItems.size() > 0) {
                for (BusinessChargeItem businessChargeItem : businessChargeItems) {
                    businessChargeItemService.updateSelective(businessChargeItem);
                }
            }
        }

        BeanUtils.copyProperties(meterDetailDto, meterDetailDtoInfo);
        meterDetailDtoInfo.setModifyTime(LocalDateTime.now());
        meterDetailDtoInfo.setVersion(meterDetailDtoInfo.getVersion() + 1);
        if (this.updateSelective(meterDetailDtoInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        return meterDetailDtoInfo;
    }

    /**
     * 提交水电费单(生成缴费单和结算单)
     *
     * @param  idList
     * @return List
     * @date   2020/7/6
     */
    @Override
    @GlobalTransactional
    public List<MeterDetailDto> submit(List<Long> idList, UserTicket userTicket) throws BusinessException {
        List<MeterDetailDto> meterDetailDtoList = this.getActualDao().getMeterDetailDtoListByIds(idList);
        if (CollectionUtils.isNotEmpty(meterDetailDtoList)) {
            for (MeterDetailDto meterDetailDtoInfo : meterDetailDtoList) {
                // 修改水电费单状态为已提交，多人提交缴费单也忽略
                meterDetailDtoInfo.setState(MeterDetailStateEnum.SUBMITED.getCode());
                meterDetailDtoInfo.setSubmitterId(userTicket.getId());
                meterDetailDtoInfo.setSubmitter(userTicket.getRealName());
                meterDetailDtoInfo.setSubmitTime(LocalDateTime.now());
                if (this.updateSelective(meterDetailDtoInfo) == 0) {
                    logger.info("多人提交水电费单!");
                }

                // 创建缴费单
                Meter meter = meterService.get(meterDetailDtoInfo.getMeterId());
                BizTypeEnum bizTypeEnum = BizTypeEnum.WATER;
                if (MeterTypeEnum.ELECTRIC_METER.getCode().equals(meter.getType())) {
                    bizTypeEnum = BizTypeEnum.ELECTRICITY;
                }

                PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket, bizTypeEnum);
                paymentOrder.setBusinessId(meterDetailDtoInfo.getId());
                paymentOrder.setBusinessCode(meterDetailDtoInfo.getCode());
                paymentOrder.setAmount(meterDetailDtoInfo.getAmount());
                paymentOrder.setBizType(BizTypeEnum.ELECTRICITY.getCode());
                if (MeterTypeEnum.WATER_METER.getCode().equals(meterDetailDtoInfo.getType())) {
                    // 默认电费，判断后是水费则水费
                    paymentOrder.setBizType(BizTypeEnum.WATER.getCode());
                }
                paymentOrderService.insertSelective(paymentOrder);

                // 调用结算接口,缴费
                SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, meterDetailDtoInfo, paymentOrder.getCode(), paymentOrder.getAmount());
                settlementRpcResolver.submit(settleOrderDto);
            }
        }
        return meterDetailDtoList;
    }

    /**
     * 全部提交水电费单(生缴费单和结算单)
     *
     * @param  userTicket
     * @param  metertype
     * @return List
     * @date   2020/7/29
     */
    @Override
    @GlobalTransactional
    public List<MeterDetailDto> submitAll(UserTicket userTicket, Integer metertype) throws BusinessException {
        // 查询所有未提交的水电费单
        MeterDetailDto meterDetailQuery = new MeterDetailDto();
        meterDetailQuery.setType(metertype);
        meterDetailQuery.setState(MeterDetailStateEnum.CREATED.getCode());
        List<MeterDetailDto> meterDetailInfoList = this.getActualDao().listByStateCreatedAndType(meterDetailQuery);
        List<Long> idList = new ArrayList<>();
        if (meterDetailInfoList != null && meterDetailInfoList.size() > 0) {
            meterDetailInfoList.stream().map(BaseDomain::getId).forEach(idList::add);
            this.submit(idList, userTicket);
        }

        return meterDetailInfoList;
    }

    /**
     * 撤回水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @param  userTicket
     * @return MeterDetailDto
     * @date   2020/7/6
     */
    @Override
    @GlobalTransactional
    public MeterDetailDto withdraw(Long id, UserTicket userTicket) throws BusinessException {
        // 查询数据,对比状态
        MeterDetailDto meterDetailDtoInfo = this.getActualDao().getMeterDetailDtoById(id);
        if (meterDetailDtoInfo != null && !MeterDetailStateEnum.SUBMITED.getCode().equals(meterDetailDtoInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已提交，不能撤回。");
        }

        meterDetailDtoInfo.setWithdrawOperatorId(userTicket.getId());
        meterDetailDtoInfo.setWithdrawOperator(userTicket.getRealName());
        meterDetailDtoInfo.setModifyTime(LocalDateTime.now());
        meterDetailDtoInfo.setState(MeterDetailStateEnum.CREATED.getCode());
        if (this.updateSelective(meterDetailDtoInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 撤销缴费单
        PaymentOrder paymentOrder = this.findPaymentOrder(userTicket, meterDetailDtoInfo);
        paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
        if (paymentOrderService.updateSelective(paymentOrder) == 0) {
            logger.info("撤回水电费【删除缴费单】失败.");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 撤回结算单多人操作已判断
        settlementRpcResolver.cancel(settlementAppId, paymentOrder.getCode());

        return meterDetailDtoInfo;
    }

    /**
     * 取消水电费单(只有在已创建状态下可取消)
     *
     * @param  id
     * @param  userTicket
     * @return MeterDetailDto
     * @date   2020/7/6
     */
    @Override
    @GlobalTransactional
    public MeterDetailDto cancel(Long id, UserTicket userTicket) throws BusinessException {
        // 查询数据,对比状态
        MeterDetailDto meterDetailDtoInfo = this.getActualDao().getMeterDetailDtoById(id);
        if (meterDetailDtoInfo != null && !MeterDetailStateEnum.CREATED.getCode().equals(meterDetailDtoInfo.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该状态不是已创建，不能取消。");
        }

        meterDetailDtoInfo.setCancelerId(userTicket.getId());
        meterDetailDtoInfo.setCanceler(userTicket.getRealName());
        meterDetailDtoInfo.setModifyTime(LocalDateTime.now());
        meterDetailDtoInfo.setState(MeterDetailStateEnum.CANCELLED.getCode());
        if (this.updateSelective(meterDetailDtoInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return meterDetailDtoInfo;
    }

    /**
     * 缴费回调
     *
     * @param  settleOrder
     * @return MeterDetail
     * @date   2020/7/6
     */
    @Override
    @GlobalTransactional
    public MeterDetailDto settlementDealHandler(SettleOrder settleOrder) throws BusinessException {
        // 修改缴费单相关数据
        if (null == settleOrder) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "回调参数为空！");
        }

        PaymentOrder condition = new PaymentOrder();
        //结算单code唯一
        condition.setCode(settleOrder.getOrderCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        MeterDetailDto meterDetailDtoInfo = this.getActualDao().getMeterDetailDtoById(paymentOrderPO.getBusinessId());
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) { //如果已支付，直接返回
            return meterDetailDtoInfo;
        }
        if (!paymentOrderPO.getState().equals(PaymentOrderStateEnum.NOT_PAID.getCode())) {
            logger.info("缴费单状态已变更！状态为：" + PaymentOrderStateEnum.getPaymentOrderStateEnum(paymentOrderPO.getState()).getName());
            throw new BusinessException(ResultCode.DATA_ERROR, "缴费单状态已变更！");
        }

        //缴费单数据更新
        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setPayedTime(settleOrder.getOperateTime());
        paymentOrderPO.setSettlementCode(settleOrder.getCode());
        paymentOrderPO.setSettlementOperator(settleOrder.getOperatorName());
        paymentOrderPO.setSettlementWay(settleOrder.getWay());
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            logger.info("缴费单成功回调 -- 更新【缴费单】,乐观锁生效！【付款单paymentOrderID:{}】", paymentOrderPO.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 修改水电费单
        meterDetailDtoInfo.setModifyTime(LocalDateTime.now());
        meterDetailDtoInfo.setState(MeterDetailStateEnum.PAID.getCode());
        if (this.updateSelective(meterDetailDtoInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "修改水电费单，多人操作，请重试！");
        }

        // 更新表信息中的当前指数
        Meter meterInfo = meterService.get(meterDetailDtoInfo.getMeterId());
        meterInfo.setThisAmount(meterDetailDtoInfo.getThisAmount());
        if (meterService.updateSelective(meterInfo) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "更新表信息中的当前指数，多人操作，请重试！");
        }

        return meterDetailDtoInfo;
    }

    /**
     * 根据表 meterId、用户 customerId 查询未缴费的记录数量
     *
     * @param meterId
     * @param customerId
     * @return 未缴费记录的数量
     * @date 2020/6/29
     */
    @Override
    public Integer countUnPayByMeterAndCustomer(Long meterId, Long customerId) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setCustomerId(customerId);
        meterDetailDto.setState(PaymentOrderStateEnum.NOT_PAID.getCode());

        // 判断水表还是电表
        Meter meterInfo = meterService.get(meterId);
        meterDetailDto.setBizType(BizTypeEnum.ELECTRICITY.getCode());
        if (MeterTypeEnum.WATER_METER.getCode().equals(meterInfo.getType())) {
            meterDetailDto.setBizType(BizTypeEnum.WATER.getCode());
        }

        List<Long> list = this.getActualDao().countUnPayByMeterAndCustomer(meterDetailDto);
        if (CollectionUtils.isEmpty(list)) {
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

        return BaseOutput.success().setData(lastAmount);
    }

    /**
     * 计费规则
     *
     * @param meterDetailDto
     * @return list
     * @date 2020/7/17
     */
    @Override
    public List<QueryFeeOutput> getCost(MeterDetailDto meterDetailDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<QueryFeeInput> queryFeeInputs = new ArrayList<>();
        meterDetailDto.getBusinessChargeItems().forEach(itme -> {
            QueryFeeInput queryFeeInput = new QueryFeeInput();
            queryFeeInput.setMarketId(userTicket.getFirmId());
            queryFeeInput.setBusinessType("WATER_ELECTRICITY");
            queryFeeInput.setChargeItem(itme.getChargeItemId());
            Map<String, Object> calcParams = new HashMap<String, Object>();
            calcParams.put("lastAmount", meterDetailDto.getLastAmount());
            calcParams.put("thisAmount", meterDetailDto.getThisAmount());
            calcParams.put("price", meterDetailDto.getPrice());
            queryFeeInput.setCalcParams(calcParams);
            queryFeeInputs.add(queryFeeInput);
        });
        BaseOutput<List<QueryFeeOutput>> batchQueryFee = chargeRuleRpc.batchQueryFee(queryFeeInputs);

        return batchQueryFee.getData();
    }

    /**
     * 票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return PrintDataDto
     * @date   2020/7/10
     */
    @Override
    public PrintDataDto<MeterDetailPrintDto> receiptPaymentData(String orderCode, Integer reprint) throws BusinessException {
        PaymentOrder paymentOrderCondition = new PaymentOrder();
        PrintDataDto<MeterDetailPrintDto> printDataDto = new PrintDataDto<>();

        paymentOrderCondition.setCode(orderCode);
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new RuntimeException("businessCode无效");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "此单未支付!");
        }

        // 组装数据
        MeterDetailDto meterDetailInfo = this.getActualDao().getMeterDetailByCode(paymentOrder.getBusinessId());
        if (meterDetailInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "水电费单不存在!");
        }
        SettleOrder order = settlementRpcResolver.get(settlementAppId, meterDetailInfo.getCode());

        MeterDetailPrintDto meterDetailPrintDto = new MeterDetailPrintDto();
        meterDetailPrintDto.setPrintTime(LocalDateTime.now());
        meterDetailPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        meterDetailPrintDto.setCode(meterDetailInfo.getCode());
        meterDetailPrintDto.setCustomerName(meterDetailInfo.getCustomerName());
        meterDetailPrintDto.setCustomerCellphone(meterDetailInfo.getCustomerCellphone());
        // 业务类型（判断是水费还是电费）
        meterDetailPrintDto.setBusinessType(PrintTemplateEnum.ELECTRICITY_FEE.getName());
        if (MeterTypeEnum.WATER_METER.equals(meterDetailInfo.getState())) {
            meterDetailPrintDto.setBusinessType(PrintTemplateEnum.WATER_FEE.getCode());
        }
        meterDetailPrintDto.setAmount(MoneyUtils.centToYuan(meterDetailInfo.getAmount()));
        meterDetailPrintDto.setNumber(meterDetailInfo.getNumber());
        meterDetailPrintDto.setUsageTime(meterDetailInfo.getUsageTime());
        meterDetailPrintDto.setAssetsType(AssetsTypeEnum.getAssetsTypeEnumByBizType(meterDetailInfo.getAssetsType()).getName());
        meterDetailPrintDto.setAssetsName(meterDetailInfo.getAssetsName());

        // 项目里的字段
        meterDetailPrintDto.setLastAmount(meterDetailInfo.getLastAmount());
        meterDetailPrintDto.setThisAmount(meterDetailInfo.getThisAmount());
        meterDetailPrintDto.setUsageAmount(meterDetailInfo.getUsageAmount());
        meterDetailPrintDto.setPrice(meterDetailInfo.getPrice());
        meterDetailPrintDto.setReceivable(meterDetailInfo.getReceivable());
        meterDetailPrintDto.setSharedAmount(meterDetailInfo.getAmount() - meterDetailInfo.getReceivable());
        // 支付方式
        String settleDetails = "";
        if (SettleWayEnum.CARD.getCode() == order.getWay()) {
            // 园区卡支付
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【卡号：" + order.getAccountNumber() +
                    "（" + order.getCustomerName() + "）】";
        } else if (SettleWayEnum.CASH.getCode() == order.getWay()) {
            // 现金
            settleDetails = "付款方式：" + SettleWayEnum.getNameByCode(order.getWay()) + "     【" + order.getChargeDate() + "  流水号：" + order.getSerialNumber() + "  备注："
                    + order.getNotes() + "】";
        }
        meterDetailPrintDto.setSettleWayDetails(settleDetails);

        meterDetailPrintDto.setStartTime(meterDetailInfo.getStartTime());
        meterDetailPrintDto.setEndTime(meterDetailInfo.getEndTime());
        meterDetailPrintDto.setNotes(meterDetailInfo.getNotes());
        meterDetailPrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        meterDetailPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        meterDetailPrintDto.setSubmitter(paymentOrder.getCreator());

        printDataDto.setItem(meterDetailPrintDto);
        printDataDto.setName(PrintTemplateEnum.ELECTRICITY_FEE.getName());
        if ( MeterTypeEnum.WATER_METER.getCode().equals(meterDetailInfo.getType()) ) {
            printDataDto.setName(PrintTemplateEnum.WATER_FEE.getName());
        }

        return printDataDto;
    }

    /**
     * 构建新增时动态收费项
     */
    private List<BusinessChargeItem> buildAddBusinessCharge(List<BusinessChargeItem> businessChargeItems, Long businessId, String businessCode, Integer type) {
        List<BusinessChargeItem> businessChargeItemList = new ArrayList<>();

        businessChargeItems.stream().filter(item -> item.getAmount() != null).forEach(item -> {
            item.setVersion(0);
            item.setPaidAmount(0L);
            item.setBusinessId(businessId);
            item.setBusinessCode(businessCode);
            item.setWaitAmount(item.getAmount());
            item.setCreateTime(LocalDateTime.now());
            item.setModifyTime(LocalDateTime.now());
            item.setPaymentAmount(item.getAmount());
            item.setBizType(BizTypeEnum.ELECTRICITY.getCode());
            if (MeterTypeEnum.WATER_METER.getCode().equals(type)) {
                // 默认电费，如果是水费则为水费
                item.setBizType(BizTypeEnum.WATER.getCode());
            }
            businessChargeItemList.add(item);
        });

        return businessChargeItemList;
    }

    /**
     * 构建修改时动态收费项
     */
    private List<BusinessChargeItem> buildUpdateBusinessCharge(List<BusinessChargeItem> businessChargeItems, Long businessId, String businessCode, Integer type) {
        List<BusinessChargeItem> businessChargeItemList = new ArrayList<>();

        businessChargeItems.stream().filter(item -> item.getAmount() != null).forEach(item -> {
            BusinessChargeItem itemInfo = businessChargeItemService.get(item.getId());

            item.setPaidAmount(0L);
            item.setBusinessId(businessId);
            item.setBusinessCode(businessCode);
            item.setAmount(item.getAmount());
            item.setWaitAmount(item.getAmount());
            item.setModifyTime(LocalDateTime.now());
            item.setPaymentAmount(item.getAmount());
            item.setBizType(BizTypeEnum.ELECTRICITY.getCode());
            if (MeterTypeEnum.WATER_METER.getCode().equals(type)) {
                // 默认电费，如果是水费则为水费
                item.setBizType(BizTypeEnum.WATER.getCode());
            }
            item.setVersion(itemInfo.getVersion());
            businessChargeItemList.add(item);
        });

        return businessChargeItemList;
    }

    /**
     * 构建结算实体类
     *
     * @param  userTicket
     * @param  meterDetailDto
     * @param  orderCode
     * @param  amount
     * @return SettleOrderDto
     */
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, MeterDetailDto meterDetailDto, String orderCode, Long amount) {
        // 判断水电表
        BizTypeEnum bizTypeEnum = BizTypeEnum.ELECTRICITY;
        if (MeterTypeEnum.WATER_METER.getCode().equals(meterDetailDto.getType())) {
            bizTypeEnum = BizTypeEnum.WATER;
        }

        SettleOrderInfoDto settleOrderInfoDto = new SettleOrderInfoDto(userTicket, bizTypeEnum, SettleTypeEnum.PAY, SettleStateEnum.WAIT_DEAL);
        settleOrderInfoDto.setMarketCode(userTicket.getFirmCode());
        settleOrderInfoDto.setMarketId(meterDetailDto.getMarketId());
        settleOrderInfoDto.setBusinessCode(meterDetailDto.getCode());
        settleOrderInfoDto.setAmount(amount);
        settleOrderInfoDto.setOrderCode(orderCode);
        settleOrderInfoDto.setAppId(settlementAppId);
        settleOrderInfoDto.setBusinessDepId(meterDetailDto.getDepartmentId());
        settleOrderInfoDto.setBusinessDepName(meterDetailDto.getDepartmentName());
        settleOrderInfoDto.setCustomerId(meterDetailDto.getCustomerId());
        settleOrderInfoDto.setCustomerName(meterDetailDto.getCustomerName());
        settleOrderInfoDto.setCustomerPhone(meterDetailDto.getCustomerCellphone());
        settleOrderInfoDto.setSubmitterId(userTicket.getId());
        settleOrderInfoDto.setSubmitterName(userTicket.getRealName());
//        settleOrderInfoDto.setBusinessType(Integer.valueOf(bizTypeEnum.getCode()));
        settleOrderInfoDto.setType(SettleTypeEnum.PAY.getCode());
        settleOrderInfoDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
//        settleOrderInfoDto.setReturnUrl(settlerHandlerUrl);
        if (userTicket.getDepartmentId() != null) {
            settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }

        return settleOrderInfoDto;
    }

    /**
     * 根据条件查询缴费单
     *
     * @param  userTicket
     * @param  meterDetailDto
     * @return PaymentOrder
     */
    private PaymentOrder findPaymentOrder(UserTicket userTicket, MeterDetailDto meterDetailDto) throws BusinessException {
        PaymentOrder pb = new PaymentOrder();
        pb.setMarketId(userTicket.getFirmId());
        pb.setBusinessId(meterDetailDto.getId());
        pb.setBusinessCode(meterDetailDto.getCode());
        pb.setState(PaymentOrderStateEnum.NOT_PAID.getCode());

        // 判断水费电费
        pb.setBizType(BizTypeEnum.ELECTRICITY.getCode());
        if (MeterTypeEnum.WATER_METER.getCode().equals(meterDetailDto.getType())) {
            pb.setBizType(BizTypeEnum.WATER.getCode());
        }

        PaymentOrder paymentOrder = paymentOrderService.listByExample(pb).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            logger.info("没有查询到付款单PaymentOrder【业务单businessId：{}】 【业务单businessCode:{}】", meterDetailDto.getId(), meterDetailDto.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到付款单！");
        }

        return paymentOrder;
    }

    /**
     * 根据 meterId 查询是否有待缴费的业务单
     *
     * @param meterId
     * @param
     * @return 缴费集合
     * @date 2020/6/30
     */
    private List<MeterDetailDto> listMeterDetailByUnPayBusiness(Long meterId) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();

        // 已创建和已提交状态为未缴费
        StringBuilder statusBuff = new StringBuilder();
        statusBuff.append(MeterDetailStateEnum.CREATED.getCode()).append(",").append(MeterDetailStateEnum.SUBMITED.getCode()).append(",");

        // 设置查询参数
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setStatus(statusBuff.toString());

        return this.getActualDao().listMeterDetailByUnPayBusiness(meterDetailDto);
    }

    /**
     * 根据传入月份,获取到该月份的第一天0时0分0秒和最后一天的23时59分59秒,用于数据库查询
     *
     * @param  UsageMonth
     * @return map
     * @date   2020/6/30
     */
    private Map getStartTimeAndEndTime(String UsageMonth) throws ParseException {
        Map dateTimeMap = new HashMap();

        if (StringUtils.isBlank(UsageMonth)) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");//注意月份是MM
        Date date = simpleDateFormat.parse(UsageMonth);

        ZoneId zoneId = ZoneId.systemDefault();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 将开始时间设置为第一天的0时0分0秒
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        LocalDateTime startTime = c.getTime().toInstant().atZone(zoneId).toLocalDateTime();

        // 将结束时间设置为最后一天的23时59分59秒
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        LocalDateTime endTime = c.getTime().toInstant().atZone(zoneId).toLocalDateTime();

        dateTimeMap.put("startTime", startTime);
        dateTimeMap.put("endTime", endTime);

        return dateTimeMap;
    }

    /**
     * 根据表 meterId 查询最近的一次已交费的记录的实际值/本期指数值
     *
     * @param  meterId
     * @return Long 实际值/本期指数值
     * @date   2020/6/30
     */
    private Long getLastAmountByMeterId(Long meterId) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setState(PaymentOrderStateEnum.PAID.getCode());

        // 判断水表还是电表
        Meter meterInfo = meterService.get(meterId);
        meterDetailDto.setBizType(BizTypeEnum.ELECTRICITY.getCode());
        if (MeterTypeEnum.WATER_METER.getCode().equals(meterInfo.getType())) {
            meterDetailDto.setBizType(BizTypeEnum.WATER.getCode());
        }

        return this.getActualDao().getLastAmountByMeterId(meterDetailDto);
    }
}