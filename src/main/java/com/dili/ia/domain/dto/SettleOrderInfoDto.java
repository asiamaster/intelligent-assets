package com.dili.ia.domain.dto;

import java.time.LocalDateTime;

import com.dili.ia.glossary.BizTypeEnum;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 继承结算单,创建默认公共参数
 * @author yangfan
 * @date 2020年7月1日
 */
public class SettleOrderInfoDto extends SettleOrderDto{
	
	public SettleOrderInfoDto(UserTicket userTicket,BizTypeEnum bizType,SettleTypeEnum payType,SettleStateEnum stateEnum) {
		super();
		this.setBusinessType(bizType.getCode());
		this.setMarketId(userTicket.getFirmId());
		this.setMarketCode(userTicket.getFirmCode());
		this.setSubmitterDepId(userTicket.getDepartmentId());
		//参数
        //settleOrder.setSubmitterDepName(null == userTicket.getDepartmentId() ? null : departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
		this.setSubmitterId(userTicket.getId());
		this.setSubmitterName(userTicket.getRealName());
		this.setSubmitTime(LocalDateTime.now());
		this.setType(payType.getCode());
		this.setState(stateEnum.getCode());
	}
	
}
