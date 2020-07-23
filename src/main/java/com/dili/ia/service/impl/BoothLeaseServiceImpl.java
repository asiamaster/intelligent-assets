package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.assets.sdk.dto.BoothRentDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.service.AssetsLeaseService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoothLeaseServiceImpl implements AssetsLeaseService {
    private final static Logger LOG = LoggerFactory.getLogger(BoothLeaseServiceImpl.class);
    @Autowired
    private AssetsRpc assetsRpc;

    @Override
    public Integer getAssetsType() {
        return AssetsTypeEnum.BOOTH.getCode();
    }

    @Override
    public void checkAssetState(Long assetsId) {
        BaseOutput<AssetsDTO> output = assetsRpc.getBoothById(assetsId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位接口调用异常 "+output.getMessage());
        }
        AssetsDTO booth = output.getData();
        if(null == booth){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位不存在，请重新修改后保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(booth.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位"+booth.getName()+"已禁用，请重新修改后保存");
        }else if(YesOrNoEnum.YES.getCode().equals(booth.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位"+booth.getName()+"已删除，请重新修改后保存");
        }
    }

    @Override
    public void frozenAsset(AssetsLeaseOrder leaseOrder, List<AssetsLeaseOrderItem> leaseOrderItems) {
        leaseOrderItems.forEach(o->{
            BoothRentDTO boothRentDTO = new BoothRentDTO();
            boothRentDTO.setBoothId(o.getAssetsId());
            boothRentDTO.setStart(DateUtils.localDateTimeToUdate(leaseOrder.getStartTime()));
            boothRentDTO.setEnd(DateUtils.localDateTimeToUdate(leaseOrder.getEndTime()));
            boothRentDTO.setOrderId(leaseOrder.getId().toString());
            BaseOutput assetsOutput = assetsRpc.addBoothRent(boothRentDTO);
            if(!assetsOutput.isSuccess()){
                LOG.info("冻结摊位异常【编号：{}】", leaseOrder.getCode());
                if(assetsOutput.getCode().equals("2500")){
                    throw new BusinessException(ResultCode.DATA_ERROR,o.getAssetsName()+"选择的时间期限重复，请修改后重新保存");
                }else{
                    throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
                }
            }
        });
    }

    /**
     * 解冻租赁订单所有摊位
     * @param leaseOrderId
     */
    @Override
    public void unFrozenAllAsset(Long leaseOrderId) {
        BoothRentDTO boothRentDTO = new BoothRentDTO();
        boothRentDTO.setOrderId(leaseOrderId.toString());
        BaseOutput assetsOutput = assetsRpc.deleteBoothRent(boothRentDTO);
        if(!assetsOutput.isSuccess()){
            LOG.info("解冻租赁订单【leaseOrderId:{}】所有摊位异常{}", leaseOrderId, assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
        }
    }

    /**
     * 解冻消费摊位
     * @param leaseOrder
     */
    @Override
    public void leaseAsset(AssetsLeaseOrder leaseOrder) {
        BoothRentDTO boothRentDTO = new BoothRentDTO();
        boothRentDTO.setOrderId(leaseOrder.getId().toString());
        BaseOutput assetsOutput = assetsRpc.rentBoothRent(boothRentDTO);
        if(!assetsOutput.isSuccess()){
            LOG.info("摊位解冻出租异常{}",assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
        }
    }
}
