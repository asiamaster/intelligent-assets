package com.dili.ia.provider;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.dto.DepartmentDto;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 用户提供者
 * @author wangmi
 */
@Component
public class UserProvider extends BatchDisplayTextProviderAdaptor {

    @SuppressWarnings("all")
    @Autowired
    private UserRpc userRpc;

    @Override
    protected List getFkList(List<String> ids, Map map) {
        UserQuery user = DTOUtils.newInstance(UserQuery.class);
        user.setIds(ids);
        PageOutput<List<User>> listPageOutput = userRpc.listByExample(user);
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
            map.put(metaMap.get(FIELD_KEY).toString(), "realName");
            return map;
        }
    }

    @Override
    protected String getFkField(Map metaMap) {
        String fkField = (String)metaMap.get(FK_FILED_KEY);
        return fkField == null ? "userId" : fkField;
    }



    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        if(null == val){
            return buffer;
        }
        UserQuery user= DTOUtils.newInstance(UserQuery.class);
        user.setRealName(val.toString());
        BaseOutput<List<User>> usersOutput = userRpc.listByExample(user);
        if(!usersOutput.isSuccess()){
            return buffer;
        }
        usersOutput.getData().forEach(o->{
            buffer.add(new ValuePairImpl(o.getRealName(), o.getId().toString()));
        });
        return buffer;
    }

    @Override
    protected Function getMismatchHandler(Map metaMap) {
        return t -> "-";
    }
}
