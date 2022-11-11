package com.example.miaofupos.mefragment.setup;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.MyDialog1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: qgl
 * 创建日期：2022/3/31
 * 描述:签约界面
 */
public class MeSigningActivity extends BaseActivity implements View.OnClickListener {
    private Button signing_btn;
    private LinearLayout iv_back;

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.mesigning_layout;
    }

    @Override
    protected void initView() {
        signing_btn = findViewById(R.id.signing_btn);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        signing_btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signing_btn:
                showDialog("您是否签约？");
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    //设置支付密码提示Dialog
    private void showDialog(String value) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText(value);
        Dialog dialog = new MyDialog1(MeSigningActivity.this, true, true, (float) 0.7).setNewView(view);
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
                dialog.dismiss();
                posData();

            }
        });
    }


    private void posData() {
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        HttpRequest.postSubmitCreateUser(params, getToken(),new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");
                    Intent intent = new Intent(MeSigningActivity.this, SiginWebActivity.class);
                    intent.putExtra("url",data.getString("url"));
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }
}