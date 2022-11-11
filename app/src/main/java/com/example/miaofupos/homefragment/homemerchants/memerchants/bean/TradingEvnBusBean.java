package com.example.miaofupos.homefragment.homemerchants.memerchants.bean;

/**
 * 作者: qgl
 * 创建日期：2021/3/11
 * 描述:商户详情传输交易fragment 时使用
 */
public class TradingEvnBusBean {
    //本月交易额
    private String month_turnover;
    //累计交易额
    private String cumulative_turnover;
    //交易笔数
    private String turnover_number;

    public String getMonth_turnover() {
        return month_turnover;
    }

    public void setMonth_turnover(String month_turnover) {
        this.month_turnover = month_turnover;
    }

    public String getCumulative_turnover() {
        return cumulative_turnover;
    }

    public void setCumulative_turnover(String cumulative_turnover) {
        this.cumulative_turnover = cumulative_turnover;
    }

    public String getTurnover_number() {
        return turnover_number;
    }

    public void setTurnover_number(String turnover_number) {
        this.turnover_number = turnover_number;
    }
}
