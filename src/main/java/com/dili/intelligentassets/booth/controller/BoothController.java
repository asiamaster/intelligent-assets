package com.dili.intelligentassets.booth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * test
 */
@Controller
@RequestMapping("/booth")
public class BoothController {

    /**
     * test
     * @return
     */
    @RequestMapping
    public String test(){
        return "booth/list";
    }
}
