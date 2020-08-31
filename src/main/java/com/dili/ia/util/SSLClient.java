package com.dili.ia.util;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
/**
 * Created by chenzw on 2015/8/17.
 */
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

//用于进行Https请求的HttpClient
public class SSLClient {
	/*public SSLClient() throws Exception{
	    super();
	    SSLContext ctx = SSLContext.getInstance("TLS");
	    X509TrustManager tm = new X509TrustManager() {
	        @Override
	        public void checkClientTrusted(X509Certificate[] chain,
	                                       String authType) throws CertificateException {
	        }
	        @Override
	        public void checkServerTrusted(X509Certificate[] chain,
	                                       String authType) throws CertificateException {
	        }
	        @Override
	        public X509Certificate[] getAcceptedIssuers() {
	            return null;
	        }
	    };
	    ctx.init(null, new TrustManager[]{tm}, null);
	    SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	    ClientConnectionManager ccm = this.getConnectionManager();
	    SchemeRegistry sr = ccm.getSchemeRegistry();
	    sr.register(new Scheme("https", 443, ssf));
	}*/
    
    public static CloseableHttpClient createSSLClientDefault(){
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return  HttpClients.createDefault();
    }
}
