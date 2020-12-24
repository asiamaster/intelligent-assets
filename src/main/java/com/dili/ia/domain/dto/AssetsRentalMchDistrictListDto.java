package com.dili.ia.domain.dto;

import java.util.List;

public class AssetsRentalMchDistrictListDto {

    /**
     * 商户-区域集合
     */
    List<AssetsRentalMchDistrictDto> mchDistrictListDtoList;

    public List<AssetsRentalMchDistrictDto> getMchDistrictListDtoList() {
        return mchDistrictListDtoList;
    }

    public void setMchDistrictListDtoList(List<AssetsRentalMchDistrictDto> mchDistrictListDtoList) {
        this.mchDistrictListDtoList = mchDistrictListDtoList;
    }
}
