package com.dili.ia.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.rpc.UserRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 摊位控制器
 */
@Controller
@RequestMapping("/booth")
public class BoothController {

    @Autowired
    private AssetsRpc assetsRpc;

    /**
     * 新增BoothOrderR
     */
    @GetMapping(value = "/search.action")
    public @ResponseBody
    BaseOutput<List<AssetsDTO>> search(String keyword) {
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
            return BaseOutput.success().setData(new ArrayList<>());
        }

    }
}