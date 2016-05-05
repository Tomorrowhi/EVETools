package com.eve.everyone.evetool.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class DateUtil {

    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static DateFormat dateNowFormatStyle1 = new SimpleDateFormat("dd日");
    static DateFormat dateYestoadayFormatStyle1 = new SimpleDateFormat("yyy年MM月dd日");
    static DateFormat dateCouponFormat = new SimpleDateFormat("yyyy.MM.dd");
    static DateFormat dateCouponFormat_phone = new SimpleDateFormat("yyyy-MM-dd");
    static DateFormat recordItemFormat = new SimpleDateFormat("dd日HH:mm");
    static DateFormat recordItemFormat_phone = new SimpleDateFormat("MM-dd\nHH:mm");
    static DateFormat recordCustomerItemFormat = new SimpleDateFormat("MM月dd日HH:mm");
    static DateFormat recordCustomerItemFormat_phone = new SimpleDateFormat("MM-dd\t\tHH:mm");
    static DateFormat serviceDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");


    static SimpleDateFormat serviceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat showDate = new SimpleDateFormat("yyyy.MM.dd");
    static SimpleDateFormat showDatePhone = new SimpleDateFormat("yyyy-MM-dd");

    public static String getNowDateStr() {

        return dateFormat.format(new Date());
    }

    /*今天*/
    public static String getNowDateStrStyle1() {

        return dateNowFormatStyle1.format(new Date());
    }

    /*昨天*/
    public static String getYestodayDateStrStyle1() {

        return dateYestoadayFormatStyle1.format(System.currentTimeMillis() - 86400000);
    }

    public static String getDateTimeCouponStr(Long time) {
        return dateCouponFormat.format(time);
    }

    public static String getDateTimeCouponStrPhone(Long time) {
        return dateCouponFormat_phone.format(time);
    }

    public static String getDateTimeCheckStr(Long time) {
        return serviceDateFormat.format(time);
    }

    public static String getDateTimeCouponStr(String date) {
        try {
            if (date != null) {
                Date mDate = serviceDate.parse(date);
                return showDate.format(mDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateTimeCouponStrPhone(String date) {
        try {
            if (date != null) {
                Date mDate = serviceDate.parse(date);
                return showDatePhone.format(mDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateTimeRecordItemStr(Long time) {
        return recordItemFormat.format(time);
    }

    public static String getDateTimeRecordItemStrPhone(Long time) {
        return recordItemFormat_phone.format(time);
    }

    public static String getDateTimeRecordCustomerItemStr(Long time) {
        return recordCustomerItemFormat.format(time);
    }

    public static String getDateTimeRecordCustomerItemStrPhOne(Long time) {
        return recordCustomerItemFormat_phone.format(time);
    }


    /**
     * 时间转换  str--->毫秒值
     *
     * @param time 时间
     * @return 毫秒值
     */
    public static long getMS(String time) {
        try {
            return serviceDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }


}
