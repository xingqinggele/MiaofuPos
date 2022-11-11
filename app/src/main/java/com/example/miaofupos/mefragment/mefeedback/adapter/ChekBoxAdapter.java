package com.example.miaofupos.mefragment.mefeedback.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.mefragment.mefeedback.bean.ChekBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者: qgl
 * 创建日期：2020/12/19
 * 描述:
 */
public class ChekBoxAdapter extends BaseAdapter {
    private Context mContext;
    private List<ChekBean> mDatas = new ArrayList();
    public ChekBoxAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public ChekBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_check_box, null);
            mViewHolder.checkBox = convertView.findViewById(R.id.check_box);
            mViewHolder.tv_name = convertView.findViewById(R.id.check_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (mDatas.get(position).isChecked()) {
            mViewHolder.checkBox.setChecked(true);
        } else {
            mViewHolder.checkBox.setChecked(false);
        }

        mViewHolder.tv_name.setText(mDatas.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        CheckBox checkBox;
        TextView tv_name;
    }

}
