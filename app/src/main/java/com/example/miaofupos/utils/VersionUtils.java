package com.example.miaofupos.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.example.miaofupos.R;
import com.example.miaofupos.views.BaseDialog;

/**
 * 作者: qgl
 * 创建日期：2020/12/26
 * 描述: 版本更新工具类
 */
public class VersionUtils {

    /**
     * 自定义Notification
     *
     * @return
     */
    public static NotificationBuilder createCustomNotification(Context context) {
        return NotificationBuilder.create()
                .setRingtone(true)
                .setIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.version_title));
    }

    /**
     * 自定义Dialog
     *
     * @return
     */
    public static CustomVersionDialogListener createCustomDialogTwo() {
        return new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.version_dialog_layout);
                //versionBundle 就是UIData，之前开发者传入的，在这里可以拿出UI数据并展示
                TextView tv_title = baseDialog.findViewById(R.id.tv_title);
                TextView tv_context = baseDialog.findViewById(R.id.tv_context);
                tv_title.setText(versionBundle.getTitle());
                tv_context.setText(versionBundle.getContent());
                return baseDialog;
            }
        };
    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    public static UIData crateUIData(String url, String UpTitle, String UpContext) {
        UIData uiData = UIData.create();
        uiData.setTitle(UpTitle);
        uiData.setDownloadUrl(url);
        uiData.setContent(UpContext);
        return uiData;

    }

    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    /**
     * 获取版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
