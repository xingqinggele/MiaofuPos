package com.example.miaofupos.mefragment.meaddres;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.mefragment.meaddres.adapter.AddressAdapter;
import com.example.miaofupos.mefragment.meaddres.bean.AddressBean;
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
 * 创建：  qgl
 * 时间： 2021-04-27
 * 描述： 我的收货地址
 */
public class MeAddressActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    //返回键
    private LinearLayout iv_back;
    //添加地址
    private LinearLayout below_liner;
    //下拉刷新控件
    private SwipeRefreshLayout address_swipe;
    //列表控件
    private RecyclerView list_view;
    //实体类容器
    private List<AddressBean> beanList = new ArrayList<>();
    //列表适配器
    private AddressAdapter adapter;
    //暂无数据界面
    private RelativeLayout emptyBg;
    // 1 从订单跳转的 2 正常点击进来的
    private String sd = "1";

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.me_address_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        below_liner = findViewById(R.id.below_liner);
        address_swipe = findViewById(R.id.address_swipe);
        list_view = findViewById(R.id.list_view);
        emptyBg = findViewById(R.id.emptyBg);
        initList();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        below_liner.setOnClickListener(this);
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        address_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        address_swipe.setOnRefreshListener(this);
        // 设置RecyclerView使用的布局
        list_view.setLayoutManager(new LinearLayoutManager(this));
        // 设置RecyclerView分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        list_view.addItemDecoration(itemDecoration);
        // 设置RecyclerView适配器
        adapter = new AddressAdapter(this);
        list_view.setAdapter(adapter);
        posData(true, true);
    }

    @Override
    protected void initData() {
        sd = getIntent().getStringExtra("status");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.below_liner:
                //新增收货地址
                Intent intent = new Intent(MeAddressActivity.this, NewAddressActivity.class);
                //type 1 新增 2 修改
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
        }
    }

    //请求
    private void posData(boolean isRefresh, boolean value) {
        RequestParams params = new RequestParams();
        HttpRequest.getAddressList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                if (value) {
                    address_swipe.setRefreshing(false);
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<AddressBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<AddressBean>>() {
                            }.getType());
                    //判断刷新还是加载
                    if (isRefresh) {
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (beanList != null) {
                            beanList.clear();
                        }
                    }
                    if (memberList.size() == 0) {
                        emptyBg.setVisibility(View.VISIBLE);
                    } else {
                        emptyBg.setVisibility(View.GONE);
                    }
                    //在adapter List 中添加 list
                    beanList.addAll(memberList);
                    adapter.setList(beanList);
                    // 通知RecyclerView更新视图
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                if (value) {
                    address_swipe.setRefreshing(false);
                }
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }


    //下拉刷新
    @Override
    public void onRefresh() {
        //调用刷新逻辑
        setRefresh(true);
    }

    //处理刷新逻辑
    private void setRefresh(boolean value) {
        if (value) {
            //开启刷新
            address_swipe.setRefreshing(true);
        }
        //请求接口、填充数据
        posData(true, value);
    }

    //修改默认地址
    public void AddressType(String id) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        HttpRequest.getAddressType(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                setRefresh(false);
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //删除地址
    public void AddressDelete(String id) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        HttpRequest.DeleteAddress(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //删除成功刷新列表
                setRefresh(false);
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //编辑地址
    public void AddressEditor(String id, String name, String phone, String addr, String addrInfo) {
        Intent intent = new Intent(MeAddressActivity.this, NewAddressActivity.class);
        //type 1 新增 2 修改
        intent.putExtra("type", "2");
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("addr", addr);
        intent.putExtra("addrInfo", addrInfo);
        startActivity(intent);
    }
    //点击Item
    public void onClickItem(String id,String address){
        if (sd.equals("1")){
            Intent i = new Intent();
            i.putExtra("id", id);
            i.putExtra("address", address);
            setResult(110, i);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        //返回界面刷新列表
        onRefresh();
        super.onRestart();
    }


}
