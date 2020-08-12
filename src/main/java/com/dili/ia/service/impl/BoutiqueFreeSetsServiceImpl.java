package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.domain.dto.BoutiqueFreeSetsDto;
import com.dili.ia.glossary.BoutiqueCarTypeEnum;
import com.dili.ia.mapper.BoutiqueFreeSetsMapper;
import com.dili.ia.service.BoutiqueFreeSetsService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-13 10:49:05.
 */
@Service
public class BoutiqueFreeSetsServiceImpl extends BaseServiceImpl<BoutiqueFreeSets, Long> implements BoutiqueFreeSetsService {

    public BoutiqueFreeSetsMapper getActualDao() {
        return (BoutiqueFreeSetsMapper)getDao();
    }


    @Override
    public BoutiqueFreeSetsDto getHour() {
        BoutiqueFreeSetsDto boutiqueFreeSetsDto = new BoutiqueFreeSetsDto();

        BoutiqueFreeSets boutiqueFreeSets = new BoutiqueFreeSets();
        List<BoutiqueFreeSets> list = this.list(boutiqueFreeSets);
        list.forEach(freeSets -> {
            if (BoutiqueCarTypeEnum.TRAILER.getName().equals(freeSets.getCarTypeName())) {
                boutiqueFreeSetsDto.setTrailer(freeSets.getFreeHours());
            } else if (BoutiqueCarTypeEnum.CONTAINER_TRUCK.getName().equals(freeSets.getCarTypeName())) {
                boutiqueFreeSetsDto.setTruck(freeSets.getFreeHours());
            }
        });

        return boutiqueFreeSetsDto;
    }

    /**
     * 修改免费时长
     *
     * @param
     * @return
     * @date   2020/8/11
     */
    @Override
    public void updateFeeSets(BoutiqueFreeSetsDto boutiqueFreeSetsDto) {

        BoutiqueFreeSets boutiqueFreeSets = new BoutiqueFreeSets();
        List<BoutiqueFreeSets> listAdd = this.list(boutiqueFreeSets);
        List<BoutiqueFreeSets> list = this.list(boutiqueFreeSets);
        for (BoutiqueFreeSets freeSets : list) {
            if (BoutiqueCarTypeEnum.TRAILER.getName().equals(freeSets.getCarTypeName())) {
                freeSets.setFreeHours(boutiqueFreeSetsDto.getTrailer());
                listAdd.add(freeSets);
            } else if (BoutiqueCarTypeEnum.CONTAINER_TRUCK.getName().equals(freeSets.getCarTypeName())) {
                freeSets.setFreeHours(boutiqueFreeSetsDto.getTruck());
                listAdd.add(freeSets);
            }
        }

        this.batchUpdateSelective(listAdd);
    }
}