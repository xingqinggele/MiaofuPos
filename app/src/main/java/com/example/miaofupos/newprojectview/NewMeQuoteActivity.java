package com.example.miaofupos.newprojectview;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homemerchants.memerchants.activity.MeMerchantsDetailActivity;
import com.example.miaofupos.homefragment.homequoteactivity.HomeQuoteActivity1;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.net.Utils;
import com.example.miaofupos.newprojectview.adapter.MeQuoDialogGridViewAdapter;
import com.example.miaofupos.newprojectview.adapter.QuoteAdapter;
import com.example.miaofupos.newprojectview.bean.NewMeQuoBean;
import com.example.miaofupos.newprojectview.bean.QuoteBean;
import com.example.miaofupos.newprojectview.editMerchats.EditNewMerchantsActivity;
import com.example.miaofupos.views.MyDialog;
import com.example.miaofupos.views.MyGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2022/8/22
 * 描述:新商户列表
 */
public class NewMeQuoteActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, QuoteAdapter.BindCallback, QuoteAdapter.EditCallback {
    //返回键
    private LinearLayout iv_back;
    private EditText me_merchants_person_ed_search;
    private TextView screening_tv;
    private SwipeRefreshLayout swipe_layout;
    private RecyclerView recycler_view;
    //类型实体类List
    private List<NewMeQuoBean> newMeQuoBeans = new ArrayList<>();
    //类型适配器Adapter
    private MeQuoDialogGridViewAdapter madapter;
    //数据请求页码
    private int mCount = 1;
    // 请求数据数量
    private int pageSize = 20;
    //搜索内容
    private String nickName = "";
    //筛选类型 1失败、2进行中 3成功
    private String auditStatus = "";
    private QuoteAdapter quoteAdapter;
    private List<QuoteBean>mData = new ArrayList<>();
    private TextView me_merchants_list_size;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.new_me_quote_activity;
    }


    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        me_merchants_person_ed_search = findViewById(R.id.me_merchants_person_ed_search);
        screening_tv = findViewById(R.id.screening_tv);
        swipe_layout = findViewById(R.id.swipe_layout);
        recycler_view = findViewById(R.id.recycler_view);
        me_merchants_list_size = findViewById(R.id.me_merchants_list_size);
        initList();
        search();
    }



    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        screening_tv.setOnClickListener(this);
    }



    @Override
    protected void initData() {
        newMeQuoBeans.add(new NewMeQuoBean("","全部"));
        newMeQuoBeans.add(new NewMeQuoBean("3","成功"));
        newMeQuoBeans.add(new NewMeQuoBean("2","进行中"));
        newMeQuoBeans.add(new NewMeQuoBean("1","失败"));
        screening_tv.setText(newMeQuoBeans.get(0).getName());
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.screening_tv:
                //弹出类型选择弹框
                showDialog(newMeQuoBeans);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        swipe_layout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        swipe_layout.setOnRefreshListener(this);
        //adapter配置data
        quoteAdapter = new QuoteAdapter(R.layout.item_new_merchats, mData,this,this);
        //打开加载动画
        quoteAdapter.openLoadAnimation();
        //设置启用加载更多
        quoteAdapter.setEnableLoadMore(true);
        //设置为加载更多监听器
        quoteAdapter.setOnLoadMoreListener(this, recycler_view);
        //数据为空显示xml
        quoteAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        // RecyclerView设置布局管理器
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerView配置adapter
        recycler_view.setAdapter(quoteAdapter);
        //RecyclerView点击事件
        quoteAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击Item事件
                //点击Item事件
                if (mData.get(position).getType().equals("1")) {
                    //点击Item事件
                    Intent intent = new Intent(NewMeQuoteActivity.this, MeMerchantsDetailActivity.class);
                    intent.putExtra("MeMerchants_id", mData.get(position).getMerchCode());
                    startActivity(intent);
                }
            }
        });
        //请求接口
        posData(true);
    }

    /***
     * 筛选
     */
    public void showDialog(List<NewMeQuoBean> list) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.me_quo_type_dialog, null);
        Button data_bill_dialog_btn = view.findViewById(R.id.data_bill_dialog_btn);
        MyGridView data_bill_dialog_grid = view.findViewById(R.id.data_bill_dialog_grid);
        madapter = new MeQuoDialogGridViewAdapter(NewMeQuoteActivity.this, list);
        data_bill_dialog_grid.setAdapter(madapter);
        data_bill_dialog_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //把点击的position传递到adapter里面去
                madapter.changeState(i);
                screening_tv.setText(list.get(i).getName());
                auditStatus = list.get(i).getId();
            }
        });
        Dialog dialog = new MyDialog(NewMeQuoteActivity.this, true, true, (float) 1).setNewView(view);
        dialog.show();
        data_bill_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                // 开始处理类型查询
                onRefresh();
            }
        });
    }

    /**
     * 请求数据
     */
    private void posData(boolean isRefresh){
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("auditStatus", auditStatus);
        params.put("nickName", nickName);
        HttpRequest.postFyMerch(params,getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                swipe_layout.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<QuoteBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<QuoteBean>>() {
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
                    me_merchants_list_size.setText("共" + result.getString("num") + "户");
                    if (memberList.size() < pageSize) {
                        quoteAdapter.loadMoreEnd();
                    } else {
                        quoteAdapter.loadMoreComplete();
                    }
                    //更新adapter
                    quoteAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    @Override
    public void onRefresh() {
        //开启刷新
        swipe_layout.setRefreshing(true);
        //页码设置为1
        mCount = 1;
        //请求接口、填充数据
        posData(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        //页码 n + 1
        mCount = mCount + 1;
        //请求接口、填充数据
        posData(false);
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

    /**
     * 点击绑定
     * @param mcId
     * @param snCode
     */
    @Override
    public void addBind(String mcId, String snCode) {
        Intent intent = new Intent(this, BindingEquipmentActivity.class);
        intent.putExtra("snCode",snCode);
        intent.putExtra("mcId",mcId);
        startActivity(intent);
    }

    /**
     * 点击修改
     * @param id
     * @param type
     */
    @Override
    public void edit(String id, String type,String isSta) {
        if (type.equals("1")){
            startActivity(new Intent(this, AddMerchantsActivity1.class));
        }else {
            Intent intent;
            if (isSta.equals("1")){
                intent = new Intent(this, HomeQuoteActivity1.class);
                intent.putExtra("id",id);
                intent.putExtra("type","2");
                intent.putExtra("bj_type",type);
            }else {
                intent = new Intent(this, EditNewMerchantsActivity.class);
                intent.putExtra("sid",id);
            }
            startActivity(intent);
        }
    }
}