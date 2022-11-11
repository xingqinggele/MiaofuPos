package com.example.miaofupos.homefragment.transaction.bean;

import android.content.Context;

/**
 * 作者: qgl
 * 创建日期：2021/10/29
 * 描述:
 */
public class TransactionBean {
    private String hostDate;
    private String acqName;
    private String amnount;

    public String getHostDate() {
        return hostDate;
    }

    public void setHostDate(String hostDate) {
        this.hostDate = hostDate;
    }

    public String getAcqName() {
        return acqName;
    }

    public void setAcqName(String acqName) {
        this.acqName = acqName;
    }

    public String getAmnount() {
        return amnount;
    }

    public void setAmnount(String amnount) {
        this.amnount = amnount;
    }
}