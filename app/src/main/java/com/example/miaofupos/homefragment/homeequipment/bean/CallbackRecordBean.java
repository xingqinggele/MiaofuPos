package com.example.miaofupos.homefragment.homeequipment.bean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/19
 * 描述: 回调记录,划拨 Bean
 */
public class CallbackRecordBean {
    private String posCounts;
    private String operator;
    private String operatorName;
    private String allocName;
    private String adjustName;
//    private String posCodes;
    private List<String> posCodes;
    private String operateTime;

    public String getPosCounts() {
        return posCounts;
    }

    public void setPosCounts(String posCounts) {
        this.posCounts = posCounts;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getAllocName() {
        return allocName;
    }

    public void setAllocName(String allocName) {
        this.allocName = allocName;
    }

    public String getAdjustName() {
        return adjustName;
    }

    public void setAdjustName(String adjustName) {
        this.adjustName = adjustName;
    }


    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public List<String> getPosCodes() {
        return posCodes;
    }

    public void setPosCodes(List<String> posCodes) {
        this.posCodes = posCodes;
    }
}
