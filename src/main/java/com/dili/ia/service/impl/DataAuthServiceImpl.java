package com.dili.ia.service.impl;

import com.dili.ia.service.DataAuthService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-23 15:07
 */
@Service
public class DataAuthServiceImpl implements DataAuthService {
    @Autowired
    DepartmentRpc departmentRpc;

    @Override
    public List<Long> getDepartmentDataAuth(UserTicket userTicket){
        if (null == userTicket){
            return Collections.emptyList();
        }
        BaseOutput<List<Department>> out =departmentRpc.listUserAuthDepartmentByFirmId(userTicket.getId(), userTicket.getFirmId());
        List<Long> malist = new ArrayList<>();
        if (out.isSuccess() && CollectionUtils.isNotEmpty(out.getData())){
            List<Long> departmentCodeList = (List<Long>) out.getData().stream().map(m -> m.getId()).collect(Collectors.toList());
            malist.addAll(departmentCodeList);
        }
        return malist;
    }
}
