package com.itdr.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {

    private static final String STNDARD_FORMAT="yyyy-MM-dd HH:mm:ss";

    /**
     * Date --> String
     * */

    public static String dateToStr(Date date,String format){    //String format自定义的时间格式
          //用到之前下载的joda.time依赖下的包 DateTime
        DateTime dateTime = new DateTime(date);
        return dateTime.toString();
    }

    public static String dateToStr(Date date){
        //用到之前下载的joda.time依赖下的包 DateTime
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STNDARD_FORMAT);
    }

    /**
     * String --> Date
     * */
    public static Date strToDate(String str){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STNDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }
    public static Date strToDate(String str,String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

/*    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(dateToStr(date));
        System.out.println(strToDate("2018-11-03 1:16:13"));
    }*/
}
