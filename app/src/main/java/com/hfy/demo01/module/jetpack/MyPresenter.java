package com.hfy.demo01.module.jetpack;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

class MyPresenter implements LifecycleObserver {

    private static final String TAG = "Lifecycle_Test";
    private final IView mView;

    public MyPresenter(IView view) {
        mView = view;
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    public void getDataOnStart(){
        Log.i(TAG, "getDataOnStart: ");
        //getData...

        mView.showView();
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_STOP)
    public void hideDataOnStop(){
        Log.i(TAG, "hideDataOnStop: ");
        //hideData...
        mView.hideView();
    }

    /**
     * 注解的方法可以接受参数
     */
//    class TestObserver implements LifecycleObserver {
//
//        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//        void onCreated(LifecycleOwner owner) {
////            owner.getLifecycle().addObserver(anotherObserver);
////            owner.getLifecycle().getCurrentState();
//        }
//        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
//        void onAny(LifecycleOwner owner, Lifecycle.Event event) {
////            event.name()
//        }
//    }
}
