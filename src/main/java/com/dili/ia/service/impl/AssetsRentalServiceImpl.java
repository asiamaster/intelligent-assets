package com.dili.ia.service.impl;

import com.dili.ia.controller.AssetsRentalController;
import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.mapper.AssetsRentalItemMapper;
import com.dili.ia.mapper.AssetsRentalMapper;
import com.dili.ia.service.AssetsRentalService;
import com.dili.ss.base.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设
 */
@Service
public class AssetsRentalServiceImpl extends BaseServiceImpl<AssetsRental, Long> implements AssetsRentalService {

    private final static Logger logger = LoggerFactory.getLogger(AssetsRentalServiceImpl.class);

    public AssetsRentalMapper getActualDao() {
        return (AssetsRentalMapper)getDao();
    }

    public AssetsRentalItemServiceImpl assetsRentalItemService;

    /**
     * 新增资产出租预设
     *
     * @param  assetsRentalDto
     * @return AssetsRental
     * @date   2020/11/26
     */
    @Override
    public AssetsRental addAssetsRental(AssetsRentalDto assetsRentalDto) {
        // 新增到资产出租预设表



        // 新增到关联表

        return null;
    }

    /**
     * 修改资产出租预设
     *
     * @param  assetsRentalDto
     * @return AssetsRental
     * @date   2020/11/26
     */
    @Override
    public AssetsRental updateAssetsRental(AssetsRentalDto assetsRentalDto) {





        return null;
    }
}