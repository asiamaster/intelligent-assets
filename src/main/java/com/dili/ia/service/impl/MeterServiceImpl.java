package com.dili.ia.service.impl;

import com.dili.ia.controller.MeterController;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ia.mapper.MeterMapper;
import com.dili.ia.service.MeterService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
     * @param:       id
     * @return:      Meter
     * @description：根据 id 查询表信息
     */
    @Override
    public Meter getMeterById(Long id) {
        Meter meter = this.getActualDao().getMeterById(id);
        return meter;
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

        // 校验用户是否登陆, 并设置相关信息
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }
        meterDto.setCreatorId(userTicket.getId());
        meterDto.setCreator(userTicket.getUserName());
        meterDto.setCreatorDepId(userTicket.getDepartmentId());

        // 设置开始、修改时间
        meterDto.setCreateTime(new Date());
        meterDto.setModifyTime(new Date());

        // TODO 差市场code 市场id

        BeanUtils.copyProperties(meterDto, meter);
        this.getActualDao().insert(meter);

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

        // 设置修改时间
        meterDto.setModifyTime(new Date());

        //修改
        BeanUtils.copyProperties(meterDto, meter);
        this.getActualDao().updateByPrimaryKeySelective(meter);

        return BaseOutput.success();
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

        // TODO sql 未完成
        List<Meter> meterList = this.getActualDao().getUnbindMeterByType(type);
        baseOutput.setData(baseOutput);

        return baseOutput;
    }


//    /**
//     * @author:      xiaosa
//     * @date:        2020/6/16
//     * @description：校验用户是否登陆(方法抽取)
//     */
//    private BaseOutput checkUserLogin() {
//
//
//
//    }


}