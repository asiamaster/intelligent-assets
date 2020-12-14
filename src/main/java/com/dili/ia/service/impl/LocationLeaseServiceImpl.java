package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.assets.sdk.dto.AssetsQuery;
import com.dili.assets.sdk.dto.AssetsRentDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.service.AssetsLeaseService;
import com.dili.ia.service.AssetsRentalService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 冷库租赁相关实现
 */
@Service
public class LocationLeaseServiceImpl implements AssetsLeaseService {
    private final static Logger LOG = LoggerFactory.getLogger(LocationLeaseServiceImpl.class);
    @Autowired
    private AssetsRpc assetsRpc;

    @Override
    public Integer getAssetsType() {
        return AssetsTypeEnum.LOCATION.getCode();
    }

    @Override
    public Long checkAssets(List<Long> assetsIds, Long mchId, Long batchId) {
        AssetsQuery assetsQuery = new AssetsQuery();
        assetsQuery.setIds(assetsIds.stream().map(o -> o.toString()).collect(Collectors.toList()));
        BaseOutput<List<AssetsDTO>> output = assetsRpc.searchAssets(assetsQuery);
        if (!output.isSuccess()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "冷库接口调用异常 " + output.getMessage());
        }

        List<AssetsDTO> assetsDTOS = output.getData();
        if (assetsDTOS.size() != assetsIds.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "冷库不存在，请重新修改后保存");
        }

        //检查是否有不同商户的摊位
        if (assetsDTOS.stream().collect(Collectors.groupingBy(AssetsDTO::getMarketId, Collectors.counting())).size() > 1) {
            throw new BusinessException(ResultCode.DATA_ERROR, "合同中冷库分属不同组织，请修改后再操作");
        }

        return assetsDTOS.get(0).getMarketId();
    }

    @Override
    public void frozenAsset(AssetsLeaseOrder leaseOrder, List<AssetsLeaseOrderItem> leaseOrderItems) {
        leaseOrderItems.forEach(o->{
            AssetsRentDTO boothRentDTO = new AssetsRentDTO();
            boothRentDTO.setBoothId(o.getAssetsId());
            boothRentDTO.setStart(DateUtils.localDateTimeToUdate(leaseOrder.getStartTime()));
            boothRentDTO.setEnd(DateUtils.localDateTimeToUdate(leaseOrder.getEndTime()));
            boothRentDTO.setOrderId(leaseOrder.getId().toString());
            BaseOutput assetsOutput = assetsRpc.addAssetsRent(boothRentDTO);
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
     * 释放租赁订单所有摊位
     * @param leaseOrderId
     */
    @Override
    public void unFrozenAllAsset(Long leaseOrderId) {
        AssetsRentDTO boothRentDTO = new AssetsRentDTO();
        boothRentDTO.setOrderId(leaseOrderId.toString());
        BaseOutput assetsOutput = assetsRpc.deleteAssetsRent(boothRentDTO);
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
        AssetsRentDTO boothRentDTO = new AssetsRentDTO();
        boothRentDTO.setOrderId(leaseOrder.getId().toString());
        BaseOutput assetsOutput = assetsRpc.rentAssetsRent(boothRentDTO);
        if(!assetsOutput.isSuccess()){
            LOG.info("摊位解冻出租异常{}",assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
        }
    }
}
