package com.example.miaofupos.homefragment.homeintegral.adpter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeintegral.bean.ShoppingCartBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/7/22
 * 描述:购物车Adapter
 */
public class ShoppingCartAdapter extends BaseQuickAdapter<ShoppingCartBean, BaseViewHolder>  {
    //回调方法
    private FoodActionCallback callback;
    private List<ShoppingCartBean>list;
    public ShoppingCartAdapter(int layoutResId, @Nullable List<ShoppingCartBean> data,FoodActionCallback callback) {
        super(layoutResId, data);
        this.callback = callback;
        this.list = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingCartBean item) {
        SimpleDraweeView merchant_person_logo = helper.itemView.findViewById(R.id.item_me_exchange_logo);
        merchant_person_logo.setImageURI(item.getImg());
        helper.setText(R.id.item_me_exchange_type,item.getPosName());
        helper.setText(R.id.item_good_num,item.getPosNum()+"");
        helper.setText(R.id.item_me_exchange_price,item.getReturnMoney());
        helper.setOnClickListener(R.id.delete_iv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback == null) return;
                callback.removeData(helper.getAdapterPosition());
            }
        });
        helper.setOnClickListener(R.id.shopAdd, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback == null) return;
                callback.addAction(helper.getAdapterPosition());
            }
        });
        helper.setOnClickListener(R.id.shopShor, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback == null || 1 == list.get(helper.getAdapterPosition()).getPosNum()) return;
                callback.reduceGood(helper.getAdapterPosition());
            }
        });

    }

    //加减商品回调方法
    public interface FoodActionCallback {
        void addAction(int position);
        void reduceGood(int position);
        void removeData(int position);
    }
}
