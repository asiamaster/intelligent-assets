package com.dili.ia.controller;

import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.util.PinyinUtil;
import com.dili.ss.domain.BaseOutput;
import io.seata.spring.annotation.GlobalTransactional;
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
    private AssetsRpc assetsRpc;

    /**
     * 跳转到品类列表页
     */
    @RequestMapping("list.html")
    @GlobalTransactional
    public String list() {
        return "category/list";
    }

    /**
     * 返回添加品类页面片段
     */
    @RequestMapping("addView.html")
    public String toAdd(Long pid, ModelMap map) {
        // 品类未选择或者选择根品类时，添加页面不显示上级品类字段
        if (pid != null && pid != 0) {
            map.put("pid", pid);
            CategoryDTO data = assetsRpc.get(pid).getData();
            map.put("parentName", data.getName());
        }
        return "category/add";
    }

    /**
     * 返回修改品类页面片段
     */
    @RequestMapping("editView.html")
    public String toEdit(Long id, ModelMap map) {
        map.put("obj", assetsRpc.get(id).getData());
        return "category/edit";
    }

    /**
     * 中文转拼音
     */
    @RequestMapping(value = "/convert.action")
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
    @RequestMapping(value = "/getTree.action")
    @ResponseBody
    public BaseOutput<List<CategoryDTO>> getTree(CategoryDTO input) {
        return assetsRpc.list(input);
    }

    /**
     * 新增品类
     */
    @RequestMapping(value = "/save.action")
    @ResponseBody
    public BaseOutput save(CategoryDTO input) {
        return assetsRpc.save(input);
    }

    /**
     * 品类列表页面
     *
     * @param input
     * @return
     */
    @RequestMapping("/table.html")
    public ModelAndView list(@ModelAttribute CategoryDTO input) {
        List<CategoryDTO> results = new ArrayList<>();

        if (input.getParent() != null && input.getParent() != 0) {
            CategoryDTO c = assetsRpc.get(input.getParent()).getData();
            if (c.getState() != 3) {
                results.add(c);
            }
        }
        Map<String, Object> map = new HashMap<>();
        List<CategoryDTO> list = assetsRpc.list(input).getData();
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
    @RequestMapping(value = "/batchUpdate.action")
    @ResponseBody
    public BaseOutput batchUpdate(Long id, Integer value) {
        return assetsRpc.batchUpdate(id, value);
    }
}
