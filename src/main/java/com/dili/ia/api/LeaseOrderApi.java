package com.dili.ia.api;

import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.LeaseOrderService;
import com.dili.ia.service.LeaseOrderWorkerService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.AppException;
import com.dili.ss.exception.BusinessException;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 摊位租赁api
 */
@RestController
@RequestMapping("/api/leaseOrder")
public class LeaseOrderApi {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderApi.class);

    @Autowired
    LeaseOrderService leaseOrderService;
    @Autowired
    LeaseOrderItemService leaseOrderItemService;
    @Autowired
    LeaseOrderWorkerService leaseOrderWorkerService;

    @Autowired
    SettlementRpc settlementRpc;

    @Autowired
    AssetsRpc assetsRpc;

    /**
     * 测试分布式事务回滚
     * 新增品类和结算单，在抛异常前可以看到数据已经正常插入，抛异常后，数据成功回滚
     * http://127.0.0.1:8381/api/leaseOrder/testGlobalTransactional
     * @return
     */
    @RequestMapping(value="/testGlobalTransactional")
    @GlobalTransactional
    public @ResponseBody BaseOutput<String> test(){
        //新增品类
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("测试品类");
        categoryDTO.setPingying("ceshi");
        categoryDTO.setPyInitials("cspl");
        categoryDTO.setIsDelete(0);
        categoryDTO.setPath("test");
        categoryDTO.setCode("test");
        categoryDTO.setCreatorId(1L);
        categoryDTO.setCreateTime(new Date());
        categoryDTO.setModifyTime(new Date());
        categoryDTO.setState(1);
        try {
            assetsRpc.save(categoryDTO);
        }catch (Exception e){
            e.printStackTrace();
        }

        //新增结算单
        SettleOrderDto settleOrderDto = new SettleOrderDto();
        settleOrderDto.setMarketId(1L);
        settleOrderDto.setMarketCode("group");
        settleOrderDto.setAppId(101L);
        settleOrderDto.setBusinessCode("T0001");
        settleOrderDto.setBusinessType(1);
        settleOrderDto.setBusinessDepId(49L);
        settleOrderDto.setBusinessDepName("成都技术部new");
        settleOrderDto.setCustomerId(1L);
        settleOrderDto.setCustomerName("test");
        settleOrderDto.setCustomerPhone("13333333333");
        settleOrderDto.setAmount(100L);
        settleOrderDto.setSubmitterId(1L);
        settleOrderDto.setSubmitterName("admin");
        settleOrderDto.setSubmitterDepId(49L);
        settleOrderDto.setSubmitterDepName("成都技术部new");
        settleOrderDto.setSubmitTime(LocalDateTime.now());
        settleOrderDto.setType(1);
        settleOrderDto.setWay(1);
        settleOrderDto.setState(1);
        settleOrderDto.setOperatorId(1L);
        settleOrderDto.setOperatorName("admin");
        settleOrderDto.setOperateTime(LocalDateTime.now());
        settleOrderDto.setAccountNumber("123456789");
        settleOrderDto.setBankName("testBank");
        settleOrderDto.setBankCardHolder("");
        settleOrderDto.setSerialNumber("");
        settleOrderDto.setEditEnable(1);
        settleOrderDto.setNotes("test info");
        settleOrderDto.setBusinessName("test");
        settleOrderDto.setReturnUrl("http://www.baidu.com");
        try {
            settlementRpc.submit(settleOrderDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //主动抛异常回滚事务
        if(true){
            throw new AppException("测试事务回滚");
        }
        return BaseOutput.success();
    }
    /**
     * 摊位租赁结算成功回调
     * @param settleOrder
     * @return
     */
    @RequestMapping(value="/settlementDealHandler", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder){
        try{
            return leaseOrderService.updateLeaseOrderBySettleInfo(settleOrder);
        }catch (BusinessException e){
            LOG.info("摊位租赁结算成功回调异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("摊位租赁结算成功回调异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 扫描已生效但状态未变更的单子，更改其状态
     * cron 0 0 0 * * ?
     * @return
     */
    @RequestMapping(value="/scanEffectiveLeaseOrder")
    public @ResponseBody BaseOutput<Boolean> scanEffectiveLeaseOrder(){
        try{
            return leaseOrderWorkerService.scanEffectiveLeaseOrder();
        }catch (Exception e){
            LOG.error("扫描已生效但状态未变更的单子异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 扫描已到期但状态未变更的单子，更改其状态
     * cron 0 0 0 * * ?
     * @return
     */
    @RequestMapping(value="/scanExpiredLeaseOrder")
    public @ResponseBody BaseOutput<Boolean> scanExpiredLeaseOrder(){
        try{
            return leaseOrderWorkerService.scanExpiredLeaseOrder();
        }catch (Exception e){
            LOG.error("扫描已到期但状态未变更的单子异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 扫描等待停租的摊位
     * cron 0 0 0 * * ?
     * @return
     */
    @RequestMapping(value="/scanWaitStopRentLeaseOrder")
    public @ResponseBody BaseOutput<Boolean> scanWaitStopRentLeaseOrder(){
        try{
            return leaseOrderWorkerService.scanWaitStopRentLeaseOrder();
        }catch (Exception e){
            LOG.error("扫描等待停租的摊位异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 扫描等待停租的摊位
     * cron 0 0 0 * * ?
     * @return
     */
    @RequestMapping(value="/queryPrintData")
    public @ResponseBody BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint){
        try{
            if(StringUtils.isBlank(orderCode) || null == reprint){
                return BaseOutput.failure("参数错误");
            }
            return leaseOrderService.queryPrintData(orderCode,reprint);
        }catch (Exception e){
            LOG.error("扫描等待停租的摊位异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
