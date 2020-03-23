package com.dili.ia.service;

import java.util.List;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-23 15:06
 */

public interface DataAuthService {
    /**
     * 获取市场权限
     * @param userTicketId 当前登录客户ID
     * @return List<Long> 权限市场ID list
     */
    List<Long> getMarketDataAuth(Long userTicketId);

    /**
     * 获取部门权限
     * @param userTicketId 当前登录客户ID
     * @return List<Long> 权限部门ID list
     */
    List<Long> getDepartmentDataAuth(Long userTicketId);

}
