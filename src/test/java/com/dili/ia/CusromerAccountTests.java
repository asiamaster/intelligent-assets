package com.dili.ia;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.dto.CustomerQuery;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.DepositBalanceService;
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

    @Autowired
    DepositBalanceService depositBalanceService;

    @Test
    void testFrozenAccount() {
        Long orderId = 1L;
        String orderCode = "test202003050001";
        Long customerId = 4L; //张三
        Long earnestDeduction = 100L;
        Long transferDeduction = 100L;
        Long marketId = 11L; //杭州水产
//        Long startTime = System.currentTimeMillis();
//        BaseOutput out = customerAccountService.submitLeaseOrderCustomerAmountFrozen(orderId, orderCode, customerId, earnestDeduction, transferDeduction, marketId, 1L,"克兰");
//        Long endTime = System.currentTimeMillis();
//        Long time = endTime - startTime;
//        System.out.println("------接口耗时: ms----------" + time);
//        System.out.println("-------------------------" + out.isSuccess() + out.getMessage());
    }

    @Test
    void testCustomer() {
        CustomerQuery query = new CustomerQuery();
        query.setMarketId(1L);
        BaseOutput<List<Customer>> out = customerRpc.list(query);
        List<Customer> list = out.getData();
        System.out.println(list.size());

    }

    @Test
    void testRechargeEarnestBalance(){
        CustomerAccountParam caParam = new CustomerAccountParam();
        caParam.setBizType(BizTypeEnum.EARNEST.getCode());
        caParam.setAmount(1L);
        caParam.setCustomerId(3L); //克兰
        caParam.setOrderId(1L);
        caParam.setOrderCode("23534534543");
        caParam.setOperaterId(1L);
        caParam.setOperatorName("测试");
        caParam.setSceneType(TransactionSceneTypeEnum.TRANSFER_IN.getCode());
        caParam.setMarketId(11L);

//        BaseOutput count = customerAccountService.rechargeEarnestBalance(caParam);

//        CustomerAccountMapper.addEarnestBalance(ca.getId(), caParam.getAmount());

    }
    @Test
    void testAddDepositBalance(){

//        Integer count = depositBalanceService.addDepositBalance(60L, 100L);

//        System.out.println("-------------------- count :" + count);

    }



}
