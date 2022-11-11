package com.example.miaofupos.homefragment.homemerchants.homemerchantschange.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/22
 * 描述:商户变更，商户审核进度Adapter
 */
public class MerchantsProgressAdapter extends BaseQuickAdapter<HomeTeamBean, BaseViewHolder> {
    public MerchantsProgressAdapter(int layoutResId, @Nullable List<HomeTeamBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeTeamBean item) {

    }
}