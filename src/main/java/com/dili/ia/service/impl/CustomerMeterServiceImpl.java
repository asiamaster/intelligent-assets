package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.AssetsLeaseOrderItemListDto;
import com.dili.ia.domain.dto.CustomerMeterDto;
import com.dili.ia.glossary.CustomerMeterStateEnum;
import com.dili.ia.mapper.CustomerMeterMapper;
import com.dili.ia.service.AssetsLeaseOrderItemService;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.service.MeterService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/16
 * @version:     农批业务系统重构
 * @description: 表用户关系 service 层
 */
@Service
public class CustomerMeterServiceImpl extends BaseServiceImpl<CustomerMeter, Long> implements CustomerMeterService {

    public CustomerMeterMapper getActualDao() {
        return (CustomerMeterMapper)getDao();
    }

    @Autowired
    private MeterDetailService meterDetailService;

    @Autowired
    private MeterService meterService;

    @Autowired
    private AssetsLeaseOrderItemService assetsLeaseOrderItemService;

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

        customerMeterDto.setState(CustomerMeterStateEnum.CREATED.getCode());
        // 查询
        List<CustomerMeterDto> customerMeterDtoList= this.getActualDao().listCustomerMeters(customerMeterDto);

        // 基础代码
        long total = customerMeterDtoList instanceof Page ? ((Page)customerMeterDtoList).getTotal() : (long)customerMeterDtoList.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(customerMeterDto, customerMeterDtoList) : customerMeterDtoList;

        return new EasyuiPageOutput(total, results);
    }

    /**
     * 新增表用户关系
     *
     * @param  customerMeterDto
     * @param  userTicket
     * @return CustomerMeter
     * @date   2020/6/16
     */
    @Override
    @GlobalTransactional
    public CustomerMeter addCustomerMeter(CustomerMeterDto customerMeterDto, UserTicket userTicket) {
        CustomerMeter customerMeter = new CustomerMeter();

        // 根据 number 查询 meter，拿取 meterId
        if (StringUtils.isNotEmpty(customerMeterDto.getNumber())) {
            Meter meterInfo = meterService.getMeterByNumber(customerMeterDto.getNumber());
            customerMeterDto.setMeterId(meterInfo.getId()
            );
        }

        customerMeterDto.setVersion(0);
        customerMeterDto.setCreatorId(userTicket.getId());
        customerMeterDto.setCreateTime(LocalDateTime.now());
        customerMeterDto.setModifyTime(LocalDateTime.now());
        customerMeterDto.setMarketId(userTicket.getFirmId());
        customerMeterDto.setCreator(userTicket.getUserName());
        customerMeterDto.setMarketCode(userTicket.getFirmCode());
        customerMeterDto.setCreatorDepId(userTicket.getDepartmentId());
        customerMeterDto.setState(CustomerMeterStateEnum.CREATED.getCode());

        BeanUtils.copyProperties(customerMeterDto, customerMeter);
        this.insertSelective(customerMeter);

        return customerMeter;
    }

    /**
     * 修改表用户关系(解绑)
     *
     * @param  id
     * @return CustomerMeter
     * @date   2020/6/16
     */
    @Override
    @GlobalTransactional
    public CustomerMeter updateCustomerMeter(Long id) throws BusinessException{
        CustomerMeter customerMeter = new CustomerMeter();
        CustomerMeterDto customerMeterDto = new CustomerMeterDto();

        customerMeterDto.setId(id);
        customerMeterDto.setState(CustomerMeterStateEnum.CANCELD.getCode());
        CustomerMeter customerMeterInfo = this.get(customerMeterDto.getId());
        if (customerMeterInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该记录已删除，解绑失败！");
        }

        // 根据表 meterId、用户 customerId 查询未缴费的记录数量
        Integer count = meterDetailService.countUnPayByMeterAndCustomer(customerMeterInfo.getMeterId(), customerMeterInfo.getCustomerId());
        if (count > 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该表用户当前存在交费记录，不允许删除！");
        }

        customerMeterDto.setModifyTime(LocalDateTime.now());
        customerMeterDto.setVersion(customerMeterInfo.getVersion() + 1);
        BeanUtils.copyProperties(customerMeterDto, customerMeter);

        if (this.updateSelective(customerMeter) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");
        }

        return customerMeter;
    }

    /**
     * 根据表主键 meterId 获取表绑定的用户信息以及上期指数
     *
     * @param  meterId
     * @return CustomerMeter
     * @date   2020/6/28
     */
    @Override
    public CustomerMeterDto getBindInfoByMeterId(Long meterId) throws BusinessException {
        CustomerMeterDto customerMeterDto = new CustomerMeterDto();

        CustomerMeter customerMeterInfo = this.getActualDao().getBindInfoByMeterId(meterId);
        if (customerMeterInfo == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "表已被删除！");
        }
        BeanUtils.copyProperties(customerMeterInfo, customerMeterDto);

        // 查询上期指数
        BaseOutput lastAmountOut = meterDetailService.getLastAmount(meterId);
        Long lastAmount = (Long)lastAmountOut.getData();

        customerMeterDto.setLastAmount(lastAmount);

        return customerMeterDto;
    }

    /**
     * 根据表编号模糊查询表客户信息列表
     *
     * @param  type
     * @param  keyword
     * @return List
     * @date   2020/7/10
     */
    @Override
    public List<CustomerMeterDto> listCustomerMetersByLikeName(Integer type, String keyword) {
        CustomerMeterDto customerMeterDto = new CustomerMeterDto();
        customerMeterDto.setType(type);
        if (StringUtils.isNotEmpty(keyword)) {
            customerMeterDto.setKeyword(keyword);
        }
        List<CustomerMeterDto> customerMeterDtoList = this.getActualDao().listCustomerMetersByLikeName(customerMeterDto);
        return customerMeterDtoList;
    }

    /**
     * 根据表地址查询是否处于租期状态和相应的用户
     *
     * @param  assetsId
     * @param  assetsType
     * @return CustomerMeterDto
     * @date   2020/12/10
     */
    @Override
    public CustomerMeterDto getCustomerByAssetsIdAndAssetsType(UserTicket code, Long assetsId, Integer assetsType) {
        CustomerMeterDto customerMeterDto = new CustomerMeterDto();
        AssetsLeaseOrderItem assetsLeaseOrderItem = new AssetsLeaseOrderItem();
        assetsLeaseOrderItem.setState(5);
        assetsLeaseOrderItem.setAssetsId(assetsId);
        assetsLeaseOrderItem.setAssetsType(assetsType);
        AssetsLeaseOrderItemListDto itemListDto = assetsLeaseOrderItemService.getCustomerByAssetsIdAndAssetsType(assetsLeaseOrderItem);
        if (itemListDto != null) {
            customerMeterDto.setCustomerId(itemListDto.getCustomerId());
            customerMeterDto.setCustomerName(itemListDto.getCustomerName());
            customerMeterDto.setCertificateNumber(itemListDto.getCertificateNumber());
            customerMeterDto.setCustomerCellphone(itemListDto.getCustomerCellphone());
        }

        return customerMeterDto;
    }
}