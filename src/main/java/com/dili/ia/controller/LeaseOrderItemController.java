package com.dili.ia.controller;

import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.domain.dto.LeaseOrderItemListDto;
import com.dili.ia.glossary.DepositAmountFlagEnum;
import com.dili.ia.glossary.PayStateEnum;
import com.dili.ia.glossary.RefundStateEnum;
import com.dili.ia.glossary.StopWayEnum;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Api("/leaseOrderItem")
@Controller
@RequestMapping("/leaseOrderItem")
public class LeaseOrderItemController {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderItemController.class);
    @Autowired
    LeaseOrderItemService leaseOrderItemService;


    /**
     * 分页查询LeaseOrderItem，返回easyui分页信息
     * @param leaseOrderItem
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询LeaseOrderItem", notes = "分页查询LeaseOrderItem，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="LeaseOrderItem", paramType="form", value = "LeaseOrderItem的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(LeaseOrderItem leaseOrderItem) throws Exception {
        return leaseOrderItemService.listEasyuiPageByExample(leaseOrderItem, true).toString();
    }

    /**
     * 订单项查询
     * @param leaseOrderItem
     * @return
     */
    @RequestMapping(value="/queryDepositAmountAvailableItem.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput queryDepositAmountAvailableItem(LeaseOrderItemListDto leaseOrderItem) {
        return BaseOutput.success().setData(leaseOrderItemService.queryDepositAmountAvailableItem(leaseOrderItem));
    }

    /**
     * 摊位停租
     * @param leaseOrderItem
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOOTH_LEASE,content = "${boothName}",operationType="stopLease",systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/stopRent.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput stopRent(LeaseOrderItem leaseOrderItem){
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (userTicket == null) {
                throw new RuntimeException("未登录");
            }
            if(null == leaseOrderItem.getStopWay()|| null == leaseOrderItem.getId()){
                return BaseOutput.failure("参数错误");
            }
            if(StopWayEnum.TIMING.getCode().equals(leaseOrderItem.getStopWay()) && null == leaseOrderItem.getStopTime()){
                return BaseOutput.failure("参数错误");
            }
            BaseOutput output = leaseOrderItemService.stopRent(leaseOrderItem);
            LoggerUtil.buildLoggerContext(leaseOrderItem.getId(),leaseOrderItem.getLeaseOrderCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),leaseOrderItem.getStopReason());
            return output;
        }catch (BusinessException e){
            LOG.info("摊位停租异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("摊位停租异常！", e);
            return BaseOutput.failure(e.getMessage());
        }


    }
}