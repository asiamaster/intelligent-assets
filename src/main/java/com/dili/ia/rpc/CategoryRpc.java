package com.dili.ia.rpc;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "assets-service")
public interface CategoryRpc {

}
