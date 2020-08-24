package com.dili.ia.service.impl;

import com.dili.ia.domain.MessageFee;
import com.dili.ia.mapper.MessageFeeMapper;
import com.dili.ia.service.MessageFeeService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-08-24 16:16:50.
 */
@Service
public class MessageFeeServiceImpl extends BaseServiceImpl<MessageFee, Long> implements MessageFeeService {

    public MessageFeeMapper getActualDao() {
        return (MessageFeeMapper)getDao();
    }
}