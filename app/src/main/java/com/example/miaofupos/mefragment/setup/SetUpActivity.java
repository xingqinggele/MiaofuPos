package com.example.miaofupos.mefragment.setup;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.socket.JWebSocketClientService;
import com.example.miaofupos.useractivity.ModifyPasswordActivity;
import com.example.miaofupos.utils.DataCleanManager;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.MyDialog1;


/**
 * 作者: qgl
 * 创建日期：2020/12/22
 * 描述:设置
 */
public class SetUpActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private RelativeLayout set_up_clear_cache_relative;
    private RelativeLayout set_up_personal_relative;
    private TextView set_up_clear_cache_tv;
    private RelativeLayout set_up_out_login_relative;
    private RelativeLayout set_up_modify_password_relative;
    //设置支付密码
    private RelativeLayout set_up_pay_password_relative;

    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private Context mContext;

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

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.set_up_activity;
    }

    @Override
    protected void initView() {
        mContext = SetUpActivity.this;
        //绑定服务
        bindService();
        iv_back = findViewById(R.id.iv_back);
        set_up_clear_cache_relative = findViewById(R.id.set_up_clear_cache_relative);
        set_up_clear_cache_tv = findViewById(R.id.set_up_clear_cache_tv);
        set_up_personal_relative = findViewById(R.id.set_up_personal_relative);
        set_up_out_login_relative = findViewById(R.id.set_up_out_login_relative);
        set_up_modify_password_relative = findViewById(R.id.set_up_modify_password_relative);
        set_up_pay_password_relative = findViewById(R.id.set_up_pay_password_relative);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        set_up_clear_cache_relative.setOnClickListener(this);
        set_up_personal_relative.setOnClickListener(this);
        set_up_out_login_relative.setOnClickListener(this);
        set_up_modify_password_relative.setOnClickListener(this);
        set_up_pay_password_relative.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        try {
            set_up_clear_cache_tv.setText(DataCleanManager.getTotalCacheSize(SetUpActivity.this) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.set_up_clear_cache_relative:
                //清理缓存
                showDialog("您确定要删除缓存数据吗？");
                break;
            case R.id.set_up_personal_relative:
                //个人信息
                startActivity(new Intent(SetUpActivity.this, PersonalActivity.class));
                break;
            case R.id.set_up_out_login_relative:
                showDialog1();
                break;
            case R.id.set_up_modify_password_relative:
                //修改密码
                startActivity(new Intent(SetUpActivity.this, ModifyPasswordActivity.class));
                break;
            case R.id.set_up_pay_password_relative:
                //设置支付密码选项
                startActivity(new Intent(SetUpActivity.this, MePayPassOptionsActivity.class));
                break;
        }
    }

    private void showDialog(String value) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText(value);
        Dialog dialog = new MyDialog1(SetUpActivity.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
                dialog.dismiss();
                DataCleanManager.clearAllCache(SetUpActivity.this);
                set_up_clear_cache_tv.setText("0k");
            }
        });
    }

    private void showDialog1() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_out_login, null);
        RadioButton dialog_radio_cancel = view.findViewById(R.id.dialog_radio_cancel);
        RadioButton dialog_radio_confirm = view.findViewById(R.id.dialog_radio_confirm);
        Dialog dialog = new MyDialog1(SetUpActivity.this, true, true, (float) 0.9).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog_radio_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_radio_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 退出登录,清除本地数据
                SPUtils.clear(SetUpActivity.this);
                exitApp(SetUpActivity.this);
                jWebSClientService.stopWebSocket();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
