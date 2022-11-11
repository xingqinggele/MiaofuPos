package com.example.miaofupos.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.miaofupos.views.RoundImageView;
import com.youth.banner.loader.ImageLoader;


/**
 * 作者: qgl
 * 创建日期：2020/11/28
 * 描述:重写图片加载器
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);

    }
    /**
     * 自定义圆角类
     * @param context
     * @return
     */
    @Override
    public ImageView createImageView(Context context) {
        RoundImageView img = new RoundImageView(context);
        return img;

    }


}
