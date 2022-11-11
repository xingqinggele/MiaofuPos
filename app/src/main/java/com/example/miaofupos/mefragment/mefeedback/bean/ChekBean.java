package com.example.miaofupos.mefragment.mefeedback.bean;

import java.io.Serializable;

/**
 * 作者: qgl
 * 创建日期：2020/12/21
 * 描述: 意见反馈bean
 */
public class ChekBean implements Serializable {
    private String Title ; //每条item的数据
    private boolean isChecked; //每条item的状态
    public ChekBean(String title){
        Title = title;
    }
    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
