package com.dili.ia.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:       xiaosa
 * @date:         2020/12/21
 * @version:      农批业务系统重构
 * @description:  根据个人所属市场查询是否是沈阳的子商户
 */
@Component
public class MchProvider implements ValueProvider {
    @Autowired
    private FirmRpc firmRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<Firm> data = firmRpc.getAllChildrenByParentId(userTicket.getFirmId()).getData();
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        data.forEach(o->{
            buffer.add(new ValuePairImpl(o.getName(),o.getId()));
        });
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
