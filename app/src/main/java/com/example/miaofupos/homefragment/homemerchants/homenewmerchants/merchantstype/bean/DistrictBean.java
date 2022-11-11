package com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述: 区
 */
public class DistrictBean implements Parcelable {
    private String dictValue;
    private String cname;
    public static final Creator<DistrictBean> CREATOR = new Creator<DistrictBean>() {
        public DistrictBean createFromParcel(Parcel source) {
            return new DistrictBean(source);
        }

        public DistrictBean[] newArray(int size) {
            return new DistrictBean[size];
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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dictValue);
        dest.writeString(this.cname);
    }

    public DistrictBean() {
    }

    protected DistrictBean(Parcel in) {
        this.dictValue = in.readString();
        this.cname = in.readString();
    }
}

