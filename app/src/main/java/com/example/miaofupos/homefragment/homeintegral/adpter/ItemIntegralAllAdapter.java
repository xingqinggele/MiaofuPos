package com.example.miaofupos.homefragment.homeintegral.adpter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralAllBean;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/25
 * 描述:回调记录Item Adapter
 */
public class ItemIntegralAllAdapter extends BaseQuickAdapter<IntegralAllBean.Detail, BaseViewHolder> {
    public ItemIntegralAllAdapter(int layoutResId, @Nullable List<IntegralAllBean.Detail> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralAllBean.Detail item) {
        helper.setText(R.id.item_integarall_type,item.getIntegralType());
        helper.setText(R.id.item_integarall_time,item.getCreateTime());
        String value = "";
        if (item.getValue()>0){
            value = "+" + item.getValue();
        }else {
            value = "" + item.getValue();
        }
        helper.setText(R.id.item_integarall_price,value);
    }
}
