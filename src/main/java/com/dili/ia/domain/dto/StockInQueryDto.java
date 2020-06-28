package com.dili.ia.domain.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
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
	private Date createdStart;
	
	@Column(name = "`create_time`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private Date createdEnd;
	
	@Column(name = "`expire_date`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private Date expireStart;
	
	@Column(name = "`expire_date`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private Date expireEnd;

	public Date getCreatedStart() {
		return createdStart;
	}

	public void setCreatedStart(Date createdStart) {
		this.createdStart = createdStart;
	}

	public Date getCreatedEnd() {
		return createdEnd;
	}

	public void setCreatedEnd(Date createdEnd) {
		this.createdEnd = createdEnd;
	}

	public Date getExpireStart() {
		return expireStart;
	}

	public void setExpireStart(Date expireStart) {
		this.expireStart = expireStart;
	}

	public Date getExpireEnd() {
		return expireEnd;
	}

	public void setExpireEnd(Date expireEnd) {
		this.expireEnd = expireEnd;
	}
	
	
}
