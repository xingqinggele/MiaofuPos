package com.example.miaofupos.mefragment.meorder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.adapter.RecyclerViewItemDecoration;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalBean;
import com.example.miaofupos.mefragment.meorder.adapter.CeshiAdapter1;
import com.example.miaofupos.mefragment.meorder.fragment.ApplyExchangeFragment;
import com.example.miaofupos.mefragment.meorder.fragment.HomeIntegerIntervalFragment;
import com.example.miaofupos.mefragment.meorder.fragment.HomeIntegerSelectFragment;
import com.example.miaofupos.mefragment.meorder.fragment.MeExchangeFragment;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.net.Utils;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.MyDialog1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2021/2/24
 * 描述: 积分兑换发放极具
 */
public class  HomeIntegraTransferActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private LinearLayout iv_back;
    ArrayList<String> titleDatas = new ArrayList<>();
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private String parentNum = ""; //划拨数量
    private String merchId = ""; //划拨接收者ID
    private String orderId = ""; //订单ID
    private String posId = "";//pos机类型
    private String orderNo = "";//
    private static final HomeIntegraTransferActivity h = null;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.home_integrat_ransfer_activity;
    }

    @Override
    protected void initView() {
        tab_layout = findViewById(R.id.tab_layout);
        viewpager = findViewById(R.id.viewpager);
        iv_back = findViewById(R.id.iv_back);
    }


    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        parentNum = getIntent().getStringExtra("parentNum");
        merchId = getIntent().getStringExtra("merchId");
        orderId = getIntent().getStringExtra("orderId");
        posId = getIntent().getStringExtra("posId");
        orderNo = getIntent().getStringExtra("orderNo");
        titleDatas.add("选择划拨");
        titleDatas.add("区间划拨");
        fragmentList.add(new HomeIntegerSelectFragment().newInstance(parentNum,merchId,orderId,posId,orderNo));
        fragmentList.add(new HomeIntegerIntervalFragment().newInstance(parentNum,merchId,orderId,posId));
        init();
    }


    private void init() {
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        tab_layout.setSelectedTabIndicator(0);
        viewpager.setAdapter(myViewPageAdapter);
        tab_layout.setupWithViewPager(viewpager);
        tab_layout.setTabsFromPagerAdapter(myViewPageAdapter);
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
