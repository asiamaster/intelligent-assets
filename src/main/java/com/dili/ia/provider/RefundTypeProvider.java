package com.dili.ia.provider;

import com.dili.ia.rpc.SettlementRpc;
import com.dili.settlement.domain.SettleConfig;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-11 18:13
 */
@Component
public class RefundTypeProvider implements ValueProvider {
    private final static Logger LOG = LoggerFactory.getLogger(RefundTypeProvider.class);
    @Autowired
    private SettlementRpc settlementRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        try {
            BaseOutput<List<SettleConfig>> settleOutput = settlementRpc.listEnableRefundWay(userTicket.getFirmId());
            if (settleOutput.isSuccess()){
                if (CollectionUtils.isNotEmpty(settleOutput.getData())){
                    settleOutput.getData().forEach(o->{
                        buffer.add(new ValuePairImpl(o.getVal(), o.getCode()));
                    });
                    return buffer;
                }else {
                    LOG.info("调用结算，获取市场[{}]退款方式，数据为空！", userTicket.getFirmName());
                }
            }else {
                LOG.info("调用结算，获取市场[{}]退款方式，返回失败[{}]！", userTicket.getFirmName(), settleOutput.getMessage());
            }
        }catch (Exception e){
            LOG.error("调用结算，获取市场[{}]退款方式，结算服务异常！", userTicket.getFirmName());
        }
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
