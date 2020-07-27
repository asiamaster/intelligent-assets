package com.dili.ia.mapper;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ss.base.MyMapper;

public interface BoutiqueEntranceRecordMapper extends MyMapper<BoutiqueEntranceRecord> {


    /**
     * 根据code获取相关信息
     *
     * @param
     * @return
     * @date   2020/7/23
     */
    BoutiqueFeeOrderDto getBoutiqueAndOrderByCode(String code);
}