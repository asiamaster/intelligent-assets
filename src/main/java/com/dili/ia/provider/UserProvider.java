package com.dili.ia.provider;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户提供者
 * @author wangmi
 */
@Component
public class UserProvider implements ValueProvider {

    @SuppressWarnings("all")
    @Autowired
    private UserRpc userRpc;

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
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
