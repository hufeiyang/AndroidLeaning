package com.hfy.demo01.module.jetpack.lifecycle_livedata;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * 生命周期观察者
 * @author hufeiyang
 */
public class MyObserver implements LifecycleObserver {

    private String TAG = "Lifecycle_Test";

    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    public void connect(){
        Log.i(TAG, "connect: ");
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    public void disConnect(){
        Log.i(TAG, "disConnect: ");
    }
}
