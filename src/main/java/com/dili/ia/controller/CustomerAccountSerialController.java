package com.dili.ia.controller;

import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.dto.CustomerAccountSerialDto;
import com.dili.settlement.rpc.CustomerAccountRpc;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Controller
@RequestMapping("/customerAccountSerial")
public class CustomerAccountSerialController {
    private final static Logger LOG = LoggerFactory.getLogger(CustomerAccountSerialController.class);

    @Autowired
    CustomerAccountRpc customerAccountRpc;

    /**
     * 跳转到TransactionDetails页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.add(Calendar.DAY_OF_MONTH, -2);
        Date operateTimeStart = c.getTime();

        Calendar ce = Calendar.getInstance();
        ce.set(ce.get(Calendar.YEAR), ce.get(Calendar.MONTH), ce.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date operateTimeEnd = ce.getTime();

        modelMap.put("operateTimeStart", operateTimeStart);
        modelMap.put("operateTimeEnd", operateTimeEnd);
        return "customerAccountSerial/index";
    }

    /**
     * 分页查询定金流水分页信息
     * @param
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(CustomerAccountSerialDto customerAccountSerialDto) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        customerAccountSerialDto.setMarketId(userTicket.getFirmId());
        PageOutput<List<CustomerAccountSerialDto>> caOutput = customerAccountRpc.listSerialPage(customerAccountSerialDto);
        long total = caOutput.getTotal();
        List results = ValueProviderUtils.buildDataByProvider(customerAccountSerialDto, caOutput.getData());
        return new EasyuiPageOutput(total, results).toString();
    }


}