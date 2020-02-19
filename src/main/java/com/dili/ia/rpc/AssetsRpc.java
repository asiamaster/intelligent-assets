package com.dili.ia.rpc;

import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "assets-service", url = "http://127.0.0.1:8182")
public interface AssetsRpc {

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

    /**
     * 新增摊位
     */
    @RequestMapping(value = "/api/booth/save", method = RequestMethod.POST)
    BaseOutput save(BoothDTO input);

    /**
     * 获取摊位列表
     */
    @RequestMapping(value = "/api/booth/list", method = RequestMethod.GET)
    String listPage(BoothDTO input);

    /**
     * 新增区域
     */
    @RequestMapping(value = "/api/district/save", method = RequestMethod.POST)
    BaseOutput addDistrict(DistrictDTO input);

    /**
     * 获取区域列表
     */
    @RequestMapping(value = "/api/district/list", method = RequestMethod.GET)
    String listDistrictPage(DistrictDTO input);
}
