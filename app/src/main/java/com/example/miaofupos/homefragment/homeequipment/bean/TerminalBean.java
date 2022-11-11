package com.example.miaofupos.homefragment.homeequipment.bean;

import java.io.Serializable;

/**
 * 作者: qgl
 * 创建日期：2021/1/16
 * 描述: 终端查询Bean
 */
public class TerminalBean implements Serializable {
    private String posId; //posId
    private String posName; //pos名称
    private String var2; //商户号
    private String posCode; //pos序列号
    private String imgPath; //pos机图片
    private String posType; //pos类型

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    private String var1; //终端类型
    private String posActivateStatus;
    private String posBindTime; //绑定激活时间
    private String posActivity; //活动类型 1，传统POS刷够返活动
    private String posDeposit; //押金 金额
    private String posCashback; //返现 金额
    private String posModel; //刷返够模式，1-返合方
    private String recordsType;//激活状态，1激活 0未激活
    private String posTypeName;
    private String specialName;

    public String getRecordsType() {
        return recordsType;
    }

    public void setRecordsType(String recordsType) {
        this.recordsType = recordsType;
    }


    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }



    public String getPosActivateStatus() {
        return posActivateStatus;
    }

    public void setPosActivateStatus(String posActivateStatus) {
        this.posActivateStatus = posActivateStatus;
    }

    public String getPosBindTime() {
        return posBindTime;
    }

    public void setPosBindTime(String posBindTime) {
        this.posBindTime = posBindTime;
    }

    public String getPosActivity() {
        return posActivity;
    }

    public void setPosActivity(String posActivity) {
        this.posActivity = posActivity;
    }

    public String getPosDeposit() {
        return posDeposit;
    }

    public void setPosDeposit(String posDeposit) {
        this.posDeposit = posDeposit;
    }

    public String getPosCashback() {
        return posCashback;
    }

    public void setPosCashback(String posCashback) {
        this.posCashback = posCashback;
    }

    public String getPosModel() {
        return posModel;
    }

    public void setPosModel(String posModel) {
        this.posModel = posModel;
    }


    public String getPosTypeName() {
        return posTypeName;
    }

    public void setPosTypeName(String posTypeName) {
        this.posTypeName = posTypeName;
    }

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    public String getPosType() {
        return posType;
    }

    public void setPosType(String posType) {
        this.posType = posType;
    }
}
