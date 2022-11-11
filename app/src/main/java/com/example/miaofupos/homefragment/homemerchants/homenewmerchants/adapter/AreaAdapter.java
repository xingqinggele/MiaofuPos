package com.example.miaofupos.homefragment.homemerchants.homenewmerchants.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.DistrictBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:
 */
public class AreaAdapter extends BaseAdapter {
    Context context;
    List<DistrictBean> mDistrictList;
    private int districtIndex = -1;

    public AreaAdapter(Context context, List<DistrictBean> mDistrictList) {
        this.context = context;
        this.mDistrictList = mDistrictList;
    }

    public int getSelectedPosition() {
        return this.districtIndex;
    }

    public void updateSelectedPosition(int index) {
        this.districtIndex = index;
    }

    public int getCount() {
        return this.mDistrictList.size();
    }

    public DistrictBean getItem(int position) {
        return (DistrictBean)this.mDistrictList.get(position);
    }

    public long getItemId(int position) {
        return Long.parseLong(((DistrictBean)this.mDistrictList.get(position)).getDictValue());
    }

    @SuppressLint("WrongConstant")
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_jdcitypicker_item, parent, false);
            holder = new Holder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.selectImg = (ImageView)convertView.findViewById(R.id.selectImg);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        DistrictBean item = this.getItem(position);
        holder.name.setText(item.getCname());
        boolean checked = this.districtIndex != -1 && ((DistrictBean)this.mDistrictList.get(this.districtIndex)).getCname().equals(item.getCname());
        holder.name.setEnabled(!checked);
//        holder.selectImg.setVisibility(checked ? 0 : 8);
        holder.selectImg.setVisibility(checked ? 8 : 8);
        return convertView;
    }

    class Holder {
        TextView name;
        ImageView selectImg;

        Holder() {
        }
    }
}

