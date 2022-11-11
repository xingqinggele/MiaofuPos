package com.example.miaofupos.homefragment.homeequipment.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/25
 * 描述:回调记录Item Adapter
 */
public class ItemCallbackRecordAdapter extends BaseQuickAdapter<BusinessMessageBean, BaseViewHolder> {
    public ItemCallbackRecordAdapter(int layoutResId, @Nullable List<BusinessMessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BusinessMessageBean item) {

    }
}
