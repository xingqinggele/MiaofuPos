package com.example.miaofupos.homefragment.homemessage;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homemessage.fragment.BusinessMessageFragment;
import com.example.miaofupos.homefragment.homemessage.fragment.SystemMessageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/17
 * 描述:消息中心
 */
public class HomeMessageActivity extends BaseActivity implements View.OnClickListener {
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
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.home_message_activity;
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
        //tab标题存入容器里
        title_dates.add("业务消息");
        title_dates.add("系统消息");
        //fragment存入容器里
        fragmentList.add(new BusinessMessageFragment());
        fragmentList.add(new SystemMessageFragment());
        //配置adapter
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
