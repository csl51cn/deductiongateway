package org.starlightfinancial.deductiongateway.utility;

import chinapay.Base64;

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
    public static String sign(String merId, String data, String priKey) {
        chinapay.PrivateKey key = new chinapay.PrivateKey();
        chinapay.SecureLink t;
        boolean flag;
        String ChkValue2;
        String paths = null;
        String line = File.separator;
        if ("\\".equals(line)) {
            paths = "D://" + priKey;
        } else if ("/".equals(line)) {
            paths = "/root/" + priKey;
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
        // Value2为签名后的字符串
        ChkValue2 = t.Sign(data0);
        return ChkValue2;
    }

    /**
     * 调用CP公钥，验证CP的响应信息
     *
     * @param data
     * @param chkValue
     * @return
     */
    private boolean check(String data, String chkValue, String pubPath, String merId ,String pubKey) {
        chinapay.PrivateKey key = new chinapay.PrivateKey();
        chinapay.SecureLink t;
        boolean flag;
        String path = pubPath + "\\" + pubKey;
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
        if (map.get("PayStat") != null && "1001".equals(map.get("PayStat").toString())) {
            return true;
        } else {
            return false;
        }
    }
}
