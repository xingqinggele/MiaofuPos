package com.example.miaofupos.homefragment.homelist;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.adapter.MyViewPageAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.bean.MerMachineBean;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.IntervalTransferFragment;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.SelectTransferFragment1;
import com.example.miaofupos.homefragment.homelist.fragment.ThisMonthFragment;
import com.example.miaofupos.homefragment.homelist.fragment.YesterdayFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者: qgl
 * 创建日期：2020/12/12
 * 描述: 排行榜
 */
public class HomeListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private LinearLayout iv_back;
    //title名称存储
    ArrayList<String> titleDatas = new ArrayList<>();
    //title界面存储
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    //viewpager
    private ViewPager view_pager;
    private TabLayout terminal_transfer_tab_layout;
    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homelistactivity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        view_pager = findViewById(R.id.view_pager);
        terminal_transfer_tab_layout = findViewById(R.id.terminal_transfer_tab_layout);
        initList();
    }
    //点击事件
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        titleDatas.add("昨日推广排行");
        titleDatas.add("本月推广排行");
        fragmentList.add(new YesterdayFragment());
        fragmentList.add(new ThisMonthFragment());
        init();
    }

    private void init() {
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        terminal_transfer_tab_layout.setSelectedTabIndicator(0);
        view_pager.setAdapter(myViewPageAdapter);
        terminal_transfer_tab_layout.setupWithViewPager(view_pager);
        terminal_transfer_tab_layout.setTabsFromPagerAdapter(myViewPageAdapter);
    }
    private void initList() {
//        home_list_swipe_refresh.setOnRefreshListener(this);
//        mAdapter = new HomeListAdapter(R.layout.item_home_list,mData);
//        mAdapter.openLoadAnimation();
//        mAdapter.setEnableLoadMore(false);
//        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
//        home_list_listview.setLayoutManager(new LinearLayoutManager(this));
//        home_list_listview.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//        });
//        postData();

    }


    public void postData(){
//        home_list_swipe_refresh.setRefreshing(false);
////        for (int i = 0;i < 10;i++){
////            bean = new MerMachineBean();
////            bean.setType(i+1+"");
////            mData1.add(bean);
////        }
//        mData.addAll(mData1);
//        mAdapter.loadMoreEnd();
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
//        home_list_swipe_refresh.setRefreshing(true);
//        setRefresh();
//        postData();
    }

    private void setRefresh() {
//        mData1.clear();
//        mData.clear();
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
