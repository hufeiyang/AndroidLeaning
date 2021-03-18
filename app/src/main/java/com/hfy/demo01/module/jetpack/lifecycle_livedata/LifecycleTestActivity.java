package com.hfy.demo01.module.jetpack.lifecycle_livedata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.hfy.demo01.R;

/**
 * Lifecycle、liveData 学习
 * @author hufeiyang
 */
public class LifecycleTestActivity extends AppCompatActivity implements IView {

    private String TAG = "Lifecycle_Test";

    private MutableLiveData<String> mLiveData;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, LifecycleTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle_test);

        //1、Lifecycle 生命周期
//        getLifecycle().addObserver(new MyObserver());


        //1.1 MVP中使用Lifecycle
//        getLifecycle().addObserver(new MyPresenter(this));


        //2、liveData，活着的数据

        //2.1基本使用
        mLiveData = new MutableLiveData<>();
        mLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i(TAG, "onChanged: "+s);
            }
        });
        Log.i(TAG, "onCreate: ");
        mLiveData.setValue("onCreate1");
        mLiveData.setValue("onCreate2");


        //2.1修改数据-map
//        MutableLiveData<Integer> liveData1 = new MutableLiveData<>();
//        LiveData<String> liveDataMap = Transformations.map(liveData1, new Function<Integer, String>() {
//            @Override
//            public String apply(Integer input) {
//                String s = input + " + Transformations.map";
//                Log.i(TAG, "apply: " + s);
//                return s;
//            }
//        });
//        liveDataMap.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.i(TAG, "onChanged1: "+s);
//            }
//        });
//
//        liveData1.setValue(100);

//        //2.2 切换数据-switchMap
//        MutableLiveData<String> liveData3 = new MutableLiveData<>();
//        MutableLiveData<String> liveData4 = new MutableLiveData<>();
//
//        MutableLiveData<Boolean> liveDataSwitch = new MutableLiveData<>();
//
//        LiveData<String> liveDataSwitchMap = Transformations.switchMap(liveDataSwitch, new Function<Boolean, LiveData<String>>() {
//            @Override
//            public LiveData<String> apply(Boolean input) {
//                if (input) {
//                    return liveData3;
//                }
//                return liveData4;
//            }
//        });
//
//        liveDataSwitchMap.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.i(TAG, "onChanged2: " + s);
//            }
//        });
//
//
//        boolean switchValue = false;
//        liveDataSwitch.setValue(switchValue);
//        Log.i(TAG, "switchValue=" + switchValue);
//
//        liveData3.setValue("liveData3");
//        liveData4.setValue("liveData4");
//
        //2.3 观察多个数据(一般用上面两个即可，上面两个就是对MediatorLiveData的使用)
//        MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();
//
//        MutableLiveData<String> liveData5 = new MutableLiveData<>();
//        MutableLiveData<String> liveData6 = new MutableLiveData<>();
//
//        mediatorLiveData.addSource(liveData5, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.i(TAG, "onChanged3: " + s);
//                mediatorLiveData.setValue(s);
//            }
//        });
//
//        mediatorLiveData.addSource(liveData6, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.i(TAG, "onChanged4: " + s);
//                mediatorLiveData.setValue(s);
//            }
//        });
//
//        mediatorLiveData.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.i(TAG, "onChanged5: "+s);
//            }
//        });
//        liveData5.setValue("liveData5");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
//        mLiveData.setValue("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
//        mLiveData.setValue("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
//        mLiveData.setValue("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
//        mLiveData.setValue("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
//        mLiveData.setValue("onDestroy");
    }

    @Override
    public void showView() {

    }

    @Override
    public void hideView() {

    }
}
