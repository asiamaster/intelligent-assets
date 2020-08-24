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
public class StockInRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long>
		implements RefundOrderDispatcherService {

	@Autowired
	private StockInService stockInService;

	@Override
	public BaseOutput submitHandler(RefundOrder refundOrder) {
		/*String code = refundOrder.getBusinessCode();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = stockInService.getStockInByCode(code);
		if (stockIn.getState() != StockInStateEnum.PAID.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn domain = new StockIn(userTicket);
		updateState(domain, stockIn.getCode(), stockIn.getVersion(), StockInStateEnum.SUBMITTED_REFUND);*/
		return BaseOutput.success();
	}

	@Override
	public BaseOutput withdrawHandler(RefundOrder refundOrder) {
		
		return BaseOutput.success();
	}

	@Override
	public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		stockInService.refundSuccessHandler(settleOrder, refundOrder);
		return BaseOutput.success();
	}

	@Override
	public BaseOutput cancelHandler(RefundOrder refundOrder) {
		String code = refundOrder.getBusinessCode();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		StockIn stockIn = stockInService.getStockInByCode(code);
		if (stockIn.getState() != StockInStateEnum.SUBMITTED_REFUND.getCode()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
		}
		StockIn domain = new StockIn(userTicket);
		updateState(domain, code, stockIn.getVersion(), StockInStateEnum.PAID);
		return BaseOutput.success();
	}

	@Override
	public BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder) {
		
		return BaseOutput.success().setData(stockInService.receiptPaymentData(refundOrder.getBusinessCode(), "reprint"));
	}

	@Override
	public Set<String> getBizType() {
		return Sets.newHashSet(BizTypeEnum.STOCKIN.getCode());
	}

	private void updateState(StockIn domain, String code, Integer version, StockInStateEnum state) {
		domain.setVersion(version + 1);
		domain.setState(state.getCode());
		StockIn condition = new StockIn();
		condition.setCode(code);
		condition.setVersion(version);
		// 修改入库单状态提交入库单
		int row = stockInService.updateSelectiveByExample(domain, condition);
		if (row != 1) {
			throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
		}
	}
}