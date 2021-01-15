package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
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
        businessChargeItemDto.setChargeType(BusinessChargeItemEnum.ChargeType.收费.getCode());
        //获取业务收费项目
        BaseOutput<List<BusinessChargeItemDto>> chargeItemsOutput = businessChargeItemRpc.listByExample(businessChargeItemDto);
        if(!chargeItemsOutput.isSuccess()){
            LOG.error("收费项配置查询接口异常",chargeItemsOutput.getMessage());
        }
        return chargeItemsOutput.getData();
    }

    @Override
    public List<BusinessChargeItemDto> queryFixedBusinessChargeItemConfig(Long marketId, String bizType, Integer isEnable, Integer fixed, String code) {
        this.checkParam(marketId, bizType, isEnable, fixed, code);
        BusinessChargeItemDto businessChargeItemDto = new BusinessChargeItemDto();
        businessChargeItemDto.setMarketId(marketId);
        businessChargeItemDto.setBusinessType(bizType);
        businessChargeItemDto.setIsEnable(isEnable);
        businessChargeItemDto.setFixed(fixed);
        businessChargeItemDto.setCode(code);
        businessChargeItemDto.setChargeType(BusinessChargeItemEnum.ChargeType.收费.getCode());
        //获取业务收费项目
        try{
            BaseOutput<List<BusinessChargeItemDto>> chargeItemsOutput = businessChargeItemRpc.listByExample(businessChargeItemDto);
            if(!chargeItemsOutput.isSuccess()){
                LOG.error("查询收费项接口返回失败，参数：{}，原因：{}", businessChargeItemDto.toString(), chargeItemsOutput.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, "查询收费项接口返回失败!");
            }
            return chargeItemsOutput.getData();
        }catch (Exception e){
            LOG.error("查询收费项服务异常!，参数：{},原因：{}", businessChargeItemDto.toString(), e.getMessage());
            throw new BusinessException(ResultCode.APP_ERROR, "查询收费项服务异常!");
        }
    }

    private void checkParam(Long marketId, String bizType, Integer isEnable, Integer fixed, String code){
        if (null == marketId){
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数市场ID【marketId】不能为空!");
        }
        if (null == bizType){
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数业务类型【bizType】不能为空!");
        }
        if (null == isEnable){
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数收费项是否可以状态【isEnable】不能为空!");
        }
        if (null == fixed){
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数是否规定【fixed】不能为空!");
        }
        if (null == code){
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数静态code【code】不能为空!");
        }
    }

    @Override
    public List<BusinessChargeItemDto> queryBusinessChargeItemMeta(String bizType, List<Long> businessIds) {
        return getActualDao().queryBusinessChargeItemMeta(bizType, businessIds);
    }

    /**
     * 根据业务ID、Code、Type 删除旧有的动态收费项
     *
     * @param  businessId
     * @param  businessCode
     * @param  bizType
     * @return int
     * @date   2020/7/16
     */
    @Override
    public int deleteByBusinessIdAndCodeAndBizType(Long businessId, String businessCode, Integer bizType) {
//        int code = this.getActualDao().deleteByBusinessIdAndCodeAndBizType(businessId, businessCode, bizType);
        return 0;
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

	@Override
	public List<BusinessChargeItem> getByBizCode(String bizCode) {
		BusinessChargeItem chargeItemCondition = new BusinessChargeItem();
        chargeItemCondition.setBusinessCode(bizCode);
		return list(chargeItemCondition);
	}
}