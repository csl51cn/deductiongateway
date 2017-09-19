package org.starlightfinancial.deductiongateway.utility;


import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
public class HttpClientUtil {

    public static Map send(List<BasicNameValuePair> nvps) throws Exception {
        //批量发送扣款通信相关的对象
        Map map = new HashMap();
//        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        HttpAsyncClientBuilder httpAsyncClientBuilder = HttpAsyncClientBuilder.create();
        //信任所有证书,跳过证书验证
        configureHttpClient(httpAsyncClientBuilder);
        CloseableHttpAsyncClient httpclient = httpAsyncClientBuilder.build();
        String url = Utility.SEND_BANK_URL;
        HttpPost httpPost = new HttpPost(url);
        CountDownLatch latch = new CountDownLatch(1);
        try {
            //将表单的值放入postMethod中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpclient.start();

            System.out.println("开始调用银联接口");
            //执行postMethod
            httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    try {
                        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                        System.out.println(content);
                        String payStat = null;
                        try {
                            payStat = content.substring(content.indexOf("PayStat") + 16, content.indexOf("PayStat") + 20);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("payStat|" + payStat);
                        map.put("PayStat", payStat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }

                public void failed(final Exception ex) {
                    latch.countDown();
                    close(httpclient);
                }

                public void cancelled() {
                    latch.countDown();
                    close(httpclient);
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            close(httpclient);
        }
        return map;
    }

    /**
     * 关闭client对象
     *
     * @param client
     */
    private static void close(CloseableHttpAsyncClient client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 配置HttpAsyncClientBuilder,信任所有证书
     * @param clientBuilder
     */
    public static void configureHttpClient(HttpAsyncClientBuilder clientBuilder)
    {
        try
        {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
            {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                    return true;
                }
            }).build();

            clientBuilder.setSSLContext(sslContext);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
