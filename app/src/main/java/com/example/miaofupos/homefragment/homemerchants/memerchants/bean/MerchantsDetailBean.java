package com.example.miaofupos.homefragment.homemerchants.memerchants.bean;

/**
 * 创建：  qgl
 * 时间：
 * 描述：
 */
public class MerchantsDetailBean {
    private String mouthCount; //月交易笔数
    private String sumCount; //总交易笔数
    private String mouthAmount; //月交易量
    private String sumAmount; //总交易量
    private String sn;
    private String terminalNo; //设备终端编号

    public String getMouthCount() {
        return mouthCount;
    }

    public void setMouthCount(String mouthCount) {
        this.mouthCount = mouthCount;
    }

    public String getSumCount() {
        return sumCount;
    }

    public void setSumCount(String sumCount) {
        this.sumCount = sumCount;
    }

    public String getMouthAmount() {
        return mouthAmount;
    }

    public void setMouthAmount(String mouthAmount) {
        this.mouthAmount = mouthAmount;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }
}
