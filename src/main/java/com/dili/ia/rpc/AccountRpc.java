package com.dili.ia.rpc;

import com.dili.ia.domain.account.AccountInfo;
import com.dili.ia.domain.account.CardInfo;
import com.dili.ia.domain.account.CardQuery;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "account-service")
public interface AccountRpc {

    /**
     * 获取卡信息
     */
    @RequestMapping(value = "/api/account/simpleInfo", method = RequestMethod.GET)
    BaseOutput<CardInfo> cardInfo(@RequestParam("cardNo") String cardNo, @RequestParam("firmId") Long firmId);

    /**
     * 获取客户园区卡
     */
    @RequestMapping(value = "/api/account/getList", method = RequestMethod.POST)
    BaseOutput<List<AccountInfo>> getList(@RequestBody CardQuery query);

}
