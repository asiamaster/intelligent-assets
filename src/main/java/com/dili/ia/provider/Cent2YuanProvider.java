package com.dili.ia.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @description: 分转元提供者
 * @author: WM
 * @time: 2020/8/5 15:23
 */
@Component
public class Cent2YuanProvider implements ValueProvider {
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        if(val == null){
            return "-";
        }
        return MoneyUtils.centToYuan((Long) val);
    }
}
