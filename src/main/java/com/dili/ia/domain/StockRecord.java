package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 库存流水记录
 * This file was generated on 2020-06-12 11:14:28.
 */
@Table(name = "`stock_record`")
public class StockRecord extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 入库数量
     */
    @Column(name = "`quantity`")
    private Long quantity;

    /**
     * 入库数量
     */
    @Column(name = "`weight`")
    private Long weight;

    /**
     * 业务操作编号(入库单号,出库单号)
     */
    @Column(name = "`business_code`")
    private String businessCode;

    /**
     * 入库,出库
     */
    @Column(name = "`type`")
    private Integer type;

    /**
     * 库存
     */
    @Column(name = "`stock_id`")
    private Long stockId;

    /**
     * 市场id
     */
    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`market_code`")
    private String marketCode;
    
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    @Column(name = "`operation_day`")
    private LocalDate operationDay;
    
    @Column(name = "`stock_quantity`")
    private Long stockQuantity;
    
    @Column(name = "`stock_weight`")
    private Long stockWeight;
    
    /**
     * @return id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取入库数量
     *
     * @return quantity - 入库数量
     */
    @FieldDef(label="入库数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getQuantity() {
        return quantity;
    }

    /**
     * 设置入库数量
     *
     * @param quantity 入库数量
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取入库数量
     *
     * @return weight - 入库数量
     */
    @FieldDef(label="入库数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWeight() {
        return weight;
    }

    /**
     * 设置入库数量
     *
     * @param weight 入库数量
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    /**
     * 获取业务操作编号(入库单号,出库单号)
     *
     * @return business_code - 业务操作编号(入库单号,出库单号)
     */
    @FieldDef(label="业务操作编号(入库单号,出库单号)", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBusinessCode() {
        return businessCode;
    }

    /**
     * 设置业务操作编号(入库单号,出库单号)
     *
     * @param businessCode 业务操作编号(入库单号,出库单号)
     */
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    /**
     * 获取入库,出库
     *
     * @return type - 入库,出库
     */
    @FieldDef(label="入库,出库")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getType() {
        return type;
    }

    /**
     * 设置入库,出库
     *
     * @param type 入库,出库
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取库存
     *
     * @return stock_id - 库存
     */
    @FieldDef(label="库存")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getStockId() {
        return stockId;
    }

    /**
     * 设置库存
     *
     * @param stockId 库存
     */
    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    /**
     * 获取市场id
     *
     * @return market_id - 市场id
     */
    @FieldDef(label="市场id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场id
     *
     * @param marketId 市场id
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * @return market_code
     */
    @FieldDef(label="marketCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * @param marketCode
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDate getOperationDay() {
		return operationDay;
	}

	public void setOperationDay(LocalDate operationDay) {
		this.operationDay = operationDay;
	}

	public Long getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Long stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public Long getStockWeight() {
		return stockWeight;
	}

	public void setStockWeight(Long stockWeight) {
		this.stockWeight = stockWeight;
	}
    
    
}