package com.dili.ia.controller;

import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
}