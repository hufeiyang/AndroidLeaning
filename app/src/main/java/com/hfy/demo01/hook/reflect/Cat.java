package com.hfy.demo01.hook.reflect;

import android.util.Log;

import com.hfy.demo01.hook.TestHookActivity;

public class Cat {

    private final String TAG = TestHookActivity.TAG;

    private String name;

    public void eat(){
        Log.i(TAG, "Cat eat: ");
    }
}
