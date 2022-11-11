package com.example.miaofupos.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 作者: qgl
 * 创建日期：2021/1/26
 * 描述:
 */
public class Utils {
    String cityJsonStr = "";

    public Utils() {
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));

            String line;
            while((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static void setBackgroundAlpha(Context mContext, float bgAlpha) {
        if (bgAlpha == 1.0F) {
            clearDim((Activity)mContext);
        } else {
            applyDim((Activity)mContext, bgAlpha);
        }

    }

    private static void applyDim(Activity activity, float bgAlpha) {
        if (Build.VERSION.SDK_INT >= 18) {
            ViewGroup parent = (ViewGroup)activity.getWindow().getDecorView().getRootView();
            Drawable dim = new ColorDrawable(-16777216);
            dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
            dim.setAlpha((int)(255.0F * bgAlpha));
            ViewGroupOverlay overlay = parent.getOverlay();
            overlay.add(dim);
        }

    }

    private static void clearDim(Activity activity) {
        if (Build.VERSION.SDK_INT >= 18) {
            ViewGroup parent = (ViewGroup)activity.getWindow().getDecorView().getRootView();
            ViewGroupOverlay overlay = parent.getOverlay();
            overlay.clear();
        }

    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}
