package com.dili.ia.service.impl;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.mapper.MeterDetailMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.MeterDetailService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/23
 * @version:     农批业务系统重构
 * @description: 水电费 service 实现层
 */
@Service
public class MeterDetailServiceImpl extends BaseServiceImpl<MeterDetail, Long> implements MeterDetailService {

    private final static Logger logger = LoggerFactory.getLogger(MeterDetailServiceImpl.class);

    public MeterDetailMapper getActualDao() {
        return (MeterDetailMapper)getDao();
    }

    @Autowired
    private UidRpcResolver UidRpcResolver;


    /**
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param:       meterDetail
     * @return:      EasyuiPageOutput
     * @description：meter、meterDetail 两表查询列表数据
     */
    @Override
    public EasyuiPageOutput listMeterDetails(MeterDetailDto meterDetailDto, boolean useProvider) throws Exception {
        // 查询列表时必须表类型不能为空
        Integer type = meterDetailDto.getType();
        if (type == null) {
            logger.info("查询缴费信息表类型为空，报错！");
        }
        // 分页
        if (meterDetailDto.getRows() != null && meterDetailDto.getRows() >= 1) {
            PageHelper.startPage(meterDetailDto.getPage(), meterDetailDto.getRows());
        }

        // 联表查询表编号、部门等
        List<MeterDetailDto> meterDetailDtooList= this.getActualDao().listMeterDetails(meterDetailDto);

        // 基础代码
        long total = meterDetailDtooList instanceof Page ? ((Page)meterDetailDtooList).getTotal() : (long)meterDetailDtooList.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(meterDetailDto, meterDetailDtooList) : meterDetailDtooList;
        return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);
    }

    @Override
    public void addMeterDetail(MeterDetailDto meterDetailDto, UserTicket userTicket) {
        MeterDetail meterDetail = new MeterDetail();
        // 生成水电费单号的 code
        meterDetail.setCode(UidRpcResolver.bizNumber(userTicket.getFirmCode()+"_"+ BizNumberTypeEnum.METER_DETAIL_CODE.getCode()));




    }

    /**
     * @author:       xiaosa
     * @date:         2020/6/29
     * @param:        meterId, customerId
     * @return:       Integer
     * @description： 根据 meterId、customerId 查询未缴费单的数量
     */
    @Override
    public Integer countUnPayByMeterAndCustomer(Long meterId, Long customerId) {
        MeterDetailDto meterDetailDto = new MeterDetailDto();
        // 业务类型(水表、电表)
        List<Integer> bizTypeList = Lists.newArrayList(BizTypeEnum.WATER_METER.getCode(), BizTypeEnum.ELECTRIC_METER.getCode());
        String bizTypes = bizTypeList.toString().replace("[", "").replace("]", "");
        meterDetailDto.setBizTypes(bizTypes);
        meterDetailDto.setMeterId(meterId);
        meterDetailDto.setCustomerId(customerId);
        List<Long> list = this.getActualDao().countUnPayByMeterAndCustomer(meterDetailDto);
        if (CollectionUtils.isEmpty(list)){
            return 0;
        }
        return list.size();
    }
}