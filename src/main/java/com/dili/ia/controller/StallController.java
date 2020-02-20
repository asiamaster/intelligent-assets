package com.dili.ia.controller;

import com.dili.ia.rpc.StallRpc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Api("/stall")
@Controller
@RequestMapping("/stall")
public class StallController {
   /* @Autowired
    StallRpc stallRpc;*/

   /**
     * 新增StallOrder
     */
   @ApiOperation("新增Stall")
   @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public String list() throws CloneNotSupportedException {
       return "[{\"id\":1,\"name\":\"三号摊位\",\"rentAmount\":5000,\"manageAmount\":4000,\"depositAmount\":2000},{\"id\":2,\"name\":\"四号摊位\",\"rentAmount\":5000,\"manageAmount\":4000,\"depositAmount\":2000},{\"id\":3,\"name\":\"五号摊位\",\"rentAmount\":5000,\"manageAmount\":4000,\"depositAmount\":2000},{\"id\":4,\"name\":\"六号摊位\",\"rentAmount\":5000,\"manageAmount\":4000,\"depositAmount\":2000}]";
   }
}