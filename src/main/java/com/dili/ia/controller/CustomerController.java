package com.dili.ia.controller;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.dto.CustomerQuery;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Api("/customer")
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerRpc customerRpc;

    /**
     * 查询客户
     * @param likeName
     * @param certificateNumberMatch
     * @param certificateNumber
     * @return
     * @throws CloneNotSupportedException
     */
    @ApiOperation("查询CustomerOrder")
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Customer> list(String likeName, String certificateNumberMatch,String certificateNumber,String keyword) throws CloneNotSupportedException {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return null;
        }
        CustomerQuery customerQuery = new CustomerQuery();
        if(StringUtils.isNotBlank(likeName)){
            customerQuery.setName(likeName);
        }else if(StringUtils.isNotBlank(certificateNumberMatch)){
            customerQuery.setCertificateNumberMatch(certificateNumberMatch);
        }else if(StringUtils.isNotBlank(certificateNumber)){
            customerQuery.setCertificateNumber(certificateNumber);
        }else if(StringUtils.isNotBlank(keyword)){
            customerQuery.setKeyword(keyword);
        }
        customerQuery.setMarketId(userTicket.getFirmId());
        List<Customer> list = customerRpc.list(customerQuery).getData();
        return list;
    }
}