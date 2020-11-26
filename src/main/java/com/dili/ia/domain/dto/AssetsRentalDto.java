package com.dili.ia.domain.dto;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设Dto
 */
public class AssetsRentalDto extends AssetsRental {
    // 一个出租预设可能有多个摊位
    private List<AssetsRentalItem> assetsRentalItemList;

    public List<AssetsRentalItem> getAssetsRentalItemList() {
        return assetsRentalItemList;
    }

    public void setAssetsRentalItemList(List<AssetsRentalItem> assetsRentalItemList) {
        this.assetsRentalItemList = assetsRentalItemList;
    }
}