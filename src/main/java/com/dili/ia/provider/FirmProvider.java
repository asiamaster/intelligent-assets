package com.dili.ia.provider;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2018/11/1 17:17
 */

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
 * 城市提供者
 *
 * @author asiamaster
 */
@Component
public class FirmProvider implements ValueProvider {
    @Autowired
    private FirmRpc firmRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {

        List<Firm> data = firmRpc.listByExample(DTOUtils.newInstance(FirmDto.class)).getData();
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        data.forEach(o -> {
            buffer.add(new ValuePairImpl(o.getName(), o.getCode()));
        });
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (null == obj) {
            return null;
        }
        return firmRpc.getById(Long.parseLong(obj.toString())).getData().getName();
    }
}
