package com.dili.ia.controller;

import com.dili.ia.domain.Customer;
import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ia.glossary.CustomerMeterStateEnum;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;

import com.dili.ss.domain.EasyuiPageOutput;
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
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        modelMap
     * @return       String
     * @description：跳转到CustomerMeter页面
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerMeter/index";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        modelMap
     * @return       String
     * @description：跳转到表用户关系-新增页面
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "customerMeter/add";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        modelMap
     * @return       String
     * @description：跳转到表用户关系-查看页面
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
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        modelMap
     * @return       String
     * @description：跳转到表用户关系-修改页面
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
     * @author:      xiaosa
     * @date:        2020/6/17
     * @param        customerMeterDto
     * @description：分页根据条件查询列表
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute CustomerMeterDto customerMeterDto) throws Exception {
        return customerMeterService.listCustomerMeters(customerMeterDto, true).toString();
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        customerMeterDto
     * @description：新增 CustomerMeter
     */
    @BusinessLogger(businessType = LogBizTypeConst.CUSTOMER_METER, content="${businessCode!}", operationType="add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput add(@ModelAttribute CustomerMeterDto customerMeterDto) {

        // 新增
        BaseOutput<CustomerMeter> output = customerMeterService.addCustomerMeter(customerMeterDto);

        // 写业务日志
        if (output.isSuccess()) {
            CustomerMeter customerMeter = output.getData();
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            LoggerUtil.buildLoggerContext(customerMeter.getId(), customerMeterDto.getId().toString(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return output;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/22
     * @param        likeName
     * @description：获取所有未绑定的表
     */
    @RequestMapping(value="/listUnbindCustomerMeter.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listUnbindCustomerMeter(String likeName) throws Exception {
//        return customerMeterService.listCustomerMeters(customerMeterDto, true).toString();
        return null;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        customerMeterDto
     * @description：修改 CustomerMeter(解绑同接口)
     */
    @BusinessLogger(businessType = LogBizTypeConst.CUSTOMER_METER, content="${businessCode!}", operationType="update", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute CustomerMeterDto customerMeterDto) {

        // 修改
        BaseOutput<CustomerMeter> output = customerMeterService.updateCustomerMeter(customerMeterDto);

        // 写业务日志
        if (output.isSuccess()) {
            CustomerMeter customerMeter = output.getData();
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            LoggerUtil.buildLoggerContext(customerMeter.getId(), customerMeterDto.getId().toString(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return output;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        id
     * @description：解绑 CustomerMeter
     */
    @BusinessLogger(businessType = LogBizTypeConst.CUSTOMER_METER, content="${businessCode!}", operationType="update", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/unbind.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput unbind(Long id) {
        CustomerMeterDto customerMeterDto = new CustomerMeterDto();

        // 解绑
        customerMeterDto.setId(id);
        customerMeterDto.setState(CustomerMeterStateEnum.CANCELD.getCode());
        BaseOutput<CustomerMeter> output = customerMeterService.updateCustomerMeter(customerMeterDto);

        // 写业务日志
        if (output.isSuccess()) {
            CustomerMeter customerMeter = output.getData();
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            LoggerUtil.buildLoggerContext(customerMeter.getId(), customerMeterDto.getId().toString(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return output;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        id
     * @description：删除 CustomerMeter
     */
    @BusinessLogger(businessType = LogBizTypeConst.CUSTOMER_METER, content="${businessCode!}", operationType="delete", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {

        // 删除
        BaseOutput<CustomerMeter> output = customerMeterService.deleteCustomerMeter(id);

        // 写业务日志
        if (output.isSuccess()) {
            CustomerMeter customerMeter = output.getData();
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            LoggerUtil.buildLoggerContext(customerMeter.getId(), id.toString(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return output;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param
     * @return       BaseOutput
     * @description：根据表类型获取未绑定的表编号
     */
    @RequestMapping(value="/getUnbindMeterByType.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getUnbindMeterByType() {
        // TODO sql 未完成
        BaseOutput<Meter> output = customerMeterService.getUnbindMeterByType(1L);
        return output;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param        meterId
     * @return       BaseOutput
     * @description：根据表主键id获取表绑定的用户信息
     */
    @RequestMapping(value="/getBindInfoByMeterId.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getBindInfoByMeterId(Long meterId) {
        // TODO 不知道该方法是否可行
        BaseOutput<CustomerMeter> output = customerMeterService.getBindInfoByMeterId(meterId);
        return output;
    }
}