package com.dili.ia.controller;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.StallDetailsReport;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.StallDetailsReportService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Controller
@RequestMapping("/stallDetailsReport")
public class StallDetailsReportController {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderController.class);

    @Autowired
    private StallDetailsReportService stallDetailsReportService;

    @Autowired
    private AssetsRpc assertRpc;

    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    /**
     * 跳转到列表页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String listByQueryParams(ModelMap modelMap) {
        modelMap.put("chargeItems", businessChargeItemService.queryBusinessChargeItemConfig(11L, AssetsTypeEnum.getAssetsTypeEnum(1).getBizType(), null));
        return "stallDetailsReport/list";
    }

    /**
     * 根据参数查询
     *
     * @param stallDetailsReport
     * @return
     */

    @RequestMapping("/listByQueryParams.action")
    @ResponseBody
    public String listByQueryParams(StallDetailsReport stallDetailsReport) throws Exception {
        stallDetailsReport.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        stallDetailsReport.setDepartmentId(SessionContext.getSessionContext().getUserTicket().getDepartmentId());
//        if (Objects.isNull(stallDetailsReport.getSearchDate())) {
//            stallDetailsReport.setSearchDate(new Date());
//        }
//        // 动态获取收费项
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemConfig(11L, AssetsTypeEnum.getAssetsTypeEnum(1).getBizType(), YesOrNoEnum.YES.getCode());
        PageOutput<List<StallDetailsReport>> listPageOutput = stallDetailsReportService.listByQueryParams(stallDetailsReport, chargeItemDtos);
        return new EasyuiPageOutput(listPageOutput.getTotal(), ValueProviderUtils.buildDataByProvider(stallDetailsReport, listPageOutput.getData())).toString();
    }

    /**
     * 根据一级区域获取二级
     *
     * @param id
     * @return
     */
    @RequestMapping("/serachSec.action")
    @ResponseBody
    public BaseOutput<List<DistrictDTO>> serachSec(Long id) {
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        districtDTO.setParentId(id);
        return assertRpc.searchDistrict(districtDTO);
    }

}