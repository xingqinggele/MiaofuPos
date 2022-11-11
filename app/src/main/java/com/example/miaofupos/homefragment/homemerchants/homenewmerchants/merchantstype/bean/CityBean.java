package com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:城市
 */
public class CityBean implements Parcelable {
    private String dictValue;
    private String cname;
    private ArrayList<DistrictBean> cityList;
    public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
        public CityBean createFromParcel(Parcel source) {
            return new CityBean(source);
        }

        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };

    public String toString() {
        return this.cname;
    }

    public String getDictValue() {
        return this.dictValue == null ? "" : this.dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public ArrayList<DistrictBean> getCityList() {
        return this.cityList;
    }

    public void setCityList(ArrayList<DistrictBean> cityList) {
        this.cityList = cityList;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dictValue);
        dest.writeString(this.cname);
        dest.writeTypedList(this.cityList);
    }

    public CityBean() {
    }

    protected CityBean(Parcel in) {
        this.dictValue = in.readString();
        this.cname = in.readString();
        this.cityList = in.createTypedArrayList(DistrictBean.CREATOR);
    }
}

