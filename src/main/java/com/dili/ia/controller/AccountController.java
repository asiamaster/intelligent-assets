package com.dili.ia.controller;

import com.dili.ia.domain.account.AccountInfo;
import com.dili.ia.service.AccountService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @GetMapping("/icCheck")
    public @ResponseBody BaseOutput<AccountInfo> icCheck(String ic) {
        try {
            return accountService.checkCardNo(ic);
        } catch (Exception e) {
            LOG.error("卡信息查询接口异常",e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}