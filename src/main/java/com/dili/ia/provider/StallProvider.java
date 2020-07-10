package com.dili.ia.provider;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.BatchProviderMeta;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderSupport;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 摊位
 */

@Component
@Scope("prototype")
public class StallProvider extends BatchDisplayTextProviderSupport {

    @Autowired
    AssetsRpc assetsRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (Objects.isNull(obj)) {
            return new ArrayList<>();
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        JSONObject json = new JSONObject();
        json.put("keyword", obj.toString());
        json.put("marketId", userTicket.getFirmId());
        List<BoothDTO> data = assetsRpc.searchBooth(json).getData();
        List<ValuePair<?>> resultList = data.stream().map(f -> {
            return (ValuePair<?>) new ValuePairImpl(f.getName(), f.getId());
        }).collect(Collectors.toList());
        return resultList;
    }

    @Override
    protected BatchProviderMeta getBatchProviderMeta(Map metaMap) {
        BatchProviderMeta batchProviderMeta = DTOUtils.newInstance(BatchProviderMeta.class);
        //设置主DTO和关联DTO需要转义的字段名
        batchProviderMeta.setEscapeFiled("name");
        //忽略大小写关联
        batchProviderMeta.setIgnoreCaseToRef(true);
        //关联(数据库)表的主键的字段名，默认取id
        batchProviderMeta.setRelationTablePkField("id");
        //当未匹配到数据时，返回的值
        batchProviderMeta.setMismatchHandler(t -> "-");
        return batchProviderMeta;
    }

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        return null;
    }
}
