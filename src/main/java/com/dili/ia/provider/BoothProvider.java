package com.dili.ia.provider;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2018/11/1 17:17
 */

import com.alibaba.fastjson.JSON;
import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 城市提供者
 * @author asiamaster
 */
@Component
public class BoothProvider implements ValueProvider {

    @Autowired
    private AssetsRpc assetsRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
//        BoothDTO query = new BoothDTO();
//        query.setName(val.toString());
//        List<BoothDTO> boothList = assetsRpc.listPage(query).getData();
//
//        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
//        boothList.forEach(o->{
//            buffer.add(new ValuePairImpl(o.getName(),o.getId().toString()));
//        });
        return (List) JSON.parseArray("[{value:'001',text:'一号摊位'},{value:'002',text:'二号摊位'}]", ValuePairImpl.class);
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
