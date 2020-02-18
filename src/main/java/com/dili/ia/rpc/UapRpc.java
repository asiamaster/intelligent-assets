package com.dili.ia.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOField;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.SystemConfig;

import java.util.List;

@Restful("${uap.contextPath}")
public interface UapRpc {

    @POST("/systemConfigApi/getByCode.api")
    BaseOutput<SystemConfig> getByCode(@VOField("code") String code) ;

    @POST("/dataDictionaryApi/list.api")
    BaseOutput<List<DataDictionaryValue>> listDataDictionaryValue(@VOField("ddCode") String ddCode) ;

//    /**
//     * 获取优惠券编号
//     * @return
//     */
//    @POST("/bizNumberApi/getCouponCode.api")
//    BaseOutput<String> getCouponCode();

}
