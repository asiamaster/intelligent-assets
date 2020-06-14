package com.dili.ia.controller;

import com.dili.assets.sdk.dto.ChargeItemDto;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 16:13:04.
 */
@Controller
@RequestMapping("/businessChargeItem")
public class BusinessChargeItemController {
    private final static Logger LOG = LoggerFactory.getLogger(BusinessChargeItemController.class);
    @Autowired
    BusinessChargeItemService businessChargeItemService;
    @Autowired
    private BusinessChargeItemRpc businessChargeItemRpc;

    /**
     * 收费项目查询
     * @return
     */
    @RequestMapping(value = "/queryBusinessChargeItem.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput<List<Map<String, String>>> queryBusinessChargeItem() {
        List<ChargeItemDto> chargeItemDtos = new ArrayList<>();
        ChargeItemDto chargeItemDto = new ChargeItemDto();
        chargeItemDto.setId(1L);
        chargeItemDto.setName("水费");
        chargeItemDtos.add(chargeItemDto);

        ChargeItemDto chargeItemDto1 = new ChargeItemDto();
        chargeItemDto1.setId(2L);
        chargeItemDto1.setName("电费");
        chargeItemDtos.add(chargeItemDto1);
        try {
            return BaseOutput.success().setData(businessChargeItemService.queryBusinessChargeItem("1", Arrays.asList(1L,2L),chargeItemDtos));
        }catch (Exception e){
            LOG.error("收费项目查询异常！", e);
            return BaseOutput.failure(e.getMessage());
        }

    }

}