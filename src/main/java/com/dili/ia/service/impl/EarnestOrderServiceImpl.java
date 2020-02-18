package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.glossary.EarnestOrderStateEnum;
import com.dili.ia.mapper.EarnestOrderMapper;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.EarnestOrderService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addEarnestOrder(EarnestOrder earnestOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        earnestOrder.setCreatorId(userTicket.getId());
        earnestOrder.setCreator(userTicket.getRealName());
        earnestOrder.setMarketId(userTicket.getFirmId());
        earnestOrder.setState(EarnestOrderStateEnum.CREATED.getCode());
        earnestOrder.setAssetsType(AssetsTypeEnum.BOOTH.getCode());
        //@TODO业务单号, 多摊位存储
//        earnestOrder.setCode();
        this.getActualDao().insertSelective(earnestOrder);

        CustomerAccount ca = customerAccountService.getCustomerAccountByCustomerId(earnestOrder.getCustomerId(), userTicket.getFirmId());
        if (null == ca){
            CustomerAccount customerAccount = DTOUtils.newDTO(CustomerAccount.class);
            customerAccount.setMarketId(userTicket.getFirmId());
            customerAccount.setCustomerId(earnestOrder.getCustomerId());
            customerAccount.setCustomerCellphone(earnestOrder.getCustomerCellphone());
            customerAccount.setCustomerCertificateNumber(earnestOrder.getCustomerCertificateNumber());
            customerAccount.setCustomerName(earnestOrder.getCustomerName());
            customerAccount.setEarnestBalance(earnestOrder.getAmount());
            customerAccount.setEarnestAvailableBalance(earnestOrder.getAmount());
            customerAccount.setEarnestFrozenAmount(0L);
            customerAccount.setEarnestVersion(0L);
            customerAccount.setTransferAvailableBalance(0L);
            customerAccount.setTransferBalance(0L);
            customerAccount.setTransferFrozenAmount(0L);
            customerAccount.setTransferVersion(0L);
            customerAccountService.insertSelective(customerAccount);
        }else {
            //@TODO数据安全控制
            ca.setEarnestBalance(ca.getEarnestBalance() + earnestOrder.getAmount());
            ca.setEarnestAvailableBalance(ca.getEarnestAvailableBalance() + earnestOrder.getAmount());
            ca.setEarnestVersion(ca.getEarnestVersion() + 1);
            customerAccountService.update(ca);
        }
        return 0;
    }

    @Override
    public BaseOutput updateEarnestOrder(Long earnestOrderId) {
        return null;
    }

    @Override
    public BaseOutput submitEarnestOrder(Long earnestOrderId) {
        return null;
    }

    @Override
    public BaseOutput withdrawEarnestOrder(Long earnestOrderId) {
        return null;
    }
}