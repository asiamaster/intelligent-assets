package com.dili.ia.util;

import java.util.ArrayList;
import java.util.List;

import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.enums.LinkTypeEnum;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 
 * @author yangfan
 * @date 2020年12月19日
 */
public class SettleOrderLinkUtils {

	/**
	 * 结算回调链接组装
	 * @Title buildLinks
	 * @Description TODO
	 * @param settlerPrintUrl 结算打印链接
	 * @param settleViewUrl 业务单查看链接
	 * @param settlerHandlerUrl 结算回调链接
	 * @param bizCode 业务code
	 * @param payCode 支付单code
	 */
	public static List<SettleOrderLink> buildLinks(String settlerPrintUrl, String settleViewUrl,String settlerHandlerUrl,String bizCode,
			String payCode) {
		// 组装回调url
		List<SettleOrderLink> settleOrderLinkList = new ArrayList<>();
		// 详情
		SettleOrderLink view = new SettleOrderLink();
		view.setType(LinkTypeEnum.DETAIL.getCode());
		view.setUrl(settleViewUrl + "?code=" + bizCode);
		// 打印
		SettleOrderLink print = new SettleOrderLink();
		print.setType(LinkTypeEnum.PRINT.getCode());
		print.setUrl(settlerPrintUrl + "?orderCode=" + payCode);
		// 回调
		SettleOrderLink callBack = new SettleOrderLink();
		callBack.setType(LinkTypeEnum.CALLBACK.getCode());
		callBack.setUrl(settlerHandlerUrl);
		settleOrderLinkList.add(view);
		settleOrderLinkList.add(print);
		settleOrderLinkList.add(callBack);
		return settleOrderLinkList;

	}
	
}
