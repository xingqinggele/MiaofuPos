package com.example.miaofupos.homefragment.homeintegral;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeintegral.fragment.IntegralAllFragment;
import com.example.miaofupos.homefragment.homeintegral.fragment.IntegralIncomeFragment;
import com.example.miaofupos.homefragment.homeintegral.fragment.IntegralSpendingFragment;

import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2021/2/19
 * 描述:积分明细
 */
public class HomeIntegralDetailActivity extends BaseActivity implements View.OnClickListener {
    //选项卡控件
    private TabLayout message_table_layout;
    //ViewPager
    private ViewPager message_viewpager;
    //返回键
    private LinearLayout iv_back;
    //存储标题容器
    ArrayList<String> title_dates = new ArrayList<>();
    //存储界面容器
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.homeintegraldetail_activity;
    }

    //初始化控件
    @Override
    protected void initView() {
        message_table_layout = findViewById(R.id.message_table_layout);
        message_viewpager = findViewById(R.id.message_viewpager);
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
        title_dates.add("全部");
        title_dates.add("收入");
        title_dates.add("支出");
        //界面容器填充界面
        fragmentList.add(new IntegralAllFragment());
        fragmentList.add(new IntegralIncomeFragment());
        fragmentList.add(new IntegralSpendingFragment());
        init();
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

    //配置ViewPager adapter
    private void init() {
        //初始化Adapter
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), title_dates, fragmentList);
        //设置选中的标签指示器、默认选中第一个
        message_table_layout.setSelectedTabIndicator(0);
        //viewPager设置适配器
        message_viewpager.setAdapter(myViewPageAdapter);
        //设置ViewPager
        message_table_layout.setupWithViewPager(message_viewpager);
        //设置选项卡
        message_table_layout.setTabsFromPagerAdapter(myViewPageAdapter);
    }
}
