package org.starlightfinancial.deductiongateway.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MerSeq {

    public static int seq = 0;

    public static int tickSeq() {
        return seq++;
    }

    public static String format(int num, int width) {
        if (num < 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String s = "" + num;
        if (s.length() < width) {
            int addNum = width - s.length();
            for (int i = 0; i < addNum; i++) {
                sb.append("0");
            }
            sb.append(s);
        } else {
            return s.substring(s.length() - width, s.length());
        }
        return sb.toString();
    }

    public static String tickOrder() {
        int n = tickSeq();
        String ns = format(n, 4);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(new Date()) + ns;
    }
}
