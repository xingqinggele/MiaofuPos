package com.example.miaofupos.homefragment.homelist.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.bean.MerMachineBean;
import com.example.miaofupos.homefragment.homelist.bean.HomeRankingBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/14
 * 描述: 排行榜Adapter
 */
public class HomeListAdapter extends BaseQuickAdapter<HomeRankingBean, BaseViewHolder> {
    public HomeListAdapter(int layoutResId, @Nullable List<HomeRankingBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeRankingBean item) {
        SimpleDraweeView head = helper.itemView.findViewById(R.id.item_home_list_head);
        if (item.getIndex() == 0) {
            helper.setVisible(R.id.home_list_item_ranking_iv, true);
            helper.setImageResource(R.id.home_list_item_ranking_iv, R.mipmap.home_list_card01);
            helper.setVisible(R.id.home_list_item_ranking_tv, false);
        } else if (item.getIndex() == 1) {
            helper.setVisible(R.id.home_list_item_ranking_iv, true);
            helper.setImageResource(R.id.home_list_item_ranking_iv, R.mipmap.home_list_card02);
            helper.setVisible(R.id.home_list_item_ranking_tv, false);
        } else if (item.getIndex() == 2) {
            helper.setVisible(R.id.home_list_item_ranking_iv, true);
            helper.setImageResource(R.id.home_list_item_ranking_iv, R.mipmap.home_list_card03);
            helper.setVisible(R.id.home_list_item_ranking_tv, false);
        } else {
            helper.setVisible(R.id.home_list_item_ranking_iv, false);
            helper.setVisible(R.id.home_list_item_ranking_tv, true);
            helper.setText(R.id.home_list_item_ranking_tv, (item.getIndex() + 1) + "");
        }
        helper.setText(R.id.home_list_item_name, item.getNickNAme());
        helper.setText(R.id.home_list_item_num, item.getActivateNum());

        Uri imgurl = Uri.parse(item.getHeadPortrait());
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        head.setImageURI(imgurl);


    }
}
