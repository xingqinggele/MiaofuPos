package com.example.miaofupos.homefragment.homeequipment.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeequipment.bean.PersonBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/21
 * 描述:终端划拨选择人Adapter
 */
public class TraminalPersonAdapter extends BaseQuickAdapter<PersonBean, BaseViewHolder> {
    public TraminalPersonAdapter(int layoutResId, @Nullable List<PersonBean> mData) {
        super(layoutResId, mData);
    }

    @Override
    protected void convert(BaseViewHolder holder, PersonBean report) {
        SimpleDraweeView merchant_person_logo = holder.itemView.findViewById(R.id.merchant_person_logo);
        Uri imgurl=Uri.parse(report.getPortrait());
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        merchant_person_logo.setImageURI(imgurl);
        holder.setText(R.id.merchat_person_name, report.getDictLabel());
        if (!report.getMobile().equals("") && !report.getMobile().equals(null) && report.getMobile() != null)
        holder.setText(R.id.merchats_person_phone, report.getMobile().substring(0,3) + "****" + report.getMobile().substring(report.getMobile().length() - 4));
    }
}
