package com.dili.ia.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.Department;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年6月17日
 */
@Component
public class DistrictProvider implements ValueProvider{

	@Autowired
	private AssetsRpc assetsRpc;
	
	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		DistrictDTO input = new DistrictDTO();
		input.setDepartmentId((Long)metaMap.get("departmentId"));
		List<DistrictDTO> districts = assetsRpc.searchDistrict(input).getData();
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        districts.forEach(o->{
            buffer.add(new ValuePairImpl(o.getName(),o.getId()));
        });
        return buffer;
	}

	@Override
	public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
		// TODO Auto-generated method stub
		return null;
	}

}
