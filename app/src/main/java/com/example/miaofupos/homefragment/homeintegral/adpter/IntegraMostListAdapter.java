package com.example.miaofupos.homefragment.homeintegral.adpter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralMostBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/5/17
 * 描述:商城列表Adapter
 */
public class IntegraMostListAdapter extends BaseAdapter {

    private Context context;
    private List<IntegralMostBean.PosTypeList> foodDatas;
    //回调方法
    private FoodActionCallback callback;
    //,FoodActionCallback callback
    public IntegraMostListAdapter(Context context, List<IntegralMostBean.PosTypeList> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_integramost_list, null);
            viewHold = new ViewHold();
            viewHold.tv_name = convertView.findViewById(R.id.item_home_name);
            viewHold.item_home_price = convertView.findViewById(R.id.item_home_price);
            viewHold.iv_icon = convertView.findViewById(R.id.item_album);
//            viewHold.shopShor = convertView.findViewById(R.id.shopShor);
//            viewHold.shopAdd = convertView.findViewById(R.id.shopAdd);
//            viewHold.item_good_num = convertView.findViewById(R.id.item_good_num);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.tv_name.setText(foodDatas.get(position).getTypeName());
        //viewHold.item_good_num.setText(foodDatas.get(position).getNum()+"");
        viewHold.item_home_price.setText("￥" + foodDatas.get(position).getReturnIntegral());
        Uri uri = Uri.parse(foodDatas.get(position).getImgPath());
        viewHold.iv_icon.setImageURI(uri);
//        viewHold.shopShor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (callback == null || 0 == foodDatas.get(position).getNum()) return;
//                callback.reduceGood(position);
//            }
//        });
//        viewHold.shopAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (callback == null) return;
//                callback.addAction(view, position);
//            }
//        });
        return convertView;
    }

    private static class ViewHold {
        private TextView tv_name;
        private TextView item_home_price;
        private SimpleDraweeView iv_icon;
//        private ImageView shopShor; //减
//        private ImageView shopAdd; //加
//        private TextView item_good_num; //商品数量
    }

    //加减商品回调方法
    public interface FoodActionCallback {
        void addAction(View view, int position);

        void reduceGood(int position);
    }

}

