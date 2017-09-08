package org.starlightfinancial.deductiongateway.utility;

import chinapay.Base64;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sili.chen on 2017/9/8
 */
public class UnionPayUtil {

    /**
     * 用私钥对字符串签名
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String sign(String merId, String data) {
        chinapay.PrivateKey key = new chinapay.PrivateKey();
        chinapay.SecureLink t;
        boolean flag;
        String ChkValue2;
        String paths = null;
        String line = File.separator;
        if ("\\".equals(line)) {
            paths = "D://" + Utility.SEND_BANK_KEY_FILE;
        } else if ("/".equals(line)) {
            paths = "/root/" + Utility.SEND_BANK_KEY_FILE;
        }
        System.out.println("paths" + paths);
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
}
