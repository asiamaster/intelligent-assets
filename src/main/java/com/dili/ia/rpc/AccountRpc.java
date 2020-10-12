package com.dili.ia.rpc;

import com.dili.ia.domain.account.CardInfo;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "account-service")
public interface AccountRpc {

    /**
     * 获取卡信息
     */
    @RequestMapping(value = "/api/account/simpleInfo", method = RequestMethod.GET)
    BaseOutput<CardInfo> cardInfo(@RequestParam("cardNo") String cardNo, @RequestParam("firmId") Long firmId);

}
