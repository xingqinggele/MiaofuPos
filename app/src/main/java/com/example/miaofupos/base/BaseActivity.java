package com.example.miaofupos.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.useractivity.LoginActivity;
import com.example.miaofupos.utils.CountDownTimerUtils;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.LoadingDialog;
import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created: qgl
 * 2020/10/19
 * 描述:基类Activity
 */
public abstract class BaseActivity extends FragmentActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    protected Context mContext;
    public static List<Activity> activitys;
    protected LoadingDialog loadDialog;//加载等待弹窗
    private String userId; // 用户ID
    private String Token; // 用户ID
    private String userName; // 用户手机号
    private String secretId; // 腾讯云ID
    private String secretKey; // 腾讯云Key
    private String bucketName; // 存储桶名称
    private ImmersionBar mImmersionBar;//状态栏沉浸
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        mContext = this;
        userId = SPUtils.get(this, "userId", "").toString();
        userName = SPUtils.get(this, "userName", "").toString();
        Token = SPUtils.get(this, "Token", "").toString();
        secretId = SPUtils.get(this, "secretId", "").toString();
        secretKey = SPUtils.get(this, "secretKey", "").toString();
        bucketName = SPUtils.get(this, "bucketName", "").toString();
        if (activitys == null) {
            activitys = new ArrayList<Activity>();
        }
        activitys.add(this);
        loadDialog = new LoadingDialog(this);
        initView();
        initListener();
        initData();
    }


    /**
     * 初始化沉浸式状态栏
     */
    public ImmersionBar statusBarConfig(int color,boolean value) {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(color)
                .statusBarDarkFont(value)    //默认状态栏字体颜色为黑色
                .keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
        //必须设置View树布局变化监听，否则软键盘无法顶上去，还有模式必须是SOFT_INPUT_ADJUST_PAN
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        return mImmersionBar;
    }

    /**
     * 获取状态栏字体颜色
     */
    public boolean statusBarDarkFont() {
        //返回false表示白色字体
        return false;
    }

    //引入布局
    protected abstract int getLayoutId();

    //初始化控件
    protected abstract void initView();

    //控件点击事件
    protected abstract void initListener();

    //初始化数据
    protected abstract void initData();

    //Toast 1 Top、2 Center、3 bot
    public void showToast(int location, String text) {
        Toast toast = Toast.makeText(BaseActivity.this, text + "", Toast.LENGTH_LONG);
        if (location == 1) {
            Display display = getWindowManager().getDefaultDisplay();
            // 获取屏幕高度
            int height = display.getHeight();
            // 这里给了一个1/4屏幕高度的y轴偏移量
            toast.setGravity(Gravity.TOP, 0, height / 4);
            toast.show();
        } else if (location == 2) {
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            toast.show();
        }
    }

    //Log
    public void shouLog(String Interface, String text) {
        Log.e(Interface, text + "");
    }

    @Override
    public void onGlobalLayout() {

    }

    /**
     * 退出应用,返回到登录
     *
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

    /**
     * 腾讯对象存储错误码
     *
     * @param msg
     * @param title
     */
    public void popTip(String msg, String title) {
        String msgShow = "错误码：" + msg;
        TextView titleTextView = new TextView(this);
        titleTextView.setText(title);
        titleTextView.setPadding(10, 10, 10, 10);
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setTextSize(20);
        titleTextView.setTextColor(Color.rgb(0, 0, 0));
        TextView contentView = new TextView(this);
        contentView.setText(msgShow);
        contentView.setPadding(10, 10, 10, 10);
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(15);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(titleTextView);
        builder.setView(contentView);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void Failuer(int code, String msg) {
        if (code == 401) {
            showToast(3, "登录过期，请重新登录");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(2000);//休眠3秒
                        // 退出登录,清除本地数据
                        SPUtils.clear(mContext);
                        exitApp(mContext);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /**
                     * 要执行的操作
                     */
                }
            }.start();

        } else {
            showToast(3, msg);
        }
    }

    //返回用户Id
    public String getUserId() {
        return userId;
    }

    //返回Token
    public String getToken() {
        return Token;
    }

    //返回用户名
    public String getUserName() {
        return userName;
    }

    //返回腾讯id
    public String getSecretId() {
        return secretId;
    }

    //返回腾讯Key
    public String getSecretKey() {
        return secretKey;
    }

    //返回腾讯存储桶名称
    public String getBucketName() {
        return bucketName;
    }


    /**
     * 发送手机验证码
     */
    public void getPhoneCode(String Phone, TextView tv) {
        RequestParams params = new RequestParams();
        params.put("mobile", Phone);
        HttpRequest.getRegister_Code(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    showToast(3, "短信验证码发送成功");
                    // 开始倒计时 60秒，间隔1秒
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tv, 60000, 1000);
                    mCountDownTimerUtils.start();
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


    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
