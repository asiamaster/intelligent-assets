package com.dili.ia.service.impl;

import com.dili.assets.sdk.rpc.AreaMarketRpc;
import com.dili.ia.service.MchAndDistrictService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:       xiaosa
 * @date:         2020/12/17
 * @version:      农批业务系统重构
 * @description:  商户和区域的部分接口
 */
@Service
public class MchAndDistrictServiceImpl implements MchAndDistrictService {

    private final static Logger logger = LoggerFactory.getLogger(MchAndDistrictServiceImpl.class);

    @Autowired
    AreaMarketRpc areaMarketRpc;

    /**
     * 根据 区域ID 获取 商户ID
     *
     * @param  firstDistrictId
     * @param  secondDistrictId
     * @return mchId
     * @date   2020/12/17
     */
    @Override
    public Long getMchIdByDistrictId(Long firstDistrictId, Long secondDistrictId) {
        Long districtId = null;
        Long mchId = null;
        if (secondDistrictId != null) {
            districtId = secondDistrictId;
        }
        if (secondDistrictId == null && firstDistrictId != null) {
            districtId = firstDistrictId;
        }
        if (districtId != null) {
            //@TODO 为空抛异常，现在基础数据有问题，暂时注释掉代码，后期打开
            try {
                BaseOutput<Long> mchOutput = areaMarketRpc.getMarketByArea(districtId);
                if (!mchOutput.isSuccess()){
                    logger.error("根据区域ID查询商户，返回失败：{}", mchOutput.getMessage());
                    throw new BusinessException(ResultCode.APP_ERROR, "根据区域ID查询商户，返回失败!");
                }
                mchId = mchOutput.getData();
            }catch (Exception e){
                logger.error("根据区域ID查询商户，接口调用异常："+e.getMessage(),e);
                throw new BusinessException(ResultCode.APP_ERROR, "根据区域ID查询商户，接口调用异常！");
            }
//        if (mchId == null){
//            LOG.error("根据区域ID查询商户，返回为空，districtId:{}", districtId);
//            throw new BusinessException(ResultCode.APP_ERROR, "根据区域ID查询商户，返回为空！");
//        }
        }
        return mchId;
    }
}
