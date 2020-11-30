package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.glossary.RentalStateEnum;
import com.dili.ia.mapper.AssetsRentalMapper;
import com.dili.ia.service.AssetsRentalItemService;
import com.dili.ia.service.AssetsRentalService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设
 */
@Service
public class AssetsRentalServiceImpl extends BaseServiceImpl<AssetsRental, Long> implements AssetsRentalService {

    private final static Logger logger = LoggerFactory.getLogger(AssetsRentalServiceImpl.class);

    public AssetsRentalMapper getActualDao() {
        return (AssetsRentalMapper)getDao();
    }

    public AssetsRentalItemService assetsRentalItemService;

    /**
     * 新增资产出租预设
     *
     * @param  assetsRentalDto
     * @param userTicket
     * @return AssetsRental
     * @date   2020/11/26
     */
    @Override
    public AssetsRental addAssetsRental(AssetsRentalDto assetsRentalDto, UserTicket userTicket) throws Exception {
        // 新增到资产出租预设表
        AssetsRental assetsRental = new AssetsRental();

        assetsRental.setName(assetsRentalDto.getName());
        List<AssetsRental> assetsRentalList = this.getActualDao().select(assetsRental);
        if (CollectionUtils.isNotEmpty(assetsRentalList)) {
            throw new BusinessException(ResultCode.DATA_ERROR, "新增资产出租预设失败,资产名称已存在！");
        }

        //TODO 关于同一批设置的资产一个批次号，是否该加一个字段
        assetsRentalDto.setVersion(0);
        assetsRentalDto.setState(RentalStateEnum.ENABLE.getCode());
        assetsRentalDto.setCreatorId(userTicket.getId());
        assetsRentalDto.setCreateTime(LocalDateTime.now());
        assetsRentalDto.setModifyTime(LocalDateTime.now());
        assetsRentalDto.setMarketId(userTicket.getFirmId());
        assetsRentalDto.setCreator(userTicket.getUserName());
        assetsRentalDto.setMarketCode(userTicket.getFirmCode());
        BeanUtils.copyProperties(assetsRentalDto, assetsRental);

        this.insertSelective(assetsRental);

        // 新增到关联表
        List<AssetsRentalItem> assetsRentalItemList = assetsRentalDto.getAssetsRentalItemList();
        for (AssetsRentalItem assetsRentalItem : assetsRentalItemList) {
            assetsRentalItem.setStallRentPresetId(assetsRental.getId());
            assetsRentalItem.setVersion(0);
        }
        assetsRentalItemService.batchInsert(assetsRentalItemList);

        return assetsRental;
    }

    /**
     * 修改资产出租预设
     *
     * @param  assetsRentalDto
     * @return AssetsRental
     * @date   2020/11/26
     */
    @Override
    public AssetsRental updateAssetsRental(AssetsRentalDto assetsRentalDto) throws Exception{
        AssetsRental assetsRental = new AssetsRental();

        // 根据表编号查询是否已存在
        assetsRental.setName(assetsRentalDto.getName());
        List<AssetsRental> assetsRentalList = this.getActualDao().select(assetsRental);
        if (CollectionUtils.isNotEmpty(assetsRentalList)) {
            for (AssetsRental assetsRentalInfo : assetsRentalList) {
                if (!assetsRentalInfo.getId().equals(assetsRentalDto.getId())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "修改资产出租预设失败,资产名称已存在！");
                }
            }
        }

        AssetsRental assetsRentalInfo = this.get(assetsRentalDto.getId());
        assetsRentalInfo.setModifyTime(LocalDateTime.now());
        assetsRentalInfo.setVersion(assetsRentalInfo.getVersion() + 1);

        //修改操作
        BeanUtils.copyProperties(assetsRentalInfo, assetsRental);
        if (this.updateSelective(assetsRental) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        // 修改到关联表
        List<AssetsRentalItem> assetsRentalItemList = assetsRentalDto.getAssetsRentalItemList();
        for (AssetsRentalItem assetsRentalItem : assetsRentalItemList) {
            assetsRentalItem.setStallRentPresetId(assetsRental.getId());
            assetsRentalItem.setVersion(0);
        }
        assetsRentalItemService.deleteByRentalId(assetsRentalDto.getId());
        assetsRentalItemService.batchInsert(assetsRentalItemList);

        return assetsRental;
    }

    /**
     * 启用或者禁用
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/11/26
     */
    @Override
    public void enableOrDisable(Long id) {
        AssetsRental assetsRental = this.get(id);
        if (assetsRental != null) {
            if (RentalStateEnum.ENABLE.getCode().equals(assetsRental.getState())) {
                assetsRental.setState(RentalStateEnum.DISABLE.getCode());
            } else {
                assetsRental.setState(RentalStateEnum.ENABLE.getCode());
            }
        }
        assetsRental.setModifyTime(LocalDateTime.now());
        assetsRental.setVersion(assetsRental.getVersion() + 1);

        if (this.updateSelective(assetsRental) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }
    }
}