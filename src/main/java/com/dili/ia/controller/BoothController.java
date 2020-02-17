package com.dili.ia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 摊位控制器
 */
@Controller
@RequestMapping("/booth")
public class BoothController {

    /**
     * test
     * @return
     */
    @RequestMapping("/list")
    public String test(){
        return "booth/list";
    }

    /**
     * add
     * @return
     */
    @RequestMapping("/add")
    public String add(){
        return "booth/add";
    }
}