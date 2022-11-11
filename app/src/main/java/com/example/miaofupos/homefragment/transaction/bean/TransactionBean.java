package com.example.miaofupos.homefragment.transaction.bean;

import android.content.Context;

/**
 * 作者: qgl
 * 创建日期：2021/10/29
 * 描述:
 */
public class TransactionBean {
    //    @ApiModelProperty(value = "交易金额")
    private String jyje;
    //    @ApiModelProperty(value = "商户编号")
    private String shbh;
    //    @ApiModelProperty(value = "商户名称")
    private String shmc;
    //    @ApiModelProperty(value = "交易名称")
    private String jymc;
    //    @ApiModelProperty(value = "卡类型")
    private String klx;
    //    @ApiModelProperty(value = "费率")
    private String fl;
    //    @ApiModelProperty(value = "交易状态")
    private String jyzt;
    //    @ApiModelProperty(value = "交易时间")
    private String  jysj;
    //    @ApiModelProperty(value = "银行卡")
    private String yhk;
    //    @ApiModelProperty(value = "所属")
    private String ss;

    public String getJyje() {
        return jyje;
    }

    public void setJyje(String jyje) {
        this.jyje = jyje;
    }

    public String getShbh() {
        return shbh;
    }

    public void setShbh(String shbh) {
        this.shbh = shbh;
    }

    public String getShmc() {
        return shmc;
    }

    public void setShmc(String shmc) {
        this.shmc = shmc;
    }

    public String getJymc() {
        return jymc;
    }

    public void setJymc(String jymc) {
        this.jymc = jymc;
    }

    public String getKlx() {
        return klx;
    }

    public void setKlx(String klx) {
        this.klx = klx;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getJyzt() {
        return jyzt;
    }

    public void setJyzt(String jyzt) {
        this.jyzt = jyzt;
    }

    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        this.jysj = jysj;
    }

    public String getYhk() {
        return yhk;
    }

    public void setYhk(String yhk) {
        this.yhk = yhk;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }
}