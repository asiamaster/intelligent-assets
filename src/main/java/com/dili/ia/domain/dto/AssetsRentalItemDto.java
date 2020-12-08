package com.dili.ia.domain.dto;

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

/**
 * @author:       xiaosa
 * @date:         2020/12/8
 * @version:      农批业务系统重构
 * @description:  摊位出租预设中的资产表
 */
public class AssetsRentalItemDto extends AssetsRentalItem {
        
}