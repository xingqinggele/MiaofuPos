package com.example.miaofupos.useractivity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaofupos.MainActivity;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.socket.JWebSocketClientService;
import com.example.miaofupos.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者: qgl
 * 创建日期：2020/12/10
 * 描述:登录界面
 */
public class LoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    //是否记住密码
    private boolean Remember_Pass = false;
    //手机号输入框
    private EditText login_et_userName;
    //密码输入框
    private EditText login_et_passWord;
    //登录按钮
    private Button login_bt_login;
    //是否记住密码选项
    private CheckBox login_remember_password;
    //忘记密码按钮
    private TextView login_tv_forgot_password;
    //隐私政策
    private TextView privacy_btn_tv;
    //清空账号按钮
    private ImageView login_cha;
    //密码可视按钮
    private ImageView login_eyes;
    //密码是否可视状态
    private boolean eyes = false;
    //上下文
    private Context mContext;
    //客户绑定JWebSocket
    private JWebSocketClientService.JWebSocketClientBinder binder;
    //JWebSocket客户服务
    private JWebSocketClientService jWebSClientService;
    //服务连接
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };

    //xml界面
    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.white,true).init();
        return R.layout.loginactivity_main1;
    }

    //初始化控件
    @Override
    protected void initView() {
        mContext = LoginActivity.this;
        //绑定服务
        bindService();
        login_et_userName = findViewById(R.id.login_et_userName);
        login_et_passWord = findViewById(R.id.login_et_passWord);
        login_bt_login = findViewById(R.id.login_bt_login);
        login_remember_password = findViewById(R.id.login_remember_password);
        login_tv_forgot_password = findViewById(R.id.login_tv_forgot_password);
        privacy_btn_tv = findViewById(R.id.privacy_btn_tv);
        login_cha = findViewById(R.id.login_cha);
        login_eyes = findViewById(R.id.login_eyes);
    }

    //点击事件绑定
    @Override
    protected void initListener() {
        login_remember_password.setOnCheckedChangeListener(this);
        login_bt_login.setOnClickListener(this);
        login_tv_forgot_password.setOnClickListener(this);
        privacy_btn_tv.setOnClickListener(this);
        login_cha.setOnClickListener(this);
        login_eyes.setOnClickListener(this);
    }

    /**
     * 数据处理
     */
    @Override
    protected void initData() {
        //判断本地是否存在数据,如果存在就直接去登录
        if (SPUtils.contains(LoginActivity.this, "passWord")) {
            getLogin(SPUtils.get(LoginActivity.this, "userName", "-1").toString(),
                    SPUtils.get(LoginActivity.this, "passWord", "-1").toString());
        }
    }

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param passWord 密码
     */
    public void getLogin(String userName, String passWord) {
        // 开启加载框
        loadDialog.show();
        RequestParams params = new RequestParams();
        //用户名
        params.put("username", userName);
        //密码
        params.put("password", passWord);
        // 登录时不需要Token
        HttpRequest.getLogin(params, "", new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                // 关闭加载框
                loadDialog.dismiss();
                try {
                    //String 转换 JSONObject
                    JSONObject result = new JSONObject(responseObj.toString());
                    //秘钥
                    String token = result.getString("token");
                    //待用的秘钥
                    String ticket = result.getString("ticket");
                    //用户ID
                    String userId = result.getJSONObject("loginUser").getJSONObject("user").getString("userId");
                    //本地存储用户名
                    SPUtils.put(LoginActivity.this, "userName", userName);
                    //如果用户点击记住密码、存储密码到本地
                    if (Remember_Pass) {
                        //本地存储密码
                        SPUtils.put(LoginActivity.this, "passWord", passWord);
                    }
                    //本地存储秘钥
                    SPUtils.put(LoginActivity.this, "Token", token);
                    //本地存储待用秘钥
                    SPUtils.put(LoginActivity.this, "ticket", ticket);
                    //本地存储用户ID
                    SPUtils.put(LoginActivity.this, "userId", userId);
                    //存储腾讯云ID
                    SPUtils.put(LoginActivity.this, "secretId", result.getString("secretId"));
                    //存储腾讯云密钥
                    SPUtils.put(LoginActivity.this, "secretKey", result.getString("secretKey"));
                    //存储腾讯存储桶名称
                    SPUtils.put(LoginActivity.this, "bucketName", result.getString("bucketName"));
                    //注册时间
                    SPUtils.put(LoginActivity.this, "createTime", result.getJSONObject("loginUser").getJSONObject("user").getString("createTime"));
                    //跳转到主页
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //启动服务
                    startJWebSClientService();
                    //检测通知是否开启
                    checkNotification(mContext);
                    //启动WebSocket
                    jWebSClientService.startWebSocket(userId);
                    //关闭当前登录页
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                // 关闭加载框
                loadDialog.dismiss();
                // 根据失败返回码返回操作
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * 记住密码点击事件监听
     *
     * @param compoundButton
     * @param b              false true
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            //记住密码按钮
            case R.id.login_remember_password:
                //赋值是否记住密码
                Remember_Pass = b;
                break;
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //登录按钮
            case R.id.login_bt_login:
                //判断用户是否输入手机号
                if (TextUtils.isEmpty(login_et_userName.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                //判断用户是否输入密码
                if (TextUtils.isEmpty(login_et_passWord.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                //满足条件进行登录
                getLogin(login_et_userName.getText().toString().trim(), login_et_passWord.getText().toString().trim());
                break;
            // 忘记密码按钮
            case R.id.login_tv_forgot_password:
                // 跳转到找回密码界面
                Intent intent = new Intent(LoginActivity.this, RetrievePassActivity.class);
                intent.putExtra("title", "忘记密码");
                startActivity(intent);
                break;
            //隐私政策
            case R.id.privacy_btn_tv:
                startActivity(new Intent(LoginActivity.this, PrivacyActivity.class));
                break;
            case R.id.login_cha:
                login_et_userName.setText("");
                break;
            case R.id.login_eyes:
                Eyes(eyes);
                eyes = !eyes;
                break;

        }
    }

    //判断是否显示密码方法
    private void Eyes(boolean value){
        if (value){
            login_eyes.setImageResource(R.mipmap.login_password_eys1);
            //显示密码
            login_et_passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            // 隐藏密码
            login_et_passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
            login_eyes.setImageResource(R.mipmap.login_password_eys);
        }
    }
/************************************--长连接方法开始--**********************************************/
    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /************************************--长连接方法结束--**********************************************/
    //界面销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除服务绑定
        unbindService(serviceConnection);
    }


}