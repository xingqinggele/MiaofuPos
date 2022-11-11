package com.example.miaofupos.mefragment.meorder.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.allenliu.versionchecklib.core.http.HttpParams;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homeequipment.bean.CallbackRecordBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalEvenBusBean;
import com.example.miaofupos.homefragment.homeequipment.fragment.transfer.SelectTransferFragment;
import com.example.miaofupos.homefragment.homemessage.HomeMessageDetailsActivity;
import com.example.miaofupos.homefragment.homemessage.adapter.BusinessMessageAdapter;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean1;
import com.example.miaofupos.mefragment.meorder.MeOrderDetailActivity;
import com.example.miaofupos.mefragment.meorder.adapter.MeExchangeAdapter;
import com.example.miaofupos.mefragment.meorder.adapter.MeExchangeItemAdapter;
import com.example.miaofupos.mefragment.meorder.bean.MeExchangeBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.MyListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2021/2/20
 * 描述:我的兑换
 */
public class MeExchangeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //刷新控件
    private SwipeRefreshLayout me_exchange_swipe;
    //列表控件
    private RecyclerView me_exchange_list_view;
    //数据请求页码
    private int mCount = 1;
    // 请求数据数量
    private int pageSize = 20;
    //Adapter
    private MeExchangeAdapter meExchangeAdapter;
    //实体类
    private List<MeExchangeBean> mData = new ArrayList<>();
    //前台传值
    private String status;

    //xml界面
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.meexchange_fragment;
    }

    //初始化控件
    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
        me_exchange_swipe = rootView.findViewById(R.id.me_exchange_swipe);
        me_exchange_list_view = rootView.findViewById(R.id.me_exchange_list_view);

        initList();
    }

    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        me_exchange_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        me_exchange_swipe.setOnRefreshListener(this);
        //adapter配置data
        meExchangeAdapter = new MeExchangeAdapter(R.layout.item_me_exchange, mData,getActivity());
        //打开加载动画
        meExchangeAdapter.openLoadAnimation();
        //设置启用加载更多
        meExchangeAdapter.setEnableLoadMore(true);
        //设置为加载更多监听器
        meExchangeAdapter.setOnLoadMoreListener(this, me_exchange_list_view);
        //数据为空显示xml
        meExchangeAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.list_empty, null));
        // RecyclerView设置布局管理器
        me_exchange_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        //RecyclerView配置adapter
        me_exchange_list_view.setAdapter(meExchangeAdapter);
        //RecyclerView点击事件
        meExchangeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击Item事件
                Intent intent = new Intent(getActivity(), MeOrderDetailActivity.class);
                intent.putExtra("message_id", mData.get(position).getId());
                intent.putExtra("pageCode", "1"); // 我的申请不需要划拨机器功能
                startActivity(intent);
            }
        });
        initData(true);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        //开启刷新
        me_exchange_swipe.setRefreshing(true);
        //调用刷新逻辑
        setRefresh();

    }

    //处理刷新逻辑
    private void setRefresh() {
        //页码设置为1
        mCount = 1;
        //请求接口、填充数据
        initData(true);
    }

    //上拉加载
    @Override
    public void onLoadMoreRequested() {
        //页码 n + 1
        mCount = mCount + 1;
        //请求接口、填充数据
        initData(false);
    }

    //请求数据、添加到数据容器、实体类
    public void initData(boolean isRefresh) {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("orderType", "1"); // 1我的兑换 2兑换申请
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("status", status + "");
        HttpRequest.getOrderList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                me_exchange_swipe.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<MeExchangeBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<MeExchangeBean>>() {
                            }.getType());
                    //判断刷新还是加载
                    if (isRefresh) {
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (mData != null) {
                            mData.clear();
                        }
                    }
                    mData.addAll(memberList);
                    if (memberList.size() < pageSize) {
                        meExchangeAdapter.loadMoreEnd();
                    } else {
                        meExchangeAdapter.loadMoreComplete();
                    }
                    meExchangeAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                me_exchange_swipe.setRefreshing(false);
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    public void onEventMainThread(MeExchangeFragment ev) {
        shouLog("(MeExchangeFragment)返回刷新---->", "GO");
        onRefresh();
    }

    public void onEventMainThread(TerminalEvenBusBean bean) {
        shouLog("(MeExchangeFragment)点击筛选---->", "GO");
        status = bean.getMostType();
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
