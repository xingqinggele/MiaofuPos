package com.example.miaofupos.datafragment.datapersonalresults.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.datafragment.datapersonalresults.adapter.DataTradingVolumeAdapter;
import com.example.miaofupos.datafragment.datapersonalresults.bean.DataPersonBean;
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
 * 创建日期：2020/12/25
 * 描述:总日交易量
 */
public class DataDayTotalFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //下拉刷新控件
    private SwipeRefreshLayout data_day_results_swipe;
    //列表控件
    private RecyclerView data_day_results_list_view;
    //Adapter
    private DataTradingVolumeAdapter mAdapter;
    //实体类
    private List<DataPersonBean> mData = new ArrayList<>();
    //页码
    private int mCount = 1;
    // 请求数据数量
    private int pageSize = 2;
    //xml界面
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.data_day_results_activity;
    }

    //初始化控件
    @Override
    protected void initView(View rootView) {
        data_day_results_swipe = rootView.findViewById(R.id.data_day_results_swipe);
        data_day_results_list_view = rootView.findViewById(R.id.data_day_results_list_view);
        initList();
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        data_day_results_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        data_day_results_swipe.setOnRefreshListener(this);
        //adapter配置data
        mAdapter = new DataTradingVolumeAdapter(R.layout.item_dataday_results_list_view, mData);
        //打开加载动画
        mAdapter.openLoadAnimation();
        //设置启用加载更多
        mAdapter.setEnableLoadMore(true);
        //设置为加载更多监听器
        mAdapter.setOnLoadMoreListener(this, data_day_results_list_view);
        //数据为空显示xml
        mAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.list_empty, null));
        // RecyclerView设置布局管理器
        data_day_results_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        //RecyclerView配置adapter
        data_day_results_list_view.setAdapter(mAdapter);
        posData(false);
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        //开启刷新
        data_day_results_swipe.setRefreshing(true);
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
        mCount = mCount + 1;
        //请求接口、填充数据
        posData(false);
    }
    //获取接口数据
    public void posData(boolean isRefresh){
        //进行网络请求数据
        RequestParams params = new RequestParams();
        //页码
        params.put("pageNo", mCount + "");
        //页长度
        params.put("pageSize", pageSize + "");
        //1 日交易量 2 月交易量
        params.put("dimeType", "1");
        HttpRequest.getTotalAmount(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //关闭加载框
                data_day_results_swipe.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    //String 转 JSONObject
                    JSONObject result = new JSONObject(responseObj.toString());
                    //List<实体类>list 填充数据
                    List<DataPersonBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<DataPersonBean>>() {
                            }.getType());
                    //判断刷新还是加载
                    if (isRefresh){
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (mData != null){
                            mData.clear();
                        }
                    }
                    //在adapter List 中添加 list
                    mData.addAll(memberList);
                    //判断返回的数据长度
                    if (memberList.size() < pageSize) {
                        //数据长度小于定义的长度,adapter 加载更多的结束
                        mAdapter.loadMoreEnd();
                    } else {
                        //数据长度小于定义的长度,adapter 继续加载
                        mAdapter.loadMoreComplete();
                    }
                    //更新adapter
                    mAdapter.notifyDataSetChanged();
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
