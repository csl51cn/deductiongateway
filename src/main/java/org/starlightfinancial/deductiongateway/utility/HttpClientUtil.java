package org.starlightfinancial.deductiongateway.utility;


import chinapay.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.domain.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.MortgageDeductionRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpClientUtil {

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    private Map sendMessage(String MerId, String BusiId, String OrdId, String OrdAmt, String CuryId, String Version, String BgRetUrl, String PageRetUrl,
                            String GateId, String Param1, String Param2, String Param3, String Param4, String Param5, String Param6, String Param7, String Param8,
                            String Param9, String Param10, String OrdDesc, String ShareType, String ShareData, String Priv1, String CustomIp, String ChkValue,
                            String pubPath, int contractId, String customerNo, String customerName, String contractNo,
                            BigDecimal mount1, BigDecimal mount2, String orderDesc, int staffId, String ormid, String rePlanId) throws Exception {
        //批量发送扣款通信相关的对象
        HttpClient httpClient = new HttpClient();
        Map map = new HashMap();
        PostMethod postMethod = null;
        String url = Utility.SEND_BANK_URL;
        postMethod = new PostMethod(url);

        try {
            // 填入各个表单域的值
            NameValuePair[] data = {
                    new NameValuePair("MerId", MerId),          //商户号
                    new NameValuePair("BusiId", BusiId),        //业务号，可选
                    new NameValuePair("OrdId", OrdId),          //订单号
                    new NameValuePair("OrdAmt", OrdAmt),        //金额
                    new NameValuePair("CuryId", CuryId),        //币种
                    new NameValuePair("Version", Version),      //版本号，由银联提供
                    new NameValuePair("BgRetUrl", BgRetUrl),    //后台通知URL
                    new NameValuePair("PageRetUrl", PageRetUrl),//前台返回URL
                    new NameValuePair("GateId", GateId),        //网关ID，由银联提供
                    new NameValuePair("Param1", Param1),        //开户行
                    new NameValuePair("Param2", Param2),        //卡折标志
                    new NameValuePair("Param3", Param3),        //卡号/折号
                    new NameValuePair("Param4", Param4),        //持卡人姓名
                    new NameValuePair("Param5", Param5),        //证件类型
                    new NameValuePair("Param6", Param6),        //证件号
                    new NameValuePair("Param7", Param7),
                    new NameValuePair("Param8", Param8),
                    new NameValuePair("Param9", Param9),
                    new NameValuePair("Param10", Param10),
                    new NameValuePair("OrdDesc", OrdDesc),      //订单描述
                    new NameValuePair("ShareType", ShareType),
                    new NameValuePair("ShareData", ShareData),
                    new NameValuePair("Priv1", Priv1),
                    new NameValuePair("CustomIp", CustomIp),    //绑定固定IP
                    new NameValuePair("ChkValue", ChkValue)
            };

            //将表单的值放入postMethod中
            postMethod.setRequestBody(data);
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
            postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            //执行postMethod
            int statusCode = httpClient.executeMethod(postMethod);
            //取得按回的内容
            if (statusCode == HttpStatus.SC_OK) {
                try {
                    String str = postMethod.getResponseBodyAsString();
                    MortgageDeduction mortgageDeduction = new MortgageDeduction();
                    mortgageDeduction = mortgageDeductionRepository.save(mortgageDeduction);
                    if (mortgageDeduction != null) {
                        map.put("mresultid", mortgageDeduction.getId() + "");
                        map.put("mount1", mount1 + "");
                        map.put("mount2", mount2 + "");
                        map.put("contractId", contractId + "");
                        map.put("rePlanId", rePlanId);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    throw e;
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            postMethod.releaseConnection();
        }

        return map;
    }


    /**
     * @param messages
     * @param path
     * @param staffId
     * @return
     */
    public List<Map> sendInformation(List<GoPayBean> messages, String path, int staffId) {
        String chkValue;//签名数据
        List<Map> results = new ArrayList<Map>();
        Map result;
        int contractid = 0;

        for (GoPayBean goPay : messages) {
            try {
                StringBuffer sb = new StringBuffer();
                if (StringUtils.isNotBlank(goPay.getContractId())) {
                    contractid = Integer.parseInt(goPay.getContractId());
                }
                sb.append(goPay.getMerId());
                sb.append(goPay.getBusiId());
                sb.append(goPay.getOrdId());
                sb.append(goPay.getOrdAmt());
                sb.append(goPay.getCuryId());
                sb.append(goPay.getVersion());
                sb.append(goPay.getBgRetUrl());
                sb.append(goPay.getPageRetUrl());
                sb.append(goPay.getGateId());
                sb.append(goPay.getParam1());
                sb.append(goPay.getParam2());
                sb.append(goPay.getParam3());
                sb.append(goPay.getParam4());
                sb.append(goPay.getParam5());
                sb.append(goPay.getParam6());
                sb.append(goPay.getParam7());
                sb.append(goPay.getParam8());
                sb.append(goPay.getParam9());
                sb.append(goPay.getParam10());
                sb.append(goPay.getShareType());
                sb.append(goPay.getShareData());
                sb.append(goPay.getPriv1());
                sb.append(goPay.getCustomIp());

                //加密数据
                chkValue = sign(goPay.getMerId(), sb.toString(), path);
                if (StringUtils.isEmpty(chkValue) || chkValue.length() != 256) {
                    return null;
                }

                result = sendMessage(goPay.getMerId(), goPay.getBusiId(), goPay.getOrdId(), goPay.getOrdAmt(), goPay.getCuryId(), goPay.getVersion(),
                        goPay.getBgRetUrl(), goPay.getPageRetUrl(), goPay.getGateId(), goPay.getParam1(), goPay.getParam2(), goPay.getParam3(), goPay.getParam4(), goPay.getParam5(),
                        goPay.getParam6(), goPay.getParam7(), goPay.getParam8(), goPay.getParam9(), goPay.getParam10(), goPay.getOrdDesc(), goPay.getShareType(),
                        goPay.getShareData(), goPay.getPriv1(), goPay.getCustomIp(), chkValue, path,
                        contractid, goPay.getCustomerNo(), goPay.getCustomerName(), goPay.getContractNo(), goPay.getSplitData1(), goPay.getSplitData2(),
                        goPay.getOrdDesc(), staffId, goPay.getOrgManagerId(), goPay.getRePlanId());
                results.add(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    private boolean checkReponseData(String content, String pubPath, String merId) {

        String from = content.substring(content.indexOf("<form"), content.lastIndexOf("</form>"));
        from = from.substring(from.indexOf("<table>"), from.indexOf("</table>"));
        String[] contents = from.split("</tr>");
        Map map = new HashMap();

        for (int i = 0; i < contents.length; i++) {
            String t = contents[i];
            if (t.length() > 20) {
                String name = t.substring(t.indexOf("name") + 6, t.indexOf("value") - 2);
                String value = t.substring(t.indexOf("value") + 7, t.indexOf("></td>") - 1);
                map.put(name, value);
            }

        }
        //PayStat表示交易状态，只有"1001"表示支付成功，其他状态均表示未成功的交易。因此验证签名数据通过后，还需要判定交易状态代码是否为"1001"
        if (map.get("PayStat") != null && "1001".equals(map.get("PayStat").toString()))
            return true;
        else
            return false;
    }

    /**
     * 解析银联返回的数据
     *
     * @param content
     * @return
     */
    private String checkReponseShareData(String content) {

        String from = content.substring(content.indexOf("<form"), content.lastIndexOf("</form>"));
        from = from.substring(from.indexOf("<table>"), from.indexOf("</table>"));
        String[] contents = from.split("</tr>");
        Map map = new HashMap();

        for (int i = 0; i < contents.length; i++) {
            String t = contents[i];
            if (t.length() > 20) {
                String name = t.substring(t.indexOf("name") + 6, t.indexOf("value") - 2);
                String value = t.substring(t.indexOf("value") + 7, t.indexOf("></td>") - 1);
                map.put(name, value);
            }

        }
        //PayStat表示交易状态，只有"1001"表示支付成功，其他状态均表示未成功的交易。因此验证签名数据通过后，还需要判定交易状态代码是否为"1001"
        if (map.get("ShareData") != null && !"".equals(map.get("ShareData").toString().trim())) {
            return map.get("ShareData").toString();
        } else {
            return "";
        }
    }

    /**
     * 用私钥对字符串签名
     *
     * @param data
     * @return
     * @throws Exception
     */
    private String sign(String merId, String data, String path) throws Exception {
        chinapay.PrivateKey key = new chinapay.PrivateKey();
        chinapay.SecureLink t;
        boolean flag;
        String ChkValue2;
        String paths = path + "\\" + Utility.SEND_BANK_KEY_FILE;//"MerPrK_808080211388159_20130917115334.key";;
        if (paths == null || "".equals(paths)) {
            return null;
        }
        flag = key.buildKey(merId, 0, paths);
        if (flag == false) {
            return null;
        }
        t = new chinapay.SecureLink(key);
        String data0 = new String(Base64.encode(data.getBytes()));
        ChkValue2 = t.Sign(data0); // Value2为签名后的字符串
        return ChkValue2;
    }

    /**
     * 调用CP公钥，验证CP的响应信息
     *
     * @param data
     * @param chkValue
     * @return
     */
    private boolean check(String data, String chkValue, String pubPath, String merId) {
        chinapay.PrivateKey key = new chinapay.PrivateKey();
        chinapay.SecureLink t;
        boolean flag;
        String path = pubPath + "\\" + Utility.SEND_BANK_KEY_PUB_FILE;
        ;
        if (path == null || "".equals(path)) {
            System.out.println("找不到CP公钥存放路径!");
            return false;
        }
        flag = key.buildKey(merId, 0, path);
        if (flag == false) {
            System.out.println("build key error!");
            return false;
        }
        String data0 = new String(Base64.encode(data.getBytes()));
        t = new chinapay.SecureLink(key);
        return t.verifyAuthToken(data0, chkValue);
    }

    /**
     * 读取map中的数据
     *
     * @param map
     * @return
     */
    private GoPayBean getGoPayBean(Map map) {
        GoPayBean goPayBean = new GoPayBean();
        goPayBean.setMerId(map.get("MerId").toString());
        goPayBean.setBusiId(map.get("BusiId").toString());
        goPayBean.setOrdId(map.get("OrdId").toString());
        goPayBean.setOrdAmt(map.get("OrdAmt").toString());
        goPayBean.setCuryId(map.get("CuryId").toString());
        goPayBean.setVersion(map.get("Version").toString());
        goPayBean.setGateId(map.get("GateId").toString());
        goPayBean.setParam1(map.get("Param1").toString());
        goPayBean.setParam2(map.get("Param2").toString());
        goPayBean.setParam3(map.get("Param3").toString());
        goPayBean.setParam4(map.get("Param4").toString());
        goPayBean.setParam5(map.get("Param5").toString());
        goPayBean.setParam6(map.get("Param6").toString());
        goPayBean.setParam7(map.get("Param7").toString());
        goPayBean.setParam8(map.get("Param8").toString());
        goPayBean.setParam9(map.get("Param9").toString());
        goPayBean.setParam10(map.get("Param10").toString());
        goPayBean.setOrdDesc(map.get("OrdDesc").toString());
        goPayBean.setShareType(map.get("ShareType").toString());
        goPayBean.setShareData(map.get("ShareData").toString());
        goPayBean.setPriv1(map.get("Priv1").toString());
        goPayBean.setCustomIp(map.get("CustomIp").toString());
        goPayBean.setPayStat(map.get("PayStat").toString());
        goPayBean.setPayTime(map.get("PayTime").toString());
        goPayBean.setChkValue(map.get("ChkValue").toString());
        return goPayBean;

    }

    /**
     * 得到待签名数据
     *
     * @param bean
     * @return
     */
    private String getSignData(GoPayBean bean) {
        StringBuffer sb = new StringBuffer();
        sb.append(bean.getMerId() == null ? "" : bean.getMerId());
        sb.append(bean.getBusiId() == null ? "" : bean.getBusiId());
        sb.append(bean.getOrdId() == null ? "" : bean.getOrdId());
        sb.append(bean.getOrdAmt() == null ? "" : bean.getOrdAmt());
        sb.append(bean.getCuryId() == null ? "" : bean.getCuryId());
        sb.append(bean.getVersion() == null ? "" : bean.getVersion());
        sb.append(bean.getGateId() == null ? "" : bean.getGateId());
        sb.append(bean.getParam1() == null ? "" : bean.getParam1());
        sb.append(bean.getParam2() == null ? "" : bean.getParam2());
        sb.append(bean.getParam3() == null ? "" : bean.getParam3());
        sb.append(bean.getParam4() == null ? "" : bean.getParam4());
        sb.append(bean.getParam5() == null ? "" : bean.getParam5());
        sb.append(bean.getParam6() == null ? "" : bean.getParam6());
        sb.append(bean.getParam7() == null ? "" : bean.getParam7());
        sb.append(bean.getParam8() == null ? "" : bean.getParam8());
        sb.append(bean.getParam9() == null ? "" : bean.getParam9());
        sb.append(bean.getParam10() == null ? "" : bean.getParam10());
        sb.append(bean.getShareType() == null ? "" : bean.getShareType());
        sb.append(bean.getShareData() == null ? "" : bean.getShareData());
        sb.append(bean.getPriv1() == null ? "" : bean.getPriv1());
        sb.append(bean.getCustomIp() == null ? "" : bean.getCustomIp());
        sb.append(bean.getPayStat() == null ? "" : bean.getPayStat());
        sb.append(bean.getPayTime() == null ? "" : bean.getPayTime());
        return sb.toString();
    }
}
