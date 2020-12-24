package com.dili.ia.domain.dto;

import java.util.List;

import com.dili.assets.sdk.dto.CityDto;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年12月16日
 */
public class CityTreeDto {

	private List<CityTreeDto> childs;
	
	private String cityCode;
	
	private Long id;
	
	private String mergerName;
	
	private String name;

	public List<CityTreeDto> getChilds() {
		return childs;
	}

	public void setChilds(List<CityTreeDto> childs) {
		this.childs = childs;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMergerName() {
		return mergerName;
	}

	public void setMergerName(String mergerName) {
		this.mergerName = mergerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
