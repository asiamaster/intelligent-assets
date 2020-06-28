package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ia.mapper.CustomerMeterMapper;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/16
 * @version:     农批业务系统重构
 * @description: 表用户关系 service 实现层
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
    public CustomerMeterDto getMeterById(Long id) {
        CustomerMeterDto customerMeterDto = this.getActualDao().getMeterById(id);
        return customerMeterDto;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/17
     * @param        customerMeterDto
     * @description：分页根据条件查询列表
     */
    @Override
    public EasyuiPageOutput listCustomerMeters(CustomerMeterDto customerMeterDto, boolean useProvider) throws Exception {
        // 分页
        if (customerMeterDto.getRows() != null && customerMeterDto.getRows() >= 1) {
            PageHelper.startPage(customerMeterDto.getPage(), customerMeterDto.getRows());
        }

        // 查询
        List<CustomerMeterDto> customerMeterDtoList= this.getActualDao().listCustomerMeters(customerMeterDto);

        // 基础代码
        long total = customerMeterDtoList instanceof Page ? ((Page)customerMeterDtoList).getTotal() : (long)customerMeterDtoList.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(customerMeterDto, customerMeterDtoList) : customerMeterDtoList;
        return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);
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

        // 校验用户是否登陆
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }

        // 并设置相关信息
        customerMeterDto.setCreatorId(userTicket.getId());
        customerMeterDto.setCreator(userTicket.getUserName());
        customerMeterDto.setCreatorDepId(userTicket.getDepartmentId());
        customerMeterDto.setMarketId(userTicket.getFirmId());
        customerMeterDto.setMarketCode(userTicket.getFirmCode());
        customerMeterDto.setCreateTime(new Date());
        customerMeterDto.setModifyTime(new Date());
        customerMeterDto.setVersion(1);

        BeanUtils.copyProperties(customerMeterDto, customerMeter);
        this.insertSelective(customerMeterDto);

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

        CustomerMeter customerMeterInfo = get(customerMeterDto.getId());
        if (customerMeterInfo == null) {
            return BaseOutput.failure("该表用户信息已删除");
        }

        customerMeterDto.setModifyTime(new Date());
        customerMeterDto.setVersion(customerMeterInfo.getVersion() + 1);

        BeanUtils.copyProperties(customerMeterDto, customerMeter);
        this.updateSelective(customerMeter);

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
        int i = this.delete(id);
        if (i == 0) {
            return BaseOutput.failure("该数据已删除");
        }

        return BaseOutput.success("删除成功");
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        type
     * @return       BaseOutput
     * @description：根据表类型获取未绑定的表编号
     */
    @Override
    public BaseOutput<Meter> getUnbindMeterByType(Long type) {
        BaseOutput baseOutput = new BaseOutput();

        List<Meter> meterList = this.getActualDao().getUnbindMeterByType(type);
        baseOutput.setData(meterList);

        return baseOutput;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param        meterId
     * @return       BaseOutput
     * @description：根据表主键id获取表绑定的用户信息
     */
    @Override
    public BaseOutput<CustomerMeter> getBindInfoByMeterId(Long meterId) {
        BaseOutput baseOutput = new BaseOutput();
        CustomerMeter customerMeter = this.getActualDao().getBindInfoByMeterId(meterId);
        baseOutput.setData(customerMeter);
        return baseOutput;
    }
}