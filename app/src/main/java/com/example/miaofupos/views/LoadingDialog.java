package com.example.miaofupos.views;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.miaofupos.R;


/**
 * Created: qgl
 * 2020/10/19
 * 描述:弹框
 */
public class LoadingDialog extends CustomDialog1 {
    private CharSequence titleStr;//标题
    private TextView titleTxt;//标题
    public LoadingDialog(Context context) {
        this(context, false);
    }

    public LoadingDialog(Context context, boolean dimEnabled) {
        super(context, dimEnabled);
    }

    @Override
    protected void onCreateView(WindowManager windowManager) {
        setDialogWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setDialogHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.dialog_loding);
        //dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        setCanceledOnTouchOutside(false);

        //dialog弹出后会点击屏幕或物理返回键，dialog不消失
        setCancelable(false);
        initView();//初始化控件
        setShow();//设置显示内容
    }

    public void setShow(){
        if (null != titleStr) {
            titleTxt.setVisibility(View.VISIBLE);//显示控件
            titleTxt.setText(titleStr);//显示内容
        }
    }

    /**
     * 设置显示标题
     *
     * @param titleStr
     */
    public LoadingDialog setTitleStr(CharSequence titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        titleTxt = findViewById(R.id.dialog_title_tv);
    }

}
