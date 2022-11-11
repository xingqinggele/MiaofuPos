package com.example.miaofupos.utils;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeUtils {
    private static SimpleDateFormat sf = null;


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowTime(String type) {
        String timeString = null;
        Time time = new Time();
        time.setToNow();
        String year = thanTen(time.year);
        String month = thanTen(time.month + 1);
        String monthDay = thanTen(time.monthDay);
        String hour = thanTen(time.hour);
        String minute = thanTen(time.minute);
        String second = thanTen(time.second);
        //获取到日
        if (type.equals("day")) {
            timeString = year + month + monthDay;
        } else if (type.equals("month")) {
            //获取到月
            timeString = year + "-" + month;
        } else if (type.equals("days")) {
            timeString = year + "-" + "-" + month + "-" + "-" + monthDay;
        }
        return timeString;
    }


    /**
     * 十一下加零
     *
     * @param str
     * @return
     */
    public static String thanTen(int str) {
        String string = null;
        if (str < 10) {
            string = "0" + str;
        } else {
            string = "" + str;
        }
        return string;
    }


    /**
     * 转换时间日期格式字串为long型
     *
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 把时间戳变MM-dd HH:mm格式时间
     *
     * @param time
     * @return
     */
    public static String getDateToString0(String time) {
        sf = new SimpleDateFormat("MM-dd HH:mm");
        Date d = new Date(convertTimeToLong(time));
        return sf.format(d);
    }

    public static String getDateToString1(String time) {
        sf = new SimpleDateFormat("HH:mm");
        Date d = new Date(convertTimeToLong(time));
        return sf.format(d);
    }

    public static String getDateToString6(String time) {
        sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(convertTimeToLong(time));
        return sf.format(d);
    }

    /**
     * 把时间戳变yyyy/MM格式时间
     *
     * @param time
     * @return
     */
    public static String getDateToString3(String time) {
        Date d = new Date(convertTimeToLong(time));
        sf = new SimpleDateFormat("yyyy/MM");
        return sf.format(d);
    }

    /**
     * 把时间戳变dd格式时间
     *
     * @param time
     * @return
     */
    public static String getDateToString4(String time) {
        Date d = new Date(convertTimeToLong(time));
        sf = new SimpleDateFormat("dd");
        return sf.format(d);
    }


    /**
     * list集合去重：
     * 把list里的对象遍历一遍，用list.contain()，如果不存在就放入到另外一个list集合中
     */
    public static List removeDuplicate(List list) {
        List listTemp = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (!listTemp.contains(list.get(i))) {
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }


    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    public static String getTimes(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getNewTimes(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }




    /**
     * 比较当前时间和服务器返回时间大小
     *
     * @param nowDate
     * @param compareDate
     * @return
     */
    public static boolean compareDate(String nowDate, String compareDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date now = df.parse(nowDate);
            Date compare = df.parse(compareDate);
            if (now.before(compare)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 比较两个时间大小
     *
     * @param nowDate
     * @param compareDate
     * @return
     */
    public static boolean compareNewDate(String nowDate, String compareDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = df.parse(nowDate);
            Date compare = df.parse(compareDate);

            if (now.getTime() <= compare.getTime()){
                return true;
            }else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
