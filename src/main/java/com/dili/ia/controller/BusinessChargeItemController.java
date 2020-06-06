package com.dili.ia.controller;

import com.dili.assets.sdk.dto.ChargeItemDto;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 16:13:04.
 */
@Api("/leaseChargeItem")
@Controller
@RequestMapping("/leaseChargeItem")
public class BusinessChargeItemController {
    private final static Logger LOG = LoggerFactory.getLogger(BusinessChargeItemController.class);
    @Autowired
    BusinessChargeItemService businessChargeItemService;
    @Autowired
    private BusinessChargeItemRpc businessChargeItemRpc;

    /**
     * 收费项目查询
     * @param businessId
     * @param isEnable
     * @return
     */
    @RequestMapping(value = "/listItemByBusiness.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<List<ChargeItemDto>> listItemByBusiness(@RequestParam("businessId") Long businessId, Boolean isEnable) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        try {
            return businessChargeItemRpc.listItemByMarketAndBusiness(userTicket.getFirmId(), businessId, isEnable);
        }catch (Exception e){
            LOG.error("收费项目查询异常！", e);
            return BaseOutput.failure(e.getMessage());
        }

    }

}