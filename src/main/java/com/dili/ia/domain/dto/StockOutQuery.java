package com.dili.ia.domain.dto;

import javax.persistence.Column;

import com.dili.ia.domain.StockOut;
import com.dili.ss.domain.annotation.Like;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月24日
 */
public class StockOutQuery extends StockOut {
	
    @Column(name = "`customer_name`")
    @Like
	private String likeCustomerName;
    
    @Column(name = "`creator`")
    @Like
    private String likeCreator;

	public String getLikeCustomerName() {
		return likeCustomerName;
	}

	public void setLikeCustomerName(String likeCustomerName) {
		this.likeCustomerName = likeCustomerName;
	}

	public String getLikeCreator() {
		return likeCreator;
	}

	public void setLikeCreator(String likeCreator) {
		this.likeCreator = likeCreator;
	}

}
