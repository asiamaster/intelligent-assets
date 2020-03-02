package com.dili.ia.controller;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.dto.CustomerQuery;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api("/customer")
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerRpc customerRpc;

   /**
     * 新增CustomerOrder
     * @param customer
     * @return BaseOutput
     */
    @ApiOperation("新增CustomerOrder")
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Customer> list(CustomerQuery customer) throws CloneNotSupportedException {
        List<Customer> cus = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("蒋成勇");
        customer1.setCellphone("13558720686");
        customer1.setCertificateNumber("511602198902422586");
        cus.add(customer1);

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("克兰");
        customer2.setCellphone("18781998571");
        customer2.setCertificateNumber("513023199201206627");
        cus.add(customer2);

        cus.add((Customer) customer1.clone());
        cus.add((Customer) customer1.clone());
        cus.add((Customer) customer1.clone());
        cus.add((Customer) customer1.clone());

        return cus;

//        return customerRpc.list(customer).getData();
    }
}