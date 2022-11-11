package com.example.miaofupos.datafragment.databill;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.databill.adapter.DataBillAdapter;
import com.example.miaofupos.datafragment.databill.adapter.DataBillDialogGridViewAdapter;
import com.example.miaofupos.datafragment.databillbean.BillBean;
import com.example.miaofupos.datafragment.databillbean.BillTypeBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.TimeUtils;
import com.example.miaofupos.views.MyDialog;
import com.example.miaofupos.views.MyGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.miaofupos.utils.TimeUtils.getNowTime;


/**
 * 作者: qgl
 * 创建日期：2020/12/14
 * 描述:我的账单
 */
public class DataBillActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //返回键
    private LinearLayout iv_back;
    //时间筛选按钮
    private TextView data_bill_time_tv;
    //类型筛选按钮
    private TextView data_bill_type_tv;
    //刷新控件
    private SwipeRefreshLayout data_bill_swipe_refresh;
    //列表控件
    private RecyclerView data_bill_listview;
    //列表适配器Adapter
    private DataBillAdapter mAdapter;
    //列表实体类List
    private List<BillBean> mData = new ArrayList<>();
    //类型适配器Adapter
    private DataBillDialogGridViewAdapter madapter;
    //页码
    private int mCount = 1;
    //请求数据数量
    private int pageSize = 20;
    //类型实体类List
    private List<BillTypeBean> billTypeBeans = new ArrayList<>();
    //类型实体类
    private BillTypeBean billTypeBean;
    //类型标识
    private String BillTypeValue = "";

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.databillactivity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        data_bill_time_tv = findViewById(R.id.data_bill_time_tv);
        data_bill_type_tv = findViewById(R.id.data_bill_type_tv);
        data_bill_swipe_refresh = findViewById(R.id.data_bill_swipe_refresh);
        data_bill_listview = findViewById(R.id.data_bill_listview);
        //获取当前日期
        data_bill_time_tv.setText(getNowTime("month"));
        //请求获取类型接口
        getPayType();
        //初始化列表Adapter
        initList();
    }

    //点击事件
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        data_bill_time_tv.setOnClickListener(this);
        data_bill_type_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    private void initList() {
        //下拉样式
        data_bill_swipe_refresh.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        data_bill_swipe_refresh.setOnRefreshListener(this);
        mAdapter = new DataBillAdapter(R.layout.item_data_bill, mData);
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, data_bill_listview);
        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        data_bill_listview.setLayoutManager(new LinearLayoutManager(this));
        data_bill_listview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(DataBillActivity.this, DataBillDetailActivity.class);
                intent.putExtra("billId", mData.get(position).getBillId());
                intent.putExtra("billTypeValue", mData.get(position).getBillTypeValue());
                startActivity(intent);
            }
        });
        posDate(true);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        //关闭加载条
        data_bill_swipe_refresh.setRefreshing(true);
        //调用刷新逻辑
        setRefresh();
    }

    //处理刷新逻辑
    private void setRefresh() {
        //页码设置成1
        mCount = 1;
        //请求列表数据接口
        posDate(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回键
            case R.id.iv_back:
                finish();
                break;
            //时间筛选
            case R.id.data_bill_time_tv:
                //弹出时间选择弹框
                selectTime();
                break;
            //类型筛选
            case R.id.data_bill_type_tv:
                //清空类型标识
                BillTypeValue = "";
                //弹出类型选择弹框
                showDialog(billTypeBeans);
                break;
        }
    }

    /**
     * 选择时间
     */
    private void selectTime() {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                data_bill_time_tv.setText(TimeUtils.getTime(date));
                //进行筛选 刷新接口
                onRefresh();
            }
        }).setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();
        pvTime.show();
    }

    /***
     * 选择类型
     */
    public void showDialog(List<BillTypeBean> list) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.data_bill_type_dialog, null);
        Button data_bill_dialog_btn = view.findViewById(R.id.data_bill_dialog_btn);
        MyGridView data_bill_dialog_grid = view.findViewById(R.id.data_bill_dialog_grid);
        madapter = new DataBillDialogGridViewAdapter(DataBillActivity.this, list);
        data_bill_dialog_grid.setAdapter(madapter);
        data_bill_dialog_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //把点击的position传递到adapter里面去
                madapter.changeState(i);
                data_bill_type_tv.setText(list.get(i).getDictLabel());
                BillTypeValue = list.get(i).getDictValue();
            }
        });
        Dialog dialog = new MyDialog(DataBillActivity.this, true, true, (float) 1).setNewView(view);
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

    //上拉加载
    @Override
    public void onLoadMoreRequested() {
        //页码加1
        mCount = mCount + 1;
        //请求接口
        posDate(false);
    }

    //获取账单列表
    public void posDate(boolean isRefresh) {
        RequestParams params = new RequestParams();
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("billTypeValue", BillTypeValue);
        params.put("yearMonth", data_bill_time_tv.getText().toString().trim());
        HttpRequest.getBillList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                data_bill_swipe_refresh.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<BillBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<BillBean>>() {
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
                    //判断返回的数据长度
                    if (memberList.size() < pageSize) {
                        //数据长度小于定义的长度,adapter 加载更多的结束
                        mAdapter.loadMoreEnd();
                    } else {
                        //数据长度小于定义的长度,adapter 继续加载
                        mAdapter.loadMoreComplete();
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                data_bill_swipe_refresh.setRefreshing(false);
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //获取账单类型
    public void getPayType() {
        billTypeBean = new BillTypeBean();
        RequestParams params = new RequestParams();
        HttpRequest.getBillType(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<BillTypeBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<BillTypeBean>>() {
                            }.getType());
                    billTypeBean.setDictLabel("全部");
                    billTypeBean.setDictValue("");
                    memberList.add(billTypeBean);
                    // 重新排序一下
                    Collections.reverse(memberList);

                    billTypeBeans.addAll(memberList);
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
