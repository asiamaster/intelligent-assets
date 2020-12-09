package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.glossary.AssetsRentalStateEnum;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
        assetsRentalDto.setState(AssetsRentalStateEnum.ENABLE.getCode());
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
            assetsRentalItem.setAssetsRentalId(assetsRental.getId());
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
            assetsRentalItem.setAssetsRentalId(assetsRental.getId());
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
            if (AssetsRentalStateEnum.ENABLE.getCode().equals(assetsRental.getState())) {
                assetsRental.setState(AssetsRentalStateEnum.DISABLE.getCode());
            } else {
                assetsRental.setState(AssetsRentalStateEnum.ENABLE.getCode());
            }
        }
        assetsRental.setModifyTime(LocalDateTime.now());
        assetsRental.setVersion(assetsRental.getVersion() + 1);

        if (this.updateSelective(assetsRental) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }
    }

    /**
     * 根据摊位 id 查询相关的预设信息
     *
     * @param  assetsId
     * @return AssetsRentalDto
     * @date   2020/12/2
     */
    @Override
    public AssetsRentalDto getRentalByAssetsId(Long assetsId) {
        AssetsRentalDto assetsRentalDto = new AssetsRentalDto();
        assetsRentalDto.setAssetsId(assetsId);
        assetsRentalDto.setState(AssetsRentalStateEnum.ENABLE.getCode());
        return this.getActualDao().getRentalByRentalDto(assetsRentalDto);
    }

    /**
     * 根据同一批次、同一商户，模糊查询摊位出租预设信息集合
     *
     * @param  assetsRentalDto
     * @return list
     * @date   2020/12/7
     */
    @Override
    public List<AssetsRentalDto> listRentalsByRentalDtoAndKeyWord(AssetsRentalDto assetsRentalDto) {
        assetsRentalDto.setState(AssetsRentalStateEnum.ENABLE.getCode());
        return this.getActualDao().listRentalsByRentalDtoAndKeyWord(assetsRentalDto);
    }

    /**
     * 根据是否属于预设池，传入 assetIds, 过滤掉不属于预设池的 ids,摊位出租预设的主表状态是启用
     *
     * @param  assetsIds
     * @return List
     * @date   2020/12/7
     */
    @Override
    public List<Long> filterAssetsIdsByTable(List<Long> assetsIds) {
        return assetsRentalItemService.listRentalItemsByAssetsIds(assetsIds, AssetsRentalStateEnum.ENABLE.getCode());
    }

    /**
     * 根据摊位 ids 查询是否属于一个批次
     *
     * @param  assetsIds
     * @return boolean
     * @date   2020/12/2
     */
    @Override
    public boolean belongBatchAndMchByAssetsIds(List<Long> assetsIds) {
        List<AssetsRentalDto> assetsRentalDtoList = this.getActualDao().belongBatchAndMchByAssetsIds(assetsIds);
        if (assetsRentalDtoList != null && assetsRentalDtoList.size() > 1) {
            return false;
        }
        return true;
    }

    /**
     * 根据区域id删除对应的关联摊位
     *
     * @param
     * @return
     * @date   2020/12/8
     */
    @Override
    public void deleteAssetsByDistrictId(Long districtId) throws Exception {
        this.getActualDao().deleteAssetsByDistrictId(districtId);
    }
}