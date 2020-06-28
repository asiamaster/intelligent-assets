package com.dili.ia.controller;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.service.MeterDetailService;
import com.dili.ss.domain.BaseOutput;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:      xiaosa
 * @date:        2020/6/23
 * @version:     农批业务系统重构
 * @description: 水电费
 */
@Controller
@RequestMapping("/meterDetail")
public class MeterDetailController {

    private final static Logger logger = LoggerFactory.getLogger(MeterDetailController.class);

    @Autowired
    MeterDetailService meterDetailService;

    /**
     * @author:      xiaosa
     * @date:        2020/6/23
     * @param:
     * @return:
     * @description：跳转到欢迎页面
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "meterDetail/index";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/23
     * @param:
     * @return:      
     * @description：跳转到新增页面
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "meterDetail/add";
    }


    /**
     * @author:      xiaosa
     * @date:        2020/6/23
     * @param:
     * @return:
     * @description：跳转到修改页面
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        MeterDetail meterDetail = null;
        if (id != null) {
            meterDetail = meterDetailService.get(id);
        }
        logger.info(meterDetail.toString());
        modelMap.put("meterDetail", meterDetail);
        return "meterDetail/update";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/23
     * @param:       meterDetailDto
     * @return:      String
     * @description：分页带条件请求列表数据
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute MeterDetailDto meterDetailDto) throws Exception {
        return meterDetailService.listMeterDetails(meterDetailDto, true).toString();
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/23
     * @param:
     * @return:
     * @description：跳转到查看页面
     */
    @RequestMapping(value="/view.action", method = {RequestMethod.GET, RequestMethod.POST})
    public String view(ModelMap modelMap, Long id) {
        MeterDetail meterDetail = null;
        if (id != null) {
            meterDetail = meterDetailService.get(id);
        }
        logger.info(meterDetail.toString());
        modelMap.put("meterDetail", meterDetail);
        return "meterDetail/view";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/23
     * @param:       meterDetail
     * @return:      BaseOutput
     * @description：新增 水电费单
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute MeterDetail meterDetail) {
        meterDetailService.insertSelective(meterDetail);
        return BaseOutput.success("新增成功");
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/23
     * @param:       meterDetail
     * @return:      BaseOutput
     * @description：修改 水电费单
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute MeterDetail meterDetail) {
        meterDetailService.updateSelective(meterDetail);
        return BaseOutput.success("修改成功");
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/23
     * @param:
     * @return:      
     * @description：
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        meterDetailService.delete(id);
        return BaseOutput.success("删除成功");
    }
}