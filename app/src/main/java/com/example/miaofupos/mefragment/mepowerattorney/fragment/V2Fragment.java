package com.example.miaofupos.mefragment.mepowerattorney.fragment;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.utils.SPUtils;

/**
 * 创建：  qgl
 * 时间：
 * 描述：快钱授权书
 */
public class V2Fragment extends BaseFragment {
    //授权书内容tv
    private TextView me_power_attorney_name_tv;
    //邀请码tv
    private TextView me_power_attorney_number_tv;
    //注册时间tv
    private TextView me_power_attorney_year_tv;
    //用户名
    private String name = "";
    //注册时间
    private String createTime = "";

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.v2fragment;
    }

    @Override
    protected void initView(View rootView) {
        me_power_attorney_name_tv = rootView.findViewById(R.id.me_power_attorney_name_tv);
        me_power_attorney_number_tv = rootView.findViewById(R.id.me_power_attorney_number_tv);
        me_power_attorney_year_tv = rootView.findViewById(R.id.me_power_attorney_year_tv);
        iniData();
    }

    private void iniData() {
        name = "长春众鑫助手科技有限公司";
        String address = "全国";
        //本地获取用户邀请码
        //本地获取用户注册时间
        createTime = SPUtils.get(getActivity(), "createTime", "").toString();
        //拼接富文本 -- 公司名称
        SpannableString msp = new SpannableString("\t\t兹授权\t" + name + " 为本公司在" + address + "地区快钱刷产品的外包服务商。");
        //1字体颜色
        msp.setSpan(new ForegroundColorSpan(Color.parseColor("#3CA0FF")), 5, name.length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //2字体颜色
        msp.setSpan(new ForegroundColorSpan(Color.parseColor("#3CA0FF")), 5 + name.length() + 7, 5 + name.length() + 7 + address.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //加粗
        msp.setSpan(new TypefaceSpan("default-bold"), 5, name.length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //1添加下下划线
        msp.setSpan(new UnderlineSpan(), 5, name.length() + 6, 0);
        //2添加下下划线
        msp.setSpan(new UnderlineSpan(), 5 + name.length() + 7, 5 + name.length() + 7 + address.length(), 0);
        //字体大小
        msp.setSpan(new AbsoluteSizeSpan(25), 5, name.length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //显示文本
        me_power_attorney_name_tv.setText(msp);
        //拼接富文本 -- 姓名
        String authorCode = "KQXY01041";
        SpannableString msp1 = new SpannableString("授权编号：" + authorCode);
        //字体颜色、位置
        msp1.setSpan(new ForegroundColorSpan(Color.parseColor("#3CA0FF")), 5, authorCode.length() + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //加粗
        msp1.setSpan(new TypefaceSpan("default-bold"), 5, authorCode.length() + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体大小
        msp1.setSpan(new AbsoluteSizeSpan(30), 5, authorCode.length() + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //显示文本
        me_power_attorney_number_tv.setText(msp1);
        //显示时间
        me_power_attorney_year_tv.setText(createTime.substring(0, createTime.length() - 8));
    }
}
