package com.example.miaofupos.homefragment.homeequipment.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.adapter.ChooserListAdapter;
import com.example.miaofupos.homefragment.homeequipment.bean.CallbackEvenBusBean;
import com.example.miaofupos.homefragment.homeequipment.bean.CallbackEvenBusBean1;
import com.example.miaofupos.homefragment.homeequipment.bean.ScreeningBean;
import com.example.miaofupos.homefragment.homeequipment.fragment.callback.CallIntervalFragment;
import com.example.miaofupos.homefragment.homeequipment.fragment.callback.CallSelectFragment;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
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
 * 创建日期：2020/12/23
 * 描述:终端回调
 */
public class TransferCallbackActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout my_tablayout;
    private ViewPager my_viewpager;
    private LinearLayout iv_back;
    ArrayList<String> titleDatas = new ArrayList<>();
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private DrawerLayout drawer_layout;
    private List<String> mList = new ArrayList<>();

    //筛选Bean
    private ScreeningBean.DictData dictData ;
    private String terminalType = ""; // 终端类型
    private String mostType = "";  // 极具类型
    //侧滑筛选栏adapter
    private ChooserListAdapter madapter;
    //侧滑筛选栏GridView
    private ListView gvTest;
    // 点击侧滑的Fragment 1.选择划拨,2.区间划拨
    private int fragmentCode = 1;
    private CallbackEvenBusBean busBean;
    private CallbackEvenBusBean1 busBean1;
    private Button footer_item_setting;
    private Button footer_item_out;
    //上一个界面接受的回调者的ID
    private String UserId = "";
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.transfer_callback_activity;
    }

    @Override
    protected void initView() {
        my_tablayout = findViewById(R.id.my_tablayout);
        my_viewpager = findViewById(R.id.my_viewpager);
        iv_back = findViewById(R.id.iv_back);
        drawer_layout = findViewById(R.id.drawer_layout);
        gvTest = findViewById(R.id.merchant_query_list_view);
        footer_item_setting = findViewById(R.id.footer_item_setting);
        footer_item_out = findViewById(R.id.footer_item_out);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        footer_item_setting.setOnClickListener(this);
        footer_item_out.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        UserId = getIntent().getStringExtra("id");
        titleDatas.add("选择回调");
//        titleDatas.add("区间回调");
        fragmentList.add(new CallSelectFragment().newInstance(UserId));
//        fragmentList.add(new CallIntervalFragment());
        init();
        postData1();
    }

    private void init() {
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        my_tablayout.setSelectedTabIndicator(0);
        my_viewpager.setAdapter(myViewPageAdapter);
        my_tablayout.setupWithViewPager(my_viewpager);
        my_tablayout.setTabsFromPagerAdapter(myViewPageAdapter);
    }

    public void setListSize(int value) {
        fragmentCode = value;
        if (fragmentCode == 2){
            // 重置
            terminalType = "";
            mostType = "";
            madapter.newadd();
        }
        drawer_layout.openDrawer(GravityCompat.END);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.footer_item_setting:
                // 重置
                terminalType = "";
                mostType = "";
                madapter.newadd();
                break;
            case R.id.footer_item_out:
                drawer_layout.closeDrawer(GravityCompat.END);
                // 选择划拨
                busBean = new CallbackEvenBusBean();
                busBean.setTerminalType(terminalType);
                busBean.setMostType(mostType);
                //区间划拨
                busBean1 = new CallbackEvenBusBean1();
                busBean1.setTerminalType(terminalType);
                busBean1.setMostType(mostType);
                if (fragmentCode == 1){
                    EventBus.getDefault().post(busBean);
                }else {
                    EventBus.getDefault().post(busBean1);
                }
                // 点击了确认
                break;
        }
    }

    // 获取筛选数据
    public void postData1() {
        RequestParams params = new RequestParams();
        HttpRequest.getConditions(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<ScreeningBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<ScreeningBean>>() {
                            }.getType());
                    // 每一条筛选添加手动添加一条"全部" 数据
                    for (int i = 0; i < memberList.size(); i++) {
                        for (int j = 0; j < memberList.get(i).getDictData().size(); j++) {
                            dictData = new ScreeningBean.DictData();
                            dictData.setDictLabel("全部");
                            dictData.setDictType(memberList.get(i).getDictData().get(j).getDictType());
                            dictData.setDictValue("");
                        }
                        memberList.get(i).getDictData().add(dictData);
                        // 重新排序
                        Collections.reverse(memberList.get(i).getDictData());
                    }
                    madapter = new ChooserListAdapter(memberList, TransferCallbackActivity.this);
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
    ChooserListAdapter.OnAddClickListener onItemActionClick = new ChooserListAdapter.OnAddClickListener() {
        @Override
        public void onItemClick(String add, String position) {
            if (add.equals("pos_type")) {
                terminalType = position;
            } else if (add.equals("pos_machine_type")) {
                mostType = position;
            }

        }
    };
}
