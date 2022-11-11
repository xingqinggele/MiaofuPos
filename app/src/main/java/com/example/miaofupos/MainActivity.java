package com.example.miaofupos;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.allenliu.versionchecklib.core.http.HttpParams;
import com.allenliu.versionchecklib.core.http.HttpRequestMethod;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.fragment.DataFragment;
import com.example.miaofupos.fragment.HomeFragment;
import com.example.miaofupos.fragment.MeFragment;
import com.example.miaofupos.net.Urls;
import com.example.miaofupos.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.miaofupos.utils.VersionUtils.crateUIData;
import static com.example.miaofupos.utils.VersionUtils.createCustomDialogTwo;
import static com.example.miaofupos.utils.VersionUtils.createCustomNotification;
import static com.example.miaofupos.utils.VersionUtils.getLocalVersion;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    //Radio按钮框
    private RadioGroup rg_footer;
    //Fragment 事务
    private FragmentTransaction transaction;
    //首页
    private HomeFragment fragment1;
    //数据
    private DataFragment fragment2;
    //我的
    private MeFragment fragment3;
    //退出软件，开始时间
    private long mPressedTime = 0;
    //本地软件版本号
    private int ver_code;
    //权限标识值
    private static final int REQUEST_CODE = 1;
    //权限池
    private static final String[] BASIC_PERMISSIONS = new String[]{
            //相机权限
            android.Manifest.permission.CAMERA,
            //读取手机状态和身份
            android.Manifest.permission.READ_PHONE_STATE,
            // 拨打电话
            Manifest.permission.CALL_PHONE,
            // 修改、删除SD卡中内容
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //通知权限
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            //定位
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //xml界面
    @Override
    protected int getLayoutId() {
        StatusBarUtil.transparencyBar(MainActivity.this);
        return R.layout.activity_main;
    }

    //初始化控件
    protected void initView() {
        //权限获取
        initPermission();
        //版本更新方法
        sendRequest();
        rg_footer = findViewById(R.id.rg_footer);
    }

    //控件点击时间绑定
    @Override
    protected void initListener() {
        rg_footer.setOnCheckedChangeListener(this);
        //默认选中第一按钮
        rg_footer.check(R.id.main_rb1);
    }

    /**
     * 获取权限
     */
    private void initPermission() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (int i = 0; i < BASIC_PERMISSIONS.length; i++) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), BASIC_PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    ActivityCompat.requestPermissions(this, BASIC_PERMISSIONS, REQUEST_CODE);
                }
            }

        }
    }

    //数据处理
    protected void initData() {
        // 获取当前版本信息
        ver_code = getLocalVersion(this);
    }

    /**
     * RadioGroup 选择按钮事件
     *
     * @param radioGroup
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        hideAllFragment();
        switch (checkedId) {
            //首页按钮
            case R.id.main_rb1:
                //设置状态栏颜色,修改状态栏字体颜色
                if (fragment1 == null) {
                    fragment1 = new HomeFragment().newInstance("");
                    transaction.add(R.id.main_content, fragment1);
                } else {
                    transaction.show(fragment1);
                }
                break;
            //数据按钮
            case R.id.main_rb2:
                //设置状态栏颜色
                if (fragment2 == null) {
                    fragment2 = new DataFragment().newInstance("");
                    transaction.add(R.id.main_content, fragment2);
                } else {
                    transaction.show(fragment2);
                }
                break;
            //我的按钮
            case R.id.main_rb3:
                //设置状态栏颜色
                if (fragment3 == null) {
                    fragment3 = new MeFragment().newInstance("");
                    transaction.add(R.id.main_content, fragment3);
                } else {
                    transaction.show(fragment3);
                }
                break;
        }
        //事务提交
        transaction.commit();
    }

    //判断、隐藏
    private void hideAllFragment() {
        if (fragment1 != null) this.transaction.hide(fragment1);
        if (fragment2 != null) this.transaction.hide(fragment2);
        if (fragment3 != null) this.transaction.hide(fragment3);
    }

    /*****************版本更新--->请求接口************/
    //请求服务器、版本更新 地址：http://www.poshb.cn:8081/noauth/getVersionInfo
    private void sendRequest() {
        HttpParams params = new HttpParams();
        AllenVersionChecker
                //获得实例
                .getInstance()
                //请求版本
                .requestVersion()
                //设置请求方法 GET、POST
                .setRequestMethod(HttpRequestMethod.GET)
                //请求值
                .setRequestParams(params)
                //请求地址
                .setRequestUrl(Urls.commUrls + "noauth/getVersionInfo")
                //回调方法
                .request(new RequestVersionListener() {
                    //成功回调
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        //根据请求返回数据做判断，大于当前版本开始升级  小于等于当前版本不升级 提示当前是最新版本
                        try {
                            //String 转 JSONObject
                            JSONObject object = new JSONObject(result);
                            //服务器返回的版本号
                            int sCode = Integer.valueOf(object.getJSONObject("data").getString("code"));
                            shouLog("object",sCode+"");
                            //apk下载地址
                            String sUrl = object.getJSONObject("data").getString("url");
                            //服务器返回的版本名称
                            String sUpTitle = "V" + object.getJSONObject("data").getString("name");
                            //服务器返回的版本介绍内容
                            String sUpContext = object.getJSONObject("data").getString("descs");
                            //判断当前版本是否 大于等于 服务器版本
                            if (ver_code >= sCode) {
                                // 不用更新
                                return null;
                            } else {
                                //提示用户去升级版本
                                return crateUIData(sUrl, sUpTitle, sUpContext);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    //失败回调
                    @Override
                    public void onRequestVersionFailure(String message) {
                        shouLog("MainActivity", message);
                    }
                })
                //设置显示下载对话框
                .setShowDownloadingDialog(false)
                //设置通知建设者
                .setNotificationBuilder(createCustomNotification(this))
                //设置自定义版本对话框监听器
                .setCustomVersionDialogListener(createCustomDialogTwo())
                //默认false
                .setForceRedownload(true)
                //执行任务
                .executeMission(this);
    }

    /************退出软件**************/
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();
        //获取第一次按键时间
        if ((mNowTime - mPressedTime) > 2000) {
            //比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {
            //退出程序
            this.finish();
        }
    }

    /*********HomeFragment调用的方法切换Fragment***********/
    public void homeFragment(){
        rg_footer.check(R.id.main_rb2);
    }


}