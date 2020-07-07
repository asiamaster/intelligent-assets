package com.dili.ia.service.impl;

import com.dili.ia.controller.MeterController;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ia.mapper.MeterMapper;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.service.MeterService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.Department;
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
import java.util.Date;
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
     * @return 是否成功
     * @date   2020/6/16
     */
    @Override
    public BaseOutput<Meter> addMeter(MeterDto meterDto) {
        Meter meter = new Meter();

        // 校验用户是否登陆
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("表信息新增失败,未登录");
        }

        // 根据表编号查询是否已存在
        meter.setNumber(meterDto.getNumber());
        List<Meter> meterList = this.getActualDao().select(meter);
        if (meterList != null && meterList.size() > 0) {
            return BaseOutput.failure("表信息新增失败,表编号已存在！");
        }

        // 设置相关属性值
        meterDto.setCreatorId(userTicket.getId());
        meterDto.setCreator(userTicket.getUserName());
        meterDto.setCreatorDepId(userTicket.getDepartmentId());
        meterDto.setMarketId(userTicket.getFirmId());
        meterDto.setMarketCode(userTicket.getFirmCode());
        meterDto.setCreateTime(LocalDateTime.now());
        meterDto.setModifyTime(LocalDateTime.now());
        meterDto.setVersion(1);

        BeanUtils.copyProperties(meterDto, meter);
        this.insertSelective(meter);

        return BaseOutput.success();
    }

    /**
     * 修改表信息
     *
     * @param  meterDto
     * @return 是否成功
     * @date   2020/6/29
     */
    @Override
    public BaseOutput<Meter> updateMeter(MeterDto meterDto) {
        Meter meter = new Meter();

        // 校验用户是否登陆, 并设置相关信息
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("表信息修改失败,未登录");
        }

        // 根据表 meterId、用户 customerId 查询未缴费的记录数量
        Integer count = meterDetailService.countUnPayByMeterAndCustomer(meterDto.getId(), null);
        if (count > 0) {
            return BaseOutput.failure("该表存在待交费单，不能修改");
        }

        // 根据表编号查询是否已存在
        meter.setNumber(meterDto.getNumber());
        List<Meter> meterList = this.getActualDao().select(meter);
        if (CollectionUtils.isNotEmpty(meterList)) {
            for (Meter meterRe : meterList) {
                if (!meterRe.getId().equals(meterDto.getId())) {
                    return BaseOutput.failure("表信息修改失败,表编号已存在");
                }
            }
        }

        Meter meterInfo = this.get(meterDto.getId());
        meterDto.setModifyTime(LocalDateTime.now());
        meterDto.setVersion(meterInfo.getVersion() + 1);

        //修改操作
        BeanUtils.copyProperties(meterDto, meter);
        int code = this.updateSelective(meter);
        if (code == 0) {
            return BaseOutput.failure("当前数据正在被其他用户操作，提交失败！请关闭当前弹窗重新选择操作");
        }

        return BaseOutput.success();
    }

    /**
     * 根据表类型,获取未绑定的表编号集合(新增表用户关系页面回显)
     *
     * @param  type 表类型,有枚举 meterTypeEnum
     * @return meterList
     * @date   2020/6/16
     */
    @Override
    public List<Meter> listUnbindMetersByType(Integer type) {
        List<Meter> meterList = this.getActualDao().listUnbindMetersByType(type);

        return meterList;
    }

    /**
     * 根据表类型、表编号查询表信息(新增缴水电费时页面回显)
     *
     * @param  type   表类型,有枚举 meterTypeEnum
     * @param  number 表编号
     * @return meterList
     * @date   2020/6/28
     */
    @Override
    public Meter getMeterLikeNumber(Integer type, String number) {
        Meter meterInfo = new Meter();

        // 如果表地址未输入,则直接返回空
        Meter meter = new Meter();
        if (StringUtils.isNotEmpty(number)) {
            meter.setType(type);
            meter.setNumber(number);
            meterInfo = this.getActualDao().getMeterLikeNumber(meter);
        }

        return meterInfo;
    }

}