package com.dili.ia.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.rpc.AssetsMockRpc;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.UserRpc;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;

/**
 * 摊位控制器
 */
@Controller
@RequestMapping("/booth")
public class BoothController {

    @Autowired
    private AssetsRpc assetsRpc;

    @Autowired
    private UserRpc userRpc;


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
        if (input == null) {
            input = new BoothDTO();
        }
        input.setIsDelete(YesOrNoEnum.NO.getCode());
        input.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
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
     * add
     *
     * @return
     */
    @RequestMapping("/split.html")
    public String split(Long id, ModelMap map) {
        map.put("obj", assetsRpc.getBoothById(id).getData());
        BaseOutput<Double> boothBalance = assetsRpc.getBoothBalance(id);
        map.put("number", boothBalance.getData());
        return "booth/split";
    }

    /**
     * edit
     *
     * @return
     */
    @RequestMapping("/update.html")
    public String update(Long id, ModelMap map) {
        map.put("obj", assetsRpc.getBoothById(id).getData());
        return "booth/edit";
    }

    /**
     * edit
     *
     * @return
     */
    @RequestMapping("/view.html")
    public String view(Long id, ModelMap map) {
        BoothDTO data = assetsRpc.getBoothById(id).getData();
        if (data != null && data.getCreatorId() != null) {
            BaseOutput<User> userBaseOutput = userRpc.get(data.getCreatorId());
            if (userBaseOutput.isSuccess()) {
                data.setCreatorUser(userBaseOutput.getData().getRealName());
            }
        }
        map.put("obj", data);
        return "booth/view";
    }

    /**
     * insert
     *
     * @param input
     * @return
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public BaseOutput save(BoothDTO input) {
        input.setCreateTime(new Date());
        input.setCreatorId(SessionContext.getSessionContext().getUserTicket().getId());
        input.setModifyTime(new Date());
        input.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        input.setState(EnabledStateEnum.DISABLED.getCode());
        return assetsRpc.save(input);
    }

    /**
     * insert
     *
     * @param input
     * @return
     */
    @RequestMapping("/update.action")
    @ResponseBody
    public BaseOutput update(BoothDTO input) {
        input.setModifyTime(new Date());
        return assetsRpc.updateBooth(input);
    }

    /**
     * split
     */
    @RequestMapping("/split.action")
    @ResponseBody
    public BaseOutput split(Long parentId, String[] names, String notes, String[] numbers) {
        assetsRpc.boothSplit(parentId, names, notes, numbers);
        return BaseOutput.success();
    }


    /**
     * delete
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete.action")
    @ResponseBody
    public BaseOutput delete(Long id) {
        return assetsRpc.delBoothById(id);
    }

    /**
     * 新增BoothOrderR
     */
    @ApiOperation("新增booth")
    @RequestMapping(value = "/search.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String search(String keyword) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        JSONObject json = new JSONObject();
        json.put("keyword", keyword);
        json.put("marketId", userTicket.getFirmId());
        try {
            return JSON.toJSONString(assetsRpc.searchBooth(json).getData());
        }catch (Exception e){
            return "[]";
        }
    }
}