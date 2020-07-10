package com.dili.ia.mapper;

import com.dili.ia.domain.StallDetailsReport;

import java.util.List;

public interface StallDetailsReportMapper {

    List<StallDetailsReport> listByQueryParams(StallDetailsReport stallDetailsReport);

}