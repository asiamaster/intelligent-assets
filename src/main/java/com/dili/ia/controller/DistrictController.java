
package com.dili.ia.controller;

import cn.hutool.core.util.ArrayUtil;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * DistrictController
 */
@Controller
@RequestMapping(value = "/district")
public class DistrictController {

    @Autowired
    private AssetsRpc assetsRpc;

    @Autowired
    private DepartmentRpc departmentRpc;

    /**
     * 跳转到区域列表页
     */
    @RequestMapping("list.html")
    public String list() {
        return "district/list";
    }

    /**
     * 跳转导新增页面
     */
    @RequestMapping("add.html")
    public String toAdd() {
        return "district/add";
    }

    /**
     * 跳转导新增页面
     */
    @RequestMapping("edit.html")
    public String toEdit(Long id, ModelMap map) {
        BaseOutput<DistrictDTO> districtById = assetsRpc.getDistrictById(id);
        map.put("obj", districtById.getData());
        return "district/edit";
    }

    /**
     * 跳转导划分区域页面
     */
    @RequestMapping("division.html")
    public String toDivision(Long id, ModelMap map) {
        BaseOutput<DistrictDTO> districtById = assetsRpc.getDistrictById(id);
        map.put("obj", districtById.getData());
        return "district/division";
    }

    /**
     * 新增区域
     *
     * @param input
     * @return
     */
    @RequestMapping("insert.action")
    @ResponseBody
    @BusinessLogger(businessType = LogBizTypeConst.DISTRICT, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    public BaseOutput save(DistrictDTO input) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            input.setCreateTime(new Date());
            input.setCreatorId(userTicket.getId());
            input.setModifyTime(new Date());
            input.setMarketId(userTicket.getFirmId());
            BaseOutput baseOutput = assetsRpc.addDistrict(input);
            LoggerUtil.buildLoggerContext(input.getId(), input.getNumber(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            return baseOutput;
        } catch (Exception e) {
            return BaseOutput.failure("系统异常");
        }
    }


    /**
     * 拆分区域
     */
    @RequestMapping("divisionSave.action")
    @ResponseBody
    @BusinessLogger(businessType = LogBizTypeConst.DISTRICT, content = "", operationType = "split", systemCode = "INTELLIGENT_ASSETS")
    public BaseOutput divisionSave(Long parentId, String[] names, String[] notes, String[] numbers) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            BaseOutput baseOutput = assetsRpc.divisionSave(parentId, names, notes, numbers);
            LoggerUtil.buildLoggerContext(parentId, null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            return baseOutput;
        } catch (Exception e) {
            return BaseOutput.failure("系统异常");
        }
    }


    /**
     * 修改区域
     *
     * @param input
     * @return
     */
    @RequestMapping("edit.action")
    @ResponseBody
    @BusinessLogger(businessType = LogBizTypeConst.DISTRICT, content = "", operationType = "edit", systemCode = "INTELLIGENT_ASSETS")
    public BaseOutput edit(DistrictDTO input) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            input.setModifyTime(new Date());
            BaseOutput baseOutput = assetsRpc.editDistrict(input);
            LoggerUtil.buildLoggerContext(input.getId(), input.getNumber(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            return baseOutput;
        } catch (Exception e) {
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 获取区域列表
     *
     * @param input
     * @return
     */
    @RequestMapping("/listPage.action")
    @ResponseBody
    public String listPage(DistrictDTO input) {
        if (input == null) {
            input = new DistrictDTO();
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        input.setMarketId(userTicket.getFirmId());
        if (input.getDepartmentId() == null) {
            List<Department> departments = departmentRpc.listUserAuthDepartmentByFirmId(userTicket.getId(), userTicket.getFirmId()).getData();
            long[] ids = departments.stream().mapToLong(Department::getId).toArray();
            if(ids.length > 0){
                input.setDeps(ArrayUtil.join(ids, ","));
            }
        }
        return assetsRpc.listDistrictPage(input);
    }


    /**
     * 删除区域
     */
    @RequestMapping("delete.action")
    @ResponseBody
    @BusinessLogger(businessType = LogBizTypeConst.DISTRICT, content = "", operationType = "del", systemCode = "INTELLIGENT_ASSETS")
    public BaseOutput delete(Long id) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            BaseOutput baseOutput = assetsRpc.delDistrictById(id);
            LoggerUtil.buildLoggerContext(id, null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            return baseOutput;
        } catch (Exception e) {
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 删除区域
     */
    @RequestMapping("search.action")
    @ResponseBody
    public BaseOutput<List<DistrictDTO>> search(DistrictDTO input) {
        if (input == null) {
            input = new DistrictDTO();
        }
        input.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        return assetsRpc.searchDistrict(input);
    }

}
