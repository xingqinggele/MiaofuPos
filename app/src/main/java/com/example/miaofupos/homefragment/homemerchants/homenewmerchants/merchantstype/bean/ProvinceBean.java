package com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:省
 */
public class ProvinceBean implements Parcelable {
    private String dictValue;
    private String cname;
    private ArrayList<CityBean> cityList;
    public static final Creator<ProvinceBean> CREATOR = new Creator<ProvinceBean>() {
        public ProvinceBean createFromParcel(Parcel source) {
            return new ProvinceBean(source);
        }

        public ProvinceBean[] newArray(int size) {
            return new ProvinceBean[size];
        }
    };

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String toString() {
        return this.cname;
    }

    public String getDictValue() {
        return this.dictValue == null ? "" : this.dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }



    public ArrayList<CityBean> getCityList() {
        return this.cityList;
    }

    public void setCityList(ArrayList<CityBean> cityList) {
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

    public ProvinceBean() {
    }

    protected ProvinceBean(Parcel in) {
        this.dictValue = in.readString();
        this.cname = in.readString();
        this.cityList = in.createTypedArrayList(CityBean.CREATOR);
    }
}

