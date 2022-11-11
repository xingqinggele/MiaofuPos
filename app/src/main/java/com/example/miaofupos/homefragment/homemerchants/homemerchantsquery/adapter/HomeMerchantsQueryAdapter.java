package com.example.miaofupos.homefragment.homemerchants.homemerchantsquery.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/19
 * 描述:商户查询Adapter
 */
public class HomeMerchantsQueryAdapter extends BaseQuickAdapter<HomeTeamBean, BaseViewHolder> {
    public HomeMerchantsQueryAdapter(int layoutResId, @Nullable List<HomeTeamBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeTeamBean item) {

    }
}
