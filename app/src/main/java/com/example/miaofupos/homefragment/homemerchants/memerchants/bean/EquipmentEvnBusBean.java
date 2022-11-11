package com.example.miaofupos.homefragment.homemerchants.memerchants.bean;

/**
 * 作者: qgl
 * 创建日期：2021/3/11
 * 描述:商户详情传输设备fragment 时使用
 */
public class EquipmentEvnBusBean {
    //设备编号
    private String snCode;
    //激活时间
    private String timer;

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
