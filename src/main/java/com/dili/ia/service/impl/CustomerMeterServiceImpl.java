package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ia.mapper.CustomerMeterMapper;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:35:07.
 */
@Service
public class CustomerMeterServiceImpl extends BaseServiceImpl<CustomerMeter, Long> implements CustomerMeterService {

    public CustomerMeterMapper getActualDao() {
        return (CustomerMeterMapper)getDao();
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @return:      BaseOutput
     * @description：根据主键 id 查询
     */
    @Override
    public CustomerMeter getMeterById(Long id) {
        CustomerMeter customerMeter = this.getActualDao().getMeterById(id);
        return customerMeter;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @return:      BaseOutput
     * @description：新增表用户关系
     */
    @Override
    public BaseOutput<CustomerMeter> addCustomerMeter(CustomerMeterDto customerMeterDto) {
        CustomerMeter customerMeter = new CustomerMeter();

        // 校验用户是否登陆, 并设置相关信息
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }
        customerMeterDto.setCreatorId(userTicket.getId());
        customerMeterDto.setCreator(userTicket.getUserName());
        customerMeterDto.setCreatorDepId(userTicket.getDepartmentId());

        // 设置开始、修改时间
        customerMeterDto.setCreateTime(new Date());
        customerMeterDto.setModifyTime(new Date());

        // TODO 差市场code 市场id

        BeanUtils.copyProperties(customerMeterDto, customerMeter);
        this.getActualDao().insert(customerMeter);

        return BaseOutput.success();
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @return:      BaseOutput
     * @description：修改表用户关系
     */
    @Override
    public BaseOutput<CustomerMeter> updateCustomerMeter(CustomerMeterDto customerMeterDto) {
        CustomerMeter customerMeter = new CustomerMeter();

        // 校验用户是否登陆, 并设置相关信息
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }

        // 设置开始、修改时间
        customerMeterDto.setModifyTime(new Date());

        // TODO 差市场code 市场id

        BeanUtils.copyProperties(customerMeterDto, customerMeter);
        this.getActualDao().updateByPrimaryKeySelective(customerMeter);

        return BaseOutput.success();
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       customerMeterDto
     * @description：删除表用户关系
     */
    @Override
    public BaseOutput<CustomerMeter>  deleteCustomerMeter(Long id) {
        // 先查询是否还存在该用户表关系
        CustomerMeter customerMeter = this.getActualDao().getMeterById(id);
        if (customerMeter == null) {
            return BaseOutput.failure("该数据已删除");
        }

        // 再删除(乐观锁)
        int i = this.getActualDao().deleteByPrimaryKey(id);
        if (i == 0) {
            return BaseOutput.failure("该数据已删除");
        }

        return BaseOutput.success("删除成功");
    }
}