package com.dili.ia.controller;

import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.domain.dto.CustomerSimpleExtendDto;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @SuppressWarnings("all")
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
    @RequestMapping(value="/listNormal.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    PageOutput<List<CustomerSimpleExtendDto>> listNormal(String likeName, String certificateNumberMatch, String certificateNumber, String keyword) throws CloneNotSupportedException {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return PageOutput.failure("未登录");
        }
        CustomerQueryInput customerQuery = new CustomerQueryInput();
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
        try {
            return customerRpc.listSimpleNormalPage(customerQuery);
        } catch (Exception e) {
            return PageOutput.failure("服务器异常");
        }

    }
    
    /**
     * 查询个人客户  individual
     * @param likeName
     * @return
     * @throws CloneNotSupportedException
     */
    @RequestMapping(value="/listNormalV1.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput<List<CustomerExtendDto>> listNormal(String likeName) throws CloneNotSupportedException {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        CustomerQueryInput customerQuery = new CustomerQueryInput();
        if(StringUtils.isNotBlank(likeName)){
            customerQuery.setName(likeName);
        }
        customerQuery.setOrganizationType("individual");
        customerQuery.setMarketId(userTicket.getFirmId());
        return BaseOutput.success().setData(customerRpc.listSimpleNormalPage(customerQuery).getData());
    }
}