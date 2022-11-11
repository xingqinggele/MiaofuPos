package com.example.miaofupos.mefragment.meorder.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeintegral.bean.ShoppingCartBean;
import com.example.miaofupos.mefragment.meorder.bean.MeExchangeBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/7/23
 * 描述: 我的订单 二级列表 adapter
 */
public class MeExchangeItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<MeExchangeBean.list> mDatas = new ArrayList();

    public MeExchangeItemAdapter(Context context) {
        mContext = context;
    }

    public void setDates(List<MeExchangeBean.list> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.homeintegraorder_item1, null);
            mViewHolder.item_me_exchange_type = convertView.findViewById(R.id.item_me_exchange_type);
            mViewHolder.item_me_exchange_price = convertView.findViewById(R.id.item_me_exchange_price);
            mViewHolder.item_good_num = convertView.findViewById(R.id.item_good_num);
            mViewHolder.item_me_exchange_logo = convertView.findViewById(R.id.item_me_exchange_logo);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.item_me_exchange_type.setText(mDatas.get(position).getTypeName());
        mViewHolder.item_me_exchange_price.setText(mDatas.get(position).getReturnIntegral());
        mViewHolder.item_good_num.setText("x"+mDatas.get(position).getPosNum());
        Uri imgurl = Uri.parse(mDatas.get(position).getImgPath());
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        mViewHolder.item_me_exchange_logo.setImageURI(imgurl);

        return convertView;
    }

    class ViewHolder {
        TextView item_me_exchange_type;
        TextView item_me_exchange_price;
        TextView item_good_num;
        SimpleDraweeView item_me_exchange_logo;
    }
}
