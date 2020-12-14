package com.dili.ia.provider;

import com.dili.settlement.enums.ActionEnum;
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
 * <B>结算业务记录的定金流水【类型】provider</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qunkelan
 * @createTime 2020/12/14 10:17
 */
@Component
public class ActionProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        BUFFER.addAll(Stream.of(ActionEnum.values())
                .map(e -> new ValuePairImpl<>(e.getName(), Integer.valueOf(e.getCode()).toString()))
                .collect(Collectors.toList()));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object object, Map map, FieldMeta fieldMeta) {
        if (null == object) {
            return null;
        }
        ValuePair<?> valuePair = BUFFER.stream().filter(val -> object.toString().equals(val.getValue())).findFirst().orElseGet(null);
        if (null != valuePair) {
            return valuePair.getText();
        }
        return null;
    }
}
