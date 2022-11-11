package com.example.miaofupos.homefragment.homemerchants.memerchants.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.datafragment.databenefit.bean.DataBenefitBean;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.MeMerchantsTransferBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/3/15
 * 描述:商户转移Adapter
 */
public class MeMerchantsTransferAdapter extends BaseQuickAdapter<MeMerchantsTransferBean,BaseViewHolder> {

    public MeMerchantsTransferAdapter(int layoutResId, @Nullable List<MeMerchantsTransferBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeMerchantsTransferBean item) {
        SimpleDraweeView merchant_person_logo = helper.itemView.findViewById(R.id.merchant_person_logo);
        Uri imgurl=Uri.parse(item.getPortrait());
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        merchant_person_logo.setImageURI(imgurl);
        helper.setText(R.id.merchat_person_name, item.getNickName());
        if (!item.getPhonenumber().equals("") && !item.getPhonenumber().equals(null) && item.getPhonenumber() != null)
            helper.setText(R.id.merchats_person_phone, item.getPhonenumber().substring(0,3) + "****" + item.getPhonenumber().substring(item.getPhonenumber().length() - 4));
    }
}
