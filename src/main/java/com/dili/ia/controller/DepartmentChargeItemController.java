package com.dili.ia.controller;

import com.dili.ia.domain.DepartmentChargeItem;
import com.dili.ia.domain.dto.BoutiqueFreeSetsDto;
import com.dili.ia.domain.dto.DepartmentChargeItemDto;
import com.dili.ia.domain.dto.OtherFeeDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.service.BoutiqueFreeSetsService;
import com.dili.ia.service.DepartmentChargeItemService;
import com.dili.ia.util.AssertUtils;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/8/19
 * @version:      农批业务系统重构
 * @description:  其他收费 收费项关联
 */
@Controller
@RequestMapping("/departmentChargeItem")
public class DepartmentChargeItemController {

    private final static Logger LOG = LoggerFactory.getLogger(DepartmentChargeItemController.class);

    @Autowired
    DepartmentChargeItemService departmentChargeItemService;

    /**
     * 跳转到其它收费管理 - 部门与收费项绑定列表
     * 并且去拉取数据字典中其他收费的收费项，更新表
     */
    @RequestMapping(value="/department.html", method = RequestMethod.GET)
    public String department(ModelMap modelMap) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        // 动态收费项的操作
        departmentChargeItemService.batchUpdateChargeItems(userTicket);

        return "otherFee/department";
    }

    /**
     * 分页查询otherFee，返回easyui分页信息
     *
     * @return String
     * @date   2020/8/19
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(){
        return departmentChargeItemService.listNoParam();
    }

    /**
     * 跳转到其它收费管理 - 新增部门与收费项绑定
     *
     * @param chargeItemId
     * @return String
     * @date   2020/8/19
     */
    @RequestMapping(value="/addDepartment.html", method = RequestMethod.GET)
    public String addDepartment(ModelMap modelMap, Long chargeItemId) {

        if (chargeItemId != null) {
            // 根据类型查询
            DepartmentChargeItemDto itemDto = departmentChargeItemService.selectListByChargeItemId(chargeItemId);
            modelMap.put("departmentChargeItem", itemDto);
        }

        return "otherFee/addDepartment";
    }

    /**
     * 新增部门与收费项的绑定
     *
     * @param departmentChargeItemDto
     * @return BaseOutput
     * @date   2020/8/19
     */
    @RequestMapping(value="/doAddDepartment.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput addDepartmentCharge(@RequestBody DepartmentChargeItemDto departmentChargeItemDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(departmentChargeItemDto.getChargeItemId(), "收费项 id 不能为空");

            // 绑定操作
            departmentChargeItemService.addDepartmentChargeItems(departmentChargeItemDto, userTicket);

            return BaseOutput.success("收费项绑定部门成功");
        } catch (BusinessException e){
            LOG.info("收费项绑定部门异常:{}", e.getMessage());
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e){
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 根据部门查询关联的其他收费的收费项
     *
     * @param departmentId
     * @return BaseOutput
     * @date   2020/8/19
     */
    @RequestMapping(value="/getChargeItemsByDepartment.action", method = RequestMethod.GET)
    public @ResponseBody BaseOutput getChargeItemsByDepartment(Long departmentId) {
        if (departmentId == null) {
            return BaseOutput.failure("部门 id 不能为空");
        }

        // 根据类型查询
        List<DepartmentChargeItemDto> departmentChargeItemDtoList = departmentChargeItemService.getChargeItemsByDepartment(departmentId);
        return BaseOutput.success().setData(departmentChargeItemDtoList);
    }
}