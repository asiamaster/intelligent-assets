package com.dili.ia.service.stockIn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.StockInDetail;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.service.StockInService;
import com.dili.ia.service.StockService;
import com.dili.ss.exception.BusinessException;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年6月22日
 */
@Component
public class StockInAction {
	
	@Autowired
	private StockInService stockInService;
	
	public void stockInAction(String code, StockInState stockInState) {
		StockIn stockIn = stockInService.getStockInByCode(code);
		stockInState.action(stockIn);
		stockInService.update(condtion);
	}
}
class Cancle implements StockInState{

	@Override
	public StockIn action(StockIn stockIn) {
		StockIn domain = new StockIn();
		if(stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException("", "");
		}
		domain.setCode(stockIn.getCode());
		domain.setState(StockInStateEnum.CANCELLED.getCode());
		return stockIn;
	}
	
}
class Submit implements StockInState{

	@Override
	public StockIn action(StockIn stockIn) {
		StockIn domain = new StockIn();
		if(stockIn.getState() != StockInStateEnum.CREATED.getCode()) {
			throw new BusinessException("", "");
		}
		domain.setCode(stockIn.getCode());
		domain.setState(StockInStateEnum.SUBMITTED.getCode());
		//TODO 创建收费单
		return domain;
	}
	
}
class Paid implements StockInState{

	@Autowired
	private StockService stockService;
	
	@Override
	public StockIn action(StockIn stockIn) {
		StockIn domain = new StockIn();
		if(stockIn.getState() != StockInStateEnum.SUBMITTED.getCode()) {
			throw new BusinessException("", "");
		}
		domain.setCode(stockIn.getCode());
		domain.setState(StockInStateEnum.PAID.getCode());
		//TODO 缴费
		//TODO 创建库存
		List<StockInDetail> stockInDetails = getStockInDetailsByStockCode(stockIn.getCode());
		stockService.inStock(stockInDetails, stockIn);
		return domain;
	}
	
}














