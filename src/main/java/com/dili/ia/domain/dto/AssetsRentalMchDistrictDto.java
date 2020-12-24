package com.dili.ia.domain.dto;

public class AssetsRentalMchDistrictDto {
    /**
     * 商户Id
     */
    private Long mchId;

    /**
     * 区域Id
     */
    private Long districtId;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
}
