package com.dili.ia.controller;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.service.AssetsRentalItemService;
import com.dili.ia.service.AssetsRentalService;
import com.dili.ia.util.AssertUtils;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设
 */
@Controller
@RequestMapping("/assetsRental")
public class AssetsRentalController {

    private final static Logger logger = LoggerFactory.getLogger(AssetsRentalController.class);

    @Autowired
    AssetsRentalService assetsRentalService;

    @Autowired
    private AssetsRentalItemService assetsRentalItemService;

    /**
     * 跳转到assetsRental页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/11/26
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "assetsRental/index";
    }

    /**
     * 跳转到新增页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/11/26
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "assetsRental/add";
    }

    /**
     * 跳转到修改页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/11/26
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        if (id != null) {
            AssetsRentalDto assetsRentalDto = new AssetsRentalDto();
            AssetsRental assetsRental = assetsRentalService.get(id);
            if (assetsRental != null) {
                BeanUtils.copyProperties(assetsRental, assetsRentalDto);

                // 关联查询预设详情中关联的摊位信息
                List<AssetsRentalItem> assetsRentalItemList = assetsRentalItemService.listRentalItemsByRentalId(assetsRental.getId());
                if (CollectionUtils.isNotEmpty(assetsRentalItemList)) {
                    assetsRentalDto.setAssetsRentalItemList(assetsRentalItemList);
                }
                modelMap.put("assetsRental", assetsRentalDto);
            }
        }
        return "assetsRental/add";
    }

    /**
     * 查询资产出租预设的集合(分页)
     *
     * @param  assetsRental
     * @return
     * @date   2020/11/26
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute AssetsRental assetsRental) throws Exception {
        return assetsRentalService.listEasyuiPageByExample(assetsRental, true).toString();
    }

    /**
     * 新增资产出租预设
     *
     * @param assetsRentalDto
     * @return BaseOutput
     * @date   2020/11/26
     */
    @BusinessLogger(businessType = LogBizTypeConst.ASSETS_RENTAL_PRESET, content="${businessCode!}", operationType="add", systemCode = "IA")
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput add(@RequestBody AssetsRentalDto assetsRentalDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 校验参数，名称和摊位不能为空
            AssertUtils.notEmpty(assetsRentalDto.getName(), "预设名称不能为空");
            AssertUtils.notNull(assetsRentalDto.getAssetsRentalItemList(), "预设摊位不能为空");

            AssetsRental assetsRental = assetsRentalService.addAssetsRental(assetsRentalDto, userTicket);

            // 写业务日志
            LoggerUtil.buildLoggerContext(assetsRental.getId(), null, userTicket.getId(), userTicket.getRealName(),
                    userTicket.getFirmId(), "新增摊位出租预设信息。");

            return BaseOutput.success().setData(assetsRental);
        } catch (BusinessException e) {
            logger.info("新增资产出租预设失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }



    /**
     * 修改资产出租预设
     *
     * @param assetsRentalDto
     * @return BaseOutput
     * @date   2020/11/26
     */
    @BusinessLogger(businessType = LogBizTypeConst.ASSETS_RENTAL_PRESET, content="${businessCode!}", operationType="update", systemCode = "IA")
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@RequestBody AssetsRentalDto assetsRentalDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 校验参数，名称和摊位不能为空
            AssertUtils.notNull(assetsRentalDto.getId(), "预设主键不能为空");
            AssertUtils.notEmpty(assetsRentalDto.getName(), "预设名称不能为空");
            AssertUtils.notNull(assetsRentalDto.getAssetsRentalItemList(), "预设摊位不能为空");

            AssetsRental assetsRental = assetsRentalService.updateAssetsRental(assetsRentalDto);

            // 写业务日志
            LoggerUtil.buildLoggerContext(assetsRental.getId(), null, userTicket.getId(), userTicket.getRealName(),
                    userTicket.getFirmId(), "修改摊位出租预设信息。");

            return BaseOutput.success().setData(assetsRental);
        } catch (BusinessException e) {
            logger.info("修改资产出租预设失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 启用或者禁用
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/11/26
     */
    @BusinessLogger(businessType = LogBizTypeConst.ASSETS_RENTAL_PRESET, content="${businessCode!}", operationType="update", systemCode = "IA")
    @RequestMapping(value="/enableOrDisable.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput enableOrDisable(@RequestParam("id") Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            AssertUtils.notNull(id, "预设主键不能为空");

            boolean enable = assetsRentalService.enableOrDisable(id);

            String enableStr = "禁用";
            if (enable) {
                enableStr = "启用";
            }

            // 写业务日志
            LoggerUtil.buildLoggerContext(id, null, userTicket.getId(), userTicket.getRealName(),
                    userTicket.getFirmId(), enableStr + "摊位出租预设。");

            return BaseOutput.success();
        } catch (BusinessException e) {
            logger.info("修改资产出租预设状态失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 根据摊位 id 查询相关的预设信息
     * @param assetsId
     * @return
     */
    @GetMapping(value="/getRentalByAssetsId.action")
    public @ResponseBody BaseOutput<AssetsRentalDto> getRentalByAssetsId(Long assetsId){
        try {
            return BaseOutput.success().setData(assetsRentalService.getRentalByAssetsId(assetsId));
        } catch (BusinessException e) {
            logger.info("根据摊位 id 查询相关的预设信息异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("根据摊位 id 查询相关的预设信息异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 根据同一批次、同一商户、名称模糊查询摊位出租预设信息集合
     * @param assetsRentalDto
     * @return
     */
    @GetMapping(value = "/listRentalsByRentalDtoAndKeyWord.action")
    public @ResponseBody
    BaseOutput<List<AssetsRentalDto>> listRentalsByRentalDtoAndKeyWord(AssetsRentalDto assetsRentalDto) {
        try {
            return BaseOutput.success().setData(assetsRentalService.listRentalsByRentalDtoAndKeyWord(assetsRentalDto));
        } catch (BusinessException e) {
            logger.info("根据同一批次、同一商户、名称模糊查询摊位出租预设信息集合异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("根据同一批次、同一商户、名称模糊查询摊位出租预设信息集合异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

}