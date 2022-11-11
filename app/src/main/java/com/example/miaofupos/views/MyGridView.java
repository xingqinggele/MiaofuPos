package com.example.miaofupos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 作者: qgl
 * 创建日期：2020/12/7
 * 描述: 自定义GridView，设置不滚动
 */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 设置不滚动
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
