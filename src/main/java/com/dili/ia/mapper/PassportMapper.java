package com.dili.ia.mapper;

import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.PassportDto;
import com.dili.ss.base.MyMapper;

/**
 * @author:      xiaosa
 * @date:        2020/7/27
 * @version:     农批业务系统重构
 * @description: 通行证
 */
public interface PassportMapper extends MyMapper<Passport> {
    
    /**
     * 根据code查询通行证
     * 
     * @param  code
     * @return PassportDto
     * @date   2020/7/27
     */
    PassportDto getPassportByCode(String code);
}