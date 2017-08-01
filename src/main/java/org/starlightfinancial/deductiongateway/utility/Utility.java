package org.starlightfinancial.deductiongateway.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utility {

    //    public static final String SEND_BANK_URL = "https://payment.chinapay.com/CTITS/cpeduinterface/OrderGet.do";//生产地址
    public static final String SEND_BANK_URL = "http://newpayment-test.chinapay.com/CTITS/cpeduinterface/OrderGet.do";//测试环境
    public static final String SEND_BANK_BGRETURL = "http://113.204.117.46:8081/PayGetBgAsyn";//后台交易接收URL地址
    public static final String SEND_BANK_PAGERETURL = "http://113.204.117.46:8081/PayGetPgAsyn";//页面交易接收URL地址
    //    public static final String SEND_BANK_KEY_FILE = "MerPrK_808080201302851_20131030113446.key";//生产环境私钥文件名
    public static final String SEND_BANK_KEY_FILE = "MerPrK808080290000001.key";//测试环境私钥文件名
    public static final String SEND_BANK_KEY_PUB_FILE = "PgPubk.key";//公钥文件名
    //    public static final String SEND_BANK_MERID = "808080201302851";//生产环境
    public static final String SEND_BANK_MERID = "808080290000001";//测试环境
    public static final String SEND_BANK_VERSION = "20100401";//版本号curyId
    public static final String SEND_BANK_GATEID = "7008";//网关
    public static final String SEND_BANK_CURYID = "156";//交易币种
    public static final String SEND_BANK_TYPE = "0001";//分账类型


    /**
     * 将PageBean中的总记录数和数据放到map中
     *
     * @param pageBean
     * @return
     */
    public static Map<String, Object> pageBean2Map(PageBean pageBean) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageBean.getTotal());
        map.put("rows", pageBean.getRows());
        return map;
    }


    public static Date addDay(Date date ,Integer days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, days);
        return  calendar.getTime();
    }

}
