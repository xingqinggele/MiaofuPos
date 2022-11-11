package com.example.miaofupos.mefragment.meorder;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.ChooseGridViewAdapter1;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.activity.TerminalActivity;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalActivityBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalEvenBusBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalEvenBusBean1;
import com.example.miaofupos.homefragment.homemessage.fragment.BusinessMessageFragment;
import com.example.miaofupos.homefragment.homemessage.fragment.SystemMessageFragment;
import com.example.miaofupos.mefragment.meorder.fragment.ApplyExchangeFragment;
import com.example.miaofupos.mefragment.meorder.fragment.MeExchangeFragment;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.MyGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2021/2/20
 * 描述: 我的订单
 */
public class MeOrderActivity extends BaseActivity implements View.OnClickListener {
    //滑动table
    private TabLayout meorder_table_layout;
    //滑动ViewPager
    private ViewPager meorder_viewpager;
    //返回键
    private LinearLayout iv_back;
    //table名容器
    ArrayList<String> title_dates = new ArrayList<>();
    //fragment容器
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    //筛选按钮
    private RelativeLayout terminal_screening;
    //侧滑控件
    private DrawerLayout drawer_layout;
    //侧滑筛选栏GridView
    private MyGridView gvTest;
    //侧滑筛选栏adapter
    private ChooseGridViewAdapter1 madapter;
    //选中的订单状态
    private String orderType = "";
    //筛选栏底下两个按钮
    private RadioButton terminal_determine_rb, terminal_cancel_rb;
    //当前界面表示
    private int page = 0;
    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.meorder_activity;
    }
    //初始化控件
    @Override
    protected void initView() {
        meorder_table_layout = findViewById(R.id.meorder_table_layout);
        meorder_viewpager = findViewById(R.id.meorder_viewpager);
        iv_back = findViewById(R.id.iv_back);
        terminal_screening = findViewById(R.id.terminal_screening);
        drawer_layout = findViewById(R.id.drawer_layout);
        gvTest = findViewById(R.id.my_grid1);
        terminal_determine_rb = findViewById(R.id.terminal_determine_rb);
        terminal_cancel_rb = findViewById(R.id.terminal_cancel_rb);
        posOrderType();
    }
    //点击事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        terminal_screening.setOnClickListener(this);
        terminal_determine_rb.setOnClickListener(this);
        terminal_cancel_rb.setOnClickListener(this);
    }
    //适配数据、界面
    @Override
    protected void initData() {
        title_dates.add("我的兑换");
        title_dates.add("兑换申请");
        fragmentList.add(new MeExchangeFragment());
        fragmentList.add(new ApplyExchangeFragment());
        init();
    }
    //viewpager适配adapter
    private void init() {
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), title_dates, fragmentList);
        meorder_table_layout.setSelectedTabIndicator(0);
        meorder_viewpager.setAdapter(myViewPageAdapter);
        meorder_table_layout.setupWithViewPager(meorder_viewpager);
        meorder_table_layout.setTabsFromPagerAdapter(myViewPageAdapter);
        meorder_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                shouLog("当前界面-------------->",i + "页");
                page = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回键
            case R.id.iv_back:
                finish();
                break;
                //筛选按钮
            case R.id.terminal_screening:
                drawer_layout.openDrawer(GravityCompat.END);
                break;
            case R.id.terminal_determine_rb:
                orderType = "";
                madapter.newAdd();
                shouLog("orderType------------->", orderType + "111");
                break;
            case R.id.terminal_cancel_rb:
                drawer_layout.closeDrawer(GravityCompat.END);
                if (orderType.equals("3")){
                    orderType = "";
                }
                shouLog("orderType------------->", orderType + "222");
                //判断当前页码，0 我的兑换界面 1 兑换申请界面
                if (page == 0){
                    TerminalEvenBusBean busBean = new TerminalEvenBusBean();
                    busBean.setMostType(orderType);
                    EventBus.getDefault().post(busBean);
                }else {
                    TerminalEvenBusBean1 busBean = new TerminalEvenBusBean1();
                    busBean.setMostType(orderType);
                    EventBus.getDefault().post(busBean);
                }
                break;
        }
    }

    //获取订单状态
    private void posOrderType(){
        RequestParams params = new RequestParams();
        HttpRequest.getOrderType(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<TerminalActivityBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<TerminalActivityBean>>() {
                            }.getType());
                    madapter = new ChooseGridViewAdapter1(MeOrderActivity.this,memberList);
                    gvTest.setAdapter(madapter);
                    madapter.setOnAddClickListener(onItemActionClick);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }

    // Adapter回调接口
    ChooseGridViewAdapter1.OnAddClickListener onItemActionClick = new ChooseGridViewAdapter1.OnAddClickListener() {
        @Override
        public void onItemClick(String position) {
            orderType = position;
        }
    };



}
