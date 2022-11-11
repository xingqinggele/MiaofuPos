package com.example.miaofupos.homefragment.hometeam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.ChooseGridViewAdapter;
import com.example.miaofupos.homefragment.hometeam.HomeTeamActivity;
import com.example.miaofupos.homefragment.hometeam.HomeTeamDetailsActivity;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamBean;
import com.example.miaofupos.homefragment.hometeam.bean.TeamBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qgl on 2020/3/11.
 * Describe:
 */
public class HomeTeamAdapter extends BaseQuickAdapter<TeamBean, BaseViewHolder> {

    private Context mContext;

    public HomeTeamAdapter(Context context, int layoutResId, @Nullable List<TeamBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TeamBean report) {
        //设置头像
        SimpleDraweeView home_team_logo = holder.itemView.findViewById(R.id.home_team_logo);
        Uri imgurl=Uri.parse(report.getPortrait());
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        home_team_logo.setImageURI(imgurl);
        //判断用户名是否存在
        if (TextUtils.isEmpty(report.getPartnerName())) {
            holder.setText(R.id.home_team_name, "未实名认证");
            holder.setText(R.id.home_team_tv_price, "0");
            holder.setText(R.id.home_team_tv_teamTransAmount, "0");
            holder.setImageDrawable(R.id.real_name_type, mContext.getResources().getDrawable(R.mipmap.no_real_name));
            holder.setVisible(R.id.home_team_tv_details, false);
        } else {
            holder.setText(R.id.home_team_name, report.getPartnerName());
            holder.setText(R.id.home_team_tv_price, new BigDecimal(report.getTeamTransAmount()).toString());
            holder.setText(R.id.home_team_tv_teamTransAmount, report.getTeamActiveCounts());
            holder.setImageDrawable(R.id.real_name_type, mContext.getResources().getDrawable(R.mipmap.have_real_name));
            holder.setVisible(R.id.home_team_tv_details, true);
        }
        holder.setText(R.id.home_team_xyk_tv,report.getRateT0());
        holder.setText(R.id.home_team_sm_tv,report.getQrsettleRate());
        holder.setOnClickListener(R.id.home_team_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(report.getPartnerName())) {
                    Intent intent = new Intent(mContext, HomeTeamDetailsActivity.class);
                    intent.putExtra("id", report.getParnterId());
                    mContext.startActivity(intent);
                }
            }
        });
        holder.setOnClickListener(R.id.home_team_phone_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemAddClick.onItemClick(report.getParnterId(),report.getRateT0(),report.getQrsettleRate());
            }
        });
    }


    public static interface OnAddClickListener {
        // true add; false cancel
        public void onItemClick(String parnterId,String xyk,String sm); //传递boolean类型数据给activity
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }

}
