package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ia.mapper.CustomerMeterMapper;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ia.service.MeterDetailService;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MeterDetailService meterDetailService;

    /**
     * 根据主键 id 查询表用户关系
     *
     * @param  id 表用户关系主键
     * @return CustomerMeterDto
     * @date   2020/6/29
     */
    @Override
    public CustomerMeterDto getMeterById(Long id) {
        CustomerMeterDto customerMeterDto = this.getActualDao().getMeterById(id);
        return customerMeterDto;
    }

    /**
     * meter、customerMeter 查询表用户关系的集合(分页)
     *
     * @param  customerMeterDto
     * @param  useProvider
     * @return customerMeterDtoList
     * @date   2020/6/17
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
     * 新增表用户关系
     *
     * @param  customerMeterDto
     * @return 是否成功
     * @date   2020/6/16
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
     * 修改表用户关系(解绑)
     *
     * @param  customerMeterDto
     * @return 是否成功
     * @date   2020/6/16
     */
    @Override
    public BaseOutput<CustomerMeter> updateCustomerMeter(CustomerMeterDto customerMeterDto) {
        CustomerMeter customerMeter = new CustomerMeter();

        // 校验用户是否登陆, 并设置相关信息
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("表用户操作失败,用户未登录。");
        }

        CustomerMeter customerMeterInfo = get(customerMeterDto.getId());
        if (customerMeterInfo == null) {
            return BaseOutput.failure("表用户操作失败,记录不存在。");
        }

        customerMeterDto.setModifyTime(new Date());
        customerMeterDto.setVersion(customerMeterInfo.getVersion() + 1);

        BeanUtils.copyProperties(customerMeterDto, customerMeter);
        int code = this.updateSelective(customerMeter);
        if (code == 0) {
            return BaseOutput.failure("当前数据正在被其他用户操作，提交失败！请关闭当前弹窗重新选择操作");
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 删除表用户关系,如果有未缴费的单,则不能删除
     *
     * @param  id 表用户关系主键
     * @return 是否成功
     * @date   2020/6/16
     */
    @Override
    public BaseOutput<CustomerMeter>  deleteCustomerMeter(Long id) {
        // 先查询是否还存在该用户表关系
        CustomerMeter customerMeter = this.getActualDao().getMeterById(id);
        if (customerMeter == null) {
            return BaseOutput.failure("删除失败，该数据已删除。");
        }

        // 根据表 meterId、用户 customerId 查询未缴费单的数量
        Integer count = meterDetailService.countUnPayByMeterAndCustomer(customerMeter.getMeterId(), customerMeter.getCustomerId());
        if (count > 0) {
            return BaseOutput.failure("该表用户当前存在交费记录，不允许删除。");
        }

        // 再删除(乐观锁)
        int code = this.delete(id);
        if (code == 0) {
            return BaseOutput.failure("删除失败，数据已被其他用户操作。");
        }

        return BaseOutput.success("删除成功");
    }

    /**
     * 根据表主键 meterId 获取表绑定的用户信息
     *
     * @param  meterId
     * @return CustomerMeter
     * @date   2020/6/28
     */
    @Override
    public BaseOutput<CustomerMeter> getBindInfoByMeterId(Long meterId) {
        BaseOutput baseOutput = new BaseOutput();
        CustomerMeter customerMeter = this.getActualDao().getBindInfoByMeterId(meterId);
        baseOutput.setData(customerMeter);
        return baseOutput;
    }
}