package com.dili.ia.rpc;

import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "assets-service")
public interface CategoryRpc {

    /**
     * 获取客户列表信息
     */
    @RequestMapping(value = "/api/category/getTree", method = RequestMethod.POST)
    BaseOutput<List<CategoryDTO>> list(CategoryDTO categoryDTO);

    /**
     * 新增品类
     */
    @RequestMapping(value = "/api/category/save", method = RequestMethod.POST)
    BaseOutput save(CategoryDTO dto);

    /**
     * 获取单个品类
     */
    @RequestMapping(value = "/api/category/get", method = RequestMethod.POST)
    BaseOutput<CategoryDTO> get(Long id);

    /**
     * 删除品类
     */
    @RequestMapping(value = "/api/category/batchUpdate", method = RequestMethod.POST)
    BaseOutput batchUpdate(@RequestParam("id") Long id, @RequestParam("value") Integer value);
}
