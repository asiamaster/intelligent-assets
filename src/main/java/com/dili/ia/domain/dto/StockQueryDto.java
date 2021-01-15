package com.dili.ia.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.dili.ia.domain.Stock;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月14日
 */
public class StockQueryDto extends Stock{
	
    @Column(name = "`customer_name`")
    @Like
	private String LikeCustomerName;

    private LocalDate day;
    
	private LocalDateTime startTime;
	
	private LocalDateTime endTime;
	
	@Column(name = "`quantity`")
	@Operator(Operator.GREAT_THAN)
	private Long quantity;

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getLikeCustomerName() {
		return LikeCustomerName;
	}

	public void setLikeCustomerName(String likeCustomerName) {
		LikeCustomerName = likeCustomerName;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	

}
