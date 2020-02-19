
package com.dili.ia.controller;

import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * DistrictController
 */
@Controller
@RequestMapping(value = "/district")
public class DistrictController {

    @Autowired
    private AssetsRpc assetsRpc;

    /**
     * 跳转到区域列表页
     */
    @RequestMapping("list.html")
    public String list() {
        return "district/list";
    }

    /**
     * 跳转导新增页面
     */
    @RequestMapping("add.html")
    public String toAdd() {
        return "district/add";
    }

    /**
     * 新增区域
     * @param input
     * @return
     */
    @RequestMapping("insert.action")
    @ResponseBody
    public BaseOutput save(DistrictDTO input) {
        return assetsRpc.addDistrict(input);
    }

    /**
     * 获取区域列表
     *
     * @param input
     * @return
     */
    @RequestMapping("/listPage.action")
    @ResponseBody
    public String listPage(DistrictDTO input) {
        return assetsRpc.listDistrictPage(input);
    }
}
