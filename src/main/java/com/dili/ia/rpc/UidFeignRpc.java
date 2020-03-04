package com.dili.ia.rpc;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author wangmi
 * @date 2020/3/3
 */
@FeignClient(name = "dili-uid")
public interface UidFeignRpc {


    /**
     * 根据业务类型获取业务号
     * @param type
     * @return
     */
    @RequestMapping(value = "/api/bizNumber", method = RequestMethod.POST)
    BaseOutput<String> bizNumber(@RequestParam(value = "type") String type);


}