package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.StallDetailsReport;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.mapper.BusinessChargeItemMapper;
import com.dili.ia.mapper.StallDetailsReportMapper;
import com.dili.ia.service.StallDetailsReportService;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StallDetailsReportServiceImpl implements StallDetailsReportService {

    @Autowired
    private StallDetailsReportMapper stallDetailsReportMapper;

    @Autowired
    private BusinessChargeItemMapper businessChargeItemMapper;

    @Override
    public PageOutput<List<StallDetailsReport>> listByQueryParams(StallDetailsReport stallDetailsReport, List<BusinessChargeItemDto> chargeItemDtos) {
        //设置分页参数
        Integer page = stallDetailsReport.getPage();
        page = (page == null) ? Integer.valueOf(1) : page;
        if (stallDetailsReport.getRows() != null && stallDetailsReport.getRows() >= 1) {
            PageHelper.startPage(page, stallDetailsReport.getRows());
        }
        List<StallDetailsReport> list = stallDetailsReportMapper.listByQueryParams(stallDetailsReport);
        Long total = list instanceof Page ? ((Page) list).getTotal() : list.size();
        int totalPage = list instanceof Page ? ((Page) list).getPages() : 1;
        int pageNum = list instanceof Page ? ((Page) list).getPageNum() : 1;
        PageOutput<List<StallDetailsReport>> output = PageOutput.success();
        //拿到当前查询list的id
        List<Long> collect = list.stream().map(x -> x.getOrderItemId()).collect(Collectors.toList());
        //查询对应的收费项
        List<Map<String, String>> businessChargeItems = businessChargeItemMapper.queryForReport(AssetsTypeEnum.getAssetsTypeEnum(1).getBizType(), collect, chargeItemDtos);
        if (CollectionUtils.isNotEmpty(businessChargeItems)) {
            //如果集合不为空，则将map转换成以订单像id为key的对象
            Map<Long, Map<String, String>> businessChargeItemMap = new HashMap<>();
            businessChargeItems.forEach(bct -> {
                businessChargeItemMap.put(Long.valueOf(bct.get("businessId")), bct);
            });
            //直接根据订单项id找到对应收费项的集合，并设置进去
            list.forEach(o -> {
                System.err.println(o.getOrderItemId());
                o.setBusinessChargeItem(businessChargeItemMap.get(o.getOrderItemId()));
            });
        }
        //逻辑判断是否出租
        for (StallDetailsReport detailsReport : list) {
            //如果根本关联不上订单项，则是未出租
            if (Objects.isNull(detailsReport.getOrderItemId())) {
                detailsReport.setIsLease("未出租");
                continue;
            }
            //判断是否是已退款
            if (Objects.equals(detailsReport.getRefundState(), 3)) {
                //判断支付状态，如果是未交清，则为未出租
                if (Objects.equals(detailsReport.getPayState(), 1)) {
                    detailsReport.setIsLease("未出租");
                    continue;
                }
                //'状态（1：已创建 2：已取消 3：已提交 4：未生效 5：已生效 6：已停租 7：已退款 8：已过期）
                //再判断是否是已停租
                if (Objects.equals(detailsReport.getState(), 6)) {
                    detailsReport.setIsLease("未出租");
                    continue;
                }
                //再判断是否是已过期
                if (Objects.equals(detailsReport.getState(), 8)) {
                    detailsReport.setIsLease("未出租");
                    continue;
                }
            }
            detailsReport.setIsLease("已出租");
        }
        output.setData(list).setPageNum(pageNum).setTotal(total.intValue()).setPageSize(stallDetailsReport.getPage()).setPages(totalPage);
        return output;
    }
}
