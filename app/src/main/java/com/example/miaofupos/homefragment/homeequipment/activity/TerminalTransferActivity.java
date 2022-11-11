package com.example.miaofupos.homefragment.homeequipment.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.ChooseGridViewAdapter1;
import com.example.miaofupos.adapter.ChooseGridViewAdapter2;
import com.example.miaofupos.adapter.ChooseGridViewAdapter3;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.adapter.ChooserListAdapter;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalActivityBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalEvenBusBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalEvenBusBean1;
import com.example.miaofupos.homefragment.homeequipment.bean.ScreeningBean;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.IntervalTransferFragment;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.SelectTransferFragment;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.SelectTransferFragment1;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralMostBean;
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
 * 创建日期：2020/12/24
 * 描述:终端划拨
 */
public class TerminalTransferActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private LinearLayout iv_back;
    ArrayList<String> titleDatas = new ArrayList<>();
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private DrawerLayout drawer_layout;
    private RadioButton terminal_transfer_determine_rb, terminal_transfer_cancel_rb;
    // 点击侧滑的Fragment 1.选择划拨,2.区间划拨
    private int fragmentCode = 1;
    private TerminalEvenBusBean busBean;
    private TerminalEvenBusBean1 busBean1;
    //终端类型
    private String posType = "";
    /*************************** 筛选 ************************/
    //当前Item项
    private List<Integer> showTitle;
    //左侧分类的数据
    private List<String> menuList = new ArrayList<>();
    //右侧商品数据
    private List<IntegralMostBean.PosTypeList> homeList = new ArrayList<>();
    //总数据
    private List<IntegralMostBean> memberList = new ArrayList<>();
    //品牌类型adapter
    private ChooseGridViewAdapter2 madapter;
    //终端类型adapter
    private ChooseGridViewAdapter3 madapter3;
    //侧滑筛选栏GridView
    private MyGridView gvTest;
    //侧滑筛选栏GridView
    private MyGridView gvTest2;

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.terminal_transfer_activity;
    }

    @Override
    protected void initView() {
        terminal_transfer_determine_rb = findViewById(R.id.terminal_transfer_determine_rb);
        terminal_transfer_cancel_rb = findViewById(R.id.terminal_transfer_cancel_rb);
        tab_layout = findViewById(R.id.terminal_transfer_tab_layout);
        viewpager = findViewById(R.id.terminal_transfer_viewpager);
        iv_back = findViewById(R.id.iv_back);
        drawer_layout = findViewById(R.id.drawer_layout);
        gvTest = findViewById(R.id.my_grid1);
        gvTest2 = findViewById(R.id.my_grid2);
        //品牌类型
        madapter = new ChooseGridViewAdapter2(TerminalTransferActivity.this,menuList);
        gvTest.setAdapter(madapter);
        //终端类型
        madapter3 = new ChooseGridViewAdapter3(TerminalTransferActivity.this,homeList);
        gvTest2.setAdapter(madapter3);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        terminal_transfer_determine_rb.setOnClickListener(this);
        terminal_transfer_cancel_rb.setOnClickListener(this);
        gvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                homeList.clear();
                homeList.addAll(memberList.get(position).getPosTypeList());
                madapter.setSelectorPosition(position);
                madapter.notifyDataSetInvalidated();
                madapter3.setaBoolean(false);
                madapter3.notifyDataSetInvalidated();
                posType = "";
            }
        });
        gvTest2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                shouLog("点击了------------>","YES");
                madapter3.setSelectorPosition(position);
                madapter3.setaBoolean(true);
                madapter3.notifyDataSetInvalidated();
                posType = homeList.get(position).getId();
            }
        });
    }

    @Override
    protected void initData() {
        titleDatas.add("选择划拨");
        titleDatas.add("区间划拨");
        fragmentList.add(new SelectTransferFragment1());
        fragmentList.add(new IntervalTransferFragment());
        init();
        postData1();
    }

    private void init() {
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        tab_layout.setSelectedTabIndicator(0);
        viewpager.setAdapter(myViewPageAdapter);
        tab_layout.setupWithViewPager(viewpager);
        tab_layout.setTabsFromPagerAdapter(myViewPageAdapter);
    }

    public void setListSize(int value) {
        fragmentCode = value;
        if (fragmentCode == 2) {
            // 重置
            posType = "";
            madapter.newAdd();
        }
        // 点击了筛选
        drawer_layout.openDrawer(GravityCompat.END);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.terminal_transfer_determine_rb:
                // 重置
                posType = "";
                homeList.clear();
                homeList.addAll(memberList.get(0).getPosTypeList());
                madapter.newAdd();
                madapter3.newAdd();
                madapter3.setaBoolean(false);
                break;
            case R.id.terminal_transfer_cancel_rb:
                drawer_layout.closeDrawer(GravityCompat.END);
                // 选择划拨
                busBean = new TerminalEvenBusBean();
                busBean.setTerminalType(posType);
//                busBean.setMostType(mostType);
                //区间划拨
//                busBean1 = new TerminalEvenBusBean1();
//                busBean1.setTerminalType(terminalType);
//                busBean1.setMostType(mostType);
//                if (fragmentCode == 1) {
                    EventBus.getDefault().post(busBean);
//                } else {
//                    EventBus.getDefault().post(busBean1);
//                }
                // 点击了确认
                break;
        }
    }

    // 获取筛选数据
    public void postData1() {
        RequestParams params = new RequestParams();
        HttpRequest.getPosBrandTypeAll(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<IntegralMostBean>>() {
                            }.getType());
                    showTitle = new ArrayList<>();
                    for (int i = 0; i < memberList.size() ; i++) {
                        menuList.add(memberList.get(i).getBrandName());
                        showTitle.add(i);
                    }
                    homeList.addAll(memberList.get(0).getPosTypeList());
                    madapter.notifyDataSetChanged();
                    madapter3.notifyDataSetChanged();
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

}
