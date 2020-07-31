package com.dili.ia.service.impl;

import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.domain.dto.printDto.MeterDetailPrintDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
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
import org.springframework.transaction.annotation.Transactional;

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
     * @param  id
     * @return MeterDetailDto
     * @date   2020/7/1
     */
    @Override
    public MeterDetailDto getMeterDetailById(Long id) {
        // 根据主键 id 查询到水电费单详情以及联表查询表信息
        MeterDetailDto meterDetailDtoInfo = this.getActualDao().getMeterDetailById(id);

        // 计算费用
        long receivable = 0L;
        if (meterDetailDtoInfo.getUsageAmount() != null && meterDetailDtoInfo.getPrice() != null) {
            receivable = meterDetailDtoInfo.getUsageAmount() * meterDetailDtoInfo.getPrice();
        }
        meterDetailDtoInfo.setReceivable(receivable);

        // 组装动态收费项
        BusinessChargeItem condtion = new BusinessChargeItem();
        condtion.setBusinessCode(meterDetailDtoInfo.getCode());
        condtion.setBizType(BizTypeEnum.WATER_ELECTRICITY.getCode());
        List<BusinessChargeItem> list = businessChargeItemService.list(condtion);
        meterDetailDtoInfo.setBusinessChargeItems(list);

        return meterDetailDtoInfo;
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
            logger.info("水电表类型为空，报错！");
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
    @GlobalTransactional
    public BaseOutput<MeterDetail> addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket) throws Exception {
        MeterDetail meterDetail = new MeterDetail();
        BeanUtils.copyProperties(meterDetailDto, meterDetail);

        // 根据 meterId 查询是否有代缴费的业务单（已创建已提交）
        List<MeterDetailDto> meterDetailDtoList = this.listMeterDetailByUnPayBusiness(meterDetailDto.getMeterId());
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
        String meterDetailCode = uidRpcResolver.bizNumber(BizNumberTypeEnum.WATER_ELECTRICITY_CODE.getCode());
        meterDetail.setVersion(0);
        meterDetail.setCode(meterDetailCode);
        meterDetail.setCreatorId(userTicket.getId());
        meterDetail.setCreateTime(LocalDateTime.now());
        meterDetail.setModifyTime(LocalDateTime.now());
        meterDetail.setMarketId(userTicket.getFirmId());
        meterDetail.setCreator(userTicket.getRealName());
        meterDetail.setMarketCode(userTicket.getFirmCode());
        meterDetail.setCreatorDepId(userTicket.getDepartmentId());
        meterDetail.setState(MeterDetailStateEnum.CREATED.getCode());
        this.getActualDao().insertSelective(meterDetail);

        //构建动态收费项
        if (meterDetailDto.getBusinessChargeItems() != null) {
            List<BusinessChargeItem> businessChargeItems = buildAddBusinessCharge(meterDetailDto.getBusinessChargeItems(), meterDetail.getId(), meterDetail.getCode());
            if (businessChargeItems != null && businessChargeItems.size() > 0) {
                businessChargeItemService.batchInsert(businessChargeItems);
            }
        }

        CustomerMeter customerMeter = customerMeterService.getBindInfoByMeterId(meterDetail.getMeterId());
        if (null == customerMeter || !customerMeter.getCustomerId().equals(meterDetail.getCustomerId())) {
            //已被解绑或删除
            return BaseOutput.failure("表已被解绑或删除，请刷新数据后重试!");
        }

        return BaseOutput.success().setData(meterDetail);
    }

    /**
     * 修改水电费单
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/7/1
     */
    @Override
    @GlobalTransactional
    public BaseOutput<MeterDetail> updateMeterDetail(MeterDetailDto meterDetailDto) {

        // 先查询
        MeterDetail meterDetailInfo = this.get(meterDetailDto.getId());
        if (meterDetailInfo == null) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "该记录已删除，修改失败。");
        }

        // 已创建状态才能修改
        if (!MeterDetailStateEnum.CREATED.getCode().equals(meterDetailInfo.getState())) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "该状态不是已创建，不能修改");
        }

        //在更新状态之前查询指数信息，不然可能会查询出当前数据(脏读)
        BaseOutput lastAmountReturn = this.getLastAmount(meterDetailInfo.getMeterId());
        if (!lastAmountReturn.isSuccess()){
            return BaseOutput.failure("该表初始指数获取失败,保存失败!");
        }

        //构建动态收费项
        if (meterDetailDto.getBusinessChargeItems() != null) {
            List<BusinessChargeItem> businessChargeItems = buildUpdateBusinessCharge(meterDetailDto.getBusinessChargeItems(), meterDetailInfo.getId(), meterDetailInfo.getCode());
            if (businessChargeItems != null && businessChargeItems.size() > 0) {
                for (BusinessChargeItem businessChargeItem : businessChargeItems) {
                    businessChargeItemService.updateSelective(businessChargeItem);
                }
            }
        }

        BeanUtils.copyProperties(meterDetailDto, meterDetailInfo);
        meterDetailInfo.setModifyTime(LocalDateTime.now());
        meterDetailInfo.setVersion(meterDetailInfo.getVersion() + 1);
        if (this.updateSelective(meterDetailInfo) == 0) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        return BaseOutput.success().setData(meterDetailInfo);
    }

    /**
     * 提交水电费单(生成缴费单和结算单)
     *
     * @param  ids
     * @return 是否成功
     * @date   2020/7/6
     */
    @Override
    @Transactional
    public BaseOutput<List<MeterDetail>> submit(List<Long> ids, UserTicket userTicket) throws Exception {
        List<MeterDetail> meterDetailList = new ArrayList<>();
        // 多个提交
        for (Long id : ids) {
            // 先查询水电费单
            MeterDetail meterDetailInfo = this.get(id);
            if (meterDetailInfo == null) {
            }

            // 修改水电费单状态为已提交
            meterDetailInfo.setState(MeterDetailStateEnum.SUBMITED.getCode());
            meterDetailInfo.setSubmitterId(userTicket.getId());
            meterDetailInfo.setSubmitter(userTicket.getRealName());
            meterDetailInfo.setSubmitTime(LocalDateTime.now());
            if (this.updateSelective(meterDetailInfo) == 0) {
                logger.info("多人提交水电费单!");
            }

            // 创建缴费单
            PaymentOrder paymentOrder = paymentOrderService.buildPaymentOrder(userTicket);
            paymentOrder.setBusinessId(meterDetailInfo.getId());
            paymentOrder.setBusinessCode(meterDetailInfo.getCode());
            paymentOrder.setAmount(meterDetailInfo.getAmount());
            paymentOrder.setBizType(BizTypeEnum.WATER_ELECTRICITY.getCode());
            paymentOrderService.insertSelective(paymentOrder);

            // 调用结算接口,缴费
            SettleOrderDto settleOrderDto = buildSettleOrderDto(userTicket, meterDetailInfo, paymentOrder.getCode(), paymentOrder.getAmount());
            settlementRpcResolver.submit(settleOrderDto);
            meterDetailList.add(meterDetailInfo);
        }
        return BaseOutput.success().setData(meterDetailList);
    }

    /**
     * 全部提交水电费单(生缴费单和结算单)
     *
     * @param
     * @param metertype
     * @return 是否成功
     * @date   2020/7/29
     */
    @Override
    @GlobalTransactional
    public BaseOutput<List<MeterDetailDto>> submitAll(UserTicket userTicket, Integer metertype) throws Exception {
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

        return BaseOutput.success().setData(meterDetailInfoList);
    }

    /**
     * 撤回水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @return 是否成功
     * @date   2020/7/6
     */
    @Override
    @GlobalTransactional
    public BaseOutput<MeterDetail> withdraw(Long id, UserTicket userTicket) throws Exception {

        // 查询数据,对比状态
        MeterDetail meterDetailInfo = this.get(id);
        if (meterDetailInfo != null && !MeterDetailStateEnum.SUBMITED.getCode().equals(meterDetailInfo.getState())) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }
        meterDetailInfo.setWithdrawOperatorId(userTicket.getId());
        meterDetailInfo.setWithdrawOperator(userTicket.getRealName());
        meterDetailInfo.setModifyTime(LocalDateTime.now());
        meterDetailInfo.setState(MeterDetailStateEnum.CREATED.getCode());
        if (this.updateSelective(meterDetailInfo) == 0) {
            logger.info("撤回水电费【修改为已创建】失败.");
            return BaseOutput.failure(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 撤销缴费单
        PaymentOrder paymentOrder = this.findPaymentOrder(userTicket, meterDetailInfo.getId(), meterDetailInfo.getCode());
        paymentOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
        if (paymentOrderService.updateSelective(paymentOrder) == 0) {
            logger.info("撤回水电费【删除缴费单】失败.");
            return BaseOutput.failure(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 撤回结算单多人操作已判断
        settlementRpcResolver.cancel(settlementAppId, paymentOrder.getCode());

        return BaseOutput.success().setData(meterDetailInfo);
    }

    /**
     * 取消水电费单(只有在已创建状态下可取消)
     *
     * @param  id
     * @return 是否成功
     * @date   2020/7/6
     */
    @Override
    public BaseOutput<MeterDetail> cancel(Long id, UserTicket userTicket) {

        // 查询数据,对比状态
        MeterDetail meterDetailInfo = this.get(id);
        if (meterDetailInfo != null && !MeterDetailStateEnum.CREATED.getCode().equals(meterDetailInfo.getState())) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }
        meterDetailInfo.setCancelerId(userTicket.getId());
        meterDetailInfo.setCanceler(userTicket.getRealName());
        meterDetailInfo.setModifyTime(LocalDateTime.now());
        meterDetailInfo.setState(MeterDetailStateEnum.CANCELLED.getCode());
        if (this.updateSelective(meterDetailInfo) == 0) {
            return BaseOutput.failure(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return BaseOutput.success().setData(meterDetailInfo);
    }

    /**
     * 缴费回调
     *
     * @param  settleOrder
     * @return
     * @date   2020/7/6
     */
    @Override
    public BaseOutput<MeterDetail> settlementDealHandler(SettleOrder settleOrder) throws Exception {

        // 修改缴费单相关数据
        if (null == settleOrder){
            throw new BusinessException(ResultCode.PARAMS_ERROR, "回调参数为空！");
        }
        PaymentOrder condition = new PaymentOrder();
        //结算单code唯一
        condition.setCode(settleOrder.getOrderCode());
        condition.setBizType(BizTypeEnum.WATER_ELECTRICITY.getCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        MeterDetail meterDetailInfo = this.get(paymentOrderPO.getBusinessId());
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) { //如果已支付，直接返回
            return BaseOutput.success().setData(meterDetailInfo);
        }
        if (!paymentOrderPO.getState().equals(PaymentOrderStateEnum.NOT_PAID.getCode())){
            logger.info("缴费单状态已变更！状态为：" + PaymentOrderStateEnum.getPaymentOrderStateEnum(paymentOrderPO.getState()).getName() );
            return BaseOutput.failure("缴费单状态已变更！");
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
        meterDetailInfo.setModifyTime(LocalDateTime.now());
        meterDetailInfo.setState(MeterDetailStateEnum.PAID.getCode());
        if (this.updateSelective(meterDetailInfo) == 0) {
            logger.info("缴费单成功回调 -- 更新【水电费单】状态,乐观锁生效！【水电费单Id:{}】", meterDetailInfo.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        // 更新表信息中的当前指数
        Meter meterInfo = meterService.get(meterDetailInfo.getMeterId());
        meterInfo.setThisAmount(meterDetailInfo.getThisAmount());
        if (meterService.updateSelective(meterInfo) == 0) {
            logger.info("缴费单成功回调 -- 更新【表信息】当前指数失败,乐观锁生效！");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return BaseOutput.success().setData(meterDetailInfo);
    }

    /**
     * 票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return
     * @date   2020/7/10
     */
    @Override
    public PrintDataDto<MeterDetailPrintDto> receiptPaymentData(String orderCode, Integer reprint) throws Exception {
        PaymentOrder paymentOrderCondition = new PaymentOrder();

        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.WATER_ELECTRICITY.getCode());
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
        MeterDetailPrintDto meterDetailPrintDto = new MeterDetailPrintDto();
        meterDetailPrintDto.setPrintTime(LocalDateTime.now());
        meterDetailPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        meterDetailPrintDto.setCode(meterDetailInfo.getCode());
        meterDetailPrintDto.setCustomerName(meterDetailInfo.getCustomerName());
        meterDetailPrintDto.setCustomerCellphone(meterDetailInfo.getCustomerCellphone());
        meterDetailPrintDto.setStartTime(meterDetailInfo.getStartTime());
        meterDetailPrintDto.setEndTime(meterDetailInfo.getEndTime());
        meterDetailPrintDto.setNotes(meterDetailInfo.getNotes());
        meterDetailPrintDto.setAmount(MoneyUtils.centToYuan(meterDetailInfo.getAmount()));
        meterDetailPrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        meterDetailPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        meterDetailPrintDto.setSubmitter(paymentOrder.getCreator());
        meterDetailPrintDto.setBusinessType(BizTypeEnum.WATER_ELECTRICITY.getName());

        PrintDataDto<MeterDetailPrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.ELECTRICITY_FEE.getCode());
        if (MeterTypeEnum.WATER_METER.equals(meterDetailInfo.getState())) {
            printDataDto.setName(PrintTemplateEnum.WATER_FEE.getCode());
        }
        printDataDto.setItem(meterDetailPrintDto);

        return printDataDto;
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
        List<Integer> bizTypeList = Lists.newArrayList(Integer.valueOf(BizTypeEnum.WATER_ELECTRICITY.getCode()));
        String bizTypes = bizTypeList.toString().replace("[", "").replace("]", "");
        meterDetailDto.setBizTypes(bizTypes);
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setCustomerId(customerId);
        meterDetailDto.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
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
     * @param   meterDetailDto
     * @return  list
     * @date   2020/7/17
     */
    @Override
    public List<QueryFeeOutput> getCost(MeterDetailDto meterDetailDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<QueryFeeInput> queryFeeInputs = new ArrayList<>();
        meterDetailDto.getBusinessChargeItems().forEach(itme -> {
            QueryFeeInput queryFeeInput =new QueryFeeInput();
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
     * 构建新增时动态收费项
     */
    private List<BusinessChargeItem> buildAddBusinessCharge(List<BusinessChargeItem> businessChargeItems, Long businessId, String businessCode){
        List<BusinessChargeItem> businessChargeItemList = new ArrayList<>();

        businessChargeItems.stream().filter(item -> item.getAmount() != null).forEach(item -> {
            item.setPaidAmount(0L);
            item.setBusinessId(businessId);
            item.setBusinessCode(businessCode);
            item.setWaitAmount(item.getAmount());
            item.setCreateTime(LocalDateTime.now());
            item.setModifyTime(LocalDateTime.now());
            item.setPaymentAmount(item.getAmount());
            item.setBizType(BizTypeEnum.WATER_ELECTRICITY.getCode());
            item.setVersion(0);
            businessChargeItemList.add(item);
        });

        return businessChargeItemList;
    }

    /**
     * 构建修改时动态收费项
     */
    private List<BusinessChargeItem> buildUpdateBusinessCharge(List<BusinessChargeItem> businessChargeItems, Long businessId, String businessCode){
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
            item.setBizType(BizTypeEnum.WATER_ELECTRICITY.getCode());
            item.setVersion(itemInfo.getVersion());
            businessChargeItemList.add(item);
        });

        return businessChargeItemList;
    }

    /**
     * 构建结算实体类
     * @param userTicket
     * @param meterDetailInfo
     * @param orderCode
     * @param amount
     * @return
     */
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, MeterDetail meterDetailInfo, String orderCode, Long amount) {

        SettleOrderInfoDto settleOrderInfoDto = new SettleOrderInfoDto(userTicket, BizTypeEnum.WATER_ELECTRICITY, SettleTypeEnum.PAY, SettleStateEnum.WAIT_DEAL);
        settleOrderInfoDto.setMarketId(meterDetailInfo.getMarketId());
        settleOrderInfoDto.setMarketCode(userTicket.getFirmCode());
        settleOrderInfoDto.setBusinessCode(meterDetailInfo.getCode());
        settleOrderInfoDto.setOrderCode(orderCode);
        settleOrderInfoDto.setAmount(amount);
        settleOrderInfoDto.setAppId(settlementAppId);
        settleOrderInfoDto.setBusinessDepId(meterDetailInfo.getDepartmentId());
        settleOrderInfoDto.setBusinessDepName(meterDetailInfo.getDepartmentName());
        settleOrderInfoDto.setCustomerId(meterDetailInfo.getCustomerId());
        settleOrderInfoDto.setCustomerName(meterDetailInfo.getCustomerName());
        settleOrderInfoDto.setCustomerPhone(meterDetailInfo.getCustomerCellphone());
        settleOrderInfoDto.setSubmitterId(userTicket.getId());
        settleOrderInfoDto.setSubmitterName(userTicket.getRealName());
        settleOrderInfoDto.setBusinessType(Integer.valueOf(BizTypeEnum.WATER_ELECTRICITY.getCode()));
        settleOrderInfoDto.setType(SettleTypeEnum.PAY.getCode());
        settleOrderInfoDto.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrderInfoDto.setReturnUrl(settlerHandlerUrl);
        if (userTicket.getDepartmentId() != null){
            settleOrderInfoDto.setSubmitterDepId(userTicket.getDepartmentId());
            settleOrderInfoDto.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }

        return settleOrderInfoDto;
    }

    /**
     * 根据条件查询缴费单
     * @param userTicket
     * @param businessId
     * @param businessCode
     * @return
     */
    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode) throws Exception{
        PaymentOrder pb = new PaymentOrder();

        pb.setBizType(BizTypeEnum.WATER_ELECTRICITY.getCode());
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(userTicket.getFirmId());
        pb.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        PaymentOrder order = paymentOrderService.listByExample(pb).stream().findFirst().orElse(null);
        if (null == order) {
            logger.info("没有查询到付款单PaymentOrder【业务单businessId：{}】 【业务单businessCode:{}】", businessId, businessCode);
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到付款单！");
        }

        return order;
    }

    /**
     * 根据 meterId 查询是否有未缴费的缴费单记录(某月份)
     * 
     * @param  meterId
     * @param
     * @return 缴费集合
     * @date   2020/6/30
     */
    private List<MeterDetailDto> listUnPayUnSubmitByMeter(Long meterId, String usageMonth) throws ParseException {
        MeterDetailDto meterDetailDto = new MeterDetailDto();

        // 获取使用月份的第一天和最后一天时间,用于数据库查询
        if (usageMonth != null) {
            Map dateTimeMap = this.getStartTimeAndEndTime(usageMonth);
            meterDetailDto.setStartTime((LocalDateTime) dateTimeMap.get("startTime"));
            meterDetailDto.setEndTime((LocalDateTime) dateTimeMap.get("endTime"));
        }

        // 未缴费
        StringBuilder statusBuff = new StringBuilder("");
        statusBuff.append(PaymentOrderStateEnum.NOT_PAID.getCode()).append(",");

        // 业务类型(水表、电表)
        List<Integer> bizTypeList = Lists.newArrayList(Integer.valueOf(BizTypeEnum.WATER_ELECTRICITY.getCode()));
        String bizTypes = bizTypeList.toString().replace("[", "").replace("]", "");

        // 设置查询参数
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setBizTypes(bizTypes);
        meterDetailDto.setStatus(statusBuff.toString());
        List<MeterDetailDto> meterDetailDtoList = this.getActualDao().listUnPayUnSubmitByMeter(meterDetailDto);

        return meterDetailDtoList;
    }

    /**
     * 根据 meterId 查询是否有待缴费的业务单
     *
     * @param  meterId
     * @param
     * @return 缴费集合
     * @date   2020/6/30
     */
    private List<MeterDetailDto> listMeterDetailByUnPayBusiness(Long meterId) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();

        // 已创建和已提交状态为未缴费
        StringBuilder statusBuff = new StringBuilder("");
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

        if (StringUtils.isBlank(UsageMonth.toString())){
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");//注意月份是MM
        Date date = simpleDateFormat.parse(UsageMonth);

        ZoneId zoneId = ZoneId.systemDefault();
//        ZonedDateTime zdt = UsageMonth.atZone(zoneId);
//        Date date = Date.from(zdt.toInstant());

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 将开始时间设置为第一天的0时0分0秒
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
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
     * @return 实际值/本期指数值
     * @date   2020/6/30
     */
    private Long getLastAmountByMeterId(Long meterId) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();

        // 业务类型(水表、电表)
        List<Integer> bizTypeList = Lists.newArrayList(Integer.valueOf(BizTypeEnum.WATER_ELECTRICITY.getCode()));
        String bizTypes = bizTypeList.toString().replace("[", "").replace("]", "");
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setState(PaymentOrderStateEnum.PAID.getCode());
        meterDetailDto.setBizTypes(bizTypes);

        return this.getActualDao().getLastAmountByMeterId(meterDetailDto);
    }
}