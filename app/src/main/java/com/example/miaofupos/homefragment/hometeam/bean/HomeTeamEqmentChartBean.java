package com.example.miaofupos.homefragment.hometeam.bean;

import java.math.BigDecimal;

/**
 * 作者: qgl
 * 创建日期：2021/4/2
 * 描述: 折线图Bean
 */
public class HomeTeamEqmentChartBean {
    /**
     * 数据点
     */
    private String num;
    /**
     * 时间
     */
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public HomeTeamEqmentChartBean() {
        super();
    }
}
