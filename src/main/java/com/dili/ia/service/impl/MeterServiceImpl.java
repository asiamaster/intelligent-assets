package com.dili.ia.service.impl;

import com.dili.ia.controller.MeterController;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ia.mapper.MeterMapper;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @version:     农批业务系统重构
     * @description: 下载表列表
     */
    @Override
    public void downMeterList(MeterDto meterDto) {

    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       meterDto
     * @return:      BaseOutput
     * @description：新增表信息
     */
    @Override
    public BaseOutput<Meter> addMeter(MeterDto meterDto) {
        Meter meter = new Meter();

        // 校验用户是否登陆
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }

        // 根据表编号查询是否已存在
        meter.setNumber(meterDto.getNumber());
        List<Meter> meterList = this.getActualDao().select(meter);
        if (meterList != null && meterList.size() > 0) {
            return BaseOutput.failure("表编号已存在");
        }

        // 获取部门名称
        BaseOutput<Department> depOut = departmentRpc.get(meterDto.getDepartmentId());
        if(!depOut.isSuccess()){
            LOGGER.info("获取部门失败！" + depOut.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "获取部门失败！");
        }

        // 设置相关属性值
        meterDto.setCreatorId(userTicket.getId());
        meterDto.setCreator(userTicket.getUserName());
        meterDto.setCreatorDepId(userTicket.getDepartmentId());
        meterDto.setDepartmentName(depOut.getData().getName());
        meterDto.setMarketId(userTicket.getFirmId());
        meterDto.setMarketCode(userTicket.getFirmCode());
        meterDto.setCreateTime(new Date());
        meterDto.setModifyTime(new Date());
        meterDto.setVersion(1);

        BeanUtils.copyProperties(meterDto, meter);
        this.insertSelective(meter);

        return BaseOutput.success();
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param:       meterDto
     * @return:      BaseOutput
     * @description：修改表信息
     */
    @Override
    public BaseOutput<Meter> updateMeter(MeterDto meterDto) {
        Meter meter = new Meter();

        // 校验用户是否登陆, 并设置相关信息
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }

        // 根据表编号查询是否已存在
        meter.setNumber(meterDto.getNumber());
        List<Meter> meterList = this.getActualDao().select(meter);
        if (meterList != null && meterList.size() > 0) {
            for (Meter meterRe : meterList) {
                if (!meterRe.getId().equals(meterDto.getId())) {
                    return BaseOutput.failure("表编号已存在");
                }
            }
        }

        Meter meterInfo = this.get(meterDto.getId());

        meterDto.setModifyTime(new Date());
        meterDto.setVersion(meterInfo.getVersion() + 1);

        //修改
        BeanUtils.copyProperties(meterDto, meter);
        this.updateSelective(meter);

        return BaseOutput.success();
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param        type, name
     * @return       String
     * @description：根据表类型、表编号查询表信息
     */
    @Override
    public BaseOutput getMeterLikeNumber(Integer type, String name) {
        // 如果表地址未输入,则直接返回空
        BaseOutput baseOutput = new BaseOutput();
        Meter meter = new Meter();
        baseOutput.setData(meter);
        if (StringUtils.isNotEmpty(name)) {
            meter.setType(type);
            meter.setNumber(name);
            Meter meterInfo = this.getActualDao().getMeterLikeNumber(meter);
            baseOutput.setData(meterInfo);
        }
        return baseOutput;
    }

}