package com.example.miaofupos.views;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/5/8
 * 描述:
 */
public class MPLineChartManager {
    //线形表
    private LineChart mLineChart;
    private List<LineDataSet> setList;

    public MPLineChartManager(LineChart lineChart) {
        this.mLineChart = lineChart;
        setList = new ArrayList<>();
    }

    /**
     * 设置X轴
     *
     * @param isEnable  设置轴启用或禁用 如果禁用以下的设置全部不生效
     * @param isgdl     x轴上每个点对应的线 true=显示/false=禁用
     * @param gdl       设置竖线的显示样式 若isgdl=true时显示： 0=实线/!0=虚线
     * @param xColor    x轴字体颜色
     * @param xPosition x轴的显示位置
     * @param xAngle    x轴标签的旋转角度
     */
    public void setXAxis(ArrayList<String> xvalue, int count, boolean isEnable, boolean isgdl, float gdl, int xColor, XAxis.XAxisPosition xPosition, float xAngle) {
        XAxis xAxis = mLineChart.getXAxis();
        //设置轴启用或禁用 如果禁用以下的设置全部不生效
        xAxis.setEnabled(isEnable);
        //设置x轴上每个点对应的线
        xAxis.setDrawGridLines(isgdl);
        //设置竖线的显示样式为虚线：lineLength控制虚线段的长度，spaceLength控制线之间的空间
        xAxis.enableGridDashedLine(gdl, gdl, 0f);
        //绘制标签  指x轴上的对应数值
        xAxis.setDrawLabels(true);
        //设置x轴的显示位置
        xAxis.setPosition(xPosition);
        //设置字体颜色
        xAxis.setTextColor(xColor);
        //图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        xAxis.setAvoidFirstLastClipping(false);
        //设置x轴标签的旋转角度
        xAxis.setLabelRotationAngle(xAngle);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawAxisLine(false);
        //设置X轴的刻度
        xAxis.setLabelCount(count, true);
        //里顶部Y轴距离
//        xAxis.setYOffset(5f);
        //填充自定义的X轴数据
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                    Log.e("--------------->","---------------");
//                    Log.e("1--------------->",value + "");
//                    Log.e("2--------------->",(int)value + "");
//                    Log.e("3--------------->",xvalue.get((int) value) + "");
//                    return xvalue.get((int) value);
                int v = (int) value;
                if (v <= xvalue.size() - 1 && v >= 0) {
                    String st = xvalue.get(v);
                    String tim1 = "";
                    tim1 = st;
                    return tim1;
                } else {
                    return null;
                }

            }
        });
    }


    /**
     * 设置Y轴
     *
     * @param lineList 限制线列表
     * @param gdl      虚线设置
     */
    public void setYLeftAndLimit(List<LimitLine> lineList, float gdl, int color, int linecolor) {
        mLineChart.getAxisRight().setEnabled(false);//禁用右边的轴线
        YAxis leftAxis = mLineChart.getAxisLeft();
        //设置坐标轴最小值：如果设置那么轴不会根据传入数据自动设置
        leftAxis.setAxisMinimum(0);
        //设置标签个数以及是否精确（false为模糊，true为精确）
        leftAxis.setLabelCount(6, false);
        //设置Y轴的值之间的最小间隔。这可以用来避免价值复制当放大到一个地步，小数设置轴不再数允许区分两轴线之间的值。
        leftAxis.setGranularity(1);
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();
        //设置限制线
        if (lineList != null) {
            for (LimitLine item : lineList) {
                leftAxis.addLimitLine(item);
            }
        }

        leftAxis.setTextColor(color);
        leftAxis.setGridColor(linecolor);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);//是否绘制0所在的网格线
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(gdl, gdl, 0f);
    }

    public void setYRightAndLimit(List<LimitLine> lineList, float gdl) {
        mLineChart.getAxisLeft().setEnabled(false);
        YAxis rightAxis = mLineChart.getAxisRight();
        //重置所有限制线,以避免重叠线
        rightAxis.removeAllLimitLines();
        //设置限制线
        for (LimitLine item : lineList) {
            rightAxis.addLimitLine(item);
        }
        rightAxis.enableGridDashedLine(gdl, gdl, 0f);
        rightAxis.setDrawLimitLinesBehindData(true);
    }

    /**
     * 设置与图表交互
     *
     * @param isTE        设置是否可以触摸  为fase时后面无效
     * @param isDE        是否可以拖拽
     * @param isDX        是否可以缩放 仅x轴
     * @param isDY        是否可以缩放 仅y轴
     * @param isDXY       设置x轴和y轴能否同时缩放。默认是false
     * @param isDoubleMax 设置是否可以通过双击屏幕放大图表。默认是true
     * @param isHPDE      能否拖拽高亮线(数据点与坐标的提示线)，默认是true
     * @param isDDLE      拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
     */
    public void setInteraction(boolean isTE, boolean isDE, boolean isDX, boolean isDY, boolean isDXY,
                               boolean isDoubleMax, boolean isHPDE, boolean isDDLE) {
        mLineChart.setTouchEnabled(isTE); // 设置是否可以触摸
        mLineChart.setDragEnabled(isDE);// 是否可以拖拽
        mLineChart.setScaleEnabled(false);// 是否可以缩放 x和y轴, 默认是true
        mLineChart.setScaleXEnabled(isDX); //是否可以缩放 仅x轴
        mLineChart.setScaleYEnabled(isDY); //是否可以缩放 仅y轴
        mLineChart.setPinchZoom(isDXY);  //设置x轴和y轴能否同时缩放。默认是否
        mLineChart.setDoubleTapToZoomEnabled(isDoubleMax);//设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setHighlightPerDragEnabled(isHPDE);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mLineChart.setDragDecelerationEnabled(isDDLE);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        mLineChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。
    }

    /**
     * 设置图例
     *
     * @param position 设置图例的位置 Legend.LegendPosition.枚举
     * @param txtSize  设置文字大小
     * @param type     正方形，圆形或线 Legend.LegendForm.枚举
     */
    public void setLegend(Legend.LegendPosition position, float txtSize, int txtColor, Legend.LegendForm type) {
        Legend legend = mLineChart.getLegend();//图例
        legend.setPosition(position);//设置图例的位置
        legend.setTextSize(txtSize);//设置文字大小
        legend.setTextColor(txtColor);
        legend.setForm(type);//正方形，圆形或线
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setFormSize(8f); // 设置Form的大小
        legend.setWordWrapEnabled(true);//是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        legend.setFormLineWidth(0f);//设置Form的宽度
    }

    //x轴动画
    public void animationX(int duration) {
        mLineChart.animateX(duration);
    }

    public void animationX(int duration, Easing.EasingOption eo) {
        mLineChart.animateX(duration, eo);
    }

    //y轴动画
    public void animationY(int duration) {
        mLineChart.animateY(duration);
    }

    public void animationY(int duration, Easing.EasingOption eo) {
        mLineChart.animateY(duration, eo);
    }

    //xy轴动画
    public void animationXY(int x, int y) {
        mLineChart.animateXY(x, y);
    }

    public void animationXY(int x, int y, Easing.EasingOption eox, Easing.EasingOption eoy) {
        mLineChart.animateXY(x, y, eox, eoy);
    }

    /**
     * 创建
     *
     * @param values    数据值
     * @param title     标题
     * @param dl        线各种长度
     * @param color     颜色
     * @param txtSize   字体大小
     * @param isDHL     是否禁用点击高亮线
     * @param dHL       线
     * @param colorDHL  线颜色
     * @param isFill    是否充填
     * @param colorFill 充填颜色
     */
    public void addData(ArrayList<Entry> values, String title, float dl, int color, float txtSize,
                        boolean isDHL, float dHL, int colorDHL,
                        boolean isFill, int colorFill) {
        // 创建一个数据集,并给它一个类型
        LineDataSet set = new LineDataSet(values, title);
        // 在这里设置线
        set.enableDashedLine(dl, dl, 0f);
        set.setColor(color);
        set.setCircleColor(color);
        set.setLineWidth(2f);
        set.setCircleRadius(2f);
        set.setValueTextSize(txtSize);
        set.setCircleColor(color);
        //是否禁用点击高亮线
        set.setHighlightEnabled(isDHL);
        set.enableDashedHighlightLine(dHL, dHL, 0f);//点击后的高亮线的显示样式
        set.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
        set.setHighLightColor(colorDHL);//设置点击交点后显示交高亮线的颜色
        //填充
        set.setDrawFilled(isFill);
        if (Utils.getSDKInt() >= 18) {// 填充背景只支持18以上
            //Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
            //set.setFillDrawable(drawable);
            set.setFillColor(colorFill);
        } else {
            set.setFillColor(Color.BLACK);
        }
        setList.add(set);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        //添加数据集
        dataSets.add(set);
        //创建一个数据集的数据对象
        LineData data = new LineData(dataSets);
        //谁知数据
        mLineChart.setData(data);
        // 里左边的距离
        mLineChart.setExtraRightOffset(25f);
    }

    public void setData() {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        //添加数据集
        for (LineDataSet item : setList) {
            dataSets.add(item);
        }
        //创建一个数据集的数据对象
        LineData data = new LineData(dataSets);
        //谁知数据
        mLineChart.setData(data);
    }

    /**
     * 刷新数据
     *
     * @param lists  数据列表
     * @param xvalue
     */
    public void refreshData(List<ArrayList<Entry>> lists, ArrayList<String> xvalue) {
        for (int i = 0; i < setList.size(); i++) {
            setList.set(i, (LineDataSet) mLineChart.getData().getDataSetByIndex(i));
            setList.get(i).setValues(lists.get(i));
        }
        mLineChart.getData().notifyDataChanged();
        mLineChart.notifyDataSetChanged();
    }

    // 刷新
    public void invalidate() {
        this.mLineChart.invalidate();
    }

    //是否显示顶点值
    public void changeTheVerValue(boolean b) {
        //获取到当前值
        List<ILineDataSet> sets = mLineChart.getData().getDataSets();
        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            //切换显示/隐藏
            set.setDrawValues(b);
        }
        //刷新
        invalidate();
    }

    //是否填充
    public void changeFilled(boolean b) {
        List<ILineDataSet> setsFilled = mLineChart.getData().getDataSets();
        for (ILineDataSet iSet : setsFilled) {
            LineDataSet set = (LineDataSet) iSet;
            set.setDrawFilled(b);
        }
        invalidate();
    }

    //是否显示圆点
    public void changeTheVerCircle(boolean b) {
        List<ILineDataSet> setsCircles = mLineChart.getData().getDataSets();
        for (ILineDataSet iSet : setsCircles) {
            LineDataSet set = (LineDataSet) iSet;
            set.setDrawCircles(b);
        }
        invalidate();
    }

    //切换立方
    public void changeMode(LineDataSet.Mode mode) {
        List<ILineDataSet> setsCubic = mLineChart.getData().getDataSets();
        for (ILineDataSet iSet : setsCubic) {
            LineDataSet set = (LineDataSet) iSet;
            set.setMode(mode);
        }
        invalidate();
    }

    //设置监听事件
    public void setListener(OnChartGestureListener onChartGestureListener, OnChartValueSelectedListener onChartValueSelectedListener) {
        //设置手势滑动事件
        mLineChart.setOnChartGestureListener(onChartGestureListener);
        //设置数值选择监听
        mLineChart.setOnChartValueSelectedListener(onChartValueSelectedListener);
    }

}
