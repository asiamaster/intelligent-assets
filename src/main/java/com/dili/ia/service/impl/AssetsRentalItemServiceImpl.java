package com.dili.ia.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.assets.sdk.dto.AssetsQuery;
import com.dili.assets.sdk.rpc.AreaMarketRpc;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.customer.sdk.constants.MqConstant;
import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.domain.dto.AssetsRentalItemDto;
import com.dili.ia.domain.dto.AssetsRentalMchDistrictDto;
import com.dili.ia.domain.dto.AssetsRentalMchDistrictListDto;
import com.dili.ia.glossary.AssetsRentalStateEnum;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.mapper.AssetsRentalItemMapper;
import com.dili.ia.service.AssetsRentalItemService;
import com.dili.ia.service.AssetsRentalService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设 中间表
 */
@Service
public class AssetsRentalItemServiceImpl extends BaseServiceImpl<AssetsRentalItem, Long> implements AssetsRentalItemService {

    public AssetsRentalItemMapper getActualDao() {
        return (AssetsRentalItemMapper)getDao();
    }

    @Autowired
    AreaMarketRpc areaMarketRpc;

    @Autowired
    private AssetsRpc assetsRpc;

    @Autowired
    private AssetsRentalService assetsRentalService;

    /**
     * 根据关联主键删除对应的资产
     *
     * @param   assetsRentalId
     * @return
     * @date    2020/11/26
     */
    @Override
    public void deleteByRentalId(Long assetsRentalId) {
        List<Long> assetsRentalIdList = new ArrayList<>();
        assetsRentalIdList.add(assetsRentalId);
        this.getActualDao().deleteByRentalIdList(assetsRentalIdList);
    }

    /**
     * 根据关联主键批量删除对应的资产
     *
     * @param   rentalIdList
     * @return  int
     * @date    2020/12/22
     */
    @Override
    public int deleteByRentalIdList(List<Long> rentalIdList) {
        return this.getActualDao().deleteByRentalIdList(rentalIdList);
    }

    /**
     * 根据 assetsIds 查询属于表中的 assetsId 的集合,摊位出租预设的主表状态是启用
     *
     * @param  assetsIds
     * @param  state
     * @return assetsIds
     * @date   2020/12/7
     */
    @Override
    public List<Long> listRentalItemsByAssetsIds(List<Long> assetsIds, Integer state) {
        return this.getActualDao().listRentalItemsByAssetsIds(assetsIds, state);
    }

    /**
     * 过滤出不属于预设池中的摊位集合
     *
     * @param  assetsRentalDto
     * @return list
     * @date   2020/12/8
     */
    @Override
    public List<AssetsDTO> filterAssets(AssetsRentalDto assetsRentalDto) {
        List<AssetsDTO> assetsDTOList = new ArrayList<>();

        // 根据条件获取到基础信息的摊位集合（根据摊位类型，区域，摊位名称，数量）
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        AssetsQuery assetsQuery = new AssetsQuery();
        assetsQuery.setKeyword(assetsRentalDto.getAssetsName());
        assetsQuery.setMarketId(userTicket.getFirmId());
        assetsQuery.setBusinessType(AssetsTypeEnum.BOOTH.getCode());
        assetsQuery.setStartNumber(assetsRentalDto.getStartNumber());
        assetsQuery.setEndNumber(assetsRentalDto.getEndNumber());
        if (null != assetsRentalDto.getFirstDistrictId()){
            assetsQuery.setArea(Integer.parseInt(assetsRentalDto.getFirstDistrictId().toString()));
        }
        if (null != assetsRentalDto.getSecondDistrictId()){
            assetsQuery.setSecondArea(Integer.parseInt(assetsRentalDto.getSecondDistrictId().toString()));
        }
        List<AssetsDTO> assets = assetsRpc.searchAssets(assetsQuery).getData();

        if (CollectionUtils.isNotEmpty(assets)) {
            // 根据条件搜索得到的摊位 ids 查询预设池中摊位集合
            List<Long> assetsIds = new ArrayList<>();
            for (AssetsDTO assetsDTO : assets) {
                assetsIds.add(assetsDTO.getId());
            }
            List<AssetsRentalItem> assetsRentalItemDtoInfoList = this.getActualDao().listAssetsItemsByAssetsIds(assetsIds);

            // 预设池中摊位集合（同时属于预设池和条件搜索的集合）
            List<Long> assetsIdsInTable = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(assetsRentalItemDtoInfoList)) {
                for (AssetsRentalItem assetsRentalItem : assetsRentalItemDtoInfoList) {
                    assetsIdsInTable.add(assetsRentalItem.getAssetsId());
                }
            }

            // 获取 搜索的摊位集合有，预设池摊位集合中没有的
            assetsIds.removeAll(assetsIdsInTable);

            // 返回预设池中没有的摊位集合
            if (CollectionUtils.isNotEmpty(assetsIds)) {
                for (AssetsDTO assetsDTO : assets) {
                    for (Long assetsId : assetsIds) {
                        if (assetsId.equals(assetsDTO.getId())) {
                            assetsDTOList.add(assetsDTO);
                        }
                    }
                }
            }
        }

        return assetsDTOList;
    }

    /**
     * 根据预设id查询预设的摊位信息
     *
     * @param  rentalId
     * @return list
     * @date   2020/12/24
     */
    @Override
    public List<AssetsRentalItem> listRentalItemsByRentalId(Long rentalId) {

        return this.getActualDao().listRentalItemsByRentalId(rentalId);
    }


    /**
     * MQ 监听 修改摊位的信息（修改区域或者修改基础信息）
     *
     * @param  assetsRentalItemDto
     * @return
     * @date   2020/12/8
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "customer.info", autoDelete = "false"),
            exchange = @Exchange(value = MqConstant.CUSTOMER_MQ_FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT)
    ))
    public void updateAssetsItemByAssets(AssetsRentalItemDto assetsRentalItemDto){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        boolean isDeleteAssets = false;
        Long assetId = 1L;

        // 根据摊位ID查询预设池中的预设摊位，如果没有，则忽略
        AssetsRentalItemDto itemDtoInfo = this.getActualDao().getAssetsItemsByAssetsId(assetId);
        if (itemDtoInfo != null) {
            // 先拿到摊位ID，查询基础信息
            AssetsQuery assetsQuery = new AssetsQuery();
            assetsQuery.setId(assetId);
            assetsQuery.setMarketId(userTicket.getFirmId());
            assetsQuery.setBusinessType(AssetsTypeEnum.BOOTH.getCode());
            List<AssetsDTO> assets = assetsRpc.searchAssets(assetsQuery).getData();
            if (CollectionUtils.isNotEmpty(assets)) {
                AssetsDTO assetsDTO = assets.get(0);
                Long districtId = Long.valueOf(assetsDTO.getArea());
                if (itemDtoInfo.getFirstDistrictId().equals(districtId) || itemDtoInfo.getSecondDistrictId().equals(districtId)) {
                    // 区域未改变，查询商户是否改变，根据摊位的区域ID查询商户ID
                    BaseOutput<Long> mchOutput = areaMarketRpc.getMarketByArea(districtId);
                    if (mchOutput.isSuccess()){
                        Long mchId = mchOutput.getData();
                        if (!itemDtoInfo.getMchId().equals(mchId) ) {
                            // 商户改变，剔除预设池中的摊位
                            isDeleteAssets = true;
                        } else {
                            // 区域和商户都没有改变，则修改相关属性
                            itemDtoInfo.setModifyTime(LocalDateTime.now());
                            itemDtoInfo.setAssetsName(assetsDTO.getName());
                            itemDtoInfo.setAssetsType(assetsDTO.getType());
                            itemDtoInfo.setNumber(assetsDTO.getNumber());
                            itemDtoInfo.setUnit(assetsDTO.getUnit());
                            itemDtoInfo.setCorner(assetsDTO.getCorner());
                            this.getActualDao().updateByPrimaryKey(itemDtoInfo);
                        }
                    }
                } else {
                    // 区域改变，剔除预设池中的摊位
                    isDeleteAssets = true;
                }
            }
        }

        // 剔除预设池中的摊位
        if (isDeleteAssets) {
            this.getActualDao().delete(itemDtoInfo);
        }
    }
}