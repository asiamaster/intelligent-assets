package com.dili.ia.service.impl;

import com.dili.bpmc.sdk.domain.ProcessInstanceMapping;
import com.dili.bpmc.sdk.dto.StartProcessInstanceDto;
import com.dili.bpmc.sdk.rpc.restful.RuntimeRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ia.cache.BpmDefKeyConfig;
import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.EarnestTransferOrderDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.uid.sdk.rpc.feign.UidFeignRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.EarnestTransferOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.util.SpringUtil;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.dto.EarnestTransferDto;
import com.dili.settlement.rpc.CustomerAccountRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class CustomerAccountServiceImpl implements CustomerAccountService {
    private final static Logger LOG = LoggerFactory.getLogger(CustomerAccountServiceImpl.class);

    @Autowired
    EarnestTransferOrderService earnestTransferOrderService;
    @Autowired
    RefundOrderService refundOrderService;
    @SuppressWarnings("all")
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    UidFeignRpc uidFeignRpc;
    @SuppressWarnings("all")
    @Autowired
    CustomerAccountRpc customerAccountRpc;
    @SuppressWarnings("all")
    @Autowired
    RuntimeRpc runtimeRpc;
    @Resource
    BpmDefKeyConfig bpmDefKeyConfig;


    public CustomerAccount getCustomerAccountById(Long customerAccountId) {
        try {
            BaseOutput<CustomerAccount> caOutput = customerAccountRpc.getById(customerAccountId);
            if (!caOutput.isSuccess()){
                LOG.info("客户账户查询返回失败，客户账户ID【{}】", customerAccountId);
                throw new BusinessException(ResultCode.DATA_ERROR,"客户账户查询返回失败！");
            }
            CustomerAccount customerAccount = caOutput.getData();
            if (customerAccount == null){
                LOG.info("客户账户查询返回数据为空，客户账户ID【{}】", customerAccountId);
                throw new BusinessException(ResultCode.DATA_ERROR,"客户账户查询返回数据为空！");
            }
            return customerAccount;
        }catch (Exception e){
            LOG.info("调用结算【customerAccountRpc.getById】查询客户账户接口异常，客户账户ID【{}】", customerAccountId);
            throw new BusinessException(ResultCode.DATA_ERROR,"调用结算查询客户账户接口异常！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<EarnestTransferOrder> earnestTransfer(EarnestTransferOrderDto etDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            throw new BusinessException(ResultCode.NOT_AUTH_ERROR, "未登录");
        }
        //创建定金转移单
        BaseOutput<EarnestTransferOrder> output = this.addEarnestTransferOrder(etDto, userTicket);
        if (!output.isSuccess()){
            return output;
        }
        try {
            BaseOutput caOutput = customerAccountRpc.transfer(this.buildEarnestTransferDto(output.getData()));
            if (!caOutput.isSuccess()){
                LOG.info("定金转移提交到结算返回失败！转移单号：code={}", output.getData().getCode());
                throw new BusinessException(ResultCode.DATA_ERROR,"定金转移提交到结算返回失败！");
            }
        }catch (Exception e){
            LOG.info("调用结算【customerAccountRpc.transfer】定金转移接口异常！");
            throw new BusinessException(ResultCode.APP_ERROR,"调用结算定金转移接口异常！！");
        }
        return BaseOutput.success().setData(output.getData());
    }

    private EarnestTransferDto buildEarnestTransferDto(EarnestTransferOrder param){
        EarnestTransferDto etDto = new EarnestTransferDto();
        etDto.setAccountId(param.getPayerCustomerAccountId());
        etDto.setCustomerId(param.getPayeeId());
        etDto.setCustomerName(param.getPayeeName());
        etDto.setCustomerPhone(param.getPayeeCellphone());
        etDto.setCustomerCertificate(param.getPayeeCertificateNumber());
        etDto.setAmount(param.getAmount());
        etDto.setRelationCode(param.getCode());
        etDto.setOperatorId(param.getCreatorId());
        etDto.setOperatorName(param.getCreator());
        etDto.setNotes(param.getTransferReason());
       return  etDto;
    }


    /**
     * 根据用户信息，新增客户账户
     * @param efDto EarnestTransferDto
     * @return EarnestTransferOrder 转移单
     * */
    public BaseOutput<EarnestTransferOrder> addEarnestTransferOrder(EarnestTransferOrderDto efDto, UserTicket userTicket){
        CustomerAccount customerAccount = this.getCustomerAccountById(efDto.getPayerCustomerAccountId());
        if ((customerAccount.getAmount() - customerAccount.getFrozenAmount()) < efDto.getAmount()){
            throw new BusinessException(ResultCode.DATA_ERROR, "转移金额不能大于可用余额！");
        }
        //保存定金转移单
        efDto.setPayeeId(efDto.getCustomerId());
        efDto.setPayeeCellphone(efDto.getCustomerCellphone());
        efDto.setPayeeCertificateNumber(efDto.getCertificateNumber());
        efDto.setPayeeName(efDto.getCustomerName());
        BaseOutput<String> bizNumberOutput = uidFeignRpc.getBizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.EARNEST_TRANSFER_ORDER.getCode());
        if(!bizNumberOutput.isSuccess()){
            LOG.info("编号生成器返回失败，{}", bizNumberOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
        }
        efDto.setCode(bizNumberOutput.getData());

        efDto.setCreatorId(userTicket.getId());
        efDto.setCreator(userTicket.getRealName());
        efDto.setMarketId(userTicket.getFirmId());
        efDto.setMchId(customerAccount.getMchId());
        efDto.setVersion(0L);
        earnestTransferOrderService.insertSelective(efDto);
        return BaseOutput.success().setData(efDto);
    }

    @Override
    public BaseOutput saveOrUpdateRefundOrder(RefundOrder refundOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            return BaseOutput.failure("未登录！");
        }
        //检查客户状态
        checkCustomerState(refundOrder.getCustomerId(), userTicket.getFirmId());
        CustomerAccount customerAccount = this.getCustomerAccountById(refundOrder.getBusinessId());
        if ((customerAccount.getAmount() - customerAccount.getFrozenAmount()) < refundOrder.getPayeeAmount()){
            return BaseOutput.failure("退款金额不能大于可用余额！");
        }
        //新增
        if(null == refundOrder.getId()){
            BaseOutput<String> bizNumberOutput = uidFeignRpc.getBizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.EARNEST.getEnName() + "_" + BizNumberTypeEnum.REFUND_ORDER.getCode());
            if(!bizNumberOutput.isSuccess()){
                LOG.info("编号生成器返回失败，{}", bizNumberOutput.getMessage());
                return BaseOutput.failure("编号生成器微服务异常");
            }
            refundOrder.setCode(bizNumberOutput.getData());
            refundOrder.setBizType(BizTypeEnum.EARNEST.getCode());
            refundOrder.setMchId(customerAccount.getMchId()); //商户ID
//            refundOrder.setDistrictId(); //没有在业务单上发起，区域为空
            //根据退款单业务类型启动流程
            StartProcessInstanceDto startProcessInstanceDto = DTOUtils.newInstance(StartProcessInstanceDto.class);
            startProcessInstanceDto.setProcessDefinitionKey(bpmDefKeyConfig.getRefundBizDefKey(refundOrder.getBizType(), refundOrder.getMarketCode()));
            startProcessInstanceDto.setBusinessKey(refundOrder.getCode());
            startProcessInstanceDto.setUserId(userTicket.getId().toString());
            BaseOutput<ProcessInstanceMapping> outputP = runtimeRpc.startProcessInstanceByKey(startProcessInstanceDto);
            if(!outputP.isSuccess()){
                throw new BusinessException(ResultCode.DATA_ERROR, outputP.getMessage());
            }
            refundOrder.setBizProcessInstanceId(outputP.getData().getProcessInstanceId());
            refundOrder.setBizProcessDefinitionId(outputP.getData().getProcessDefinitionId());

            BaseOutput output = refundOrderService.doAddHandler(refundOrder);
            if (!output.isSuccess()) {
                LOG.info("客户账户定金退款【业务ID：{}】退款申请接口异常,原因：{}", customerAccount.getId(), output.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "退款申请接口异常;" + output.getMessage());
            }
        }else { // 修改
            RefundOrder oldRefundOrder = refundOrderService.get(refundOrder.getId());
            SpringUtil.copyPropertiesIgnoreNull(refundOrder, oldRefundOrder);
            BaseOutput<RefundOrder> output = refundOrderService.doUpdatedHandler(oldRefundOrder);
            if (!output.isSuccess()) {
                LOG.info("客户账户定金退款【业务ID：{}】退款修改接口异常,原因：{}", refundOrder.getBusinessId(), output.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "退款修改接口异常;" + output.getMessage());
            }
        }
        return BaseOutput.success();
    }

    /**
     * 检查客户状态
     * @param customerId
     * @param marketId
     */
    private void checkCustomerState(Long customerId,Long marketId){
        BaseOutput<CustomerExtendDto> output = customerRpc.get(customerId,marketId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户接口调用异常 "+output.getMessage());
        }
        CustomerExtendDto customer = output.getData();
        if(null == customer){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在，请核实！");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已禁用，请核实！");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已删除，请核实！");
        }
    }
}

