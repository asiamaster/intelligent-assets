package com.dili.ia.rpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ia.domain.MessageFee;
import com.dili.ia.util.HttpClientUtil;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.BeanConver;


/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 消息系统
 * @author yangfan
 * @date 2020年8月28日
 */
@Service
public class MessageFeeRpc {
    private static final Log LOG = LogFactory.getLog(MessageFeeRpc.class);

    @Value("${message.api.http}")
    private String messageHttp;
    @Value("${message.encode}")
    private String charset;

    /**
     * 
     * @Title postPaySuccessMessageFeeCustomer
     * @Description 成功-推送到消息中心
     * @param messageFee
     * @return
     * @throws
     */
    public void postPaySuccessMessageFeeCustomer(MessageFee messageFee) {
        try {
            String url=messageHttp+"/messageApi/whitelistCustomer.api";
            String result= HttpClientUtil.doPost(url, buildPay(messageFee),charset);
            if(result==null || "".equals(result)){
                LOG.error("---消息中心【白名单推送】接口调用异常！返回为空 ---" + messageFee);
                throw new BusinessException(ResultCode.APP_ERROR, "【白名单推送】接口调用异常!");
 
            }
            JSONObject parseObject = JSON.parseObject(result);
            if("200".equals(parseObject.getString("code"))){
            	LOG.info("新增白名单同步成功！"+messageFee.getCode());
            	return;
            }
            LOG.info(parseObject.getString("result") + messageFee );
            throw new BusinessException(ResultCode.APP_ERROR, "【白名单推送】接口调用异常!");
        }catch (Exception e){
            LOG.error("---消息中心【白名单推送】接口调用异常！ ---" + messageFee,e);
            throw new BusinessException(ResultCode.APP_ERROR, "【白名单推送】接口调用异常!");
        }
    }
    
    private JSONObject buildPay(MessageFee messageFee) {
    	JSONObject json = new JSONObject();
    	json.put("customerName", messageFee.getCustomerName());
    	json.put("cellphone", messageFee.getCustomerCellphone());
    	json.put("startDate", messageFee.getStartDate());
    	json.put("endDate", messageFee.getEndDate());
    	//json.put("sysNum", messageFee.getpa);
    	json.put("marketCode", messageFee.getMarketCode());
    	json.put("id", messageFee.getId());
		return json;
    }

    /**
     * 
     * @Title postRefundMessageFeeCustomer
     * @Description 取消-推送消息中心
     * @param messageFee
     * @return
     * @throws
     */
    public void postRefundMessageFeeCustomer(MessageFee messageFee) {
        try {
            String url=messageHttp+"/messageApi/delWhitelistCustomer.api";
            JSONObject json = new JSONObject();
            json.put("id", messageFee.getId());
        	json.put("marketCode", messageFee.getMarketCode());
            String result= HttpClientUtil.doPost(url, json,charset);
            if(result==null || "".equals(result)){
                LOG.error("---消息中心【白名单推送】接口调用异常！返回为空 ---" + messageFee);
                throw new BusinessException(ResultCode.APP_ERROR, "【白名单推送】接口调用异常!");
            }
            JSONObject parseObject = JSON.parseObject(result);
            if("200".equals(parseObject.getString("code"))){
            	LOG.info("删除白名单同步成功！"+messageFee.getCode());
            	return;
            }
            LOG.info(parseObject.getString("result") + messageFee );
            throw new BusinessException(ResultCode.APP_ERROR, "【白名单推送】接口调用异常!");
        }catch (Exception e){
            LOG.error("---消息中心【白名单退款】接口调用异常！ ---" + messageFee,e);
            throw new BusinessException(ResultCode.APP_ERROR, "【白名单推送】接口调用异常!");
        }
    }

	/*private MessageFeeOutput buildParams(MessageFee messageFee){
	    MessageFeeOutput output = BeanConver.copeBean(messageFee, MessageFeeOutput.class);
	    String systemMarket = BaseController.getMarketShortName();
	    output.setMarketCode(messageFee.getMarketCode());
	    return output;
	}*/
}
