package com.example.miaofupos.homefragment.homelist.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homelist.adapter.HomeListAdapter;
import com.example.miaofupos.homefragment.homelist.bean.HomeRankingBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建：  qgl
 * 时间： 2021-04-28
 * 描述： 昨日排行榜
 */
public class YesterdayFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //实体类List
    private List<HomeRankingBean> beanList = new ArrayList<>();
    //下拉刷新控件
    private SwipeRefreshLayout me_merchants_swipe;
    //列表控件
    private RecyclerView me_merchants_list_view;
    //adapter
    private HomeListAdapter meMerchantsAdapter;
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.yester_day_fragment;
    }

    @Override
    protected void initView(View rootView) {
        me_merchants_swipe = rootView.findViewById(R.id.me_merchants_swipe);
        me_merchants_list_view = rootView.findViewById(R.id.me_merchants_list_view);
        initList();
    }
    //适配列表、刷新控件、adapter
    public void initList() {
        //下拉样式
        me_merchants_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //上拉刷新初始化
        me_merchants_swipe.setOnRefreshListener(this);
        //adapter配置data
        meMerchantsAdapter = new HomeListAdapter(R.layout.item_home_list, beanList);
        //打开加载动画
        meMerchantsAdapter.openLoadAnimation();
        //设置启用加载更多
        meMerchantsAdapter.setEnableLoadMore(false);
        //设置为加载更多监听器
        meMerchantsAdapter.setOnLoadMoreListener(this, me_merchants_list_view);
        //数据为空显示xml
        meMerchantsAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.list_empty, null));
        // RecyclerView设置布局管理器
        me_merchants_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        //RecyclerView配置adapter
        me_merchants_list_view.setAdapter(meMerchantsAdapter);
        //请求接口
        posData(true);
    }

    //请求排行榜数据
    private void posData(boolean value) {
        RequestParams params = new RequestParams();
        params.put("ident", "1");//1代表昨天 2代表本月
        HttpRequest.getRanking(params, SPUtils.get(getActivity(), "Token", "").toString(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //关闭加载条
                me_merchants_swipe.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<HomeRankingBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<HomeRankingBean>>() {
                            }.getType());
                    //判断刷新还是加载
                    if (value){
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (beanList != null){
                            beanList.clear();
                        }
                    }
                    for (int i = 0;i< memberList.size();i++){
                        memberList.get(i).setIndex(i);
                    }
                    beanList.addAll(memberList);
                    meMerchantsAdapter.loadMoreEnd();
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

    @Override
    public void onRefresh() {
        me_merchants_swipe.setRefreshing(true);
        posData(true);

    }

    @Override
    public void onLoadMoreRequested() {

    }
}
