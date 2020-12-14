package com.dili.ia.provider;

import com.dili.settlement.enums.ActionEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
/**
 * <B>结算业务记录的定金流水【场景】provider</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qunkelan
 * @createTime 2020/12/14 10:17
 */
@Component
public class ActionMoneyProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (null == obj){
            return null;
        }
        Map objData = (Map)metaMap.get(ValueProvider.ROW_DATA_KEY);
        Long amount = (Long)obj;
        Object actionTypeObj = objData.get("$_action");
        if (null != objData.get("$_action")){
            Integer actionType = Integer.valueOf(actionTypeObj.toString());
            //【金额显示为负数】 场景为：出账， 【金额显示为正数】进账。
            if (actionType.equals(ActionEnum.EXPENSE.getCode())){
                amount = 0 - amount;
            }
        }
        return null == obj ? null :  MoneyUtils.centToYuan(amount);
    }
}