package com.example.miaofupos.homefragment.homeequipment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.adapter.ChooseGridViewAdapter;
import com.example.miaofupos.homefragment.homeequipment.bean.ScreeningBean;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/16
 * 描述: 筛选Adapter
 */
public class ChooserListAdapter extends BaseAdapter {
    private List<ScreeningBean> listBean;
    private Context context;
    //侧滑筛选栏adapter
    private ChooseGridViewAdapter madapter;

    public ChooserListAdapter(List<ScreeningBean> listBean, Context context) {
        this.listBean = listBean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listBean.size(); //返回数组的长度
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        if (convertView == null) {
            //通过一个打气筒 inflate 可以把一个布局转换成一个view对象
            view = View.inflate(context, R.layout.merchants_query_content_layout_item, null);
        } else {
            view = convertView;//复用历史缓存对象
        }
        TextView most_types = view.findViewById(R.id.most_types);
        most_types.setText(listBean.get(position).getDictType().getDictName());
        GridView gvTest = view.findViewById(R.id.my_grid1);
        madapter = new ChooseGridViewAdapter(context, listBean.get(position).getDictData());
        gvTest.setAdapter(madapter);
        madapter.setOnAddClickListener(onItemActionClick);
        return view;
    }

    /**
     * Activity 调用
     */
    public void newadd() {
        //刷新Adapter
        notifyDataSetChanged();
    }

    ChooseGridViewAdapter.OnAddClickListener onItemActionClick = new ChooseGridViewAdapter.OnAddClickListener() {
        @Override
        public void onItemClick(String add, String position) {
            onItemAddClick.onItemClick(add,position);
        }
    };


    public static interface OnAddClickListener {
        // true add; false cancel
        public void onItemClick(String position,String add); //传递boolean类型数据给activity
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }

}
