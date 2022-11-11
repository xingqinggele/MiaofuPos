package com.example.miaofupos.homefragment.homeequipment.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeequipment.bean.CallbackRecordBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/22
 * 描述: 调拨记录详情Adapter
 */
public class RecordDetailsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public RecordDetailsAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_record_details_code,item);
    }
}
