package com.hfy.demo01.common.customview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author hufeiyang
 * @data 2021/4/21
 * @Description:
 */
public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {
    public MyTextView(@NonNull Context context) {
        super(context);
    }

    public MyTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
