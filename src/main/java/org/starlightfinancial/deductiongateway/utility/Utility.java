package org.starlightfinancial.deductiongateway.utility;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.starlightfinancial.deductiongateway.domain.local.SysUser;
import org.starlightfinancial.deductiongateway.domain.remote.Holiday;
import org.starlightfinancial.deductiongateway.domain.remote.HolidayRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Utility {


    /**
     * 14位时间戳格式
     */
    public static final String PATTERN = "yyyyMMddHHmmss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    private static FIFOCache<String, Boolean> holidayMap = new FIFOCache<>(3);

    /**
     * 将PageBean中的总记录数和数据放到map中
     *
     * @param pageBean
     * @return
     */
    public static Map<String, Object> pageBean2Map(PageBean pageBean) {

        HashMap<String, Object> map = new HashMap<String, Object>(2);
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
    public static String convertToString(Date date, String pattern) {
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
    public static Date convertToDate(String date, String pattern) {
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
    public static boolean checkBigDecimal2(BigDecimal checkValue) {   // 判断大于0
        return checkValue != null
                && checkValue.compareTo(BigDecimal.ZERO) > 0;
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
     * @param pageBean 页面参数
     * @param sortNum  0:desc 倒序 1:asc 正序
     * @return 返回PageRequest对象
     */
    public static PageRequest buildPageRequest(PageBean pageBean, Integer sortNum, String... properties) {
        Sort sort = null;
        if (properties.length == 0) {
            properties = new String[]{"id"};
        }
        if (sortNum == 1) {
            sort = new Sort(Sort.Direction.ASC, properties);
        } else {
            sort = new Sort(Sort.Direction.DESC, properties);
        }
        Integer pageNumber = pageBean.getPageNumber();
        Integer pageSize = pageBean.getPageSize();
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }


    /**
     * 获取访问用户的客户端IP（适用于公网与局域网）.
     */
    public static String getIpAddress(final HttpServletRequest request)
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

    /**
     * List深拷贝
     *
     * @param src 原始List
     * @param <T> List中元素类型
     * @return 返回深复制创建的List
     * @throws IOException            IO异常时抛出
     * @throws ClassNotFoundException 类型转换异常时抛出
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * 获取14位时间戳:yyyyMMddHHmmss 样式
     *
     * @return 返回时间戳
     */
    public static String getTimestamp() {
        return LocalDateTime.now(ZoneOffset.of("+8")).format(DATE_TIME_FORMATTER);
    }


    /**
     * 随机指定范围内N个不重复的数
     * 在初始化的无重复待选数组中随机产生一个数放入结果集中，使用随机数 %  范围长度的余数确定待选数组下标,
     * 范围长度自减1,将待选数组被随机到的数，用待选数组最后一个 (len-1下标)对应的数替换
     * 然后从0 到 len-2 下标里随机产生下一个随机数，放入结果集中, 待选数组如此类推
     *
     * @param max 指定范围最大值
     * @param min 指定范围最小值
     * @param n   随机数个数
     * @return int[] 随机数结果集
     */
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }

    /**
     * 按固定数量分组.例如,list中有23个元素,按照5个元素一组,将分为5组
     *
     * @param list     源数据
     * @param quantity 每组大小
     * @param <T>      类型
     * @return 返回分组后的结果
     */
    public static <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        ArrayList<List<T>> wrapList = new ArrayList<>();
        if (list == null || list.size() == 0) {
            wrapList.add(list);
            return wrapList;
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Wrong quantity.");
        }

        int count = 0;
        while (count < list.size()) {
            wrapList.add(list.subList(count, (count + quantity) > list.size() ? list.size() : count + quantity));
            count += quantity;
        }

        return wrapList;
    }

    /**
     * 获取两个日期中的天的差值,date2 - date1
     *
     * @param date1 减数
     * @param date2 被减数
     * @return 差值
     */
    public static long between(Date date1, Date date2) {
        LocalDate localDate1 = getLocalDate(date1);
        LocalDate localDate2 = getLocalDate(date2);
        return ChronoUnit.DAYS.between(localDate1, localDate2);
    }

    /**
     * 对比出同一类对象的不同属性
     *
     * @param oldObject 旧的对象
     * @param newObject 新的对象
     * @return 返回不同的属性
     */
    public static String compareObjectFieldValue(Object oldObject, Object newObject) {
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = oldObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object oldValue = getMethod.invoke(oldObject);
                Object newValue = getMethod.invoke(newObject);
                if (oldValue == null || newValue == null) {
                    continue;
                }
                if (oldValue instanceof Date) {
                    oldValue = convertToString((Date) oldValue, "yyyy-MM-dd");
                    newValue = convertToString((Date) newValue, "yyyy-MM-dd");
                }
                if (!oldValue.toString().equals(newValue.toString())) {
                    sb.append("属性:").append(field.getName()).append("[").append(oldValue).append("]->").append("[").append(newValue).append("] ");

                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 判断传入的日期是否是节假日
     *
     * @param localDate 需要判断的日期
     * @return 是否是节假日:true--是节假日,false--不是节假日
     */
    public static boolean isHoliday(LocalDate localDate) {
        if (holidayMap.containsKey(localDate.toString())) {
            return holidayMap.get(localDate.toString());
        } else {
            HolidayRepository holidayRepository = SpringContextUtil.getBean(HolidayRepository.class);
            Holiday holiday = holidayRepository.findByNonOverTime(localDate.toString());
            if (Objects.nonNull(holiday)) {
                holidayMap.put(localDate.toString(), true);
                return true;
            } else {
                holidayMap.put(localDate.toString(), false);
                return false;
            }

        }
    }

}
