package com.dili.ia.domain.dto;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年6月15日
 */
public class StockWeighmanRecordDto {

	private Long id;
	
    /**
     * 毛重
     */
    private Long grossWeight;

    /**
     * 毛重时间
     */
    private LocalDateTime grossWeightDate;

    /**
     * 皮重
     */
    private Long tareWeight;

    /**
     * 皮重时间
     */
    private LocalDateTime tareWeightDate;
    
    /**
     * 毛重司磅员
     */
    private Long operatorId;

    private String operatorName;
    
    /**
     * 回皮司磅员
     */
    private Long tareOperatorId;
    
    private String tareOperatorName;
    
    /**
     * 司磅照片  [{name:"beforegross",url:"/de/de/666.imag"},{name:"aftergross",url:"/de/de/666.imag"}
     * ,{name:"befortare",url:"/de/de/666.imag"},{name:"aftertare",url:"/de/de/666.imag"}]
     */
    private String imags;

    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Long grossWeight) {
		this.grossWeight = grossWeight;
	}

	public LocalDateTime getGrossWeightDate() {
		return grossWeightDate;
	}

	public void setGrossWeightDate(LocalDateTime grossWeightDate) {
		this.grossWeightDate = grossWeightDate;
	}

	public Long getTareWeight() {
		return tareWeight;
	}

	public void setTareWeight(Long tareWeight) {
		this.tareWeight = tareWeight;
	}

	public LocalDateTime getTareWeightDate() {
		return tareWeightDate;
	}

	public void setTareWeightDate(LocalDateTime tareWeightDate) {
		this.tareWeightDate = tareWeightDate;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getTareOperatorId() {
		return tareOperatorId;
	}

	public void setTareOperatorId(Long tareOperatorId) {
		this.tareOperatorId = tareOperatorId;
	}

	public String getTareOperatorName() {
		return tareOperatorName;
	}

	public void setTareOperatorName(String tareOperatorName) {
		this.tareOperatorName = tareOperatorName;
	}

	public String getImags() {
		return imags;
	}

	public void setImags(String imags) {
		this.imags = imags;
	}

    
}
