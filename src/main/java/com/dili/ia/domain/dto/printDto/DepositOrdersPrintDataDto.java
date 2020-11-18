package com.dili.ia.domain.dto.printDto;

import com.dili.ia.domain.DepositOrder;

import java.util.List;

/**
 * <B>提供给租赁票据打印的关联保证金数据</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-11-18 10:32
 */
public class DepositOrdersPrintDataDto {
    //总共提交金额
    private Long totalSubmitAmount;
    //总共待付金额
    private Long totalWaitAmount;
    //保证金单列表
    private List<DepositOrder> depositOrderList;

    public Long getTotalSubmitAmount() {
        return totalSubmitAmount;
    }

    public void setTotalSubmitAmount(Long totalSubmitAmount) {
        this.totalSubmitAmount = totalSubmitAmount;
    }

    public Long getTotalWaitAmount() {
        return totalWaitAmount;
    }

    public void setTotalWaitAmount(Long totalWaitAmount) {
        this.totalWaitAmount = totalWaitAmount;
    }

    public List<DepositOrder> getDepositOrderList() {
        return depositOrderList;
    }

    public void setDepositOrderList(List<DepositOrder> depositOrderList) {
        this.depositOrderList = depositOrderList;
    }
}
