package com.example.miaofupos.homefragment.homemerchants.homemerchantschange.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homemerchants.homemerchantschange.adapter.MerchantsProgressAdapter;
import com.example.miaofupos.homefragment.homemerchants.homemerchantschange.bean.MyEventBean;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2020/12/21
 * 描述:商户查询进度
 */
public class MerchantsProgressFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private SwipeRefreshLayout merchats_progress_swipe;
    private RecyclerView merchats_progress_recycle;
    private MerchantsProgressAdapter mpAdapter;
    //测试数据
    HomeTeamBean bean;
    private List<HomeTeamBean> mData = new ArrayList<>();

    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
        merchats_progress_swipe = rootView.findViewById(R.id.merchats_progress_swipe);
        merchats_progress_recycle = rootView.findViewById(R.id.merchats_progress_recycle);
        initList();

    }

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.merchants_progress_fragment;
    }

    private void initList() {
        //下拉样式
        merchats_progress_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        merchats_progress_swipe.setOnRefreshListener(this);
        mpAdapter = new MerchantsProgressAdapter(R.layout.item_merchants_progress_fragment, mData);
        mpAdapter.openLoadAnimation();
        mpAdapter.setEnableLoadMore(false);
        mpAdapter.setOnLoadMoreListener(this, merchats_progress_recycle);
        mpAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.list_empty, null));
        merchats_progress_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        merchats_progress_recycle.setAdapter(mpAdapter);
        mpAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        postData1();
    }

    public void postData1() {
        merchats_progress_swipe.setRefreshing(false);
        for (int i = 0; i < 10; i++) {
            bean = new HomeTeamBean();
            bean.setAppUserId(i + "");
            mData.add(bean);
            mpAdapter.loadMoreEnd();
            mpAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        merchats_progress_swipe.setRefreshing(true);
        mData.clear();
        postData1();
    }

    @Override
    public void onLoadMoreRequested() {

    }
    //接受Activity 传来的筛选事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MyEventBean event) {
        String msg = "返回数据:" + event.getSelect();
      shouLog("aa",msg);
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
