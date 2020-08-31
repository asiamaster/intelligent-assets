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

    public boolean postPaySuccessMessageFeeCustomer(MessageFee messageFee) {
        try {
            String url=messageHttp+"/messageApi/whitelistCustomer.api";
            String result= HttpClientUtil.doPost(url, messageFee,charset);
            if(result==null || "".equals(result)){
                LOG.error("---消息中心【白名单推送】接口调用异常！返回为空 ---" + messageFee);
                return false;
            }
            JSONObject parseObject = JSON.parseObject(result);
            if("200".equals(parseObject.getString("code"))){
            	LOG.info("同步成功！"+messageFee.getCode());
            	return true;
            }
            LOG.info(parseObject.getString("result") + messageFee );
            return false;
        }catch (Exception e){
            LOG.error("---消息中心【白名单推送】接口调用异常！ ---" + messageFee,e);
            return false;
        }
    }


    public boolean postRefundMessageFeeCustomer(MessageFee messageFee) {
        try {
            String url=messageHttp+"/messageApi/delWhitelistCustomer.api";
            String result= HttpClientUtil.doPost(url, messageFee,charset);
            if(result==null || "".equals(result)){
                LOG.error("---消息中心【白名单推送】接口调用异常！返回为空 ---" + messageFee);
                return false;
            }
            JSONObject parseObject = JSON.parseObject(result);
            if("200".equals(parseObject.getString("code"))){
            	LOG.info("同步成功！"+messageFee.getCode());
            	return true;
            }
            LOG.info(parseObject.getString("result") + messageFee );
            return false;
        }catch (Exception e){
            LOG.error("---消息中心【白名单退款】接口调用异常！ ---" + messageFee,e);
            return false;
        }
    }

	/*private MessageFeeOutput buildParams(MessageFee messageFee){
	    MessageFeeOutput output = BeanConver.copeBean(messageFee, MessageFeeOutput.class);
	    String systemMarket = BaseController.getMarketShortName();
	    output.setMarketCode(messageFee.getMarketCode());
	    return output;
	}*/
}
