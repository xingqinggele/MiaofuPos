package com.example.miaofupos.homefragment.hometeam;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.adapter.ChooseGridViewAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.fragment.MeFragment;
import com.example.miaofupos.homefragment.homemerchants.memerchants.activity.MeMerchantsActivity;
import com.example.miaofupos.homefragment.hometeam.adapter.HomeTeamAdapter;
import com.example.miaofupos.homefragment.hometeam.bean.TeamBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.net.Utils;
import com.example.miaofupos.useractivity.LoginActivity;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.utils.Utility;
import com.example.miaofupos.views.MyDialog1;
import com.example.miaofupos.views.Order1;
import com.example.miaofupos.views.Order2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.example.miaofupos.utils.Convenient_utils.UndTv;

/**
 * 作者: qgl
 * 创建日期：2020/12/16
 * 描述:我的伙伴
 */
public class HomeTeamActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    //下拉刷新控件
    private SwipeRefreshLayout home_team_swipe_refresh;
    //列表ListView
    private RecyclerView home_team_listview;
    //列表Adapter
    private HomeTeamAdapter mAdapter;
    //累计团队
    private TextView merchant_transfer_number;
    //返回键
    private LinearLayout iv_back;
    //搜索框
    private EditText merchants_person_ed_search;
    //实体类集合
    private List<TeamBean> beans = new ArrayList<>();
    //页码
    private int mCount = 1;
    //请求数据数量
    private int pageSize = 20;
    //排序按钮
    private TextView team_sorting;
    //搜索内容
    private String search_value = "";
    //排序标识 1升序2降序
    private String sortTime = "2";

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.home_team_activity;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        home_team_swipe_refresh = findViewById(R.id.home_team_swipe_refresh);
        merchant_transfer_number = findViewById(R.id.merchant_transfer_number);
        home_team_listview = findViewById(R.id.home_team_listview);
        iv_back = findViewById(R.id.iv_back);
        merchants_person_ed_search = findViewById(R.id.merchants_person_ed_search);
        team_sorting = findViewById(R.id.team_sorting);
        search();
        initList();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        team_sorting.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        UndTv(HomeTeamActivity.this, sortTime, team_sorting);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.team_sorting:
                if (sortTime.equals("1")) {
                    sortTime = "2";
                } else {
                    sortTime = "1";
                }
                UndTv(HomeTeamActivity.this, sortTime, team_sorting);
                onRefresh();
                break;
        }
    }

    private void initList() {
        //下拉样式
        home_team_swipe_refresh.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        home_team_swipe_refresh.setOnRefreshListener(this);
        //adapter配置data
        mAdapter = new HomeTeamAdapter(HomeTeamActivity.this, R.layout.item_home_team, beans);
        //打开加载动画
        mAdapter.openLoadAnimation();
        //设置启用加载更多
        mAdapter.setEnableLoadMore(true);
        //设置为加载更多监听器
        mAdapter.setOnLoadMoreListener(this, home_team_listview);
        //数据为空显示xml
        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        //RecyclerView设置布局管理器
        home_team_listview.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerView配置adapter
        home_team_listview.setAdapter(mAdapter);
        //调用回调方法
        mAdapter.setOnAddClickListener(onItemActionClick);
        //请求数据
        postData(true);
    }


    @Override
    public void onRefresh() {
        mCount = 1;
        search_value = "";
        setRefresh();
    }

    public void setRefresh() {
        home_team_swipe_refresh.setRefreshing(true);
        postData(true);
    }


    @Override
    public void onLoadMoreRequested() {
        mCount = mCount + 1;
        postData(false);
    }

    private void search() {
        merchants_person_ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    Utils.hideKeyboard(merchants_person_ed_search);
                    search_value = v.getText().toString().trim();
                    setRefresh();
                    return true;
                }

                return false;
            }
        });
    }

    /***************网络请求****************/
    public void postData(boolean isRefresh) {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("nickName", search_value);
        params.put("sortTime", sortTime + "");
        HttpRequest.updMypartnerList(params, SPUtils.get(HomeTeamActivity.this, "Token", "-1").toString(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                home_team_swipe_refresh.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<TeamBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<TeamBean>>() {
                            }.getType());
                    //判断刷新还是加载
                    if (isRefresh){
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (beans != null){
                            beans.clear();
                        }
                    }
                    //在adapter List 中添加 list
                    beans.addAll(memberList);
                    merchant_transfer_number.setText("共计:" + result.getString("partnerCounts") + "人");
                    if (memberList.size() < pageSize) {
                        mAdapter.loadMoreEnd();
                    } else {
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

    /*******************回调方法*****************/

    HomeTeamAdapter.OnAddClickListener onItemActionClick = new HomeTeamAdapter.OnAddClickListener() {
        @Override
        public void onItemClick(String parnterId,String xyk,String sm) {

            Intent intent = new Intent(HomeTeamActivity.this, HomeTeamRateActivity.class);
            intent.putExtra("parnterId",parnterId);
            intent.putExtra("xyk",xyk);
            intent.putExtra("sm",sm);
            startActivity(intent);
        }
    };

    //更新一下界面
    public void onEventMainThread(HomeTeamActivity ev) {
        onRefresh();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
