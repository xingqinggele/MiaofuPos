package com.example.miaofupos.homefragment.homeequipment.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.fragment.record.CallbackRecordFragment;
import com.example.miaofupos.homefragment.homeequipment.fragment.record.TransferRecordFragment;
import java.util.ArrayList;

/**
 * 作者: qgl
 * 创建日期：2020/12/25
 * 描述:调拨记录
 */
public class TerminalRecordActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //TabLayout
    private TabLayout record_tab_layout;
    //ViewPager
    private ViewPager record_viewpager;
    //标题数组
    ArrayList<String> titleDatas   = new ArrayList<>();
    //fragment数组
    ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.terminal_record_activity;
    }

    @Override
    protected void initView() {
        record_tab_layout = findViewById(R.id.record_tab_layout);
        record_viewpager = findViewById(R.id.record_viewpager);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        titleDatas.add("划拨记录");
        titleDatas.add("回调记录");
        fragmentList.add(new TransferRecordFragment());
        fragmentList.add(new CallbackRecordFragment());
        init();
    }

    private void init() {
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        record_tab_layout.setSelectedTabIndicator(0);
        record_viewpager.setAdapter(myViewPageAdapter);
        record_tab_layout.setupWithViewPager(record_viewpager);
        record_tab_layout.setTabsFromPagerAdapter(myViewPageAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
