package org.starlightfinancial.deductiongateway.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
        if (pageBean != null) {
            map.put("total", pageBean.getTotal());
            map.put("rows", pageBean.getRows());
        } else {
            map.put("total", 0);
            map.put("rows", null);
        }
        return map;
    }

    /**
     * 将日期转为字符串
     *
     * @param date
     * @return
     */
    public static String convertToString(Date date) {
        try {
            if (date != null) {
                return new SimpleDateFormat("yyyy-MM-dd").format(date);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param checkValue
     * @return
     */
    public static boolean checkBigDecimal2(BigDecimal checkValue)// 判断大于0
    {
        if (checkValue != null
                && checkValue.compareTo(new BigDecimal("0.00")) == 1)
            return true;
        return false;
    }

    /**
     * 天数加减
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 获取某日23:59:59 时间
     *
     * @param date
     * @return
     */
    public static Date toMidNight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR); //年
        int month = calendar.get(Calendar.MONTH);//月
        int day = calendar.get(Calendar.DAY_OF_MONTH); //日
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }


    /**
     * 创建分页请求.
     */
    public static PageRequest buildPageRequest(PageBean pageBean) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Integer pageNumber = pageBean.getPageNumber();
        Integer pageSize = pageBean.getPageSize();
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }
}
