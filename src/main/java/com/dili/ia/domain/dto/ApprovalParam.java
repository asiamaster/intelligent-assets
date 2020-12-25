package com.dili.ia.domain.dto;

import com.dili.bpmc.sdk.domain.TaskCenterParam;

import javax.validation.constraints.NotNull;

/**
 * 租赁单和退款单的审批参数
 */
public interface ApprovalParam extends TaskCenterParam {

    /**
     * 租赁单号
     * @return
     */
    @Override
    @NotNull(message = "业务号不能为空")
    String getBusinessKey();

    @Override
    void setBusinessKey(String businessKey);

    /**
     * 审批结果, 1:同意， 2:拒绝
     * 参见:ApprovalResultEnum
     * @return
     */
    @NotNull(message = "审批结果不能为空")
    Integer getResult();
    void setResult(Integer result);

    /**
     * 审批意见/内容
     * @return
     */
    String getOpinion();
    void setOpinion(String opinion);
}
