package com.dili.ia.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.service.impl.CustomerAccountServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 远程调用解析
 * @author yangfan
 * @date 2020年6月12日
 */
@Component
public class UidRpcResolver {
    private final static Logger LOG = LoggerFactory.getLogger(UidRpcResolver.class);

	@Autowired
	private UidFeignRpc uidFeignRpc;
	
	/**
	 * 
	 * @Title bizNumber
	 * @Description 编号生成rpc解析
	 * @param type
	 * @return
	 * @throws
	 */
	public String bizNumber(String type){
		BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(type);
        if(!bizNumberOutput.isSuccess()){
        	LOG.info("编号生成失败!" + type);
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成失败!");
        }
		return bizNumberOutput.getData();	
	};
	
}
