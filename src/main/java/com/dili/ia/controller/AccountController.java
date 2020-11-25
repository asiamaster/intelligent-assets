package com.dili.ia.controller;

import com.dili.ia.domain.account.AccountInfo;
import com.dili.ia.domain.account.CardInfo;
import com.dili.ia.service.AccountService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 园区卡
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    private final static Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * 卡的可用性校验
     *
     * @param ic
     * @return
     */
    @GetMapping("/icCheck.action")
    public @ResponseBody
    BaseOutput<AccountInfo> icCheck(String ic) {
        return accountService.checkCardNo(ic);
    }

    /**
     * 卡的可用性校验
     *
     * @param customerId
     * @return
     */
    @GetMapping("/getAccountListByCustomerId.action")
    public @ResponseBody
    BaseOutput<List<AccountInfo>> getAccountListByCustomerId(Long customerId) {
        return accountService.getAccountListByCustomerId(customerId);
    }
}