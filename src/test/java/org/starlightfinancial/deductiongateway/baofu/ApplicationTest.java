package org.starlightfinancial.deductiongateway.baofu;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by sili.chen on 2017/12/25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
@Rollback
public class ApplicationTest {

    @Before
    public void setUp() {
    }

    @Test
    public void test() throws Exception {
        List<BasicNameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("version", "4.0.0.0"));
        nvps.add(new BasicNameValuePair("terminal_id", "100000990"));
        nvps.add(new BasicNameValuePair("txn_type", "0431"));
        nvps.add(new BasicNameValuePair("txn_sub_type", "25"));
        nvps.add(new BasicNameValuePair("member_id", "100000276"));
        nvps.add(new BasicNameValuePair("data_type", "json"));

        String pfxpath = "bfkey_100000276@@100000990.pfx"; // 私钥
        String cerpath = "bfkey_100000276@@100000990.cer"; // 公钥

//        File pfxfile = new File(pfxpath);
//        if (!pfxfile.exists()) {
//            System.out.printf("\"私钥文件不存在！\"");
//            return;
//        }
//
//        File cerfile = new File(cerpath);
//        if (!cerfile.exists()) {//判断宝付公钥是否为空
//            System.out.printf("公钥文件不存在！");
//        }

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("txn_sub_type", "25");
        jsonMap.put("biz_type", "0000");
        jsonMap.put("terminal_id", "100000990");
        jsonMap.put("member_id", "100000276");
        jsonMap.put("pay_code", "ICBC"); // 银行编码
        jsonMap.put("pay_cm", "2"); // 安全标识 默认2
        jsonMap.put("acc_no", "6222020111122220000"); // 卡号
        jsonMap.put("id_card_type", "01"); // 身份证类型 默认01
        jsonMap.put("id_card", "320301198502169142"); // 身份证号
        jsonMap.put("id_holder", "张宝"); // 持卡人姓名
        jsonMap.put("mobile", "13637983147"); // 银行卡绑定手机号
        jsonMap.put("valid_date", ""); // 信用卡有效期
        jsonMap.put("valid_no", ""); // 信用卡安全码
        jsonMap.put("trans_id", "TID" + System.currentTimeMillis()); // 商户订单号
        // String txn_amt = String.valueOf(new BigDecimal(request.getParameter("txn_amt")).multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
        jsonMap.put("txn_amt", "100");
        String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        jsonMap.put("trade_date", trade_date);
        jsonMap.put("additional_info", "附加信息");
        jsonMap.put("req_reserved", "保留");
        jsonMap.put("trans_serial_no", "TSN" + System.currentTimeMillis());
        jsonMap.put("share_info", "100000749,10;100000178,90"); // 分账信息
        jsonMap.put("notify_url", "http://113.204.117.46:8082/PayGetBgAsyn"); // 通知地址
        jsonMap.put("fee_member_id", ""); // 分账手续费商户

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(jsonMap);
        String contentData = jsonObject.toString();
        System.out.println("contentData" + contentData);

        String base64str = SecurityUtil.Base64Encode(contentData);
        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, "123456");
        nvps.add(new BasicNameValuePair("data_content", data_content));

        HttpAsyncClientBuilder httpAsyncClientBuilder = HttpAsyncClientBuilder.create();
        CloseableHttpAsyncClient httpclient = httpAsyncClientBuilder.build();
        String url = "https://vgw.baofoo.com/cutpayment/api/backTransRequest"; // 测试地址
        HttpPost httpPost = new HttpPost(url);
        //设置读取数据超时时间:2min
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
        httpPost.setConfig(requestConfig);
        CountDownLatch latch = new CountDownLatch(1);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpclient.start();
            //执行postMethod
            httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {

                @Override
                public void completed(final HttpResponse httpResponse) {
                    String content;
                    try {
                        content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                        System.out.println("content" + content);
                        String postString = RsaCodingUtil.decryptByPubCerFile(content, cerpath);
                        if (postString.isEmpty()) {//判断解密是否正确。如果为空则宝付公钥不正确
                            System.out.printf("检查解密公钥是否正确！");
                        }
                        postString = SecurityUtil.Base64Decode(postString);
                        JSONObject jsonObject1 = (JSONObject) JSONObject.parse(postString);
                        String aa = jsonObject1.getObject("resp_msg", String.class);
                        System.out.println("postString" + aa);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }

                @Override
                public void failed(Exception e) {
                    latch.countDown();
                    close(httpclient);
                }

                @Override
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
            e.printStackTrace();
            throw e;
        } finally {
            close(httpclient);
        }
    }

    private static void close(CloseableHttpAsyncClient client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
