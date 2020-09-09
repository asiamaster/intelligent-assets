package com.dili.ia.service.impl;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ia.mapper.MeterMapper;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.service.MeterService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的相关业务 impl 层
 */
@Service
public class MeterServiceImpl extends BaseServiceImpl<Meter, Long> implements MeterService {

    private final static Logger logger = LoggerFactory.getLogger(MeterServiceImpl.class);

    @Autowired
    DepartmentRpc departmentRpc;

    public MeterMapper getActualDao() {
        return (MeterMapper)getDao();
    }

    @Autowired
    private MeterDetailService meterDetailService;

    /**
     * 新增表信息
     *
     * @param  meterDto
     * @param userTicket
     * @return Meter
     * @date   2020/6/16
     */
    @Override
    public Meter addMeter(MeterDto meterDto, UserTicket userTicket) throws BusinessException {
        Meter meter = new Meter();

        // 根据表编号查询是否已存在
        meter.setNumber(meterDto.getNumber());
        List<Meter> meterList = this.getActualDao().select(meter);
        if (meterList != null && meterList.size() > 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "表信息新增失败,表编号已存在！");
        }

        // 设置相关属性值
        meterDto.setVersion(0);

        meterDto.setCreatorId(userTicket.getId());
        meterDto.setCreateTime(LocalDateTime.now());
        meterDto.setModifyTime(LocalDateTime.now());
        meterDto.setMarketId(userTicket.getFirmId());
        meterDto.setCreator(userTicket.getUserName());
        meterDto.setMarketCode(userTicket.getFirmCode());
        meterDto.setCreatorDepId(userTicket.getDepartmentId());
        BeanUtils.copyProperties(meterDto, meter);

        this.insertSelective(meter);

        return meter;
    }

    /**
     * 修改表信息
     *
     * @param  meterDto
     * @return Meter
     * @date   2020/6/29
     */
    @Override
    public Meter updateMeter(MeterDto meterDto) throws BusinessException {
        Meter meter = new Meter();

        // 根据表 meterId、用户 customerId 查询未缴费的记录数量
        Integer count = meterDetailService.countUnPayByMeterAndCustomer(meterDto.getId(), null);
        if (count > 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "该表存在待交费单，不能修改");
        }

        // 根据表编号查询是否已存在
        meter.setNumber(meterDto.getNumber());
        List<Meter> meterList = this.getActualDao().select(meter);
        if (CollectionUtils.isNotEmpty(meterList)) {
            for (Meter meterRe : meterList) {
                if (!meterRe.getId().equals(meterDto.getId())) {
                    throw new BusinessException(ResultCode.DATA_ERROR, "表信息修改失败,表编号已存在");
                }
            }
        }

        Meter meterInfo = this.get(meterDto.getId());
        meterDto.setModifyTime(LocalDateTime.now());
        meterDto.setVersion(meterInfo.getVersion() + 1);

        //修改操作
        BeanUtils.copyProperties(meterDto, meter);
        if (this.updateSelective(meter) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请刷新页面重试！");

        }

        return meter;
    }

    /**
     * 根据表类型,获取未绑定的表编号集合(新增表用户关系页面回显)
     *
     * @param  type 表类型,有枚举 meterTypeEnum
     * @param  keyword
     * @return meterList
     * @date   2020/6/16
     */
    @Override
    public List<Meter> listUnbindMetersByType(Integer type, String keyword) {
        MeterDto meterDto = new MeterDto();

        meterDto.setType(type);
        if (StringUtils.isNotEmpty(keyword)) {
            meterDto.setKeyword(keyword);
        }
        List<Meter> meterList = this.getActualDao().listUnbindMetersByType(meterDto);

        return meterList;
    }

    /**
     * 根据 number 查询实体
     *
     * @param  number
     * @return Meter
     * @date   2020/7/14
     */
    @Override
    public Meter getMeterByNumber(String number) {
        return this.getActualDao().getMeterByNumber(number);
    }

}