package com.dili.ia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CategoryController
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    /**
     * list
     * @return
     */
    @RequestMapping
    public String list(){
        return "category/list";
    }
}
