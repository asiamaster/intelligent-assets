
package com.dili.ia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DistrictController
 */
@Controller
@RequestMapping(value = "/district")
public class DistrictController {

    /**
     * 跳转到区域列表页
     */
    @RequestMapping
    public String list() {
        return "district/list";
    }
}
