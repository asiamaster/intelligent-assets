package com.dili.ia.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.StockInService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Sets;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月2日
 */
@Service
public class StockInRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService{

	@Autowired
	private StockInService stockInService;
	
	@Override
	public BaseOutput submitHandler(RefundOrder refundOrder) {
		String code = refundOrder.getBusinessCode();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = stockInService.getStockInByCode(code);
		if(stockIn.getState() != StockInStateEnum.PAID.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn condtion = new StockIn(userTicket);
		condtion.setId(stockIn.getId());
		condtion.setState(StockInStateEnum.SUBMITTED_REFUND.getCode());
		stockInService.updateSelective(condtion);
		//stockInService
		return BaseOutput.success();
	}

	@Override
	public BaseOutput withdrawHandler(RefundOrder refundOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		stockInService.refundSuccessHandler(settleOrder, refundOrder);
		return BaseOutput.success();
	}

	@Override
	public BaseOutput cancelHandler(RefundOrder refundOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getBizType() {
		return Sets.newHashSet(BizTypeEnum.STOCKIN.getCode());
	}}
