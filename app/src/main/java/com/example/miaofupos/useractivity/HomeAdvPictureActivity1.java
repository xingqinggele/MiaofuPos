package com.example.miaofupos.useractivity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 创建：  qgl
 * 时间：
 * 描述：办卡
 */
public class HomeAdvPictureActivity1 extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private WebView webview;
    private String url = "https://gocard.lakala.com/api/channel?c=CCZS&u=";

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homeadvpictureactivity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        webview = findViewById(R.id.webView);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
            shouLog("用户ID",getUserId());
            webview.loadUrl(url+getUserId());
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
            webview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
            webview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
            webview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
            webview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
            webview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
            webview.getSettings().setAppCacheEnabled(true);//是否使用缓存
            webview.getSettings().setDomStorageEnabled(true);//DOM Storage
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            //设置webview的行为监听  步骤2
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    WebView.HitTestResult hit = webview.getHitTestResult();
                    int hitType = hit.getType();
                    if (hitType != WebView.HitTestResult.UNKNOWN_TYPE) {
                    //这里执行自定义的操作
                        return true;
                    } else {
                        return false;
                    }
                }
            });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

}
