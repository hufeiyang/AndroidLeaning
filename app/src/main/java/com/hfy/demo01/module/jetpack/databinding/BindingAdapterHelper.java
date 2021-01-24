package com.hfy.demo01.module.jetpack.databinding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.bumptech.glide.Glide;
import com.hfy.demo01.module.jetpack.databinding.bean.User;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 所有binding转换都写在这里
 *
 * 自定义属性：app：xxx
 * 1.Binding 会 自动寻找同名setter方法。原本没有属性xxx，但有setXxx方法，就可以直接使用 app：xxx
 * 2.@BindingMethods，指定属性对应的自定义方法。 android:tint 没有setTint方法，而是setImageTintList，关联起来即可。
 * 3.@BindingAdapter，绑定适配方法。Binding适配器，类似完全自定义属性和对应方法。
 * 4.@BindingConversion，binding转换，且只能注解在 public static 方法上
 */

@BindingMethods(@BindingMethod(type=ImageView.class, attribute = "android:tint", method = "setImageTintList"))
public class BindingAdapterHelper {

    public static String getName(User user) {
        return user.getName();
    }

    @BindingAdapter({"app:imageUrl", "app:placeHolder"})
    public static void loadImageFromUri(ImageView imageView, String imageUri, Drawable placeHolder){
        Glide.with(imageView.getContext())
                .load(imageUri)
                .placeholder(placeHolder)
                .into(imageView);
    }

    /**
     * convertDate（）这个方法在哪个类不重要，重要的是 @BindingConversion
     * @param date 待转换的数据
     * @return 转换后的数据 字符串 android:text= @{data实例}，即Date的实例data 转换成String 赋值给 android:text
     */
    @BindingConversion
    public static String convertDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        return format;
    }
}