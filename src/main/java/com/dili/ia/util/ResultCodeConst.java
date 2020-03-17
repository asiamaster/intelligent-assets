package com.dili.ia.util;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-17 10:04
 */
public class ResultCodeConst {
    // --------------------  自定义错误码 摊位租赁错误码号段 2001 ~ 2100  -------------------------
    /**
     * 2000 DATA_ERROR: 业务逻辑或数据错误(未查询到数据，数据验证不通过，数据发生变化等错误)
     */
    /**
     * 2001 CUSTOMER_ACCOUNT_ERROR:  客户账户不存在
     */
    public static final String CUSTOMER_ACCOUNT_ERROR="2001";
    /**
     * 2002 EARNEST_ERROR:  使用定金时，可用余额不足
     */
    public static final String EARNEST_ERROR="2002";
    /**
     * 2003 TRANSFER_ERROR: 使用转抵金时，可用余额不足
     */
    public static final String TRANSFER_ERROR="2003";

}
