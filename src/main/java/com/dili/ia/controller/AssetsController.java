package com.dili.ia.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 摊位控制器
 */
@Controller
@RequestMapping("/assets")
public class AssetsController {
    private final static Logger LOG = LoggerFactory.getLogger(AssetsController.class);

    @Autowired
    private AssetsRpc assetsRpc;

    /**
     * 新增BoothOrderR
     */
    @GetMapping(value = "/searchAssets.action")
    public @ResponseBody
    BaseOutput<List<AssetsDTO>> searchAssets(String keyword) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        JSONObject json = new JSONObject();
        json.put("keyword", keyword);
        json.put("marketId", userTicket.getFirmId());
        try {
            List<AssetsDTO> data = assetsRpc.searchBooth(json).getData();
            List<AssetsDTO> result = new ArrayList<>();
            if (CollUtil.isNotEmpty(data)) {
                for (AssetsDTO dto : data) {
                    if (dto.getParentId() != 0 && dto.getState().equals(EnabledStateEnum.ENABLED.getCode())) {
                        result.add(dto);
                    } else {
                        boolean anyMatch = data.stream().anyMatch(obj -> obj.getParentId().equals(dto.getId()));
                        if (!anyMatch && dto.getParentId() == 0 && dto.getState().equals(EnabledStateEnum.ENABLED.getCode())) {
                            result.add(dto);
                        }
                    }
                }
            }
            return BaseOutput.success().setData(result);
        } catch (Exception e) {
            LOG.error("资产查询接口异常",e);
            return BaseOutput.success().setData(new ArrayList<>());
        }

    }

    /**
     * list Category
     */
    @GetMapping(value = "/searchCategory.action")
    public @ResponseBody
    BaseOutput<List<CusCategoryDTO>> searchCategory(String keyword) {
        CusCategoryQuery categoryDTO = new CusCategoryQuery();
        categoryDTO.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        categoryDTO.setState(EnabledStateEnum.ENABLED.getCode());
        if (null == keyword) {
            categoryDTO.setParent(0L);
        } else {
            categoryDTO.setKeyword(keyword);
        }
        try {
            return assetsRpc.listCusCategory(categoryDTO);
        } catch (Exception e) {
            return BaseOutput.success().setData(new ArrayList<>());
        }
    }
}