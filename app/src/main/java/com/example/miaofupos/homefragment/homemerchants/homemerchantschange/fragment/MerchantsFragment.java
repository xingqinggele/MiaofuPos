package com.example.miaofupos.homefragment.homemerchants.homemerchantschange.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homemerchants.homemerchantschange.adapter.MerchantsAdapter;
import com.example.miaofupos.homefragment.homemerchants.homemerchantsquery.adapter.HomeMerchantsQueryAdapter;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/21
 * 描述: 商户
 */
public class MerchantsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private SwipeRefreshLayout merchants_fragment_swipe;
    private RecyclerView merchants_fragment_recycle;
    private MerchantsAdapter mAdapter;
    //测试数据
    HomeTeamBean bean;
    private List<HomeTeamBean> mData = new ArrayList<>();



    @Override
    protected void initView(View rootView) {
        merchants_fragment_swipe = rootView.findViewById(R.id.merchants_fragment_swipe);
        merchants_fragment_recycle = rootView.findViewById(R.id.merchants_fragment_recycle);
        initList();
    }

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.merchants_fragment;
    }


    private void initList() {
        //下拉样式
        merchants_fragment_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        merchants_fragment_swipe.setOnRefreshListener(this);
        mAdapter = new MerchantsAdapter(R.layout.item_merchants_fragment, mData);
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(this, merchants_fragment_recycle);
        mAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.list_empty, null));
        merchants_fragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        merchants_fragment_recycle.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        postData1();
    }

    public void postData1() {
        merchants_fragment_swipe.setRefreshing(false);
        for (int i = 0; i < 10; i++) {
            bean = new HomeTeamBean();
            bean.setAppUserId(i + "");
            mData.add(bean);
            mAdapter.loadMoreEnd();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        merchants_fragment_swipe.setRefreshing(true);
        mData.clear();
        postData1();
    }

    @Override
    public void onLoadMoreRequested() {

    }
}
