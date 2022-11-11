package com.example.miaofupos.newprojectview.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:省
 */
public class NewProvinceBean implements Parcelable {
    private String areaCode;
    private String areaName;
    private String parentCode;
    private String areaLevel;
    private ArrayList<NewCityBean> cityList;
    public static final Creator<NewProvinceBean> CREATOR = new Creator<NewProvinceBean>() {
        public NewProvinceBean createFromParcel(Parcel source) {
            return new NewProvinceBean(source);
        }

        public NewProvinceBean[] newArray(int size) {
            return new NewProvinceBean[size];
        }
    };

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    public ArrayList<NewCityBean> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<NewCityBean> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return areaName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.areaCode);
        dest.writeString(this.areaName);
        dest.writeTypedList(this.cityList);
    }

    public NewProvinceBean() {

    }

    protected NewProvinceBean(Parcel in) {
        this.areaCode = in.readString();
        this.areaName = in.readString();
        this.cityList = in.createTypedArrayList(NewCityBean.CREATOR);
    }
}

