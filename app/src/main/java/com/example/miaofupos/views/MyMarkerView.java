
package com.example.miaofupos.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {
    private final TextView tvContent;
    private final TextView title_tv;
    private ArrayList<String> x;
    LineChart lineChart;//图表控件
    private int type = 1;

    public MyMarkerView(Context context, int layoutResource, LineChart lineChart, ArrayList<String> xvalue, int type) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        title_tv = findViewById(R.id.title_tv);
        this.lineChart = lineChart;
        this.x = xvalue;
        this.type = type;
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        DecimalFormat df1 = new DecimalFormat("0.00");
        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            if (type == 1) {
                //tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true) + "元");
                tvContent.setText(df1.format(ce.getHigh()) + "元");
            } else {
                tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true) + "台");
            }
        } else {
            if (type == 1) {
                //tvContent.setText(Utils.formatNumber(, 0, false) + "元");
                tvContent.setText(df1.format(e.getY()) + "元");

            } else {
                tvContent.setText(Utils.formatNumber(e.getY(), 0, false) + "台");
            }
        }
        LineData lineData = lineChart.getLineData();//得到已经绘制成型的折线图的数据
        LineDataSet set = (LineDataSet) lineData.getDataSetByIndex(0);//获取第一条折线图Y轴数据
        int index = set.getEntryIndex(e);//根据点击的该条折线的点，获取当前Y轴数据对应的index值，
        //根据index值，分别获取当前X轴上对应的两条折线的Y轴的值
        title_tv.setText(x.get(index) + "");

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
