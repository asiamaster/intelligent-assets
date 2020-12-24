package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.domain.dto.BoutiqueFreeSetsDto;
import com.dili.ia.glossary.BoutiqueCarTypeEnum;
import com.dili.ia.mapper.BoutiqueFreeSetsMapper;
import com.dili.ia.service.BoutiqueFreeSetsService;
import com.dili.ss.base.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/15
 * @version:      农批业务系统重构
 * @description:  精品停车免费时长
 */
@Service
public class BoutiqueFreeSetsServiceImpl extends BaseServiceImpl<BoutiqueFreeSets, Long> implements BoutiqueFreeSetsService {

    public BoutiqueFreeSetsMapper getActualDao() {
        return (BoutiqueFreeSetsMapper)getDao();
    }

    /**
     * 获取不同车型的免费时长
     *
     * @return BoutiqueFreeSetsDto
     * @date   2020/8/11
     */
    @Override
    public BoutiqueFreeSetsDto getHour() {
        BoutiqueFreeSetsDto boutiqueFreeSetsDto = new BoutiqueFreeSetsDto();

        List<BoutiqueFreeSets> list = this.getActualDao().selectAll();
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
     * @param  boutiqueFreeSetsDto
     * @date   2020/8/11
     */
    @Override
    public void updateFeeSets(BoutiqueFreeSetsDto boutiqueFreeSetsDto) {
        List<BoutiqueFreeSets> listAdd = new ArrayList<>();
        List<BoutiqueFreeSets> boutiqueFreeSetsList = this.getActualDao().selectAll();
        if (!CollectionUtils.isNotEmpty(boutiqueFreeSetsList)) {
            // 初始化
            BoutiqueFreeSets boutiqueFreeSets = new BoutiqueFreeSets();
            boutiqueFreeSets.setCreateTime(LocalDateTime.now());
            boutiqueFreeSets.setModifyTime(LocalDateTime.now());
            boutiqueFreeSets.setVersion(0);
            boutiqueFreeSets.setFreeHours(boutiqueFreeSetsDto.getTrailer());
            boutiqueFreeSets.setCarTypeName(BoutiqueCarTypeEnum.TRAILER.getName());
            listAdd.add(boutiqueFreeSets);

            BoutiqueFreeSets boutiqueFreeSetsTwo = new BoutiqueFreeSets();
            boutiqueFreeSetsTwo.setCreateTime(LocalDateTime.now());
            boutiqueFreeSetsTwo.setModifyTime(LocalDateTime.now());
            boutiqueFreeSetsTwo.setVersion(0);
            boutiqueFreeSetsTwo.setFreeHours(boutiqueFreeSetsDto.getTruck());
            boutiqueFreeSetsTwo.setCarTypeName(BoutiqueCarTypeEnum.CONTAINER_TRUCK.getName());
            listAdd.add(boutiqueFreeSetsTwo);

            this.batchInsert(listAdd);
        } else {
            for (BoutiqueFreeSets freeSets : boutiqueFreeSetsList) {
                if (BoutiqueCarTypeEnum.TRAILER.getName().equals(freeSets.getCarTypeName())) {
                    freeSets.setFreeHours(boutiqueFreeSetsDto.getTrailer());
                    listAdd.add(freeSets);
                } else if (BoutiqueCarTypeEnum.CONTAINER_TRUCK.getName().equals(freeSets.getCarTypeName())) {
                    freeSets.setFreeHours(boutiqueFreeSetsDto.getTruck());
                    listAdd.add(freeSets);
                }
            }
        }


        this.batchUpdateSelective(listAdd);
    }

    /**
     * 根据车型查询免费时长
     *
     * @param  carTypeName
     * @date   2020/8/11
     */
    @Override
    public BoutiqueFreeSets getByCarTypeName(String carTypeName) {
        BoutiqueFreeSets boutiqueFreeSets = new BoutiqueFreeSets();
        BoutiqueFreeSets boutiqueFreeSetsInfo = new BoutiqueFreeSets();
        boutiqueFreeSets.setCarTypeName(carTypeName);

        List<BoutiqueFreeSets> boutiqueFreeSetsList = this.getActualDao().select(boutiqueFreeSets);
        if (CollectionUtils.isNotEmpty(boutiqueFreeSetsList)) {
            boutiqueFreeSetsInfo = boutiqueFreeSetsList.get(0);
        }
        return boutiqueFreeSetsInfo;
    }
}