package com.dili.ia.controller;

import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.ia.rpc.CategoryRpc;
import com.dili.ia.util.PinyinUtil;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CategoryController
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRpc categoryRpc;

    /**
     * 跳转到品类列表页
     */
    @RequestMapping
    public String list() {
        return "category/list";
    }

    /**
     * 返回添加品类页面片段
     */
    @RequestMapping("addView")
    public String toAdd(Long pid, ModelMap map) {
        // 品类未选择或者选择根品类时，添加页面不显示上级品类字段
        if (pid != null && pid != 0) {
            map.put("pid", pid);
            CategoryDTO data = categoryRpc.get(pid).getData();
            map.put("parentName", data.getName());
        }
        return "category/add";
    }

    /**
     * 返回修改品类页面片段
     */
    @RequestMapping("editView")
    public String toEdit(Long id, ModelMap map) {
        map.put("obj", categoryRpc.get(id).getData());
        return "category/edit";
    }

    /**
     * 中文转拼音
     */
    @RequestMapping(value = "/convert")
    @ResponseBody
    public Map<String, Object> convert(String name) {
        Map<String, Object> map = new HashMap<>();
        if (name == null || name.trim().length() == 0) {
            map.put("first", "");
            map.put("whole", "");
            return map;
        }
        map.put("first", PinyinUtil.converterToFirstSpell(name));
        map.put("whole", PinyinUtil.converterToSpell(name));
        return map;
    }

    /**
     * 获取品类树
     */
    @RequestMapping(value = "/getTree")
    @ResponseBody
    public BaseOutput<List<CategoryDTO>> getTree(CategoryDTO input) {
        return categoryRpc.list(input);
    }

    /**
     * 新增品类
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public BaseOutput save(CategoryDTO input) {
        return categoryRpc.save(input);
    }

    /**
     * 品类列表页面
     *
     * @param input
     * @return
     */
    @RequestMapping("/table")
    public ModelAndView list(@ModelAttribute CategoryDTO input) {
        List<CategoryDTO> results = new ArrayList<>();

        if (input.getParent() != null && input.getParent() != 0) {
            CategoryDTO c = categoryRpc.get(input.getParent()).getData();
            results.add(c);
        }
        Map<String, Object> map = new HashMap<>();
        List<CategoryDTO> list = categoryRpc.list(input).getData();
        results.addAll(list);
        map.put("obj", results);
        return new ModelAndView("/category/table", map);
    }

    /**
     * 批量修改状态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/batchUpdate")
    @ResponseBody
    public BaseOutput batchUpdate(Long id, Integer value) {
        return categoryRpc.batchUpdate(id, value);
    }
}