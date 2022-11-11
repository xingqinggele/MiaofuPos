package com.example.miaofupos.homefragment.homeequipment.hodler;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.miaofupos.R;


/**
 * Created by huangyaping on 16/4/9.
 */
public class ChooserRecyclerViewHodler extends RecyclerView.ViewHolder {
    public TextView textView_item_number;
    public TextView postype;
    public CheckBox checkBox_item;
    public ConstraintLayout constraintLayout;

    public ChooserRecyclerViewHodler(View view){
        super(view);
        textView_item_number = (TextView)view.findViewById(R.id.cooser_item_tv_number);
        checkBox_item = (CheckBox)view.findViewById(R.id.checkBox_item);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        postype = view.findViewById(R.id.postype);
    }
}
