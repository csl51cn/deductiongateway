package org.starlightfinancial.deductiongateway.utility;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


@Component
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    public static Map send(String url, List<BasicNameValuePair> nvps) throws Exception {
        Map<String, String> map = new HashMap();
        HttpAsyncClientBuilder httpAsyncClientBuilder = HttpAsyncClientBuilder.create();
        configureHttpClient(httpAsyncClientBuilder);
        CloseableHttpAsyncClient httpclient = httpAsyncClientBuilder.build();
        CountDownLatch latch = new CountDownLatch(1);
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpclient.start();

            LOGGER.info("开始调用接口：" + url + " 请求参数:" + URLDecoder.decode(EntityUtils.toString(httpPost.getEntity()), "UTF-8"));
            // 执行postMethod
            httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(final HttpResponse response) {
                    try {
                        String returnData = EntityUtils.toString(response.getEntity(), "UTF-8");
                        System.out.println("调用返回数据：" + returnData);
                        map.put("returnData", returnData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }

                @Override
                public void failed(final Exception ex) {
                    latch.countDown();
                }

                @Override
                public void cancelled() {
                    latch.countDown();
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
     * 配置HttpAsyncClientBuilder,信任所有证书,重定向策略
     *
     * @param clientBuilder
     */
    public static void configureHttpClient(HttpAsyncClientBuilder clientBuilder) {
        try {
            // 信任所有
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();

            clientBuilder.setSSLContext(sslContext);
            //设置重定向策略,如果是重定向,继续访问
            clientBuilder.setRedirectStrategy(new LaxRedirectStrategy());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get 请求
     *
     * @param url
     */
    public static String httpGet(String url) throws IOException {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).build();
        CloseableHttpClient client = HttpClients.createDefault();
        // 发送get请求
        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);
        String resultString = "";
        try {
            CloseableHttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                LOGGER.info("get请求提交成功:" + url);
            } else {
                LOGGER.error("get请求提交失败:" + url);
                throw new IOException("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            LOGGER.error("get请求提交失败:" + url, e);
            throw new IOException("get请求提交失败:" + url);
        } finally {
            request.releaseConnection();
        }
        return resultString;
    }
}
