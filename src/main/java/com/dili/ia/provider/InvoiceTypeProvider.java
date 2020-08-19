package com.dili.ia.provider;

import com.alibaba.fastjson.JSONObject;
import com.dili.ia.glossary.InvoiceTypeEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 开票类型
 * @description:
 * @author: WM
 * @time: 2020/8/4 11:28
 */
@Component
public class InvoiceTypeProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        BUFFER.addAll(Stream.of(InvoiceTypeEnum.values())
                .map(e -> new ValuePairImpl<>(e.getName(), e.getCode().toString()))
                .collect(Collectors.toList()));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
        removeEmpty1(map);
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object object, Map map, FieldMeta fieldMeta) {
        if (null == object) {
            return null;
        }
        ValuePair<?> valuePair = BUFFER.stream().filter(val -> val.getValue().equals(object.toString())).findFirst().orElseGet(null);
        return valuePair == null ? null : valuePair.getText();
    }

    /**
     * 移除第一个空值元素
     */
    private void removeEmpty1(Map metaMap){
        Object queryParamsObj = metaMap.get(ValueProvider.QUERY_PARAMS_KEY);
        if(queryParamsObj != null) {
            //获取查询参数
            JSONObject queryParams = JSONObject.parseObject(queryParamsObj.toString());
            //获取是否必填
            Boolean required = queryParams.getBoolean(ValueProvider.REQUIRED_KEY);
            //非必填才在首位添加空值内容
            if(required != null && required.equals(true) && BUFFER.get(0).getValue().equals("")){
                BUFFER.remove(0);
            }
        }
    }
}