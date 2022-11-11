package com.example.miaofupos.homefragment.homelist.fragment;

import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 创建：  qgl
 * 时间： 2021-04-28
 * 描述： 本月排行榜
 */
public class ThisMonthFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    //实体类List
    private List<HomeRankingBean> beanList = new ArrayList<>();
    //下拉刷新控件
    private SwipeRefreshLayout me_merchants_swipe;
    //列表控件
    private RecyclerView me_merchants_list_view;
    //adapter
    private HomeListAdapter meMerchantsAdapter;
    //第一名
    private SimpleDraweeView one_head_iv;
    private TextView one_name;
    private TextView one_number;
    private String oneHead = "";
    private String oneName = "";
    private String oneNum = "";
    //第二名
    private SimpleDraweeView two_head_iv;
    private TextView two_name;
    private TextView two_number;
    private String twoHead = "";
    private String twoName = "";
    private String twoNum = "";
    //第三名
    private SimpleDraweeView three_head_iv;
    private TextView three_name;
    private TextView three_number;
    private String threeHead = "";
    private String threeName = "";
    private String threeNum = "";

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.this_month_fragment;
    }

    @Override
    protected void initView(View rootView) {
        me_merchants_swipe = rootView.findViewById(R.id.me_merchants_swipe);
        me_merchants_list_view = rootView.findViewById(R.id.me_merchants_list_view);
        one_head_iv = rootView.findViewById(R.id.one_head_iv);
        two_head_iv = rootView.findViewById(R.id.two_head_iv);
        three_head_iv = rootView.findViewById(R.id.three_head_iv);
        one_name = rootView.findViewById(R.id.one_name);
        two_name = rootView.findViewById(R.id.two_name);
        three_name = rootView.findViewById(R.id.three_name);
        one_number = rootView.findViewById(R.id.one_number);
        two_number = rootView.findViewById(R.id.two_number);
        three_number = rootView.findViewById(R.id.three_number);
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
        params.put("ident", "2");//1代表昨天 2代表本月
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
                    if (value) {
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (beanList != null) {
                            beanList.clear();
                        }
                    }
                    for (int i = 0; i < memberList.size(); i++) {
                        memberList.get(i).setIndex(i);
                    }
                    if (memberList != null) {
                        oneHead = memberList.get(0).getHeadPortrait();
                        oneName = memberList.get(0).getNickNAme();
                        oneNum = memberList.get(0).getActivateNum();
                        one_name.setText(oneName);
                        one_number.setText(oneNum);
                        one_head_iv.setImageURI(ImagePipeline(oneHead));
                        //---------------------------------------
                        twoHead = memberList.get(1).getHeadPortrait();
                        twoName = memberList.get(1).getNickNAme();
                        twoNum = memberList.get(1).getActivateNum();
                        two_name.setText(twoName);
                        two_number.setText(twoNum);
                        two_head_iv.setImageURI(ImagePipeline(twoHead));
                        //---------------------------------------
                        threeHead = memberList.get(2).getHeadPortrait();
                        threeName = memberList.get(2).getNickNAme();
                        threeNum = memberList.get(2).getActivateNum();
                        three_name.setText(threeName);
                        three_number.setText(threeNum);
                        three_head_iv.setImageURI(ImagePipeline(threeHead));

                    }
                    for (int i = 0, length = memberList.size(); i < length; i++) {
                        if (memberList.get(i).getIndex() == 0 || memberList.get(i).getIndex() == 1 || memberList.get(i).getIndex() == 2) {
                            memberList.remove(i);
                            length--;
                            i--;
                        }
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
                Failuer(failuer.getEcode(), failuer.getEmsg());
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

    private Uri ImagePipeline(String url) {
        Uri imgurl = Uri.parse(url);
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        return imgurl;
    }
}
