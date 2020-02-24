
package com.dili.ia.controller;

import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * DistrictController
 */
@Controller
@RequestMapping(value = "/district")
public class DistrictController {

    @Autowired
    private AssetsRpc assetsRpc;

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
    public BaseOutput save(DistrictDTO input) {
        return assetsRpc.addDistrict(input);
    }


    /**
     * 拆分区域
     */
    @RequestMapping("divisionSave.action")
    @ResponseBody
    public BaseOutput divisionSave(Long parentId, String[] names, String[] notes, String[] numbers) {
        return assetsRpc.divisionSave(parentId, names, notes, numbers);
    }


    /**
     * 修改区域
     *
     * @param input
     * @return
     */
    @RequestMapping("edit.action")
    @ResponseBody
    public BaseOutput edit(DistrictDTO input) {
        return assetsRpc.editDistrict(input);
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
        return assetsRpc.listDistrictPage(input);
    }


    /**
     * 删除区域
     */
    @RequestMapping("delete.action")
    @ResponseBody
    public BaseOutput delete(Long id) {
        return assetsRpc.delDistrictById(id);
    }
}
