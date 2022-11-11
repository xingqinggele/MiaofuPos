package com.example.miaofupos.homefragment.homemerchants.homemerchantsquery;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.adapter.ChooseGridViewAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homemerchants.homemerchantsquery.adapter.HomeMerchantsQueryAdapter;
import com.example.miaofupos.homefragment.hometeam.adapter.HomeTeamAdapter;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamBean;
import com.example.miaofupos.net.Utils;
import com.example.miaofupos.views.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/16
 * 描述:商户查询
 */
public class HomeMerchantsQueryActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //数据条数
    private TextView merchants_query_number_tv;
    //返回键
    private LinearLayout iv_back;
    //侧滑筛选栏adapter
    private ChooseGridViewAdapter madapter;
    //侧滑筛选栏GridView
    private MyGridView gvTest;
    //搜索框
    private EditText merchants_query_search_ed;
    //刷新控件
    private SwipeRefreshLayout query_refresh_layout;
    //listView
    private RecyclerView query_listview;
    //测试数据
    HomeTeamBean bean;
    private List<HomeTeamBean> mData = new ArrayList<>();
    private HomeMerchantsQueryAdapter adapter;
    private List<String> mList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.home_merchants_query_activity;
    }

    @Override
    protected void initView() {
        merchants_query_number_tv = findViewById(R.id.merchants_query_number_tv);
        iv_back = findViewById(R.id.iv_back);
        merchants_query_search_ed = findViewById(R.id.merchants_query_search_ed);
        gvTest = findViewById(R.id.my_grid1);
        query_refresh_layout = findViewById(R.id.query_refresh_layout);
        query_listview = findViewById(R.id.query_listview);
        initList();
    }

    private void initList() {
        //下拉样式
        query_refresh_layout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        query_refresh_layout.setOnRefreshListener(this);
        adapter = new HomeMerchantsQueryAdapter(R.layout.item_home_merchants_query, mData);
        adapter.openLoadAnimation();
        adapter.setEnableLoadMore(false);
        adapter.setOnLoadMoreListener(this, query_listview);
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        query_listview.setLayoutManager(new LinearLayoutManager(this));
        query_listview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        postData1();
    }

    //点击事件
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        search();
    }

    @Override
    protected void initData() {
        /***模拟数据****/
        mList.add("全部");
        mList.add("传统POS");
        mList.add("电签POS");
//        madapter = new ChooseGridViewAdapter(HomeMerchantsQueryActivity.this, mList);
//        gvTest.setAdapter(madapter);
//        gvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //把点击的position传递到adapter里面去
//                madapter.changeState(i);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /********搜索框**********/
    private void search() {
        merchants_query_search_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    Utils.hideKeyboard(merchants_query_search_ed);
                    Log.e("点击了", "搜索");
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        query_refresh_layout.setRefreshing(true);
        mData.clear();
        postData1();
    }

    @Override
    public void onLoadMoreRequested() {

    }

    public void postData1() {
        query_refresh_layout.setRefreshing(false);
        for (int i = 0; i < 10; i++) {
            bean = new HomeTeamBean();
            bean.setAppUserId(i + "");
            mData.add(bean);
            adapter.loadMoreEnd();
            adapter.notifyDataSetChanged();
        }
        merchants_query_number_tv.setText(mData.size() + "");
    }
}
