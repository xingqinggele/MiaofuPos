package com.example.miaofupos.homefragment.hometeam.bean;

import java.math.BigDecimal;

/**
 * 作者: qgl
 * 创建日期：2021/4/2
 * 描述: 折线图Bean
 */
public class HomeTeamChartBean {
    /**
     * 数据点
     */
    private BigDecimal money;
    /**
     * 时间
     */
    private String time;
    private int num;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public HomeTeamChartBean() {
        super();
    }
}
