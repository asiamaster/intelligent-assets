package com.dili.ia.mapper;

import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface BoutiqueFeeOrderMapper extends MyMapper<BoutiqueFeeOrder> {

    /**
     * 根据精品停车主键 recordId 查询缴费单列表
     *
     * @param  recordId
     * @return list
     * @date   2020/7/13
     */
    List<BoutiqueFeeOrderDto> listByRecordId(Long recordId);
}