package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.mapper.BoutiqueFeeOrderMapper;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  缴费单
 */
@Service
public class BoutiqueFeeOrderServiceImpl extends BaseServiceImpl<BoutiqueFeeOrder, Long> implements BoutiqueFeeOrderService {

    public BoutiqueFeeOrderMapper getActualDao() {
        return (BoutiqueFeeOrderMapper)getDao();
    }

    /**
     * 根据精品停车主键 recordId 查询缴费单列表
     *
     * @param  recordId 精品停车主键
     * @return list
     * @date   2020/7/13
     */
    @Override
    public List<BoutiqueFeeOrderDto> listByRecordId(Long recordId) {
        List<BoutiqueFeeOrderDto> boutiqueFeeOrderDtoList = this.getActualDao().listByRecordId(recordId);
        return boutiqueFeeOrderDtoList;
    }
}