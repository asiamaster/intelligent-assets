package com.dili.ia.domain.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;

import com.dili.ia.domain.MessageFee;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年8月31日
 */
public class MessageFeeQuery extends MessageFee {

	@Column(name = "`create_time`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private LocalDateTime createdStart;
	
	@Column(name = "`create_time`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private LocalDateTime createdEnd;
	
	
	@Column(name = "`start_date`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private LocalDateTime gtStartDate;
	
	@Column(name = "`end_date`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private LocalDateTime gtEndDate;
	
	@Column(name = "`customer_name`")
	@Like
	private String likeCustomerName;
	public LocalDateTime getCreatedStart() {
		return createdStart;
	}
	public void setCreatedStart(LocalDateTime createdStart) {
		this.createdStart = createdStart;
	}
	public LocalDateTime getCreatedEnd() {
		return createdEnd;
	}
	public void setCreatedEnd(LocalDateTime createdEnd) {
		this.createdEnd = createdEnd;
	}
	public String getLikeCustomerName() {
		return likeCustomerName;
	}
	public void setLikeCustomerName(String likeCustomerName) {
		this.likeCustomerName = likeCustomerName;
	}
	public LocalDateTime getGtStartDate() {
		return gtStartDate;
	}
	public void setGtStartDate(LocalDateTime gtStartDate) {
		this.gtStartDate = gtStartDate;
	}
	public LocalDateTime getGtEndDate() {
		return gtEndDate;
	}
	public void setGtEndDate(LocalDateTime gtEndDate) {
		this.gtEndDate = gtEndDate;
	}
	
	
}
