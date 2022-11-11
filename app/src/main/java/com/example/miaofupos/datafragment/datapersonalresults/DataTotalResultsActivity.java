package com.example.miaofupos.datafragment.datapersonalresults;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;


import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.datapersonalresults.fragment.DataDayTotalFragment;
import com.example.miaofupos.datafragment.datapersonalresults.fragment.DataTotalPartnerFragment;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2021/3/18
 * 描述:总业绩
 */
public class DataTotalResultsActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //TabLayout
    private TabLayout data_partner_results_tab_layout;
    //ViewPager
    private ViewPager data_partner_results_viewpager;
    //标题数组
    ArrayList<String> titleDatas = new ArrayList<>();
    //fragment数组
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    //xml界面
    @Override
    protected int getLayoutId() {
        return R.layout.datatotalresults_activity;
    }
    //初始化控件
    @Override
    protected void initView() {
        data_partner_results_tab_layout = findViewById(R.id.data_partner_results_tab_layout);
        data_partner_results_viewpager = findViewById(R.id.data_partner_results_viewpager);
        iv_back = findViewById(R.id.iv_back);
    }
    //点击事件绑定
    @Override
    protected void initListener() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //标题容器填充标题
        titleDatas.add("日交易量");
        titleDatas.add("月交易量");
        //界面容器填充界面
        fragmentList.add(new DataDayTotalFragment());
        fragmentList.add(new DataTotalPartnerFragment());
        init();
    }


    //配置ViewPager adapter
    private void init() {
        //初始化Adapter
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        //设置选中的标签指示器、默认选中第一个
        data_partner_results_tab_layout.setSelectedTabIndicator(0);
        //viewPager设置适配器
        data_partner_results_viewpager.setAdapter(myViewPageAdapter);
        //设置ViewPager
        data_partner_results_tab_layout.setupWithViewPager(data_partner_results_viewpager);
        //设置选项卡
        data_partner_results_tab_layout.setTabsFromPagerAdapter(myViewPageAdapter);
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
}
