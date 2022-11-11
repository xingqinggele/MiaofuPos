package com.example.miaofupos.datafragment.datapersonalresults;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;


import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.datapersonalresults.fragment.DataDayResultsFragment;
import com.example.miaofupos.datafragment.datapersonalresults.fragment.DataMonthResultsFragment;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2020/12/25
 * 描述:个人业绩
 */
public class DataPersonalResultsActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //TabLayout
    private TabLayout data_personal_results_tab_layout;
    //ViewPager
    private ViewPager data_personal_results_viewpager;
    //标题数组
    ArrayList<String> titleDatas = new ArrayList<>();
    //fragment数组
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.data_personal_results_activity;
    }

    //初始化控件
    @Override
    protected void initView() {
        data_personal_results_tab_layout = findViewById(R.id.data_personal_results_tab_layout);
        data_personal_results_viewpager = findViewById(R.id.data_personal_results_viewpager);
        iv_back = findViewById(R.id.iv_back);
    }

    //点击事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    //数据处理
    @Override
    protected void initData() {
        //标题容器填充标题
        titleDatas.add("日交易量");
        titleDatas.add("月交易量");
        //界面容器填充界面
        fragmentList.add(new DataDayResultsFragment());
        fragmentList.add(new DataMonthResultsFragment());
        init();
    }

    //配置ViewPager adapter
    private void init() {
        //初始化Adapter
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        //设置选中的标签指示器、默认选中第一个
        data_personal_results_tab_layout.setSelectedTabIndicator(0);
        //viewPager设置适配器
        data_personal_results_viewpager.setAdapter(myViewPageAdapter);
        //设置ViewPager
        data_personal_results_tab_layout.setupWithViewPager(data_personal_results_viewpager);
        //设置选项卡
        data_personal_results_tab_layout.setTabsFromPagerAdapter(myViewPageAdapter);
    }

    //点击事件触发
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
