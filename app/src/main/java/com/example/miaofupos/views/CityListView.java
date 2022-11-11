package com.example.miaofupos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:
 */
public class CityListView extends ListView {
    public CityListView(Context context) {
        super(context);
    }

    public CityListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CityListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }
}
