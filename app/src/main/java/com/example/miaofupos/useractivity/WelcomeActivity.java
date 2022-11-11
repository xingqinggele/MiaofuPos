package com.example.miaofupos.useractivity;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.miaofupos.MainActivity;
import com.example.miaofupos.R;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.socket.JWebSocketClientService;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: qgl
 * 创建日期：2021/2/1
 * 描述: 欢迎界面
 */
public class  WelcomeActivity extends Activity{
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
    //延时操作
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            /**
             *要执行的操作
             */
            //判断本地是否存在数据,如果存在就直接去登录
            if (SPUtils.contains(WelcomeActivity.this, "passWord")) {
                getLogin(SPUtils.get(WelcomeActivity.this, "userName", "-1").toString(), SPUtils.get(WelcomeActivity.this, "passWord", "-1").toString());
            } else {
                //跳转到登录界面
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //导航栏设置
        StatusBarUtil.transparencyBar(this);
        //App打包apk安装后重复启动根界面的问题
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
        //xml界面
        setContentView(R.layout.welcome_activity);
        //上下文配置
        mContext = WelcomeActivity.this;
        //绑定服务
        bindService();
        //延时操作
        Timer timer = new Timer();
        timer.schedule(task, 3000);//3秒后执行TimeTask的run方法
    }


    /**
     * 用户登录
     *
     * @param userName
     * @param passWord
     */
    public void getLogin(String userName, String passWord) {
        RequestParams params = new RequestParams();
        //用户名
        params.put("username", userName);
        //密码
        params.put("password", passWord);
        //不需要传Token,传空
        HttpRequest.getLogin(params, "", new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
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
                    SPUtils.put(WelcomeActivity.this, "userName", userName);
                    //本地存储密码
                    SPUtils.put(WelcomeActivity.this, "passWord", passWord);
                    //本地存储秘钥
                    SPUtils.put(WelcomeActivity.this, "Token", token);
                    //本地存储待用秘钥
                    SPUtils.put(WelcomeActivity.this, "ticket", ticket);
                    //本地存储用户ID
                    SPUtils.put(WelcomeActivity.this, "userId", userId);
                    //本地存储注册时间
                    SPUtils.put(WelcomeActivity.this, "createTime", result.getJSONObject("loginUser").getJSONObject("user").getString("createTime"));
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    //启动服务
                    startJWebSClientService();
                    //检测通知是否开启
                    checkNotification(mContext);
                    //启动WebSocket
                    jWebSClientService.startWebSocket(userId);
                    //关闭当前欢迎页
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(WelcomeActivity.this, failuer.getEmsg(), Toast.LENGTH_LONG).show();
                //跳转到登录界面
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /*********************************长连接方法模块**********************************************/

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

    //界面销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除服务绑定
        unbindService(serviceConnection);
    }

}
