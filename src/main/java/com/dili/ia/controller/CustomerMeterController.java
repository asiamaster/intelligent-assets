package com.dili.ia.controller;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ia.glossary.CustomerMeterStateEnum;
import com.dili.ia.glossary.MeterTypeEnum;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.util.AssertUtils;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/16
 * @version:     农批业务系统重构
 * @description: 表用户 web 层
 */
@Controller
@RequestMapping("/customerMeter")
public class CustomerMeterController {

    private final static Logger logger = LoggerFactory.getLogger(CustomerMeterController.class);

    @Autowired
    CustomerMeterService customerMeterService;

    /**
     * 跳转到欢迎页面
     *
     * @param  modelMap
     * @return 欢迎页面地址
     * @date   2020/6/16
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerMeter/index";
    }

    /**
     * 跳转到新增页面
     *
     * @param  modelMap
     * @return 新增页面地址
     * @date   2020/6/16
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "customerMeter/add";
    }

    /**
     * 跳转到查看页面
     *
     * @param  id 表用户关系主键
     * @return 查看页面地址
     * @date   2020/6/16
     */
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id) {
        CustomerMeterDto customerMeter = null;
        if (id != null) {
            customerMeter = customerMeterService.getMeterById(id);
        }
        logger.info(customerMeter.toString());
        modelMap.put("customerMeter", customerMeter);
        return "customerMeter/view";
    }

    /**
     * 跳转到修改页面
     *
     * @param  id 表用户关系主键
     * @return 修改页面地址
     * @date   2020/6/16
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        CustomerMeterDto customerMeterDto = null;
        if (id != null) {
            customerMeterDto = customerMeterService.getMeterById(id);
        }
        logger.info(customerMeterDto.toString());
        modelMap.put("customerMeter", customerMeterDto);
        return "customerMeter/update";
    }

    /**
     * 查询表用户关系的集合(分页)
     *
     * @param  customerMeterDto
     * @return customerMeterDtoList
     * @date   2020/6/17
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute CustomerMeterDto customerMeterDto) throws Exception {
        return customerMeterService.listCustomerMeters(customerMeterDto, true).toString();
    }

    /**
     * 新增表用户关系
     * 
     * @param  customerMeterDto
     * @return 是否成功
     * @date   2020/6/16
     */
    @BusinessLogger(businessType = LogBizTypeConst.CUSTOMER_METER, content="${businessCode!}", operationType="add", systemCode = "IA")
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput add(@ModelAttribute CustomerMeterDto customerMeterDto) {
        try {
            // 参数校验
            AssertUtils.notNull(customerMeterDto.getType(), "表类型不能为空");
            AssertUtils.notEmpty(customerMeterDto.getNumber(), "表编号不能为空");
            AssertUtils.notNull(customerMeterDto.getAssetsId(), "表地址不能为空");
            AssertUtils.notNull(customerMeterDto.getCustomerId(), "客户不能为空");
            AssertUtils.notEmpty(customerMeterDto.getCustomerCellphone(), "客户联系电话不能为空");

            // 新增
            BaseOutput<CustomerMeter> output = customerMeterService.addCustomerMeter(customerMeterDto);

            // 写业务日志
            if (output.isSuccess()) {
                CustomerMeter customerMeter = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(customerMeter.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return output;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 解绑表用户关系
     *
     * @param  id 表用户关系主键
     * @return 是否成功
     * @date   2020/6/16
     */
    @BusinessLogger(businessType = LogBizTypeConst.CUSTOMER_METER, content="${businessCode!}", operationType="update", systemCode = "IA")
    @RequestMapping(value="/unbind.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput unbind(Long id) {
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空");

            // 解绑
            CustomerMeterDto customerMeterDto = new CustomerMeterDto();
            customerMeterDto.setId(id);
            customerMeterDto.setState(CustomerMeterStateEnum.CANCELD.getCode());
            BaseOutput<CustomerMeter> output = customerMeterService.updateCustomerMeter(customerMeterDto);

            // 写业务日志
            if (output.isSuccess()) {
                CustomerMeter customerMeter = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(customerMeter.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "解绑");
            }

            return output;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 根据表编号模糊查询表客户信息列表(新增水电费页面回显)(查询水表)
     *
     * @param  keyword 输入编号
     * @return 表客户List
     * @date   2020/7/10
     */
    @RequestMapping(value="/listCustomerMeterByLikeNameWater.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput listCustomerMeterByLikeNameWater(String keyword) {
        List<CustomerMeterDto> customerMeterDtoList = customerMeterService.listCustomerMetersByLikeName(MeterTypeEnum.WATER_METER.getCode(), keyword);

        return BaseOutput.success().setData(customerMeterDtoList);
    }

    /**
     * 根据表编号模糊查询表客户信息列表(新增水电费页面回显)(查询电表)
     *
     * @param  keyword 输入编号
     * @return 表客户List
     * @date   2020/7/10
     */
    @RequestMapping(value="/listCustomerMeterByLikeNameElectricity.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput listCustomerMeterByLikeNameElectricity(String keyword) {
        List<CustomerMeterDto> customerMeterDtoList = customerMeterService.listCustomerMetersByLikeName(MeterTypeEnum.ELECTRIC_METER.getCode(), keyword);

        return BaseOutput.success().setData(customerMeterDtoList);
    }

    /**
     * 根据表主键 meterId 获取表绑定的用户信息以及上期指数
     *
     * @param  meterId 表主键
     * @return CustomerMeter
     * @date   2020/6/28
     */
    @RequestMapping(value="/getBindInfoByMeterId.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getBindInfoByMeterId(Long meterId) throws Exception {
        CustomerMeterDto customerMeterInfo = customerMeterService.getBindInfoByMeterId(meterId);

        return BaseOutput.success().setData(customerMeterInfo);
    }
}