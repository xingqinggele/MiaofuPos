package com.example.miaofupos.newprojectview.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:城市
 */
public class NewCityBean implements Parcelable {
    private String areaCode;
    private String areaName;
    private String parentCode;
    private String areaLevel;
    private ArrayList<NewDistrictBean> cityList;
    public static final Creator<NewCityBean> CREATOR = new Creator<NewCityBean>() {
        public NewCityBean createFromParcel(Parcel source) {
            return new NewCityBean(source);
        }

        public NewCityBean[] newArray(int size) {
            return new NewCityBean[size];
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

    public ArrayList<NewDistrictBean> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<NewDistrictBean> cityList) {
        this.cityList = cityList;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return areaName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.areaCode);
        dest.writeString(this.areaName);
        dest.writeTypedList(this.cityList);
    }

    public NewCityBean() {
    }

    protected NewCityBean(Parcel in) {
        this.areaCode = in.readString();
        this.areaName = in.readString();
        this.cityList = in.createTypedArrayList(NewDistrictBean.CREATOR);
    }
}

