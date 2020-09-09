package com.dili.ia.mapper;

import com.dili.ia.domain.OtherFee;
import com.dili.ss.base.MyMapper;

public interface OtherFeeMapper extends MyMapper<OtherFee> {

    /**
     * 根据code查询数据实例
     *
     * @param  code
     * @return OtherFee
     * @date   2020/8/27
     */
    OtherFee getOtherFeeByCode(String code);
}