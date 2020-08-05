package com.dili.ia.provider;

import com.alibaba.fastjson.JSONObject;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: WM
 * @time: 2020/8/5 11:20
 */
@Component
public class YesOrNoProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER;

    static {
        BUFFER = Stream.of(YesOrNoEnum.values())
                .map(e->new ValuePairImpl<String>(e.getName(), e.getCode().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        removeEmpty1(metaMap);
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || "".equals(obj)){
            return "-";
        }
        for(ValuePair<?> valuePair : BUFFER){
            if(obj.toString().equals(valuePair.getValue())){
                return valuePair.getText();
            }
        }
        return null;
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
