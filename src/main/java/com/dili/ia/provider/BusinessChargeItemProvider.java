package com.dili.ia.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.BatchProviderMeta;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderSupport;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务收费项提供者
 */
@Component
public class BusinessChargeItemProvider extends BatchDisplayTextProviderSupport {
    private static ThreadLocal<List<BusinessChargeItemDto>> threadLocal = new ThreadLocal<>();

    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    @Override
    protected BatchProviderMeta getBatchProviderMeta(Map metaMap) {
        JSONObject queryParamsObj = JSON.parseObject(String.valueOf(metaMap.get("queryParams")));
        Integer assetType = (Integer) queryParamsObj.get("assetType");
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        BatchProviderMeta batchProviderMeta = DTOUtils.newInstance(BatchProviderMeta.class);
        //设置主DTO和关联DTO需要转义的字段名，这里直接取resource表的name属性
        Map<String, String> map = Maps.newHashMap();
        List<BusinessChargeItemDto> chargeItemDtos = getChargeItemDtos(userTicket.getFirmId(), assetType);
        if (CollectionUtils.isNotEmpty(chargeItemDtos)) {
            chargeItemDtos.forEach(o -> {
                map.put("chargeItem"+o.getId(), "chargeItem"+o.getId());
                map.put("chargeItemYuan"+o.getId(), "chargeItemYuan"+o.getId());
            });
        }
        batchProviderMeta.setEscapeFileds(map);
        //忽略大小写关联
        batchProviderMeta.setIgnoreCaseToRef(true);
        //主DTO与关联DTO的关联(java bean)属性(外键), 这里取resource_link表的resourceId字段
        batchProviderMeta.setFkField("id");
        //关联(数据库)表的主键的字段名，默认取id，这里写出来用于示例用法
        batchProviderMeta.setRelationTablePkField("businessId");
        return batchProviderMeta;
    }

    /**
     * 获取业务收费项目
     *
     * @param marketId
     * @param assetType
     * @return
     */
    private List<BusinessChargeItemDto> getChargeItemDtos(Long marketId, Integer assetType) {
        if (CollectionUtils.isEmpty(threadLocal.get())) {
            threadLocal.set(businessChargeItemService.queryBusinessChargeItemConfig(marketId,AssetsTypeEnum.getAssetsTypeEnum(assetType).getBizType(),null));
        }
        return threadLocal.get();
    }

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        JSONObject queryParamsObj = JSON.parseObject(String.valueOf(metaMap.get("queryParams")));
        Integer assetType = (Integer) queryParamsObj.get("assetType");
        List<BusinessChargeItemDto> chargeItemDtos = threadLocal.get();
        if (CollectionUtils.isEmpty(chargeItemDtos)) {
            return new ArrayList();
        }
        return businessChargeItemService.queryBusinessChargeItem(AssetsTypeEnum.getAssetsTypeEnum(assetType).getBizType(), relationIds.stream().map(Long::valueOf).collect(Collectors.toList()), chargeItemDtos);
    }


}