package com.dili.ia.api;

import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-13 11:29
 */
@Api("/api/refundOrder")
@RestController
@RequestMapping("/api/refundOrder")
public class RefundOrderApi {

    private final static Logger LOG = LoggerFactory.getLogger(RefundOrderApi.class);

    @Autowired
    RefundOrderDispatcherService refundOrderDispatcherService;

    /**
     * 定金票据打印数据加载
     * @param businessCode 业务编码
     * @param reprint 是否补打标记
     * @return BaseOutput<PrintDataDto>
     */
    @RequestMapping(value="/queryPrintData")
    public @ResponseBody
    BaseOutput<PrintDataDto> queryPrintData(String businessCode, Integer reprint){
        try{
            if(StringUtils.isBlank(businessCode) || null == reprint){
                return BaseOutput.failure("参数错误");
            }
            return refundOrderDispatcherService.queryPrintData(businessCode,reprint);
        }catch (Exception e){
            LOG.error("获取打印数据异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
