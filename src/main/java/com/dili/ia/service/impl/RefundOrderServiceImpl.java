package com.dili.ia.service.impl;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.RefundOrderPrintDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.RefundOrderStateEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.util.BeanMapUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.EditEnableEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-12 18:45
 */
@Service
public class RefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderService {

    private final static Logger LOG = LoggerFactory.getLogger(RefundOrderServiceImpl.class);
    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }
    @Autowired
    SettlementRpc settlementRpc;
    @Autowired
    DepartmentRpc departmentRpc;
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    RefundOrderService refundOrderService;
    @Autowired @Lazy
    private List<RefundOrderDispatcherService> refundBizTypes;
    private Map<Integer,RefundOrderDispatcherService> refundBiz = new HashMap<>();
    @PostConstruct
    public void init() {
        for(RefundOrderDispatcherService service : refundBizTypes) {
            for(Integer bizType : service.getBizType()) {
                this.refundBiz.put(bizType, service);
            }

        }
    }
    @Value("${settlement.app-id}")
    private Long settlementAppId;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput doSubmitDispatcher(RefundOrder refundOrder) {
        if (!refundOrder.getState().equals(RefundOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        refundOrder.setState(RefundOrderStateEnum.SUBMITTED.getCode());
        refundOrder.setSubmitTime(new Date());
        refundOrder.setSubmitterId(userTicket.getId());
        refundOrder.setSubmitter(userTicket.getRealName());
        refundOrderService.update(refundOrder);

        //获取业务service,调用业务实现
        RefundOrderDispatcherService service = refundBiz.get(refundOrder.getBizType());
        if(service!=null){
            BaseOutput refundResult = service.submitHandler(refundOrder);
            if (!refundResult.isSuccess()){
                LOG.info("提交到结算中心失败!退款回调业务，返回失败！" + refundResult.getMessage());
                throw new RuntimeException("提交到结算中心失败！" + refundResult.getMessage());
            }
        }

        //提交到结算中心 --- 执行顺序不可调整！！因为异常只能回滚自己系统，无法回滚其它远程系统
        BaseOutput<SettleOrder> out= settlementRpc.submit(buildSettleOrderDto(userTicket, refundOrder));
        if (!out.isSuccess()){
            LOG.info("提交到结算中心失败！" + out.getMessage() + out.getErrorData());
            throw new RuntimeException("提交到结算中心失败！" + out.getMessage());
        }

        return BaseOutput.success("提交成功");
    }

    //组装 -- 结算中心缴费单 SettleOrder
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, RefundOrder ro){
        SettleOrderDto settleOrder = new SettleOrderDto();
        //以下是提交到结算中心的必填字段
        settleOrder.setMarketId(ro.getMarketId()); //市场ID
        settleOrder.setMarketCode(userTicket.getFirmCode());
        settleOrder.setBusinessCode(ro.getCode()); //业务单号
        //收款人信息
        settleOrder.setCustomerId(ro.getPayeeId());//客户ID
        Customer customer = customerRpc.get(ro.getPayeeId(), userTicket.getFirmId()).getData();
        settleOrder.setCustomerPhone(customer.getContactsPhone());//"客户手机号
        settleOrder.setCustomerName(customer.getName());// "客户姓名
        settleOrder.setAmount(ro.getPayeeAmount()); //金额
        settleOrder.setAccountNumber(ro.getBankCardNo());
        settleOrder.setBankName(ro.getBank());
        settleOrder.setBankCardHolder(ro.getPayee());
        settleOrder.setWay(ro.getRefundType());

        //@TODO部门待处理
        settleOrder.setBusinessDepId(2L); //"业务部门ID
        settleOrder.setBusinessDepName("测试部门");//"业务部门名称
        settleOrder.setSubmitterId(userTicket.getId());// "提交人ID
        settleOrder.setSubmitterName(userTicket.getRealName());// "提交人姓名
        settleOrder.setSubmitterDepId(userTicket.getDepartmentId()); //"提交人部门ID
        settleOrder.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        settleOrder.setSubmitTime(LocalDateTime.now());
        settleOrder.setAppId(settlementAppId);//应用ID
        settleOrder.setBusinessType(BizTypeEnum.EARNEST.getCode()); // 业务类型
        settleOrder.setType(SettleTypeEnum.REFUND.getCode());// "结算类型  -- 退款
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrder.setEditEnable(EditEnableEnum.NO.getCode());

        String returnUrl = "http://ia.diligrp.com/refundOrder/refundSuccess";
        settleOrder.setReturnUrl(returnUrl); // 结算-- 缴费成功后回调路径

        return settleOrder;
    }


    @Override
    public BaseOutput doWithdrawDispatcher(RefundOrder refundOrder) {
        try {
            RefundOrderDispatcherService service=refundBiz.get(refundOrder.getBizType());
            if(service!=null){
                BaseOutput refundResult = service.withdrawHandler(refundOrder);
                return refundResult;
            }
        } catch (RuntimeException e) {
            LOG.info("测回异常！{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("测回异常！{}", e1.getMessage());
            return BaseOutput.failure("测回异常");
        }
        return BaseOutput.failure("测回异常");
    }

    @Override
    public BaseOutput doRefundSuccessHandlerDispatcher(Integer bizType, Long refundOrderId) {
        try {
            RefundOrderDispatcherService service=refundBiz.get(bizType);
            if(service!=null){
                BaseOutput refundResult = service.refundSuccessHandler(refundOrderId);
                return refundResult;
            }
        } catch (RuntimeException e) {
            LOG.info("退款成功回调异常！{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("退款成功回调异常！{}", e1.getMessage());
            return BaseOutput.failure("退款成功回调异常");
        }
        return BaseOutput.failure("退款成功回调异常");
    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String businessCode, Integer reprint) {
        try {
            RefundOrder refundOrder = DTOUtils.newDTO(RefundOrder.class);
            refundOrder.setCode(businessCode);
            List<RefundOrder> reList = refundOrderService.list(refundOrder);
            if (CollectionUtils.isEmpty(reList)){
                return BaseOutput.failure("没有获取到结算单【" + businessCode + "】");
            }
            if (!RefundOrderStateEnum.REFUNDED.getCode().equals(refundOrder.getState())) {
                return BaseOutput.failure("此单未退款");
            }

            RefundOrderPrintDto refundOrderPrintDto = buildCommonPrintDate(refundOrder, reprint);

            //获取业务service,调用业务实现 ---- 业务专业的
            RefundOrderDispatcherService service=refundBiz.get(refundOrder.getBizType());
            if(service!=null){
                BaseOutput<RefundOrderPrintDto> result = service.buildBusinessPrintData(refundOrderPrintDto);
                if (!result.isSuccess()){
                    LOG.info("获取打印数据异常！获取组装业务数据异常！{}", result.getMessage());
                    return BaseOutput.failure("获取打印数据异常！获取组装业务数据异常！{}" + result.getMessage());
                }
                refundOrderPrintDto = result.getData();
            }

            PrintDataDto printDataDto = new PrintDataDto();
            printDataDto.setItem(BeanMapUtil.beanToMap(refundOrderPrintDto));
            printDataDto.setName(refundOrderPrintDto.getPrintTemplateCode());
        } catch (RuntimeException e) {
            LOG.info("获取打印数据异常！{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e1){
            LOG.info("获取打印数据异常！{}", e1.getMessage());
            return BaseOutput.failure("获取打印数据异常");
        }
        return BaseOutput.failure("获取打印数据异常");
    }

    private RefundOrderPrintDto buildCommonPrintDate(RefundOrder refundOrder, Integer reprint){
        RefundOrderPrintDto roPrintDto = new RefundOrderPrintDto();
        roPrintDto.setPrintTime(new Date());
        roPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        roPrintDto.setCode(refundOrder.getCode());

        roPrintDto.setCustomerName(refundOrder.getCustomerName());
        roPrintDto.setCustomerCellphone(refundOrder.getCustomerCellphone());
        roPrintDto.setRefundReason(refundOrder.getRefundReason());
        roPrintDto.setAmount(MoneyUtils.centToYuan(refundOrder.getTotalRefundAmount()));
        //@TODO退款单冗余结算员
        roPrintDto.setSettlementOperator(refundOrder.getSubmitter());
        roPrintDto.setSubmitter(refundOrder.getSubmitter());
        roPrintDto.setBusinessType(BizTypeEnum.getBizTypeEnum(refundOrder.getBizType()).getName());
        roPrintDto.setPayee(refundOrder.getPayee());
        roPrintDto.setBank(refundOrder.getBank());
        roPrintDto.setBankCardNo(refundOrder.getBankCardNo());
        roPrintDto.setRefundType(SettleWayEnum.getNameByCode(refundOrder.getRefundType()));
        return roPrintDto;
    }

    public Map<Integer, RefundOrderDispatcherService> getRefundBiz() {
        return refundBiz;
    }

    public void setRefundBiz(Map<Integer, RefundOrderDispatcherService> refundBiz) {
        this.refundBiz = refundBiz;
    }
}
