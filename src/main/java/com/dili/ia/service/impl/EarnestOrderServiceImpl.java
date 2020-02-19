package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.PaymentBill;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.glossary.EarnestOrderStateEnum;
import com.dili.ia.glossary.PayStateEnum;
import com.dili.ia.mapper.EarnestOrderMapper;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.EarnestOrderService;
import com.dili.ia.service.PaymentBillService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class EarnestOrderServiceImpl extends BaseServiceImpl<EarnestOrder, Long> implements EarnestOrderService {

    public EarnestOrderMapper getActualDao() {
        return (EarnestOrderMapper)getDao();
    }

    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    PaymentBillService paymentBillService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addEarnestOrder(EarnestOrder earnestOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        earnestOrder.setCreatorId(userTicket.getId());
        earnestOrder.setCreator(userTicket.getRealName());
        earnestOrder.setMarketId(userTicket.getFirmId());
        earnestOrder.setState(EarnestOrderStateEnum.CREATED.getCode());
        earnestOrder.setAssetsType(AssetsTypeEnum.BOOTH.getCode());
        //@TODO业务单号, 多摊位存储  验证客户状态，摊位状态是否正常
//        earnestOrder.setCode();
        this.getActualDao().insertSelective(earnestOrder);

        CustomerAccount ca = customerAccountService.getCustomerAccountByCustomerId(earnestOrder.getCustomerId(), userTicket.getFirmId());
        if (null == ca){ //如果客户账户不存在，创建客户账户
            CustomerAccount customerAccount = DTOUtils.newDTO(CustomerAccount.class);
            customerAccount.setMarketId(userTicket.getFirmId());
            customerAccount.setCustomerId(earnestOrder.getCustomerId());
            customerAccount.setCustomerCellphone(earnestOrder.getCustomerCellphone());
            customerAccount.setCustomerCertificateNumber(earnestOrder.getCustomerCertificateNumber());
            customerAccount.setCustomerName(earnestOrder.getCustomerName());
            customerAccount.setEarnestBalance(0L);
            customerAccount.setEarnestAvailableBalance(0L);
            customerAccount.setEarnestFrozenAmount(0L);
            customerAccount.setEarnestVersion(0L);
            customerAccount.setTransferAvailableBalance(0L);
            customerAccount.setTransferBalance(0L);
            customerAccount.setTransferFrozenAmount(0L);
            customerAccount.setTransferVersion(0L);
            customerAccountService.insertSelective(customerAccount);
        }
        return 0;
    }

    @Override
    public BaseOutput updateEarnestOrder(Long earnestOrderId) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput submitEarnestOrder(Long earnestOrderId) {
        //@TODO改状态，创建缴费单，提交到结算中心
        EarnestOrder ea = this.getActualDao().selectByPrimaryKey(earnestOrderId);
        if (null == ea || !ea.getState().equals(EarnestOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        ea.setState(EarnestOrderStateEnum.SUBMITTED.getCode());
        ea.setSubmitterId(userTicket.getId());
        ea.setSubmitter(userTicket.getRealName());
        ea.setSubDate(new Date());
        this.getActualDao().updateByPrimaryKey(ea);

        PaymentBill pb = this.buildPaymentBill(userTicket, ea.getAmount(), ea.getId(), ea.getCode());
        paymentBillService.insert(pb);

        //@TODO提交到结算中心
        return BaseOutput.success();
    }

    private PaymentBill buildPaymentBill(UserTicket userTicket, Long amount, Long businessId, String businessCode){
        PaymentBill pb = DTOUtils.newDTO(PaymentBill.class);
        pb.setAmount(amount);
//        pb.setBizType();
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setCreatorId(userTicket.getId());
        pb.setCreator(userTicket.getRealName());
        pb.setMarketId(userTicket.getFirmId());
//        pb.setState();
        pb.setSubmitterId(userTicket.getId());
        pb.setSubmitter(userTicket.getRealName());
        return pb;
    }

    private PaymentBill findPaymentBill(UserTicket userTicket, Long businessId, String businessCode){
        PaymentBill pb = DTOUtils.newDTO(PaymentBill.class);
//        pb.setBizType();
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(userTicket.getFirmId());
//        pb.setState();
        List<PaymentBill> list = paymentBillService.selectByExample(pb);
        //@TODO验证不存在，并抛出异常
        return list.get(0);
    }

    @Override
    public BaseOutput withdrawEarnestOrder(Long earnestOrderId) {
        //@TODO改状态，删除缴费单，通知撤销结算中心缴费单
        EarnestOrder ea = this.getActualDao().selectByPrimaryKey(earnestOrderId);
        if (null == ea || !ea.getState().equals(EarnestOrderStateEnum.SUBMITTED.getCode())){
            return BaseOutput.failure("撤回失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        ea.setState(EarnestOrderStateEnum.CREATED.getCode());
//        ea.setCancelerId(userTicket.getId());
//        ea.setCanceler(userTicket.getRealName());
//        ea.setSubDate(new Date());
        this.getActualDao().updateByPrimaryKey(ea);

        PaymentBill pb = this.findPaymentBill(userTicket, ea.getId(), ea.getCode());
        paymentBillService.deleteByExample(pb);

        //@TODO 更改提交到结算中心的数据
        return BaseOutput.success();
    }
}