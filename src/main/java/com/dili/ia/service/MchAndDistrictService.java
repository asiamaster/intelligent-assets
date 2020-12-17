package com.dili.ia.service;

import org.springframework.stereotype.Service;

/**
 * @author:       xiaosa
 * @date:         2020/12/17
 * @version:      农批业务系统重构
 * @description:  商户和区域的部分接口
 */
public interface MchAndDistrictService {

    /**
     * 根据 区域ID 获取 商户ID
     *
     * @param  firstDistrictId
     * @param  secondDistrictId
     * @return mchId
     * @date   2020/12/17
     */
    Long getMchIdByDistrictId(Long firstDistrictId, Long secondDistrictId);

}
