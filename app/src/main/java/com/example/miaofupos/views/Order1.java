package com.example.miaofupos.views;


import com.example.miaofupos.homefragment.hometeam.bean.TeamBean;

import java.util.Comparator;

/**
 * 作者: qgl
 * 创建日期：2021/1/19
 * 描述:以上为降序写法，
 */
public class Order1 implements Comparator<TeamBean> {
    @Override
    public int compare(TeamBean teamBean, TeamBean t1) {
//        return  Integer.valueOf(t1.getTeamTransAmount()) - Integer.valueOf(teamBean.getTeamTransAmount());

        return (int) (Double.parseDouble(t1.getTeamTransAmount()) - Double.parseDouble(teamBean.getTeamTransAmount()));
    }


}
