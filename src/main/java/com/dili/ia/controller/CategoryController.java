package com.dili.ia.controller;

import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.ia.util.PinyinUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

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
    public String list() {
        return "category/list";
    }

    /**
     * 返回添加品类页面片段
     */
    @RequestMapping("addView.html")
    public String toAdd(Long pid, ModelMap map) {
        // 品类未选择或者选择根品类时，添加页面不显示上级品类字段
        if (pid == null) {
            pid = 0L;
        }
        if (pid != 0) {
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
        try {
            return assetsRpc.list(input);
        } catch (Exception e) {
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 新增品类
     */
    @RequestMapping(value = "/save.action")
    @ResponseBody
    @BusinessLogger(businessType = LogBizTypeConst.CATEGORY, content = "${contractNo}", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    public BaseOutput save(CategoryDTO input) {
        try {
            input.setCreateTime(new Date());
            input.setCreatorId(SessionContext.getSessionContext().getUserTicket().getId());
            input.setModifyTime(new Date());
            if (input.getId() != null) {
                LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "edit");
            }
            BaseOutput save = assetsRpc.save(input);
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            LoggerUtil.buildLoggerContext(input.getId(), input.getName(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            return save;
        } catch (Exception e) {
            return BaseOutput.failure("系统异常");
        }
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
        return new ModelAndView("category/table", map);
    }

    /**
     * 批量修改状态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/batchUpdate.action")
    @ResponseBody
    @BusinessLogger(businessType = LogBizTypeConst.CATEGORY, content = "${contractNo}", operationType = "edit", systemCode = "INTELLIGENT_ASSETS")
    public BaseOutput batchUpdate(Long id, Integer value) {
        try {
            BaseOutput baseOutput = assetsRpc.batchUpdate(id, value);
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (value.equals(EnabledStateEnum.ENABLED.getCode())) {
                LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "enable");
            }
            if (value.equals(EnabledStateEnum.DISABLED.getCode())) {
                LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "disable");
            }
            if (value.equals(3)) {
                LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "del");
            }


            LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            return baseOutput;
        } catch (Exception e) {
            return BaseOutput.failure("系统异常");
        }
    }
}
