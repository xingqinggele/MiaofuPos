package com.example.miaofupos.mefragment.mefeilv;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: qgl
 * 创建日期：2021/10/27
 * 描述:我的费率
 */
public class MeFeilvActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    //信用卡结算
    private TextView feilv_xyk;
    //扫码结算
    private TextView feilv_sm;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.mefeilv_activity;
    }

    @Override
    protected void initView() {
        feilv_xyk = findViewById(R.id.feilv_xyk);
        feilv_sm = findViewById(R.id.feilv_sm);
        iv_back = findViewById(R.id.iv_back);

    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        posData();
    }

    /**
     * 请求接口
     */
    private void posData() {
        RequestParams params = new RequestParams();
        params.put("userId",getUserId());
        HttpRequest.getv1terminallist(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");
                    String rateT0 = data.getString("rateT0");
                    String qrsettleRate = data.getString("qrsettleRate");
                    feilv_xyk.setText(rateT0);
                    feilv_sm.setText(qrsettleRate);
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
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

}