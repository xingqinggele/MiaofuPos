package com.example.miaofupos.newprojectview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.newprojectview.bean.NewDistrictBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:
 */
public class NewAreaAdapter extends BaseAdapter {
    Context context;
    List<NewDistrictBean> mDistrictList;
    private int districtIndex = -1;

    public NewAreaAdapter(Context context, List<NewDistrictBean> mDistrictList) {
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

    public NewDistrictBean getItem(int position) {
        return (NewDistrictBean)this.mDistrictList.get(position);
    }

    public long getItemId(int position) {
        return Long.parseLong(((NewDistrictBean)this.mDistrictList.get(position)).getAreaCode());
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

        NewDistrictBean item = this.getItem(position);
        holder.name.setText(item.getAreaName());
        boolean checked = this.districtIndex != -1 && ((NewDistrictBean)this.mDistrictList.get(this.districtIndex)).getAreaName().equals(item.getAreaName());
        holder.name.setEnabled(!checked);
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

