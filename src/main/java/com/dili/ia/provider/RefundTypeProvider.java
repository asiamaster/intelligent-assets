package com.dili.ia.provider;

import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.RefundTypeEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-11 18:13
 */
@Component
public class RefundTypeProvider implements ValueProvider {
    private static final ThreadLocal<List<ValuePair<?>>> valuePairsTL = new ThreadLocal<>() {
        @Override
        protected List<ValuePair<?>> initialValue() {
            return Stream.of(RefundTypeEnum.values())
                    .map(e -> new ValuePairImpl<>(e.getName(), e.getCode().toString()))
                    .collect(Collectors.toList());
        }
    };

    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
        return valuePairsTL.get();
    }

    @Override
    public String getDisplayText(Object object, Map map, FieldMeta fieldMeta) {
        if (null == object) {
            return null;
        }

        ValuePair<?> valuePair = valuePairsTL.get().stream().filter(val -> object.toString().equals(val.getValue())).findFirst().orElseGet(null);
        if (null != valuePair) {
            return valuePair.getText();
        }
        return null;
    }
}
