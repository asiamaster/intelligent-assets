package com.dili.ia.controller;

import cn.hutool.core.collection.CollUtil;
import com.dili.assets.sdk.dto.*;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.service.AssetsRentalService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 摊位控制器
 */
@Controller
@RequestMapping("/assets")
public class AssetsController {
    private final static Logger LOG = LoggerFactory.getLogger(AssetsController.class);

    @Autowired
    private AssetsRpc assetsRpc;
    @Autowired
    private AssetsRentalService assetsRentalService;

    /**
     * 新增BoothOrderR 资产查询接口
     * @param assetsType 资产类型【摊位，公寓，冷库】
     * @param mchId 商户ID
     * @param firstDistrictId 一级区域ID
     * @param secondDistrictId 二级区域ID
     * @param isExcludeRental 排除预设摊位（true : 查询未预设的摊位， false：查询预设摊位）
     * @param isOnlyFirstArea -- 接口默认true 只在【一级区域ID不为空 并且 二级区域ID 为空 并且 该参数的值为 true 时该条件才会生效】，即只查询该 【一级区域下的 资产，不包含该 一级下的二级区域下的资产】
     */
    @GetMapping(value = "/searchAssets.action")
    public @ResponseBody
    BaseOutput<List<AssetsDTO>> searchAssets(String keyword, Integer assetsType, Long mchId, Integer firstDistrictId, Integer secondDistrictId, boolean isExcludeRental, Boolean isOnlyFirstArea) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        AssetsQuery assetsQuery = new AssetsQuery();
        assetsQuery.setKeyword(keyword);
        assetsQuery.setMarketId(userTicket.getFirmId());
        assetsQuery.setBusinessType(assetsType);// 资产类型
        assetsQuery.setMchId(mchId);
        if (null != firstDistrictId){
            assetsQuery.setArea(firstDistrictId);
        }
        if (null != secondDistrictId){
            assetsQuery.setSecondArea(secondDistrictId);
        }
        if (null != isOnlyFirstArea){
            assetsQuery.setOnlyFirstArea(isOnlyFirstArea);
        }

        try {
            List<AssetsDTO> assets = assetsRpc.searchAssets(assetsQuery).getData();
            if (isExcludeRental && CollectionUtils.isNotEmpty(assets)) {
                List<Long> filterIds = assetsRentalService.filterAssetsIdsByTable(assets.stream().map(o -> o.getId()).collect(Collectors.toList()));
                assets = assets.stream().filter(o -> !filterIds.contains(o.getId())).collect(Collectors.toList());
            }
            return BaseOutput.success().setData(assets);
        } catch (Exception e) {
            LOG.error("资产查询接口异常",e);
            return BaseOutput.success().setData(new ArrayList<>());
        }

    }

    /**
     * 冷库可租数量查询
     */
    @GetMapping(value = "/getRentBalance.action")
    public @ResponseBody
    BaseOutput<Double> getRentBalance(AssetsRentDTO input) {
        try {
            return assetsRpc.getRentBalance(input);
        } catch (Exception e) {
            LOG.error("冷库可租数量查询异常",e);
            return BaseOutput.success();
        }

    }

    /**
     * list Category 品类查询接口
     */
    @GetMapping(value = "/searchCategory.action")
    public @ResponseBody
    BaseOutput<List<CusCategoryDTO>> searchCategory(String keyword) {
        CusCategoryQuery categoryDTO = new CusCategoryQuery();
        categoryDTO.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        categoryDTO.setState(EnabledStateEnum.ENABLED.getCode());
        if (null == keyword) {
            categoryDTO.setParent(0L);
        } else {
            categoryDTO.setKeyword(keyword);
        }
        try {
            return assetsRpc.listCusCategory(categoryDTO);
        } catch (Exception e) {
            return BaseOutput.success().setData(new ArrayList<>());
        }
    }

    /**
     * list district 区域查询接口
     */
    @GetMapping(value = "/searchDistrict.action")
    public @ResponseBody
    BaseOutput<List<DistrictDTO>> searchDistrict(Long parentId) {
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        districtDTO.setIsDelete(YesOrNoEnum.NO.getCode());
        if (null == parentId) {
            districtDTO.setParentId(0L);
        } else {
            districtDTO.setParentId(parentId);
        }
        try {
            return assetsRpc.searchDistrict(districtDTO);
        } catch (Exception e) {
            return BaseOutput.success().setData(new ArrayList<>());
        }
    }
}