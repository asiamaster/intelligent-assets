package com.dili.ia.service.stockIn;

import com.dili.ia.domain.StockIn;
import com.dili.ia.glossary.StockInStateEnum;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年6月22日
 */
public interface StockInState {
	StockIn action(StockIn stockIn);
}
