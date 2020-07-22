package com.dili.ia.provider;

import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
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
 * 街区提供者
 * @author wangmi
 */
@Component
public class DistrictProvider implements ValueProvider {

    @SuppressWarnings("all")
    @Autowired
    private AssetsRpc assetsRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        if(null == val){
            return buffer;
        }
        DistrictDTO districtDTO= new DistrictDTO();
        districtDTO.setName(val.toString());
        BaseOutput<List<DistrictDTO>> listBaseOutput = assetsRpc.searchDistrict(districtDTO);
        if(!listBaseOutput.isSuccess()){
            return buffer;
        }
        listBaseOutput.getData().forEach(o->{
            buffer.add(new ValuePairImpl(o.getName(), o.getId().toString()));
        });
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
