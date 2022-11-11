package com.example.miaofupos.homefragment.homemerchants.memerchants.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homemerchants.memerchants.adapter.MeMerchantsAdapter;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.MeMerchantsBean;
import com.example.miaofupos.homefragment.homequoteactivity.HomeQuoteActivity1;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.net.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.miaofupos.utils.Convenient_utils.UndTv;

/**
 * 作者: qgl
 * 创建日期：2021/3/5
 * 描述:我的商户
 */
public class MeMerchantsActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, MeMerchantsAdapter.FoodActionCallback {
    //返回键
    private LinearLayout iv_back;
    //实体类List
    private List<MeMerchantsBean> beanList = new ArrayList<>();
    //下拉刷新控件
    private SwipeRefreshLayout me_merchants_swipe;
    //列表控件
    private RecyclerView me_merchants_list_view;
    //adapter
    private MeMerchantsAdapter meMerchantsAdapter;
    //数据请求页码
    private int mCount = 1;
    // 请求数据数量
    private int pageSize = 20;
    //数据长度、总数量
    private TextView me_merchants_list_size;
    //交易额排序按钮
    private TextView me_merchants_turnover;
    //交易额默认值 1.升序 2.降序
    private String turnover = "2";
    //搜索框 EditText
    private EditText me_merchants_person_ed_search;
    //搜索内容
    private String nickName = "";

    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.memerchants_activity;
    }

    //控件初始化
    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        me_merchants_swipe = findViewById(R.id.me_merchants_swipe);
        me_merchants_list_view = findViewById(R.id.me_merchants_list_view);
        me_merchants_list_size = findViewById(R.id.me_merchants_list_size);
        me_merchants_turnover = findViewById(R.id.me_merchants_turnover);
        me_merchants_person_ed_search = findViewById(R.id.me_merchants_person_ed_search);
        initList();
        search();
    }

    //事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        me_merchants_turnover.setOnClickListener(this);
    }

    //数据处理
    @Override
    protected void initData() {
        //调用判断条件显示方法
        UndTv(MeMerchantsActivity.this, turnover, me_merchants_turnover);
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        me_merchants_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        me_merchants_swipe.setOnRefreshListener(this);
        //adapter配置data
        meMerchantsAdapter = new MeMerchantsAdapter(R.layout.item_me_merchats, beanList,this);
        //打开加载动画
        meMerchantsAdapter.openLoadAnimation();
        //设置启用加载更多
        meMerchantsAdapter.setEnableLoadMore(true);
        //设置为加载更多监听器
        meMerchantsAdapter.setOnLoadMoreListener(this, me_merchants_list_view);
        //数据为空显示xml
        meMerchantsAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        // RecyclerView设置布局管理器
        me_merchants_list_view.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerView配置adapter
        me_merchants_list_view.setAdapter(meMerchantsAdapter);
        //RecyclerView点击事件
        meMerchantsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (beanList.get(position).getIsAudit().equals("3")) {
                    //点击Item事件
                    Intent intent = new Intent(MeMerchantsActivity.this, MeMerchantsDetailActivity.class);
                    intent.putExtra("MeMerchants_id", beanList.get(position).getMerchCode());
                    startActivity(intent);
                }
            }
        });
        //请求接口
        posDate(true);
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回键
            case R.id.iv_back:
                finish();
                break;
            //交易额排序
            case R.id.me_merchants_turnover:
                if (turnover.equals("1")) {
                    turnover = "2";
                } else {
                    turnover = "1";
                }
                UndTv(MeMerchantsActivity.this, turnover, me_merchants_turnover);
                onRefresh();
                break;
        }
    }

    //接口获取数据
    public void posDate(boolean isRefresh) {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("orderly", turnover);
        params.put("nickName", nickName);
        HttpRequest.getMeMerchants_list(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //关闭加载条
                me_merchants_swipe.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<MeMerchantsBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<MeMerchantsBean>>() {
                            }.getType());
                    //判断刷新还是加载
                    if (isRefresh){
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (beanList != null){
                            beanList.clear();
                        }
                    }
                    //在adapter List 中添加 list
                    beanList.addAll(memberList);
                    me_merchants_list_size.setText("共" + result.getString("num") + "户");
                    if (memberList.size() < pageSize) {
                        meMerchantsAdapter.loadMoreEnd();
                    } else {
                        meMerchantsAdapter.loadMoreComplete();
                    }
                    //更新adapter
                    meMerchantsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                //关闭加载条
                me_merchants_swipe.setRefreshing(false);
                Failuer(failuer.getEcode(),failuer.getEmsg());
            }
        });
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        //开启刷新
        me_merchants_swipe.setRefreshing(true);
        //调用刷新逻辑
        setRefresh();
        //请求接口、填充数据
        posDate(true);
    }

    //上拉加载
    @Override
    public void onLoadMoreRequested() {
        //页码 n + 1
        mCount = mCount + 1;
        //请求接口、填充数据
        posDate(false);
    }

    //处理刷新逻辑
    private void setRefresh() {
        //页码设置为1
        mCount = 1;
    }

    //搜索框
    private void search() {
        me_merchants_person_ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    Utils.hideKeyboard(me_merchants_person_ed_search);
                    //输入内容赋给搜索内容
                    nickName = v.getText().toString().trim();
                    //访问接口
                    onRefresh();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //开启刷新
        me_merchants_swipe.setRefreshing(true);
        //调用刷新逻辑
        setRefresh();
        //请求接口、填充数据
        posDate(true);
    }

    @Override
    public void addAction(String id,String type) {
        Intent intent = new Intent(this, HomeQuoteActivity1.class);
        intent.putExtra("id",id);
        intent.putExtra("type","2");
        intent.putExtra("bj_type",type);
        startActivity(intent);
    }
}
