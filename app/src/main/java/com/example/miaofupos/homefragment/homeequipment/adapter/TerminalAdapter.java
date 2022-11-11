package com.example.miaofupos.homefragment.homeequipment.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalBean;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/19
 * 描述:商户查询Adapter
 */
public class TerminalAdapter extends BaseQuickAdapter<TerminalBean, BaseViewHolder> {
    public TerminalAdapter(int layoutResId, @Nullable List<TerminalBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TerminalBean item) {
        SimpleDraweeView merchant_person_logo = helper.itemView.findViewById(R.id.query_img);
        if (null != item.getImgPath()){
            Uri imgurl=Uri.parse(item.getImgPath());
            // 清除Fresco对这条验证码的缓存
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(imgurl);
            imagePipeline.evictFromDiskCache(imgurl);
            // combines above two lines
            imagePipeline.evictFromCache(imgurl);
            merchant_person_logo.setImageURI(imgurl);
        }

        if (item.getRecordsType() == null) {
            helper.setText(R.id.query_state, "未激活");
        } else  {
            if (item.getRecordsType().equals("1")){
                helper.setText(R.id.query_state, "已绑定");
            }else if (item.getRecordsType().equals("2")){
                helper.setText(R.id.query_state, "已激活");
            }else {
                helper.setText(R.id.query_state, "未激活");
            }
        }
        helper.setText(R.id.query_tv_name, "类型：" + item.getSpecialName());
        helper.setText(R.id.query_tv_number, "序列号：" + item.getPosCode());
        helper.setText(R.id.query_tv_type, "名称：" + item.getPosTypeName());
    }
}
