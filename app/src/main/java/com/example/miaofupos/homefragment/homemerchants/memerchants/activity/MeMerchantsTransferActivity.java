package com.example.miaofupos.homefragment.homemerchants.memerchants.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.databenefit.adapter.DataBenefitAdapter;
import com.example.miaofupos.datafragment.databenefit.bean.DataBenefitBean;
import com.example.miaofupos.homefragment.homeequipment.activity.TransferPersonActivity;
import com.example.miaofupos.homefragment.homemerchants.memerchants.adapter.MeMerchantsTransferAdapter;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.MeMerchantsTransferBean;
import com.example.miaofupos.homefragment.homewallet.activity.WithdrawalActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.MyDialog1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/3/27
 * 描述:商户转移
 */
public class MeMerchantsTransferActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    //商户姓名
    private String merchantName = "";
    //设备编号
    private String snCode = "";
    //上拉刷新控件
    private SwipeRefreshLayout swipe_layout;
    //列表控件
    private RecyclerView list_view;
    //数据容器
    private List<MeMerchantsTransferBean> mData = new ArrayList<>();
    //适配器Adapter
    private MeMerchantsTransferAdapter mAdapter;
    //返回键
    private LinearLayout iv_back;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.me_merchants_transfer_activity;
    }

    @Override
    protected void initView() {
        swipe_layout = findViewById(R.id.swipe_layout);
        list_view = findViewById(R.id.list_view);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        merchantName = getIntent().getStringExtra("merchantName");
        snCode = getIntent().getStringExtra("snCode");
        initList();
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        swipe_layout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        swipe_layout.setOnRefreshListener(this);
        //adapter配置data
        mAdapter = new MeMerchantsTransferAdapter(R.layout.item_me_merchants_trantsfer, mData);
        //设置启用加载更多
        mAdapter.setEnableLoadMore(false);
        //设置为加载更多监听器
        mAdapter.setOnLoadMoreListener(this, list_view);
        //数据为空显示xml
        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty1, null));
        // RecyclerView设置布局管理器
        list_view.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerView配置adapter
        list_view.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                shouLog("aa","111111111111");
                merDialog(mData.get(position).getNickName(),mData.get(position).getMerchId());
            }
        });
        posData();
    }

    /**
     * 请求可转移的伙伴
     */
    private void posData() {
        RequestParams params = new RequestParams();
        params.put("nickName", merchantName);
        HttpRequest.getTransferList(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                swipe_layout.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<MeMerchantsTransferBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<MeMerchantsTransferBean>>() {
                            }.getType());
                    mData.addAll(memberList);
                    mAdapter.loadMoreEnd();
                    mAdapter.notifyDataSetChanged();
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
        swipe_layout.setRefreshing(true);
        mData.clear();
        posData();
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void merDialog(String name,String id){
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_me_merchants_transfer, null);
        TextView textView = view.findViewById(R.id.dialog_tv2);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText(name);
        Dialog dialog = new MyDialog1(MeMerchantsTransferActivity.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //提交
                subData(id,snCode);
            }
        });
    }

    private void subData(String merId,String snCode){
        RequestParams params = new RequestParams();
        params.put("merchId",merId);
        params.put("posCode",snCode);
        HttpRequest.getTransferMerchants(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    showToast(3,result.getString("data"));
                    MeMerchantsDetailActivity.instance.finish();
                    finish();
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
