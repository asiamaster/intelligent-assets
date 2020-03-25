package com.dili.ia.util;

import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.glossary.LoggerConstant;
import org.apache.commons.lang.StringUtils;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-25 9:41
 */
public class LoggerUtil {
    public static void buildLoggerContext(Long businessId, String businessCode, Long operatorId, String operatorName, Long marketId, String notes){
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, businessCode);
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, businessId);
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, operatorId);
        LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, operatorName);
        LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, marketId);
        LoggerContext.put("notes", notes);

        return;
    }
}
