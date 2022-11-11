package com.example.miaofupos.mefragment.mepowerattorney;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.IntervalTransferFragment;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.SelectTransferFragment1;
import com.example.miaofupos.mefragment.mepowerattorney.fragment.V1Fragment;
import com.example.miaofupos.mefragment.mepowerattorney.fragment.V2Fragment;
import com.example.miaofupos.utils.SPUtils;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2020/12/15
 * 描述:我的授权书
 */
public class MePowerAttorneyActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    ArrayList<String> titleDatas = new ArrayList<>();
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private TextView number_tv;
    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.mepowerattorneyactivity;
    }

    //控件初始化
    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        tab_layout = findViewById(R.id.tab_layout);
        viewpager = findViewById(R.id.me_powerattorney_viewpager);
        number_tv = findViewById(R.id.number_tv);
    }

    //触发事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                number_tv.setText((i + 1) + "/2");

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    //数据处理
    @Override
    protected void initData() {
        titleDatas.add("选择划拨");
        titleDatas.add("区间划拨");
        fragmentList.add(new V1Fragment());
        fragmentList.add(new V2Fragment());
        init();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回键
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void init() {
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        tab_layout.setSelectedTabIndicator(0);
        viewpager.setAdapter(myViewPageAdapter);
        tab_layout.setupWithViewPager(viewpager);
        tab_layout.setTabsFromPagerAdapter(myViewPageAdapter);
    }
}
