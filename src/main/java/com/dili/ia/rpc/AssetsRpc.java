package com.dili.ia.rpc;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.assets.sdk.dto.BoothRentDTO;
import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "assets-service")
public interface AssetsRpc {

    /**
     * 获取客户列表信息
     */
    @RequestMapping(value = "/api/customCategory/getTree", method = RequestMethod.POST)
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
     * 修改区域
     */
    @RequestMapping(value = "/api/district/edit", method = RequestMethod.POST)
    BaseOutput editDistrict(DistrictDTO input);

    /**
     * 获取区域列表
     */
    @RequestMapping(value = "/api/district/list", method = RequestMethod.GET)
    String listDistrictPage(DistrictDTO input);

    /**
     * 获取单个区域
     */
    @RequestMapping(value = "/api/district/get", method = RequestMethod.POST)
    BaseOutput<DistrictDTO> getDistrictById(Long id);

    /**
     * 删除区域
     */
    @RequestMapping(value = "/api/district/delete", method = RequestMethod.POST)
    BaseOutput delDistrictById(Long id);

    /**
     * 拆分区域
     */
    @RequestMapping(value = "/api/district/divisionSave", method = RequestMethod.POST)
    BaseOutput divisionSave(@RequestParam("parentId") Long parentId, @RequestParam("names") String[] names, @RequestParam("notes") String[] notes, @RequestParam("numbers") String[] numbers);

    /**
     * 根据ID查询本身和子节点
     */
    @RequestMapping(value = "/api/district/listChild", method = RequestMethod.POST)
    BaseOutput<List<DistrictDTO>> listDistrictChild(Long id);

    /**
     * 修改搜索
     */
    @RequestMapping(value = "/api/district/search", method = RequestMethod.POST)
    BaseOutput<List<DistrictDTO>> searchDistrict(DistrictDTO input);

    /**
     * 获取单个摊位
     */
    @RequestMapping(value = "/api/booth/get", method = RequestMethod.POST)
    BaseOutput<BoothDTO> getBoothById(Long id);

    /**
     * 修改摊位
     */
    @RequestMapping(value = "/api/booth/update", method = RequestMethod.POST)
    BaseOutput updateBooth(BoothDTO input);

    /**
     * 删除摊位
     */
    @RequestMapping(value = "/api/booth/delete", method = RequestMethod.POST)
    BaseOutput delBoothById(Long id);

    /**
     * 删除摊位
     */
    @RequestMapping(value = "/api/booth/getBoothBalance", method = RequestMethod.POST)
    BaseOutput<Double> getBoothBalance(Long id);

    /**
     * 拆分摊位
     */
    @RequestMapping(value = "/api/booth/split", method = RequestMethod.POST)
    BaseOutput boothSplit(@RequestParam("parentId") Long parentId, @RequestParam("names") String[] names, @RequestParam("notes") String notes, @RequestParam("numbers") String[] numbers);

    /**
     * 搜索摊位
     */
    @RequestMapping(value = "/api/booth/search", method = RequestMethod.POST)
    BaseOutput<List<BoothDTO>> searchBooth(JSONObject query);

    /**
     * 新增摊位租赁时间，默认为冻结
     */
    @RequestMapping(value = "/api/boothRent/add", method = RequestMethod.POST)
    BaseOutput addBoothRent(BoothRentDTO input);

    /**
     * 根据@param{摊位}和@param{单号}修改出粗状态为已出租
     */
    @RequestMapping(value = "/api/boothRent/rent", method = RequestMethod.POST)
    BaseOutput rentBoothRent(BoothRentDTO input);

    /**
     * 根据@param{摊位}和@param{单号}删除信息
     */
    @RequestMapping(value = "/api/boothRent/delete", method = RequestMethod.POST)
    BaseOutput deleteBoothRent(BoothRentDTO input);

    /**
     * 根据@param{摊位}和@param{单号}修改@param{结束时间}
     */
    @RequestMapping(value = "/api/boothRent/updateEnd", method = RequestMethod.POST)
    BaseOutput updateEndBoothRent(BoothRentDTO input);

}
