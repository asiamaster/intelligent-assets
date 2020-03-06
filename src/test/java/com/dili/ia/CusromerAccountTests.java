package com.dili.ia;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.dto.CustomerQuery;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ss.domain.BaseOutput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-05 14:20
 */
@SpringBootTest(classes = IAApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CusromerAccountTests {

    @Autowired
    CustomerAccountService customerAccountService;

    @Autowired
    CustomerRpc customerRpc;

    @Test
    void testFrozenAccount() {
        Long orderId = 1L;
        String orderCode = "202003050001";
        Long customerId = 1L;
        Long earnestDeduction = 100L;
        Long transferDeduction = 100L;
        Long depositDeduction = 100L;
        Long marketId = 1L;

        BaseOutput out = customerAccountService.submitLeaseOrderCustomerAmountFrozen(orderId, orderCode, customerId, earnestDeduction, transferDeduction, depositDeduction, marketId);

        System.out.println("----------------" + out.isSuccess());
    }

    @Test
    void testCustomer() {
        CustomerQuery query = new CustomerQuery();
        query.setMarketId(1L);
        BaseOutput<List<Customer>> out = customerRpc.list(query);
        List<Customer> list = out.getData();
        System.out.println(list.size());

    }
}
