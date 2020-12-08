package com.dili.ia.api;

import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.AssetsRentalItemDto;
import com.dili.ia.domain.dto.printDto.PassportPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.glossary.MeterTypeEnum;
import com.dili.ia.service.AssetsRentalItemService;
import com.dili.ia.service.AssetsRentalService;
import com.dili.ia.service.PassportService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:      xiaosa
 * @date:        2020/12/8
 * @version:     农批业务系统重构
 * @description: 摊位预设api
 */
@RestController
@RequestMapping("/api/assetsRental")
public class AssetsRentalApi {

    private final static Logger LOG = LoggerFactory.getLogger(AssetsRentalApi.class);

    @Autowired
    private AssetsRentalService assetsRentalService;

    @Autowired
    private AssetsRentalItemService assetsRentalItemService;

    /**
     * 修改摊位的基础信息后，摊位出租预设也要改动对应的信息（修改一对多里的多的表中的字段值）
     *
     * @param  assetsRentalItemDto
     * @return BaseOutput
     * @date   2020/12/8
     */
    @BusinessLogger(content = "${code!}", operationType = "update", systemCode = "IA")
    @RequestMapping(value = "/updateAssetsToRental", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Boolean> updateAssetsToRental(@RequestBody AssetsRentalItemDto assetsRentalItemDto) {
        try {
            assetsRentalItemService.updateAssetsToRental(assetsRentalItemDto);
            return BaseOutput.success();
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 商户 - 区域绑定关系改变，剔除掉修改掉的区域下的所有摊位
     *
     * @param  districtId
     * @return BaseOutput
     * @date   2020/12/8
     */
    @BusinessLogger(content = "${code!}", operationType = "update", systemCode = "IA")
    @RequestMapping(value = "/deleteAssetsByDistrictId ", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Boolean> deleteAssetsByDistrictId(@RequestParam("districtId") Long districtId) {
        try {
            assetsRentalService.deleteAssetsByDistrictId(districtId);

            return BaseOutput.success();
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }
}
