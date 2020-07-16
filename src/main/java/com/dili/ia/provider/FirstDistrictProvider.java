package com.dili.ia.provider;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2018/11/1 17:17
 */

import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.collections.CollectionUtils;
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
public class FirstDistrictProvider implements ValueProvider {

    @Autowired
    private AssetsRpc assetsRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setParentId(0L);
        districtDTO.setMarketId(userTicket.getFirmId());
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        try{
            List<DistrictDTO> districtDTOS = assetsRpc.searchDistrict(districtDTO).getData();
            if(CollectionUtils.isNotEmpty(districtDTOS)){
                districtDTOS.forEach(o->{
                    buffer.add(new ValuePairImpl(o.getName(),o.getId().toString()));
                });
            }
        }catch (Exception e){}
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
