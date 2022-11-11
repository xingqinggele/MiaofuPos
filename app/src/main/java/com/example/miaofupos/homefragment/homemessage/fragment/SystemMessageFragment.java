package com.example.miaofupos.homefragment.homemessage.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homemessage.HomeMessageDetailsActivity;
import com.example.miaofupos.homefragment.homemessage.adapter.BusinessMessageAdapter;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean1;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/17
 * 描述:系统消息
 */
public class SystemMessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private BusinessMessageAdapter systemMeAdapter;
    private SwipeRefreshLayout system_message_swipe;
    private RecyclerView system_message_list_view;
    private List<BusinessMessageBean1> mData = new ArrayList<>();
    private int mCount = 1;

    @Override
    protected void initView(View rootView) {
        system_message_swipe = rootView.findViewById(R.id.system_message_swipe);
        system_message_list_view = rootView.findViewById(R.id.system_message_list_view);
        initList();
        initData();
    }

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.systemmessage_fragment;
    }

    public void initList() {
        //下拉样式
        system_message_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        system_message_swipe.setOnRefreshListener(this);
        systemMeAdapter = new BusinessMessageAdapter(R.layout.item_system_message, mData);
        systemMeAdapter.openLoadAnimation();
        systemMeAdapter.setEnableLoadMore(true);
        systemMeAdapter.setOnLoadMoreListener(this, system_message_list_view);
        systemMeAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.list_messge_empty, null));
        system_message_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        system_message_list_view.setAdapter(systemMeAdapter);
        systemMeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击Item事件
                Intent intent = new Intent(getActivity(), HomeMessageDetailsActivity.class);
                intent.putExtra("message_id", mData.get(position).getId());
                startActivity(intent);
            }
        });

    }

    public void initData() {
        system_message_swipe.setRefreshing(false);
        systemMeAdapter.loadMoreEnd();
    }

    @Override
    public void onRefresh() {
        system_message_swipe.setRefreshing(true);
        setRefresh();
        initData();
    }

    private void setRefresh() {
        mData.clear();
        mCount = 1;
    }

    @Override
    public void onLoadMoreRequested() {
        mCount = mCount + 1;
        initData();
    }
}
