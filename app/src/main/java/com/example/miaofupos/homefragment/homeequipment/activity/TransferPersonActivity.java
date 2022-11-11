package com.example.miaofupos.homefragment.homeequipment.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.bean.MerMachineBean;
import com.example.miaofupos.homefragment.homeequipment.adapter.TraminalPersonAdapter;
import com.example.miaofupos.homefragment.homeequipment.bean.PersonBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TransferPersonBean;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.IntervalTransferFragment;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.SelectTransferFragment;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.net.Utils;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.MyDialog1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2020/12/21
 * 描述:终端划拨选择人
 */
public class TransferPersonActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView listview;
    private TraminalPersonAdapter mAdapter;
    private List<PersonBean> mData = new ArrayList<>();
    private TextView merchant_transfer_number;
    private LinearLayout iv_back;
    private EditText merchants_person_ed_search;
    private List<TerminalBean> beanList_size = new ArrayList<>();
    private int[] data;  //需要给后台返回pos机value值
    private String search_value = ""; //搜索条件

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.transfer_person_activity;
    }

    @Override
    protected void initView() {
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        merchant_transfer_number = findViewById(R.id.merchant_transfer_number);
        listview = findViewById(R.id.listview);
        iv_back = findViewById(R.id.iv_back);
        merchants_person_ed_search = findViewById(R.id.merchants_person_ed_search);
        search();
        initList();
    }

    /**
     * 配置SwipeRefreshLayout
     * RecyclerView
     * Adapter
     * mData
     */
    private void initList() {
        //下拉样式
        swipe_refresh_layout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        swipe_refresh_layout.setOnRefreshListener(this);
        mAdapter = new TraminalPersonAdapter(R.layout.item_traminal_person, mData);
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(this, listview);
        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        listview.setLayoutManager(new LinearLayoutManager(this));
        listview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showDialog(mData.get(position).getDictLabel(),mData.get(position).getDictValue());
            }
        });
        postData();
    }

    // 提交划拨数据
    public void SubMit(String toId) {
        // 开启加载框
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("merchId", toId);
        params.put("operType", "1");  //1，划拨，2 回调
        HttpRequest.updPosListFrom(params, SPUtils.get(TransferPersonActivity.this, "Token", "-1").toString(), data, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                // 关闭加载框
                loadDialog.dismiss();
                showToast(3, "划拨成功");
                EventBus.getDefault().post(new SelectTransferFragment());
                EventBus.getDefault().post(new IntervalTransferFragment());
                finish();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                // 关闭加载框
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = this.getIntent();
        beanList_size = (List<TerminalBean>) intent.getSerializableExtra("beanList_size");
        data = new int[beanList_size.size()];
        for (int i = 0; i < beanList_size.size(); i++) {
            data[i] = Integer.valueOf(beanList_size.get(i).getPosId());
        }

    }

    @Override
    public void onRefresh() {
        swipe_refresh_layout.setRefreshing(true);
        setRefresh();

        postData();
    }


    private void setRefresh() {
        search_value = "";
        mData.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreRequested() {

    }

    public void postData() {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("nickName", search_value);
        params.put("pageSize", "-1");
        params.put("posCode", "-1");
        HttpRequest.getMerchantsList(params, SPUtils.get(TransferPersonActivity.this, "Token", "-1").toString(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                swipe_refresh_layout.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<PersonBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<PersonBean>>() {
                            }.getType());
                    mData.addAll(memberList);
                    merchant_transfer_number.setText("累计团队:" + mData.size() + "人");
                    mAdapter.loadMoreEnd();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


    private void search() {
        merchants_person_ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    Utils.hideKeyboard(merchants_person_ed_search);
                    Log.e("点击了", "搜索");
                    search_value = v.getText().toString().trim();
                    mData.clear();
                    postData();
                    return true;
                }

                return false;
            }
        });
    }



    // 可划拨弹框
    private void showDialog(String value,String id) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_fragment, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("确定划拨给" + value + "？");
        Dialog dialog = new MyDialog1(this, true, true, (float) 0.7).setNewView(view);
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
                //提交
                dialog.dismiss();
                SubMit(id);
            }
        });
    }
}
