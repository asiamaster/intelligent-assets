package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.mapper.BusinessChargeItemMapper;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 16:13:04.
 */
@Service
public class BusinessChargeItemServiceImpl extends BaseServiceImpl<BusinessChargeItem, Long> implements BusinessChargeItemService {

    private final static Logger LOG = LoggerFactory.getLogger(BusinessChargeItemServiceImpl.class);

    public BusinessChargeItemMapper getActualDao() {
        return (BusinessChargeItemMapper)getDao();
    }

    @Autowired private BusinessChargeItemRpc businessChargeItemRpc;

    @Override
    public List<Map<String, String>> queryBusinessChargeItem(String bizType, List<Long> businessIds, List<BusinessChargeItemDto> chargeItemDtos) {
        return getActualDao().queryBusinessChargeItem(bizType,businessIds,chargeItemDtos);
    }

    @Override
    public List<BusinessChargeItemDto> queryBusinessChargeItemConfig(Long marketId, String bizType, Integer isEnable) {
        BusinessChargeItemDto businessChargeItemDto = new BusinessChargeItemDto();
        businessChargeItemDto.setMarketId(marketId);
        businessChargeItemDto.setBusinessType(bizType);
        businessChargeItemDto.setIsEnable(isEnable);
        //获取业务收费项目
        BaseOutput<List<BusinessChargeItemDto>> chargeItemsOutput = businessChargeItemRpc.listByExample(businessChargeItemDto);
        if(!chargeItemsOutput.isSuccess()){
            LOG.error("收费项配置查询接口异常",chargeItemsOutput.getMessage());
        }
        return chargeItemsOutput.getData();
    }

    @Override
    public List<BusinessChargeItemDto> queryBusinessChargeItemMeta(String bizType, List<Long> businessIds) {
        return getActualDao().queryBusinessChargeItemMeta(bizType, businessIds);
    }

    @Override
    public void unityUpdatePaymentAmountByBusinessId(Long businessId, String bizType, Long... paymentAmount) {
        BusinessChargeItem chargeItemCondition = new BusinessChargeItem();
        chargeItemCondition.setBusinessId(businessId);
        chargeItemCondition.setBizType(bizType);
        List<BusinessChargeItem> businessChargeItems = list(chargeItemCondition);
        businessChargeItems.forEach(bci->{
            bci.setPaymentAmount(paymentAmount.length > 0 ? paymentAmount[0] : bci.getAmount());
        });
        if (batchUpdateSelective(businessChargeItems) != businessChargeItems.size()) {
            LOG.info("批量更新收费项支付中金额 【businessId:{},bizType:{}】", businessId, bizType);
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
    }
}