package com.example.miaofupos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.miaofupos.R;

import java.util.List;

public class ChooseGridViewAdapter2 extends BaseAdapter {
    LayoutInflater layoutInflater;
    private TextView bt_sel_peo;

    public int getSelectorPosition() {
        return selectorPosition;
    }

    public void setSelectorPosition(int selectorPosition) {
        this.selectorPosition = selectorPosition;
    }

    private int selectorPosition;
    private List<String> menuList;

    public ChooseGridViewAdapter2(Context context, List<String> list) {
        this.menuList = list;
        layoutInflater = LayoutInflater.from(context);
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
        bt_sel_peo.setText(menuList.get(position));

        if (selectorPosition == position) {
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
