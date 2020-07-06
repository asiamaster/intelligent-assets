package com.dili.ia.controller;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.service.MeterDetailService;
import com.dili.ss.domain.BaseOutput;
import java.util.List;

import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
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
     * 跳转到欢迎页面
     * 
     * @param  modelMap
     * @return 欢迎页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "meterDetail/index";
    }

    /**
     * 跳转到新增页面
     *
     * @param  modelMap
     * @return 新增页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "meterDetail/add";
    }

    /**
     * 跳转到查看页面
     *
     * @param  id 水电费单主键id
     * @return 查看页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/view.action", method = {RequestMethod.GET, RequestMethod.POST})
    public String view(ModelMap modelMap, Long id) {
        MeterDetailDto meterDetailDto = null;

        if (id != null) {
            meterDetailDto = meterDetailService.getMeterDetailById(id);
        }
        modelMap.put("meterDetail", meterDetailDto);

        return "meterDetail/view";
    }

    /**
     * 跳转到修改页面
     *
     * @param  id 水电费单主键
     * @return 修改页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        MeterDetail meterDetail = null;
        if (id != null) {
            meterDetail = meterDetailService.getMeterDetailById(id);
        }
        logger.info(meterDetail.toString());
        modelMap.put("meterDetail", meterDetail);
        return "meterDetail/update";
    }

    /**
     * 查询水电费单的集合(分页)
     *
     * @param  meterDetailDto
     * @return MeterDetailDtoList
     * @date   2020/6/28
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute MeterDetailDto meterDetailDto) throws Exception {
        return meterDetailService.listMeterDetails(meterDetailDto, true).toString();
    }

    /**
     * 新增水电费单(仅保存收费单信息)
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/6/28
     */
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute MeterDetailDto meterDetailDto) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        return meterDetailService.addMeterDetail(meterDetailDto, userTicket);
    }

    /**
     * 提交水电费单(生缴费单和结算单)
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/7/6
     */
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(@ModelAttribute MeterDetailDto meterDetailDto) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        return meterDetailService.submit(meterDetailDto, userTicket);
    }

    /**
     * 撤回水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/7/6
     */
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(@ModelAttribute MeterDetailDto meterDetailDto) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        return meterDetailService.withdraw(meterDetailDto, userTicket);
    }

    /**
     * 取消水电费单
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/7/6
     */
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancel(@ModelAttribute MeterDetailDto meterDetailDto) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        return meterDetailService.cancel(meterDetailDto, userTicket);
    }

    /**
     * 修改水电费单
     * 
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/6/29
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute MeterDetailDto meterDetailDto) {

        return meterDetailService.updateMeterDetail(meterDetailDto);
    }


    /**
     * 删除 水电费单
     *
     * @param  id 水电费单主键
     * @return 是否成功
     * @date   2020/6/29
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {

        int code = meterDetailService.delete(id);
        if (code <= 0){
            return BaseOutput.failure("删除失败,数据已被其他用户删除!");
        }

        return BaseOutput.success("删除成功");
    }

    /**
     * 根据 meterId 获取初始值
     *
     * @param  meterId
     * @return 初始值(上期指数)
     * @date   2020/6/29
     */
    @RequestMapping(value = "/getLastAmount.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getLastAmount(Long meterId){
        return meterDetailService.getLastAmount(meterId);
    }
}