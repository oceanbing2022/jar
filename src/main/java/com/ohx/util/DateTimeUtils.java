package com.ohx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 贾榕福
 * @date 2023/1/5
 */
public class DateTimeUtils {
    /**
     * 时间戳转日期的格式
     */
    public static final String DATETIME_CONVENTIONAL_CN = "yyyyMMdd";

    /** 中国地区常用时间. */
    public static String timestampToDateStr(Long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_CONVENTIONAL_CN);
        String sd = sdf.format(new Date(timestamp * 1000)); // 时间戳转换日期
        return sd;
    }

    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_CONVENTIONAL_CN);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;
        res = String.valueOf(ts);
        return res;
    }
    

}
