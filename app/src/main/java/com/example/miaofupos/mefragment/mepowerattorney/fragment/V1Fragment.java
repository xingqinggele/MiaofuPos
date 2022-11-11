package com.example.miaofupos.mefragment.mepowerattorney.fragment;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.utils.SPUtils;

/**
 * 创建：  qgl
 * 时间：
 * 描述：
 */
public class V1Fragment extends BaseFragment {
    //授权书内容tv
    private TextView me_power_attorney_name_tv;
    //邀请码tv
    private TextView me_power_attorney_number_tv;
    //注册时间tv
    private TextView me_power_attorney_year_tv;
    //用户名
    private String name = "";
    //邀请码
    private String code = "";
    //注册时间
    private String createTime = "";

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.v1_fragment;
    }

    @Override
    protected void initView(View rootView) {
        me_power_attorney_name_tv = rootView.findViewById(R.id.me_power_attorney_name_tv);
        me_power_attorney_number_tv = rootView.findViewById(R.id.me_power_attorney_number_tv);
        me_power_attorney_year_tv = rootView.findViewById(R.id.me_power_attorney_year_tv);
        iniData();
    }

    private void iniData() {
        //本地获取用户昵称
        name = SPUtils.get(getActivity(), "nickName", "").toString();
        //本地获取用户邀请码
        code = SPUtils.get(getActivity(), "merchCode", "").toString();
        //本地获取用户注册时间
        createTime = SPUtils.get(getActivity(), "createTime", "").toString();
        //拼接富文本 -- 姓名
        SpannableString msp = new SpannableString("\t\t兹授权\t" + name + " 为“众鑫助手”产品渠道合作伙伴，具体授权内容应以合作协议为准。");
        //字体颜色
        msp.setSpan(new ForegroundColorSpan(Color.parseColor("#3CA0FF")), 5, name.length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //加粗
        msp.setSpan(new TypefaceSpan("default-bold"), 5, name.length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体大小
        msp.setSpan(new AbsoluteSizeSpan(34), 5, name.length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //显示文本
        me_power_attorney_name_tv.setText(msp);
        //拼接富文本 -- 姓名
        SpannableString msp1 = new SpannableString("授权邀请码: " + code);
        //字体颜色
        msp1.setSpan(new ForegroundColorSpan(Color.parseColor("#3CA0FF")), 6, code.length() + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //加粗
        msp1.setSpan(new TypefaceSpan("default-bold"), 6, code.length() + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体大小
        msp1.setSpan(new AbsoluteSizeSpan(30), 6, code.length() + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //显示文本
        me_power_attorney_number_tv.setText(msp1);
        //显示时间
        me_power_attorney_year_tv.setText(createTime.substring(0, createTime.length() - 8));
    }
}
