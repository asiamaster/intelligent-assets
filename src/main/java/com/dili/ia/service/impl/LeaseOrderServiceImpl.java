package com.dili.ia.service.impl;

import com.dili.http.okhttp.utils.B;
import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.domain.dto.LeaseOrderListDto;
import com.dili.ia.glossary.DepositAmountFlagEnum;
import com.dili.ia.glossary.LeaseOrderStateEnum;
import com.dili.ia.glossary.PayStateEnum;
import com.dili.ia.mapper.LeaseOrderMapper;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.LeaseOrderService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IErrorMessage;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.poi.ss.formula.constant.ErrorConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Service
public class LeaseOrderServiceImpl extends BaseServiceImpl<LeaseOrder, Long> implements LeaseOrderService {

    public LeaseOrderMapper getActualDao() {
        return (LeaseOrderMapper)getDao();
    }
    @Autowired
    private LeaseOrderItemService leaseOrderItemService;

    @Override
    @Transactional
    public BaseOutput saveLeaseOrder(LeaseOrderListDto dto) throws BusinessException {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        if(null == dto.getId()){
            dto.setState(LeaseOrderStateEnum.CREATED.getCode());
            dto.setDepartmentId(userTicket.getDepartmentId());
            dto.setPayState(PayStateEnum.NOT_PAID.getCode());
//            dto.setDepartmentName(userTicket);
            insertSelective(dto);
            insertLeaseOrderItems(dto);
        }else{
            updateSelective(dto);
            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(dto.getId());
            leaseOrderItemService.deleteByExample(condition);
            insertLeaseOrderItems(dto);
        }
        return BaseOutput.success();
    }

    /**
     * 批量插入租赁单项
     * @param dto
     */
    private void insertLeaseOrderItems(LeaseOrderListDto dto) {
        dto.getLeaseOrderItems().forEach(o->{
            o.setLeaseOrderId(dto.getId());
            o.setCustomerId(dto.getCustomerId());
            o.setCustomerName(dto.getCustomerName());
            o.setState(LeaseOrderStateEnum.CREATED.getCode());
            o.setDepositAmountFlag(DepositAmountFlagEnum.PRE_TRANSFER.getCode());
            leaseOrderItemService.insertSelective(o);
        });
    }
}