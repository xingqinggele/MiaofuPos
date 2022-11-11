package com.example.miaofupos.datafragment.databenefit;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.databenefit.adapter.DataBenefitAdapter;
import com.example.miaofupos.datafragment.databenefit.bean.DataBenefitBean;
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
 * 创建日期：2020/12/26
 * 描述:我的收益
 */
public class DataBenefitActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //返回键
    private LinearLayout iv_back;
    //下拉刷新控件
    private SwipeRefreshLayout data_benefit_swipe;
    //列表控件
    private RecyclerView data_benefit_list_view;
    //Adapter
    private DataBenefitAdapter dataBenefitAdapter;
    //实体类
    private List<DataBenefitBean> mData = new ArrayList<>();
    //数据请求页码
    private int mCount = 1;
    // 请求数据数量
    private int pageSize = 20;

    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.data_benefit_activity;
    }

    //控件初始化
    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        data_benefit_swipe = findViewById(R.id.data_benefit_swipe);
        data_benefit_list_view = findViewById(R.id.data_benefit_list_view);
        initList();
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        data_benefit_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        data_benefit_swipe.setOnRefreshListener(this);
        //adapter配置data
        dataBenefitAdapter = new DataBenefitAdapter(R.layout.item_data_benefit_layout, mData);
        //打开加载动画
        dataBenefitAdapter.openLoadAnimation();
        //设置启用加载更多
        dataBenefitAdapter.setEnableLoadMore(true);
        //设置为加载更多监听器
        dataBenefitAdapter.setOnLoadMoreListener(this, data_benefit_list_view);
        //数据为空显示xml
        dataBenefitAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        // RecyclerView设置布局管理器
        data_benefit_list_view.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerView配置adapter
        data_benefit_list_view.setAdapter(dataBenefitAdapter);
        posData(true);
    }

    //事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    //数据处理
    @Override
    protected void initData() {

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

    //下拉刷新
    @Override
    public void onRefresh() {
        //开启刷新
        data_benefit_swipe.setRefreshing(true);
        //调用刷新逻辑
        setRefresh();

    }

    //处理刷新逻辑
    private void setRefresh() {
        //页码设置为1
        mCount = 1;
        //请求接口、填充数据
        posData(true);
    }

    //上拉加载
    @Override
    public void onLoadMoreRequested() {
        //页码 n + 1
        mCount = mCount + 1;
        //请求接口、填充数据
        posData(false);
    }

    //获取接口数据
    public void posData(boolean isRefresh) {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        HttpRequest.getEarnings(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                data_benefit_swipe.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<DataBenefitBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<DataBenefitBean>>() {
                            }.getType());
                    //判断刷新还是加载
                    if (isRefresh) {
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (mData != null) {
                            mData.clear();
                        }
                    }
                    mData.addAll(memberList);
                    if (memberList.size() < pageSize) {
                        dataBenefitAdapter.loadMoreEnd();
                    } else {
                        dataBenefitAdapter.loadMoreComplete();
                    }
                    dataBenefitAdapter.notifyDataSetChanged();

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
