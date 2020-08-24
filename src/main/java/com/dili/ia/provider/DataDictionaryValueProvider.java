package com.dili.ia.provider;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典批量提供者
 */
@Component
public class DataDictionaryValueProvider extends BatchDisplayTextProviderAdaptor {


    protected static final String DD_CODE_KEY = "dd_code";
    @Autowired
    DataDictionaryRpc dataDictionaryRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
        if (queryParams == null) {
            return Lists.newArrayList();
        }
        String code = JSONObject.parseObject(queryParams.toString()).getString(DD_CODE_KEY);

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        DataDictionaryValue dto = DTOUtils.newInstance(DataDictionaryValue.class);
        dto.setDdCode(DD_CODE_KEY);
        dto.setFirmId(userTicket.getFirmId());
        List<DataDictionaryValue> list = dataDictionaryRpc.listDataDictionaryValue(dto).getData();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        List<ValuePair<?>> valuePairs = Lists.newArrayList();

        for (int i = 0; i < list.size(); i++) {
            DataDictionaryValue dataDictionaryValue = list.get(i);
            valuePairs.add(new ValuePairImpl(dataDictionaryValue.getName(), dataDictionaryValue.getCode()));
        }
        return valuePairs;
    }

    @Override
    protected List getFkList(List<String> ddvIds, Map metaMap) {
        Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
        if (queryParams == null) {
            return Lists.newArrayList();
        }

        String code = JSONObject.parseObject(queryParams.toString()).getString(DD_CODE_KEY);
        return dataDictionaryRpc.listDataDictionaryValueByDdCode(code).getData();
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if (metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map) {
            return (Map) metaMap.get(ESCAPE_FILEDS_KEY);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "name");
            return map;
        }
    }

    /**
     * 关联(数据库)表的主键的字段名
     * 默认取id，子类可自行实现
     *
     * @return
     */
    @Override
    protected String getRelationTablePkField(Map metaMap) {
        return "code";
    }
}