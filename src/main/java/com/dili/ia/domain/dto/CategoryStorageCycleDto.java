package com.dili.ia.domain.dto;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月16日
 */
public class CategoryStorageCycleDto {
	
	private Long id;
	
	private String code;
	
	private Integer cycle;
	
	private String notes;
	
	private Integer state;
	
	private String moduleLabel;
	/**
	 * 是否应用于子品类
	 */
	private Boolean allChildren;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getCycle() {
		return cycle;
	}
	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Boolean getAllChildren() {
		return allChildren;
	}
	public void setAllChildren(Boolean allChildren) {
		this.allChildren = allChildren;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getModuleLabel() {
		return moduleLabel;
	}
	public void setModuleLabel(String moduleLabel) {
		this.moduleLabel = moduleLabel;
	}
	
}
