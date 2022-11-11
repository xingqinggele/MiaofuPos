package com.example.miaofupos.homefragment.homemerchants.memerchants.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralAllBean;
import com.example.miaofupos.homefragment.homemerchants.memerchants.adapter.MerchatsDetailAdapter;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.MerchantsDetailBean;
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
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/3/10
 * 描述:我的商户详情页
 */
public class MeMerchantsDetailActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    //列表ID
    private String MeMerchants_ID = "";
    //返回键
    private LinearLayout iv_back;
    //商户姓名
    private TextView me_merchants_detail_name;
    //入网时间
    private TextView me_merchants_detail_time;
    //商户编号
    private TextView me_merchants_detail_number;
    //商户名称
    private String merchantName = "";
    //当前界面
    public static MeMerchantsDetailActivity instance = null;
    private SwipeRefreshLayout merchants_detail_swipe;
    private RecyclerView merchants_detail_recycle;
    private MerchatsDetailAdapter merchatsDetailAdapter;
    private List<MerchantsDetailBean>merchantsDetailBeans = new ArrayList<>();

    //xml界面
    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.memerchants_detail_activity;
    }

    //控件初始化
    @Override
    protected void initView() {
        instance = this;
        iv_back = findViewById(R.id.iv_back);
        me_merchants_detail_name = findViewById(R.id.me_merchants_detail_name);
        me_merchants_detail_time = findViewById(R.id.me_merchants_detail_time);
        me_merchants_detail_number = findViewById(R.id.me_merchants_detail_number);
        merchants_detail_swipe = findViewById(R.id.merchants_detail_swipe);
        merchants_detail_recycle = findViewById(R.id.merchants_detail_recycle);
    }

    //事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    //数据处理
    @Override
    protected void initData() {
        //接受列表页传递的列表ID
        MeMerchants_ID = getIntent().getStringExtra("MeMerchants_id");
        initList();
    }

    //请求接口-->设备信息
    public void posData() {
        RequestParams params = new RequestParams();
        params.put("merchantNo", MeMerchants_ID);
        HttpRequest.getQueryMyCommercialTenant(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                merchants_detail_swipe.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    merchantName = result.getJSONObject("data").getString("merchName");
                    //显示商户姓名
                    me_merchants_detail_name.setText("姓名:"+merchantName);
                    me_merchants_detail_time.setText("入网时间:"+result.getJSONObject("data").getString("activeTime"));
                    me_merchants_detail_number.setText("商户编号:"+MeMerchants_ID);
                    List<MerchantsDetailBean> mList = gson.fromJson(result.getJSONArray("list").toString(),
                            new TypeToken<List<MerchantsDetailBean>>() {
                            }.getType());
                    merchantsDetailBeans.addAll(mList);
                    merchatsDetailAdapter.loadMoreEnd();
                    merchatsDetailAdapter.notifyDataSetChanged();
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

    //点击事件触发
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        merchants_detail_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        merchants_detail_swipe.setOnRefreshListener(this);
        //adapter配置data
        merchatsDetailAdapter = new MerchatsDetailAdapter(R.layout.merchants_detail_list_item, merchantsDetailBeans);
        //打开加载动画
        merchatsDetailAdapter.openLoadAnimation();
        //设置启用加载更多
        merchatsDetailAdapter.setEnableLoadMore(false);
        //设置为加载更多监听器
        //merchatsDetailAdapter.setOnLoadMoreListener(this, merchants_detail_recycle);
        //数据为空显示xml
        merchatsDetailAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        // RecyclerView设置布局管理器
        merchants_detail_recycle.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerView配置adapter
        merchants_detail_recycle.setAdapter(merchatsDetailAdapter);
        posData();
    }

    @Override
    public void onRefresh() {
        //开启刷新
        merchants_detail_swipe.setRefreshing(true);
        //调用刷新逻辑
        setRefresh();
    }

    //处理刷新逻辑
    private void setRefresh() {
        posData();
    }

}
