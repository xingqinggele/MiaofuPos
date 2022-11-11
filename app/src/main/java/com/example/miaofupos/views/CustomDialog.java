package com.example.miaofupos.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.miaofupos.R;


/**
 * Created: qgl
 * 2020/10/19
 * 描述:弹框
 */
public abstract class CustomDialog extends Dialog {
    private int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//弹窗的宽
    private int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//弹窗的高
    private int dialogGravity = Gravity.BOTTOM;//弹窗显示的位置

    private @StyleRes
    int anim = R.style.MyDialogAnimation;//弹窗显示效果
    public CustomDialog(Context context, boolean dimEnabled) {
        this(context,dimEnabled? R.style.Custom_Dialog_Theme_Background_DimEnabled_True:R.style.Custom_Dialog_Theme_Background_DimEnabled_False);
    }
    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 初始化弹窗
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        onCreateView(window.getWindowManager());
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = dialogWidth;//设置宽
        layoutParams.height = dialogHeight;//设置高
        layoutParams.gravity = dialogGravity;//设置位置
        window.setAttributes(layoutParams);//设置属性
        window.setWindowAnimations(anim);//设置动画
    }

    protected abstract void onCreateView(WindowManager windowManager);

    /**
     * 设置弹窗的宽度
     * @param dialogWidth （单位px）
     */
    public void setDialogWidth(int dialogWidth) {
        this.dialogWidth = dialogWidth;
    }

    /**
     * 设置弹窗的高度
     * @param dialogHeight （单位px）
     */
    public void setDialogHeight(int dialogHeight) {
        this.dialogHeight = dialogHeight;
    }

    /**
     * 设置弹窗的显示位置
     * @param dialogGravity
     */
    public void setDialogGravity(int dialogGravity) {
        this.dialogGravity = dialogGravity;
    }

    /**
     * 设置弹窗的显示动画效果
     * @param anim
     */
    public void setAnim(int anim) {
        this.anim = anim;
    }
}
