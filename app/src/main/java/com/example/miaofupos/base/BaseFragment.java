package com.example.miaofupos.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.miaofupos.useractivity.LoginActivity;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.LoadingDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者: qgl
 * 创建日期：2020/10/21
 * 描述:基类fragment
 */
public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    public static List<Activity> activitys;
    protected LoadingDialog loadDialog;//加载等待弹窗
    private String userId; // 用户ID
    private String Token; // 用户Token

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutInflaterResId(),container,false);

        userId = SPUtils.get(getActivity(), "userId", "").toString();
        Token = SPUtils.get(getActivity(), "Token", "").toString();
        initView(rootView);
        mContext = getActivity();
        if (activitys == null) {
            activitys = new ArrayList<Activity>();
        }
        activitys.add(getActivity());
        loadDialog = new LoadingDialog(getActivity());
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }


    /**
     * 加载数据
     */
    protected void loadData() {
    }

    /**
     * 创建presenter
     */
    protected void initPresenter() {
    }

    /**
     * 控件设置监听
     */
    protected void initListener() {
    }
    /**
     * 布局xml的id
     *
     * @return
     */
    protected abstract int getLayoutInflaterResId();
    /**
     * 初始化控件
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);


    /**
     * 退出应用,返回到登录
     * @param context
     */
    public void exitApp(Context context) {// 循环结束当前所有Activity
        for (Activity ac : activitys) {
            if (ac != null) {
                ac.finish();
            }
        }
        startActivity(new Intent(mContext, LoginActivity.class));
    }

    //Toast
    public void showToast(String text) {
        Toast.makeText(getActivity(), text + "", Toast.LENGTH_SHORT).show();
    }
    //Log
    public void shouLog(String Interface, String text) {
        Log.e(Interface, text + "");
    }

    // Token失效，退出登录
    public void Failuer(int code,String msg){
        if (code == 401) {
            showToast("登录过期，请重新登录");
            // 退出登录,清除本地数据
            SPUtils.clear(mContext);
            exitApp(mContext);
        } else {
            showToast(msg);
        }
    }

    //返回用户Id
    public String getUserId(){
        return userId;
    }
    //返回Token
    public String getToken(){
        return Token;
    }


}
