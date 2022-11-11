package com.example.miaofupos.bean;

import java.io.Serializable;

/**
 * 作者: qgl
 * 创建日期：2020/12/7
 * 描述: 终端机器bean,往接口传输的时候需要
 */
public class MerMachineBean implements Serializable {
    private String posId;
    private String version;
    private String type;

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




}
