package org.starlightfinancial.deductiongateway.utility;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.starlightfinancial.deductiongateway.domain.local.SysUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utility {

    public static final String SEND_BANK_URL = "https://payment.chinapay.com/CTITS/cpeduinterface/OrderGet.do";//生产地址
    //    public static final String SEND_BANK_URL = "http://newpayment-test.chinapay.com/CTITS/cpeduinterface/OrderGet.do";//测试环境
    public static final String SEND_BANK_BGRETURL = "http://113.204.117.46:8081/PayGetBgAsyn";//后台交易接收URL地址
    public static final String SEND_BANK_PAGERETURL = "http://113.204.117.46:8081/PayGetPgAsyn";//页面交易接收URL地址
    public static final String SEND_BANK_KEY_FILE = "MerPrK_808080201302851_20131030113446.key";//生产环境私钥文件名
    //    public static final String SEND_BANK_KEY_FILE = "MerPrK808080290000001.key";//测试环境私钥文件名
    public static final String SEND_BANK_KEY_PUB_FILE = "PgPubk.key";//公钥文件名
    public static final String SEND_BANK_MERID = "808080201302851";//生产环境
    //    public static final String SEND_BANK_MERID = "808080290000001";//测试环境
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
    public static String convertToString(Date date,String pattern) {
        try {
            if (date != null) {
                return new SimpleDateFormat(pattern).format(date);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 将字符串转为日期
     *
     * @param date
     * @return
     */
    public static Date convertToDate(String date,String pattern) {
        try {
            if (date != null) {
                return new SimpleDateFormat(pattern).parse(date);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }




    /**
     * @param checkValue
     * @return
     */
    public static boolean checkBigDecimal2(BigDecimal checkValue)
    {   // 判断大于0
        if (checkValue != null
                && checkValue.compareTo(new BigDecimal("0.00")) == 1){
            return true;
        }

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
        calendar.add(Calendar.DATE, days);
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
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH);
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }


    /**
     * 创建分页请求.
     *
     * @param pageBean
     * @param sortNum  0:desc 倒序 1:asc 正序
     * @return
     */
    public static PageRequest buildPageRequest(PageBean pageBean, Integer sortNum) {
        Sort sort = null;
        if (sortNum == 1) {
            sort = new Sort(Sort.Direction.ASC, "id");
        } else {
            sort = new Sort(Sort.Direction.DESC, "id");
        }
        Integer pageNumber = pageBean.getPageNumber();
        Integer pageSize = pageBean.getPageSize();
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }


    /**
     * 获取访问用户的客户端IP（适用于公网与局域网）.
     */
    public static  String getIpAddress(final HttpServletRequest request)
            throws Exception {
        if (request == null) {
            throw (new Exception("getIpAddress method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }

        return ipString;
    }


    /**
     * 获取登录用户StaffId
     *
     * @param session 会话session
     * @return 用户StaffId
     */
    public static int getLoginUserId(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        return Integer.parseInt(loginUser.getStaffId());
    }

    /**
     * 获取登录用户login_name
     *
     * @param session 会话session
     * @return 用户login_name
     */
    public static String getLoginUserName(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        return loginUser.getLoginName();
    }



    /**
     * Date 转换为 LocalDate
     *
     * @param date 需要转换的Date
     * @return 返回转换后的LocalDate
     */
    public static LocalDate getLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDate();
    }


    /**
     * LocalDate 转换为 Date
     *
     * @param localDate 需要转换的LocalDate
     * @return 返回转换后的Date
     */
    public static Date getDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }


}
