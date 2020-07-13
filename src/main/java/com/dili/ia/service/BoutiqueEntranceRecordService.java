package com.dili.ia.service;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  精品停车
 */
public interface BoutiqueEntranceRecordService extends BaseService<BoutiqueEntranceRecord, Long> {
    
    /**
     * 根据主键获取精品停车数据以及精品停车交费列表
     * 
     * @param
     * @return 
     * @date   2020/7/13
     */
    BoutiqueEntranceRecordDto getBoutiqueEntranceDtoById(Long id);

    /**
     * 确认计费
     * 
     * @param  boutiqueEntranceRecord
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/13
     */
    BaseOutput<BoutiqueEntranceRecord> confirm(BoutiqueEntranceRecord boutiqueEntranceRecord, UserTicket userTicket) throws Exception;

    /**
     * 离场
     *
     * @param id 主键
     * @param userTicket
     * @return BaseOutput
     * @date   2020/7/13
     */
    BaseOutput<BoutiqueEntranceRecord> leave(Long id, UserTicket userTicket) throws Exception;

    /**
     * 强制离场
     *
     * @param id 主键
     * @return BaseOutput
     * @date   2020/7/13
     */
    BaseOutput<BoutiqueEntranceRecord> forceLeave(Long id, UserTicket userTicket);
}