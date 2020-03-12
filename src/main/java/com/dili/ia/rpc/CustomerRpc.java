package com.dili.ia.rpc;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.dto.CustomerQuery;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "customer-service",url = "http://10.28.10.84:8181")
public interface CustomerRpc {

    /** 获取客户列表信息
     * @param customer
     * @return
             */
    @RequestMapping(value = "/api/customer/list", method = RequestMethod.POST)
    BaseOutput<List<Customer>> list(CustomerQuery customer);

    /** 根据市场ID和客户ID，获取客户
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    @RequestMapping(value = "/api/customer/get", method = RequestMethod.POST)
    BaseOutput<Customer> get(@RequestParam("id") Long id, @RequestParam("marketId") Long marketId);

}
