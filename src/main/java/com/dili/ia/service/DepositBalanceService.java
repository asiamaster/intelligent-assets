package com.dili.ia.service;

import com.dili.ia.domain.DepositBalance;
import com.dili.ia.domain.dto.DepositBalanceQuery;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-29 15:15:26.
 */
public interface DepositBalanceService extends BaseService<DepositBalance, Long> {
    /**
     * 余额合计
     * @param depositBalanceQuery 余额
     * @return Long
     */
    Long sumBalance(DepositBalanceQuery depositBalanceQuery);
    /**
     * 根据需求维度，查询客户余额， 精确查找 , 必要条件如下，如果不传，会按照 is null  方式查询！！！
     * @param depositBalance
     * 注意： 只需及必填参数 如果参数为空  会按照 is null 方式查询
     * customer_id  客户ID
     * type_code 保证金类型code
     * assets_type 资产类型
     * assets_id 资产ID
     * assets_name 资产名称
     * market_id 市场ID
     * @return List<DepositBalance>
     */
    List<DepositBalance> listDepositBalanceExact(DepositBalance depositBalance);
}