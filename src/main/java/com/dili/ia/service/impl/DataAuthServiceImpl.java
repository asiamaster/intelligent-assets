package com.dili.ia.service.impl;

import com.dili.ia.service.DataAuthService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.rpc.DataAuthRpc;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    DataAuthRpc dataAuthRpc;
    @Autowired
    FirmRpc firmRpc;

    @Override
    public List<Long> getMarketDataAuth(Long userTicketId){
        UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
        userDataAuth.setUserId(userTicketId);
        userDataAuth.setRefCode(DataAuthType.MARKET.getCode());
        BaseOutput<List<Map>> out = dataAuthRpc.listUserDataAuthDetail(userDataAuth);

        List<Long> malist = new ArrayList<>();
        if (out.isSuccess() && CollectionUtils.isNotEmpty(out.getData())){
            List<String> firmCodeList = (List<String>) out.getData().stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toList());
            FirmDto firmDto = DTOUtils.newInstance(FirmDto.class);
            firmDto.setCodes(firmCodeList);
            BaseOutput<List<Firm>> listBaseOutput = firmRpc.listByExample(firmDto);
            if (listBaseOutput.isSuccess() && CollectionUtils.isNotEmpty(listBaseOutput.getData())){
                List<Firm> data = listBaseOutput.getData();
                for (Firm firm : data){
                    malist.add(firm.getId());
                }
            }
        }
        return malist;
    }

    @Override
    public List<Long> getDepartmentDataAuth(Long userTicketId){
        UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
        userDataAuth.setUserId(userTicketId);
        userDataAuth.setRefCode(DataAuthType.DEPARTMENT.getCode());
        BaseOutput<List<Map>> out = dataAuthRpc.listUserDataAuthDetail(userDataAuth);
        List<Long> malist = new ArrayList<>();
        if (out.isSuccess() && CollectionUtils.isNotEmpty(out.getData())){
            List<Long> departmentCodeList = (List<Long>) out.getData().stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toList());
            malist.addAll(departmentCodeList);
        }
        return malist;
    }
}
