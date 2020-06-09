package com.dili.ia.provider;

import com.dili.assets.sdk.dto.ChargeItemDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.BatchProviderMeta;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderSupport;
import com.google.common.collect.Maps;
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

    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    @Override
    protected BatchProviderMeta getBatchProviderMeta(Map metaMap) {
        BatchProviderMeta batchProviderMeta = DTOUtils.newInstance(BatchProviderMeta.class);
        //设置主DTO和关联DTO需要转义的字段名，这里直接取resource表的name属性
        Map<String, String> map = Maps.newHashMap();

        map.put("水费", "水费");
        map.put("电费", "电费");
        batchProviderMeta.setEscapeFileds(map);
        //忽略大小写关联
        batchProviderMeta.setIgnoreCaseToRef(true);
        //主DTO与关联DTO的关联(java bean)属性(外键), 这里取resource_link表的resourceId字段
        batchProviderMeta.setFkField("id");
        //关联(数据库)表的主键的字段名，默认取id，这里写出来用于示例用法
        batchProviderMeta.setRelationTablePkField("business_id");
        return batchProviderMeta;
    }

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        List<ChargeItemDto> chargeItemDtos = new ArrayList<>();
        ChargeItemDto chargeItemDto = new ChargeItemDto();
        chargeItemDto.setId(1L);
        chargeItemDto.setName("水费");
        chargeItemDtos.add(chargeItemDto);

        ChargeItemDto chargeItemDto1 = new ChargeItemDto();
        chargeItemDto1.setId(2L);
        chargeItemDto1.setName("电费");
        chargeItemDtos.add(chargeItemDto1);

        return businessChargeItemService.queryBusinessChargeItem(BizTypeEnum.BOOTH_LEASE.getCode(), relationIds.stream().map(Long::valueOf).collect(Collectors.toList()), chargeItemDtos);
    }


}