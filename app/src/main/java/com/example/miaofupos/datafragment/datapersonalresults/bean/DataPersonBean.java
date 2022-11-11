package com.example.miaofupos.datafragment.datapersonalresults.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者: qgl
 * 创建日期：2021/3/15
 * 描述:业绩Bean
 */
public class DataPersonBean implements Serializable {
    @SerializedName("day")  // 根据接口自定义
    private String day; //交易时间
    @SerializedName("totalPartnerNum")  // 根据接口自定义
    private String totalPartnerNum; //新增伙伴
    @SerializedName("tatalActionNum")  // 根据接口自定义
    private String tatalActionNum; //新增商户
    @SerializedName("totalTransAmount")  // 根据接口自定义
    private String totalTransAmount; //总交易额
    @SerializedName("totalShaoma")  // 根据接口自定义
    private String totalShaoma; // 扫码
    @SerializedName("totalShuka")  // 根据接口自定义
    private String totalShuka; //刷卡
    @SerializedName("totalShanfu")  // 根据接口自定义
    private String totalShanfu; //闪付
    private String sftTotalShuka; //盛付通闪付
    private String sftTotalShaoma ; //闪付



    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTotalPartnerNum() {
        return totalPartnerNum;
    }

    public void setTotalPartnerNum(String totalPartnerNum) {
        this.totalPartnerNum = totalPartnerNum;
    }

    public String getTatalActionNum() {
        return tatalActionNum;
    }

    public void setTatalActionNum(String tatalActionNum) {
        this.tatalActionNum = tatalActionNum;
    }

    public String getTotalTransAmount() {
        return totalTransAmount;
    }

    public void setTotalTransAmount(String totalTransAmount) {
        this.totalTransAmount = totalTransAmount;
    }

    public String getTotalShaoma() {
        return totalShaoma;
    }

    public void setTotalShaoma(String totalShaoma) {
        this.totalShaoma = totalShaoma;
    }

    public String getTotalShuka() {
        return totalShuka;
    }

    public void setTotalShuka(String totalShuka) {
        this.totalShuka = totalShuka;
    }

    public String getTotalShanfu() {
        return totalShanfu;
    }

    public void setTotalShanfu(String totalShanfu) {
        this.totalShanfu = totalShanfu;
    }

    public String getSftTotalShuka() {
        return sftTotalShuka;
    }

    public void setSftTotalShuka(String sftTotalShuka) {
        this.sftTotalShuka = sftTotalShuka;
    }

    public String getSftTotalShaoma() {
        return sftTotalShaoma;
    }

    public void setSftTotalShaoma(String sftTotalShaoma) {
        this.sftTotalShaoma = sftTotalShaoma;
    }
}
