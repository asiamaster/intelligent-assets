package com.dili.ia.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
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

	@Override
	public BaseOutput submitHandler(RefundOrder refundOrder) {
		return null;
	}

	@Override
	public BaseOutput withdrawHandler(RefundOrder refundOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		// TODO Auto-generated method stub
		return null;
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
	public Set<Integer> getBizType() {
		return Sets.newHashSet(BizTypeEnum.STOCKIN.getCode());
	}}
