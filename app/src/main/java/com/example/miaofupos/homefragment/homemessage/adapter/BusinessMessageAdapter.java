package com.example.miaofupos.homefragment.homemessage.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean1;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/11/13
 * 描述: 系统消息adapter
 */
public class BusinessMessageAdapter extends BaseQuickAdapter<BusinessMessageBean1, BaseViewHolder> {
    public BusinessMessageAdapter(int layoutResId, @Nullable List<BusinessMessageBean1> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BusinessMessageBean1 item) {
        helper.setText(R.id.item_business_message_tv_title,item.getTitle());
        helper.setText(R.id.item_business_message_tv_content,item.getContext());
        helper.setText(R.id.item_business_message_time,item.getDate());
    }
}
