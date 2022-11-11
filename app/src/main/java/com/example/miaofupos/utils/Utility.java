package com.example.miaofupos.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 作者: qgl
 * 创建日期：2020/10/24
 * 描述:工具类
 */
public class Utility {


    private static String files = null;


    /**
     * 是否大陆手机
     *
     * @param str
     * @return
     */
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    public static String getTime(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(date);
    }

    public static String getTime1(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        return format.format(date);
    }

    public static String getTime2(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
    // 金额元-万-亿转换
    public static String toNumber(String value) {
        String str = "";
        BigDecimal bigDecimal;
        BigDecimal decimal = null;
        if (Double.valueOf(value) <= 0) {
            str = "0" + "元";
        } else if (Double.valueOf(value) < 10000) {
            str = Double.valueOf(value) + "元";
        } else if (Double.valueOf(value) < 100000000) {
            // 具体的金额（单位元）
            bigDecimal = new BigDecimal(value);
            // 转换为万元（除以10000）
            decimal = bigDecimal.divide(new BigDecimal("10000"));
            str = decimal.toString() + "万";
        } else {
            // 具体的金额（单位元）
            bigDecimal = new BigDecimal(value);
            // 转换为万元（除以10000）
            decimal = bigDecimal.divide(new BigDecimal("100000000"));
            str = decimal.toString() + "亿";
        }
        return str;
    }



    /**
     * 不能全是相同的数字或者字母（如：000000、111111、aaaaaa）
     * @param numOrStr str.length()>0
     * @return 全部相同返回true
     */
    public static boolean equalStr(String numOrStr){
        boolean flag = true;
        char str = numOrStr.charAt(0);
        for (int i = 0; i < numOrStr.length(); i++) {
            if (str != numOrStr.charAt(i)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断字符串是否为连续数字 45678901�?
     */
    public static boolean isContinuousNum(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        int len = str.length();
        for (int i = 0; i < len - 1; i++) {
            char curChar = str.charAt(i);
            char verifyChar = (char) (curChar + 1);
            if (curChar == '9')
                verifyChar = '0';
            char nextChar = str.charAt(i + 1);
            if (nextChar != verifyChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将float类型的数据转换成以3位逗号隔开的字符串，并且保留两位小数
     *
     * @return
     */
    public static String format2Decimal(String data) {
        float value=Float.parseFloat(data);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(value);
    }

    /**
     * 将float类型的数据转换成以3位逗号隔开的字符串，并且保留一位小数
     *
     * @return
     */
    public static String format1Decimal(String data) {
        float value=Float.parseFloat(data);
        DecimalFormat df = new DecimalFormat("#,##0.0");
        return df.format(value);
    }

    /**
     * 将float类型的数据转换成以3位逗号隔开的字符串，并且保留整数
     *
     * @return
     */
    public static String format0Decimal(String data) {
        float value=Float.parseFloat(data);
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(value);
    }
/*
    校验过程：
    1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
    2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
    3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
    */
    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if(bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeBankCard
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard){
        if(nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }

    //处理数字小数点后无用的0
    public static String wipeBigDecimalZero(BigDecimal number) {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(number);
    }


    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context,String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }




    public static String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/slm/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

}
