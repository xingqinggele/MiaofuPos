package com.example.miaofupos.mefragment.meorder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeintegral.adpter.HomeIntegralOrderAdapter;
import com.example.miaofupos.mefragment.meorder.bean.MeExchangeBean;
import com.example.miaofupos.views.MyListView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/11/13
 * 描述: 系统消息adapter
 */
public class MeExchangeAdapter extends BaseQuickAdapter<MeExchangeBean, BaseViewHolder> {
    //商品Adapter
    private MeExchangeItemAdapter adapter;
    private Context context;
    public MeExchangeAdapter(int layoutResId, @Nullable List<MeExchangeBean> data,Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MeExchangeBean item) {
        helper.setText(R.id.item_me_exchange_time, item.getTime());
        if (item.getState().equals("0")) {
            helper.setTextColor(R.id.item_me_exchange_state, Color.parseColor("#C62101"));
            helper.setText(R.id.item_me_exchange_state, "待发货");
        } else if (item.getState().equals("1")) {
            helper.setTextColor(R.id.item_me_exchange_state, Color.parseColor("#7EC601"));
            helper.setText(R.id.item_me_exchange_state, "已完成");
        } else if (item.getState().equals("2")) {
            helper.setTextColor(R.id.item_me_exchange_state, Color.parseColor("#ffff9920"));
            helper.setText(R.id.item_me_exchange_state, "已超时");
        }
        if (item.getPersonstate().equals("服务中心")) {
            helper.setText(R.id.item_me_exchange_name, "服务商:   " + item.getPersonstate());
        } else {
            helper.setText(R.id.item_me_exchange_name, "申请人:   " + item.getPersonstate());
        }
        helper.setText(R.id.item_me_exchange_total_amount, "合计: ￥" + item.getTotalamount());
        MyListView my_listView = helper.itemView.findViewById(R.id.my_listView);
        adapter = new MeExchangeItemAdapter(context);
        adapter.setDates(item.getList());

        my_listView.setClickable(false);
        my_listView.setPressed(false);
        my_listView.setEnabled(false);
        my_listView.setAdapter(adapter);
    }




}
