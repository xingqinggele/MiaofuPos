package com.example.miaofupos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 作者: qgl
 * 创建日期：2020/12/19
 * 描述:
 */
public class MyListView extends ListView {


    public MyListView(Context context, AttributeSet attrs) {
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
