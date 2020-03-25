package com.dili.ia.rpc;

import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "assets-mock-service")
public interface AssetsMockRpc {

    @RequestMapping(value = "/api/booth/search", method = RequestMethod.POST)
    BaseOutput searchBooth(@RequestParam("keyword") String keyword, @RequestParam("marketId") Long marketId);
}
