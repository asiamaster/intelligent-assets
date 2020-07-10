package com.dili.ia.service;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.StallDetailsReport;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
public interface StallDetailsReportService {
    /**
     * 摊位明细查询
     *
     * @param stallDetailsReport
     * @return
     */
    PageOutput<List<StallDetailsReport>> listByQueryParams(StallDetailsReport stallDetailsReport, List<BusinessChargeItemDto> chargeItemDtos);
}