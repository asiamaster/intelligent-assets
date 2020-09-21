package com.dili.ia.domain.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

/**
 * 
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 入库单数据传输
 * @author yangfan
 * @date 2020年6月12日
 */
public class StockInQueryDto extends StockIn {
	@Column(name = "`create_time`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private LocalDateTime createdStart;
	
	@Column(name = "`create_time`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private LocalDateTime createdEnd;
	
	@Column(name = "`expire_date`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private LocalDateTime expireStart;
	
	@Column(name = "`expire_date`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private LocalDateTime expireEnd;
	@Column(name = "`customer_name`")
	@Like
	private String likeCustomerName;
	
	@Column(name = "`department_id`")
	@Operator(Operator.IN)
	private List<Long> depIds;

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

	public LocalDateTime getExpireStart() {
		return expireStart;
	}

	public void setExpireStart(LocalDateTime expireStart) {
		this.expireStart = expireStart;
	}

	public LocalDateTime getExpireEnd() {
		return expireEnd;
	}

	public void setExpireEnd(LocalDateTime expireEnd) {
		this.expireEnd = expireEnd;
	}

	public String getLikeCustomerName() {
		return likeCustomerName;
	}

	public void setLikeCustomerName(String likeCustomerName) {
		this.likeCustomerName = likeCustomerName;
	}

	public List<Long> getDepIds() {
		return depIds;
	}

	public void setDepIds(List<Long> depIds) {
		this.depIds = depIds;
	}
	
	
}
