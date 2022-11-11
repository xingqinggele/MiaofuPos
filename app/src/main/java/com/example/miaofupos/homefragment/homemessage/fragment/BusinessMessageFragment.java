package com.example.miaofupos.homefragment.homemessage.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homemessage.HomeMessageDetailsActivity;
import com.example.miaofupos.homefragment.homemessage.HomeMessageDetailsBusActivity;
import com.example.miaofupos.homefragment.homemessage.adapter.BusinessNewsAdapter;
import com.example.miaofupos.homefragment.homemessage.bean.BusMessageBean;
import com.example.miaofupos.mefragment.meorder.MeOrderDetailActivity;
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

import static com.example.miaofupos.utils.TimeUtils.compareDate;
import static com.example.miaofupos.utils.TimeUtils.getDateToString6;

/**
 * 作者: qgl
 * 创建日期：2020/12/17
 * 描述:业务消息
 */
public class BusinessMessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //业务消息Adapter
    private BusinessNewsAdapter systemMeAdapter;
    //下拉刷新Layout
    private SwipeRefreshLayout business_message_swipe;
    //RecyclerView
    private RecyclerView business_message_list_view;
    // 消息列表数据容器
    private List<BusMessageBean> mData = new ArrayList<>();
    //请求页数
    private int mCount = 1;
    // 请求数据数量
    private int pageSize = 20;

    //xml界面
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.business_message_fragment;
    }

    //初始化控件
    @Override
    protected void initView(View rootView) {
        business_message_swipe = rootView.findViewById(R.id.business_message_swipe);
        business_message_list_view = rootView.findViewById(R.id.business_message_list_view);
        //适配列表、刷新控件、adapter
        initList();
    }


    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        business_message_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        business_message_swipe.setOnRefreshListener(this);
        //adapter配置data
        systemMeAdapter = new BusinessNewsAdapter(R.layout.item_business_message, mData);
        //打开加载动画
        systemMeAdapter.openLoadAnimation();
        //设置启用加载更多
        systemMeAdapter.setEnableLoadMore(true);
        //设置为加载更多监听器
        systemMeAdapter.setOnLoadMoreListener(this, business_message_list_view);
        //数据为空显示xml
        systemMeAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.list_messge_empty, null));
        //RecyclerView设置布局管理器
        business_message_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        //RecyclerView配置adapter
        business_message_list_view.setAdapter(systemMeAdapter);
        //RecyclerView点击事件
        systemMeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!compareDate(mData.get(position).getCreateTime(), "2021-03-28 00:00:00")) {
                    //判断返回类型 跳转不一样的详情页 1.入库 2.极具激活 3.预约提现成功 4.提现成功  5.提现失败 6.兑换申请 7.兑换 8.返积分
                    Intent intent = new Intent(getActivity(), HomeMessageDetailsActivity.class);
                    Intent intent1 = new Intent(getActivity(), MeOrderDetailActivity.class);
                    Intent intent2 = new Intent(getActivity(), HomeMessageDetailsBusActivity.class);
                    switch (mData.get(position).getMsgType()) {
                        //入库
                        case "1":
                            //极具激活
                        case "2":
                            //提现成功
                        case "4":
                            //提现失败
                        case "5":
                            //返积分
                        case "8":
                            intent.putExtra("message_id", mData.get(position).getMsgId());
                            intent.putExtra("msgType", mData.get(position).getMsgType());
                            startActivity(intent);
                            break;

                        //预约提现成功
                        case "3":
                            intent2.putExtra("msg_content", mData.get(position).getMsgContent());
                            intent2.putExtra("msg_time", mData.get(position).getCreateTime());
                            intent2.putExtra("msgType", "3");
                            startActivity(intent2);
                            break;

                        //兑换申请
                        case "6":
                            intent1.putExtra("message_id", mData.get(position).getOrderId());
                            intent1.putExtra("pageCode", "2"); // 别人的申请需要划拨机器功能
                            startActivity(intent1);
                            break;
                        //兑换
                        case "7":
//                        intent1.putExtra("message_id", mData.get(position).getMsgId());
//                        intent1.putExtra("pageCode", "1");
//                        startActivity(intent1);
                            break;
                        case "9":
                            break;
                        default:
                            break;
                    }

                }
            }
        });
        //获取列表数据
        getDate(true);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        //开启刷新
        business_message_swipe.setRefreshing(true);
        //调用刷新逻辑
        setRefresh();
    }

    //处理刷新逻辑
    private void setRefresh() {
        //页码设置为1
        mCount = 1;
        //请求接口、填充数据
        getDate(true);

    }

    //上拉加载
    @Override
    public void onLoadMoreRequested() {
        //页码 n + 1
        mCount = mCount + 1;
        //请求接口、填充数据
        getDate(false);
    }

    //请求数据、添加到数据容器、实体类
    public void getDate(boolean isRefresh) {
        RequestParams params = new RequestParams();
        //用户ID
        params.put("userId", getUserId());
        //页码
        params.put("pageNo", mCount + "");
        //请求数据长度
        params.put("pageSize", pageSize + "");
        //请求
        HttpRequest.getMessageList(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                //关闭加载框
                business_message_swipe.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    //String 转 JSONObject
                    JSONObject result = new JSONObject(responseObj.toString());
                    //List<实体类>list 填充数据
                    List<BusMessageBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<BusMessageBean>>() {
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
                        systemMeAdapter.loadMoreEnd();
                    } else {
                        //数据长度小于定义的长度,adapter 继续加载
                        systemMeAdapter.loadMoreComplete();
                    }
                    //更新adapter
                    systemMeAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                //关闭加载框
                business_message_swipe.setRefreshing(false);
                //根据失败调用方法
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }
}
