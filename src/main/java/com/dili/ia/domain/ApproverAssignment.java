package com.dili.ia.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.dto.IMybatisForceParams;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  由MyBatis Generator工具自动生成
 *  任务人分配
 *  This file was generated on 2020-07-22 11:16:04.
 */
@Table(
    name = "`approver_assignment`"
)
public interface ApproverAssignment extends IBaseDomain, IMybatisForceParams {
  @Column(
      name = "`id`"
  )
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  Long getId();

  void setId(Long id);

  /**
   *  区域id，可能是一级或二级
   */
  @Column(
      name = "`district_id`"
  )
  Long getDistrictId();

  void setDistrictId(Long districtId);

  /**
   *  市场id
   */
  @Column(
          name = "`firm_id`"
  )
  Long getFirmId();

  void setFirmId(Long firmId);

  /**
   *  办理用户id
   */
  @Column(
      name = "`assignee`"
  )
  Long getAssignee();

  void setAssignee(Long assignee);

  /**
   *  任务定义key
   */
  @Column(
      name = "`task_definition_key`"
  )
  String getTaskDefinitionKey();

  void setTaskDefinitionKey(String taskDefinitionKey);

  /**
   *  流程定义key
   */
  @Column(
      name = "`process_definition_key`"
  )
  String getProcessDefinitionKey();

  void setProcessDefinitionKey(String processDefinitionKey);

  /**
   *  修改时间
   */
  @Column(
      name = "`modify_time`"
  )
  Date getModifyTime();

  void setModifyTime(Date modifyTime);

  /**
   *  创建时间
   */
  @Column(
      name = "`create_time`"
  )
  Date getCreateTime();

  void setCreateTime(Date createTime);
}
