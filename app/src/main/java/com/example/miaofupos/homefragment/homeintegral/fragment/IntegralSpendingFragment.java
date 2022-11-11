package com.example.miaofupos.homefragment.homeintegral.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homeintegral.adpter.IntegralAllAdapter;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralAllBean;
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
 * 作者: qgl
 * 创建日期：2021/2/19
 * 描述: 积分支出Fragment
 */
public class IntegralSpendingFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private IntegralAllAdapter dataDayResultAdapter;
    private SwipeRefreshLayout integralspending_swipe;
    private RecyclerView integralspending_list_view;
    private List<IntegralAllBean> mData = new ArrayList<>();
    @Override
    protected void initView(View rootView) {
        integralspending_swipe = rootView.findViewById(R.id.integralspending_swipe);
        integralspending_list_view = rootView.findViewById(R.id.integralspending_list_view);
        //下拉样式
        integralspending_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        integralspending_swipe.setOnRefreshListener(this);
        integralspending_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataDayResultAdapter = new IntegralAllAdapter(getActivity());
        integralspending_list_view.setAdapter(dataDayResultAdapter);
        getData();
    }

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.integralspending_fragment;
    }


    //获取数据
    public void getData(){
        mData.clear();
        dataDayResultAdapter.clearData();
        RequestParams params = new RequestParams();
        params.put("userId",getUserId());
        params.put("pageNo", "-1");
        params.put("pageSize", "-1");
        params.put("transType", "2");
        HttpRequest.getTotal_scoreList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                integralspending_swipe.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<IntegralAllBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<IntegralAllBean>>() {
                            }.getType());

                    mData.addAll(memberList);
                    dataDayResultAdapter.addAllData(mData);
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
    public void onRefresh() {
        integralspending_swipe.setRefreshing(true);
        getData();
    }
}
