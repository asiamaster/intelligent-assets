package com.dili.ia.provider;

import bsh.StringUtil;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 街区提供者
 * @author wangmi
 */
@Component
public class DistrictProvider extends BatchDisplayTextProviderAdaptor {

    @SuppressWarnings("all")
    @Autowired
    private AssetsRpc assetsRpc;

    @Override
    protected List getFkList(List<String> ids, Map map) {
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setIds(ids);
        BaseOutput<List<DistrictDTO>> listPageOutput = assetsRpc.searchDistrict(districtDTO);
        if(!listPageOutput.isSuccess()){
            return null;
        }
        return listPageOutput.getData();
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
            return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "name");
            return map;
        }
    }

    @Override
    protected String getFkField(Map metaMap) {
        String fkField = (String)metaMap.get(FK_FILED_KEY);
        return fkField == null ? "districtId" : fkField;
    }


    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        if(null == val){
            return buffer;
        }
        DistrictDTO districtDTO= new DistrictDTO();
        districtDTO.setNameLike(val.toString());
        BaseOutput<List<DistrictDTO>> listBaseOutput = assetsRpc.searchDistrict(districtDTO);
        if(!listBaseOutput.isSuccess()){
            return buffer;
        }
        listBaseOutput.getData().forEach(o->{
            if(StringUtils.isBlank(o.getParentName())) {
                buffer.add(new ValuePairImpl(o.getName(), o.getId().toString()));
            }else{
                buffer.add(new ValuePairImpl(o.getParentName() +" -> " + o.getName(), o.getId().toString()));
            }
        });
        return buffer;
    }

}
