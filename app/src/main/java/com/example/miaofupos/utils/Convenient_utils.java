package com.example.miaofupos.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.TextView;

import com.example.miaofupos.R;


/**
 * 作者: qgl
 * 创建日期：2021/2/22
 * 描述: 一些简单的方法封装
 */
public class Convenient_utils {
    /**
     * 条件满足的时候去不同显示，是否点击
     *
     * @param value true、false
     * @param btn   按钮
     */
    public static void UndBtn(boolean value, Button btn) {
        if (value) {
            btn.setEnabled(true);
            btn.setBackgroundColor(Color.parseColor("#3CA0FF"));
        } else {
            btn.setEnabled(false);
            btn.setBackgroundColor(Color.parseColor("#E1E1E1"));
        }
    }

    /**
     * 条件满足时显示不同的drawableRight
     * @param context 上下文
     * @param value  判断值 1.升序 2.降序
     * @param tv
     */
    public static void UndTv(Context context,String value, TextView tv){
        Drawable turnover_top;
        if (value.equals("1")){
            turnover_top = context.getResources().getDrawable(R.mipmap.turnover_top);
        }else {
            turnover_top = context.getResources().getDrawable(R.mipmap.turnover_botton);
        }
        turnover_top.setBounds(0, 0, turnover_top.getMinimumWidth(), turnover_top.getMinimumHeight());
        tv.setCompoundDrawables(null,null,turnover_top,null);
    }
}
