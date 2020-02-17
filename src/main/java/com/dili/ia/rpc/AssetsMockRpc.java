package com.dili.ia.rpc;

import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "assets-mock-service", url = "http://rap2api.taobao.org/app/mock/244604")
public interface AssetsMockRpc {

    @RequestMapping(value = "/api/booth/search", method = RequestMethod.POST)
    BaseOutput searchBooth(String keyword);
}
