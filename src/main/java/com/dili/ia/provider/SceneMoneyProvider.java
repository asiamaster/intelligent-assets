package com.dili.ia.provider;

import com.dili.ia.glossary.TransactionSceneTypeEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-20 11:37
 */
@Component
public class SceneMoneyProvider implements ValueProvider {

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
        Object sceneTypeObj = objData.get("$_sceneType");
        if (null != objData.get("$_sceneType")){
            Integer sceneType = Integer.valueOf(sceneTypeObj.toString());
            //【金额显示为负数】 场景为：抵扣消费， 冻结， 定金转出， 退款 ，业务收款作废转出。
            if (sceneType.equals(TransactionSceneTypeEnum.DEDUCT_USE.getCode()) || sceneType.equals(TransactionSceneTypeEnum.FROZEN.getCode())
                    || sceneType.equals(TransactionSceneTypeEnum.EARNEST_OUT.getCode()) || sceneType.equals(TransactionSceneTypeEnum.REFUND.getCode())
                    || sceneType.equals(TransactionSceneTypeEnum.INVALID_OUT.getCode()) ){
                amount = 0 - amount;
            }
        }
        return null == obj ? null :  MoneyUtils.centToYuan(amount);
    }
}
