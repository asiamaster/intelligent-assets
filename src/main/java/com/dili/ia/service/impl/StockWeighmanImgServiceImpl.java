package com.dili.ia.service.impl;

import com.dili.ia.domain.StockWeighmanImg;
import com.dili.ia.mapper.StockWeighmanImgMapper;
import com.dili.ia.service.StockWeighmanImgService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockWeighmanImgServiceImpl extends BaseServiceImpl<StockWeighmanImg, Long> implements StockWeighmanImgService {

    public StockWeighmanImgMapper getActualDao() {
        return (StockWeighmanImgMapper)getDao();
    }
}