package com.dili.ia.controller;

import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 摊位控制器
 */
@Controller
@RequestMapping("/booth")
public class BoothController {

    @Autowired
    private AssetsRpc assetsRpc;


    /**
     * test
     *
     * @return
     */
    @RequestMapping("/list")
    public String test() {
        return "booth/list";
    }

    /**
     * 摊位列表
     *
     * @param input
     * @return
     */
    @RequestMapping("/listPage.action")
    @ResponseBody
    public String listPage(BoothDTO input) {
        return assetsRpc.listPage(input);
    }

    /**
     * add
     *
     * @return
     */
    @RequestMapping("/add.html")
    public String add() {
        return "booth/add";
    }

    /**
     * insert
     *
     * @param input
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    public BaseOutput save(BoothDTO input) {
        return assetsRpc.save(input);
    }

    /**
     * 新增BoothOrderR
     */
    @ApiOperation("新增booth")
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String list() {
        return "[{\"id\":1,\"name\":\"三号摊位\",\"rentAmount\":5000,\"manageAmount\":4000,\"depositAmount\":2000,\"number\":2000,\"unitCode\":\"001\",\"unitName\":\"平\",\"districtId\":1,\"districtName\":\"一号区域\"},{\"id\":2,\"name\":\"四号摊位\",\"rentAmount\":5000,\"manageAmount\":4000,\"depositAmount\":2000,\"number\":2000,\"unitCode\":\"001\",\"unitName\":\"平\",\"districtId\":1,\"districtName\":\"一号区域\"},{\"id\":3,\"name\":\"五号摊位\",\"rentAmount\":5000,\"manageAmount\":4000,\"depositAmount\":2000,\"number\":2000,\"unitCode\":\"001\",\"unitName\":\"平\",\"districtId\":1,\"districtName\":\"一号区域\"},{\"id\":4,\"name\":\"六号摊位\",\"rentAmount\":5000,\"manageAmount\":4000,\"depositAmount\":2000,\"number\":2000,\"unitCode\":\"001\",\"unitName\":\"平\",\"districtId\":1,\"districtName\":\"一号区域\"}]";
    }
}