package com.dili.ia.controller;

import com.dili.ia.domain.PrintTemplate;
import com.dili.ia.service.PrintTemplateService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-03 14:30:11.
 */
@Controller
@RequestMapping("/printTemplate")
public class PrintTemplateController {
    @Autowired
    PrintTemplateService printTemplateService;

    @Value("${upload.path}")
    private String path;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping("list.html")
    public String list() {
        return "print/list";
    }

    /**
     * 添加
     *
     * @return
     */
    @RequestMapping("add.html")
    public String toAdd() {
        return "print/add";
    }

    /**
     * 添加
     *
     * @return
     */
    @RequestMapping("update.html")
    public String toUpdate(Long id, ModelMap map) {
        PrintTemplate printTemplate = printTemplateService.get(id);
        map.put("obj", printTemplate);
        return "print/update";
    }

    /**
     * 获取区域列表
     *
     * @param input
     * @return
     */
    @RequestMapping("/listPage.action")
    @ResponseBody
    public String listPage(PrintTemplate input) throws Exception {
        return printTemplateService.listEasyuiPageByExample(input, true).toString();
    }

    /**
     * 新增
     */
    @RequestMapping("save.action")
    @ResponseBody
    public BaseOutput save(@RequestParam(value = "tempFileupload") MultipartFile file, PrintTemplate printTemplate) {
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = path; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            printTemplate.setPath("/getTemplate/" + fileName);
            printTemplate.setCreateTime(new Date());
            printTemplate.setModifyTime(new Date());
            printTemplateService.saveOrUpdate(printTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseOutput.success();
    }

    /**
     * 新增
     */
    @RequestMapping("update.action")
    @ResponseBody
    public BaseOutput update(@RequestParam(value = "tempFileupload", required = false) MultipartFile file, PrintTemplate printTemplate) {

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();  // 文件名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
            String filePath = path; // 上传后的路径
            fileName = UUID.randomUUID() + suffixName; // 新文件名
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!file.isEmpty()) {
                printTemplate.setPath("/getTemplate/" + fileName);
            }
        }
        printTemplate.setModifyTime(new Date());
        printTemplateService.updateSelective(printTemplate);
        return BaseOutput.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("delete.action")
    public BaseOutput delete(Long id) {
        printTemplateService.delete(id);
        return BaseOutput.success();
    }
}