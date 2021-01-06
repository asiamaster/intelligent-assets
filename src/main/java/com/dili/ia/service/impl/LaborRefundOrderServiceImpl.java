package com.dili.ia.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.domain.RefundFeeItem;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.LaborDto;
import com.dili.ia.domain.dto.printDto.LaborRefundPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.StockInPrintDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.LaborService;
import com.dili.ia.service.RefundFeeItemService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.util.BeanMapUtil;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.google.common.collect.Sets;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月28日
 */
@Service
public class LaborRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService{

	@Autowired
	private LaborService laborService;
	
	@Autowired
	private RefundFeeItemService refundFeeItemService;
	
	@Override
	public BaseOutput updateHandler(RefundOrder refundOrder) {
		LaborDto labor = laborService.getLabor(refundOrder.getBusinessCode());
		if(labor == null) {
			throw new BusinessException(ResultCode.DATA_ERROR, "劳务马甲单不存在!");
		}
		if(refundOrder.getTotalRefundAmount() > labor.getAmount()) {
			throw new BusinessException(ResultCode.DATA_ERROR, "金额不正确,最大可退款金额["+MoneyUtils.centToYuan(labor.getAmount())+"]!");
		}
		return BaseOutput.success();
	}

	@Override
	public BaseOutput submitHandler(RefundOrder refundOrder) {
		//laborService.refundSubmitHandler(refundOrder);
		return BaseOutput.success();
	}

	@Override
	public BaseOutput withdrawHandler(RefundOrder refundOrder) {
		return BaseOutput.success();
	}

	@Override
	public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
		laborService.refundSuccessHandler(settleOrder, refundOrder);
		return BaseOutput.success();
	}

	@Override
	public BaseOutput cancelHandler(RefundOrder refundOrder) {
		laborService.cancleRefund(refundOrder);
		//删除退款项
		RefundFeeItem item = new RefundFeeItem();
		item.setRefundOrderCode(refundOrder.getCode());
		refundFeeItemService.deleteByExample(item);
		return BaseOutput.success();
	}

	@Override
	public BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder) {
		PrintDataDto<LaborRefundPrintDto> print = laborService.receiptRefundPrintData(refundOrder, "reprint");
		 Map<String, Object> resultMap = new HashMap<>();
       //已交清退款单打印数据
       resultMap.put("printTemplateCode",print.getName());
       //根据要求拼装订单项
       resultMap.putAll(BeanMapUtil.beanToMap(print.getItem()));
       return BaseOutput.success().setData(resultMap);	
	}

	@Override
	public Set<String> getBizType() {
		return Sets.newHashSet(BizTypeEnum.LABOR_VEST.getCode());
	}

	
	@Override
	public List<SettleFeeItem> buildSettleFeeItem(RefundOrder refundOrder) {

		List<SettleFeeItem> settleFeeItemList = new ArrayList<>();
		List<RefundFeeItem> items = refundFeeItemService.getByBizCode(refundOrder.getCode());
		for (RefundFeeItem item : items) {
			SettleFeeItem settleFeeItem = new SettleFeeItem();
			settleFeeItem.setChargeItemId(item.getChargeItemId());
			settleFeeItem.setChargeItemName(item.getChargeItemName());
			settleFeeItem.setAmount(item.getAmount());
			settleFeeItemList.add(settleFeeItem);
		}
		return settleFeeItemList;

	}
}
