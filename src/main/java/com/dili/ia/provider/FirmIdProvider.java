package com.dili.ia.provider;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <B>根据市场ID解析市场名称</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020/12/11 17:17
 */
@Component
public class FirmIdProvider implements ValueProvider {
    @Autowired
    private FirmRpc firmRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {

        List<Firm> data = firmRpc.listByExample(DTOUtils.newInstance(FirmDto.class)).getData();
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        data.forEach(o -> {
            buffer.add(new ValuePairImpl(o.getName(), o.getId()));
        });
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (null == obj) {
            return null;
        }
        return firmRpc.getById(Long.valueOf(obj.toString())).getData().getName();
    }
}
