package com.dili.ia.domain.dto;

import com.dili.assets.sdk.dto.CategoryDTO;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月23日
 */
public class CategoryQuery extends CategoryDTO {
	
	private String queryPath;

	public String getQueryPath() {
		return queryPath;
	}

	public void setQueryPath(String queryPath) {
		this.queryPath = queryPath;
	}
	
	
}
