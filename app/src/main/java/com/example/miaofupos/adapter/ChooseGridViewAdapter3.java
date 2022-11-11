package com.example.miaofupos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralMostBean;

import java.util.List;

public class ChooseGridViewAdapter3 extends BaseAdapter {
    LayoutInflater layoutInflater;
    private TextView bt_sel_peo;
    private int selectorPosition;
    private List<IntegralMostBean.PosTypeList> menuList;
    private boolean aBoolean = false;

    public ChooseGridViewAdapter3(Context context, List<IntegralMostBean.PosTypeList> list) {
        this.menuList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getSelectorPosition() {
        return selectorPosition;
    }

    public void setSelectorPosition(int selectorPosition) {
        this.selectorPosition = selectorPosition;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    @Override
    public int getCount() {
        return menuList.size();//注意此处
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_choose_grid, null);
        bt_sel_peo = convertView.findViewById(R.id.tv);
        bt_sel_peo.setText(menuList.get(position).getTypeName());
        if (aBoolean && selectorPosition == position) {
            bt_sel_peo.setBackgroundResource(R.drawable.item_choose_tv_false);
            bt_sel_peo.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            bt_sel_peo.setBackgroundResource(R.drawable.item_choose_tv_true);
            bt_sel_peo.setTextColor(Color.parseColor("#AAAAAA"));
        }
        return convertView;
    }

    public void newAdd() {
        selectorPosition = 0;
        notifyDataSetChanged();
    }

}
