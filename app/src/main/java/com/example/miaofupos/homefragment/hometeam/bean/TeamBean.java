package com.example.miaofupos.homefragment.hometeam.bean;

/**
 * 作者: qgl
 * 创建日期：2021/1/18
 * 描述:我的伙伴Bean
 */
public class TeamBean {
    private String parnterId;  //ID
    private String partnerName; // 姓名
    private String partnerMobile; //手机号
    private String teamTransAmount;  //交易额
    private String teamActiveCounts;  //团队激活数
    private String portrait; // 用户头像
    private String rateT0; // 信用卡结算费率
    private String qrsettleRate; // 扫码结算费率

    public String getParnterId() {
        return parnterId;
    }

    public void setParnterId(String parnterId) {
        this.parnterId = parnterId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerMobile() {
        return partnerMobile;
    }

    public void setPartnerMobile(String partnerMobile) {
        this.partnerMobile = partnerMobile;
    }

    public String getTeamTransAmount() {
        return teamTransAmount;
    }

    public void setTeamTransAmount(String teamTransAmount) {
        this.teamTransAmount = teamTransAmount;
    }

    public String getTeamActiveCounts() {
        return teamActiveCounts;
    }

    public void setTeamActiveCounts(String teamActiveCounts) {
        this.teamActiveCounts = teamActiveCounts;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getRateT0() {
        return rateT0;
    }

    public void setRateT0(String rateT0) {
        this.rateT0 = rateT0;
    }

    public String getQrsettleRate() {
        return qrsettleRate;
    }

    public void setQrsettleRate(String qrsettleRate) {
        this.qrsettleRate = qrsettleRate;
    }
}
